package com.yatoufang.utils;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Result;
import com.intellij.codeInsight.template.macro.MacroUtil;
import com.intellij.codeInsight.template.macro.VariableTypeCalculator;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.containers.ContainerUtil;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Param;
import com.yatoufang.service.NotifyService;
import com.yatoufang.service.SearchScopeService;
import com.yatoufang.templet.Application;
import com.yatoufang.config.ProjectSearchScope;
import com.yatoufang.templet.Annotations;
import com.yatoufang.templet.ProjectKeys;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * this plugin is base on IntelliJ PSI,The Program Structure Interface, the layer
 * in the IntelliJ Platform
 * <p>
 * Consider using PsiViewer plugin , This plugin will show you the PSI tree structure
 * or see <a href="https://plugins.jetbrains.com/docs/intellij/psi.html">...</a> to learn more about PSI
 * <p>
 * The main content of the API　Document comes from class  file's  Javadoc comments
 * and SpringAnnotation
 * for a method description, it comes from PsiDocComment object
 * for a method param's description .it comes from  docTags @Param
 * <p>
 * An entire class parsing process is done from top to bottom, one line after another,
 * one character after another, its necessary to use many for loops
 * <p>
 * Avoid using too many PsiElement methods which are expensive  deep trees.
 * it could consume a lot of cpu or memory
 * <p>
 * I do suggest making the parsing process as simple as possible in logical
 * <p>
 * Readers are encouraged to review the Javadoc comments or IntelliJ Platform Plugin SDK
 * about PsiElement to lear more
 *
 * @author hse
 * @date : 2021/1/15 0003
 */
public class PSIUtil {

    /**
     * get Params Details for method from  annotations (@RequestParam @RequestBody @PathVariable)
     *
     * @param list PsiParameterList
     * @return ArrayList<Param>
     */
    public static ArrayList<Param> getParamsDetailList(PsiParameterList list) {
        ArrayList<Param> params = new ArrayList<>();
        PsiParameter[] parameters = list.getParameters();
        for (PsiParameter parameter : parameters) {
            String paraName = parameter.getName();
            Param param = new Param(trims(paraName));
            param.setType(parameter.getType());
            PsiAnnotation[] parameterAnnotations = parameter.getAnnotations();
            for (PsiAnnotation psiAnnotation : parameterAnnotations) {
                if (psiAnnotation.hasQualifiedName(Annotations.REQUESTPARAM)) {
                    PsiAnnotationMemberValue value = psiAnnotation.findAttributeValue("value");
                    PsiAnnotationMemberValue required = psiAnnotation.findAttributeValue("required");
                    PsiAnnotationMemberValue defaultValue = psiAnnotation.findAttributeValue("defaultValue");
                    if (defaultValue != null) {
                        param.setValue(trims(defaultValue.getText()));
                    }
                    if (required != null) {
                        param.setRequired(Boolean.valueOf(required.getText()));
                    }
                    if (value != null) {
                        if (value instanceof PsiLiteralExpression) {
                            PsiLiteralExpression valueText = (PsiLiteralExpression) value;
                            String text = trims(valueText.getText());
                            if (text.length() != 0) {
                                param.setName(text);
                            }
                        }
                    }
                } else if (psiAnnotation.hasQualifiedName(Annotations.REQUESTBODY)) {
                    PsiAnnotationMemberValue required = psiAnnotation.findAttributeValue("required");
                    param.setRequired(Boolean.valueOf(required != null ? required.getText() : null));
                } else if (psiAnnotation.hasQualifiedName(Annotations.PATHVARIABLE)) {
                    PsiAnnotationMemberValue value = psiAnnotation.findAttributeValue("value");
                    PsiAnnotationMemberValue required = psiAnnotation.findAttributeValue("required");
                    if (required != null) {
                        param.setRequired(Boolean.valueOf(required.getText()));
                    }
                    if (value != null && !"".equals(value.getText())) {
                        param.setName(trims(value.getText()));
                    }
                }
            }
            params.add(param);
        }
        return params;
    }

    public static List<Param> getParamsDetail(PsiParameterList list) {
        ArrayList<Param> params = new ArrayList<>();
        PsiParameter[] parameters = list.getParameters();
        for (PsiParameter parameter : parameters) {
            Param httpParam = new Param(parameter.getName(), parameter.getType());
            PsiAnnotation annotation = parameter.getAnnotation(Annotations.REQUESTMAPPING);
            if (annotation != null) {
                PsiAnnotationMemberValue defaultValue = annotation.findAttributeValue("defaultValue");
                if (defaultValue != null) {
                    httpParam.setValue(trims(defaultValue.getText()));
                }
            }
            params.add(httpParam);
        }
        return params;
    }

    public static String getContentType(PsiParameterList list) {
        PsiParameter[] parameters = list.getParameters();
        for (PsiParameter parameter : parameters) {
            PsiAnnotation[] annotations = parameter.getAnnotations();
            for (PsiAnnotation annotation : annotations) {
                if (annotation.hasQualifiedName(Annotations.REQUESTBODY)) {
                    return "JSON";
                }
            }
        }
        return "";
    }

    public static Object[][] getParamArray(PsiParameterList list) {
        List<Param> paramsDetail = getParamsDetail(list);
        ArrayList<Param> myParams = new ArrayList<>();
        for (Param param : paramsDetail) {
            getClassFields(param, myParams, null);
        }
        myParams.removeIf(next -> ProjectKeys.SERIAL_UID.equals(next.getName()));
        myParams = removeDuplicates(myParams);
        Object[][] objects = new Object[myParams.size()][2];
        for (int i = 0; i < myParams.size(); i++) {
            objects[i][0] = myParams.get(i).getName();
            if (myParams.get(i).getValue() != null && !myParams.get(i).getValue().startsWith("\\n\\t\\t\\n")) {
                objects[i][1] = myParams.get(i).getValue();
            }
        }
        return objects;
    }

    public static ArrayList<Param> getParamList(PsiParameterList list) {
        ArrayList<Param> paramsDetail = getParamsDetailList(list);
        ArrayList<Param> myParams = new ArrayList<>();
        for (Param param : paramsDetail) {
            getClassFields(param, myParams, null);
        }
        myParams.removeIf(next -> ProjectKeys.SERIAL_UID.equals(next.getName()));
        myParams = removeDuplicates(myParams);
        return myParams;
    }

    public static Collection<Param> getParams(PsiParameterList list) {
        Collection<Param> params = new ArrayList<>();
        List<PsiParameter> parameterList = PsiTreeUtil.getChildrenOfTypeAsList(list, PsiParameter.class);
        for (PsiParameter parameter : parameterList) {
            String paraName = parameter.getName();
            Param param = new Param(trims(paraName));
            param.setType(parameter.getType());
            params.add(param);
        }
        return params;
    }

    public static Optional<Param> getParams(PsiVariable variable) {
        PsiLocalVariable child = PsiTreeUtil.getChildOfType(variable, PsiLocalVariable.class);
        if (child == null) {
            return Optional.empty();
        }
        Param param = new Param(child.getName());
        param.setType(child.getType());
        return Optional.of(param);
    }

    public static ArrayList<Param> removeDuplicates(ArrayList<Param> params) {
        ArrayList<Param> result = new ArrayList<>();
        HashSet<String> strings = new HashSet<>();
        for (Param param : params) {
            if (strings.add(param.getName())) {
                result.add(param);
            }
        }
        return result;
    }

    /**
     * getClassFields
     *
     * @param param      decided whether load class from OS, it's represent a filed type or a class type
     * @param list       result param list
     * @param originType origin class Type for current recursion, to solve nested object caused stackoverflow problem
     */
    public static void getClassFields(Param param, List<Param> list, PsiType originType) {
        param.setType(getSuperType(param.getType()));

        if (Application.isBasicType(param.getType().getPresentableText())) {
            list.add(param);
            return;
        }

        if (originType != null && originType == param.getType()) {
            return;
        }

        PsiClass entity = PsiUtil.resolveClassInClassTypeOnly(param.getType());
        if (entity != null) {
            PsiClass superClass = entity.getSuperClass();
            loadFields(superClass, list, param.getType());
            loadFields(entity, list, param.getType());
        } else {
            if (param.getType().getPresentableText().indexOf("[]") > 0) {
                PsiType deepComponentType = param.getType().getDeepComponentType();
                entity = PsiUtil.resolveClassInClassTypeOnly(deepComponentType);
                if (entity != null) {
                    PsiClass superClass = entity.getSuperClass();
                    loadFields(superClass, list, param.getType());
                    loadFields(entity, list, param.getType());
                }
            }
        }
    }

    public static void getClassFields(PsiClass psiClass, List<Param> result, List<Param> superResult, boolean isSuperClass, PsiType originType) {
        if (psiClass == null) return;
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            if (ProjectKeys.SERIAL_UID.equals(field.getName())) {
                continue;
            }
            PsiType type = field.getType();
            type = getSuperType(type);
            if (Application.isBasicType(type.getPresentableText())) {
                if (isSuperClass) {
                    superResult.add(new Param(field.getName(), field.getType()));
                } else {
                    result.add(new Param(field.getName(), type));
                }
            }

            if (originType != null && originType == type) {
                return;
            }

            PsiClass aClass = findClass(type.getCanonicalText());

            getClassFields(aClass, result, superResult, false, type);

        }
        PsiClass superClass = psiClass.getSuperClass();
        if (superClass == null) return;
        PsiClass aClass = findClass(superClass.getQualifiedName());
        getClassFields(aClass, result, superResult, true, null);
    }

    public static void getClassFields(PsiClass psiClass, Collection<Param> result, PsiType originType) {
        if (psiClass == null) return;
        if (psiClass.isEnum()) {
            Param param = new Param(psiClass.getName());
            param.setType(originType);
            param.setDescription(getDescription(psiClass.getDocComment()));
            result.add(param);
            return;
        }
        if (Application.isBasicType(psiClass.getName())) {
            return;
        }
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            if (ProjectKeys.SERIAL_UID.equals(field.getName())) {
                continue;
            }
            PsiType type = field.getType();
            type = getSuperType(type);
            if (Application.isBasicType(type.getPresentableText())) {
                Param param = new Param(field.getName());
                param.setType(type);
                param.setDescription(getDescription(field.getDocComment()));
                result.add(param);
            }
            if (originType != null && originType == type) {
                return;
            }
            PsiClass aClass = findClass(type.getCanonicalText());
            getClassFields(aClass, result, type);
        }
    }

    public static void getClassFields(PsiClass psiClass, Collection<Param> result) {
        if (psiClass == null) return;
        if (Application.isBasicType(psiClass.getName())) {
            return;
        }
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            PsiType type = field.getType();
            if (Application.isBasicType(type.getPresentableText())) {
                Param param = new Param(field.getName());
                param.setType(type);
                param.setDescription(getDescription(field.getDocComment()));
                result.add(param);
            }
        }
    }

    public static List<Param> getClassFields(PsiClass aClass) {
        List<Param> list = Lists.newArrayList();
        getClassFields(aClass, list);
        return list;
    }

    public static void getClassAllFields(PsiClass psiClass, Collection<Param> result) {
        if (psiClass == null) return;
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            PsiType type = field.getType();
            Param param = new Param(field.getName());
            param.setTypeAlias(type.getPresentableText());
            param.setDescription(getDescription(field.getDocComment()));
            result.add(param);
        }
    }

    public static List<Param> getClassAllFields(PsiClass aClass) {
        List<Param> list = Lists.newArrayList();
        getClassAllFields(aClass, list);
        return list;
    }

    public static void delete(PsiElement psiElement) {
        if (psiElement.getContainingFile() == null) {
            psiElement = psiElement.getParent();
        }
        WriteCommandAction.runWriteCommandAction(Application.project, psiElement::delete);
    }

    public static void replace(PsiElement originElement, PsiElement newElement) {
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            originElement.replace(newElement);
        });
    }

    public static void loadFields(PsiClass entity, List<Param> list, PsiType originType) {
        if (isLocalClass(entity)) {
            PsiField[] fields = entity.getFields();
            for (PsiField field : fields) {
                getClassFields(new Param(field.getName(), field.getType()), list, originType);
            }
        }
    }

    public static boolean isLocalClass(PsiClass psiClass) {
        if (psiClass == null) return false;
        String qualifiedName = psiClass.getQualifiedName();
        if (qualifiedName != null) {
            return qualifiedName.startsWith(Application.basePackage);
        }
        return false;
    }

    public static boolean isNativeClass(PsiClass psiClass) {
        if (psiClass == null) return false;
        String qualifiedName = psiClass.getQualifiedName();
        if (qualifiedName == null) return false;
        qualifiedName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
        PsiPackage aPackage = JavaPsiFacade.getInstance(Application.project).findPackage(qualifiedName);
        return aPackage != null;
    }

    public static PsiClass findClass(String className) {
        PsiClass aClass = JavaPsiFacade.getInstance(Application.project).findClass(className, SearchScopeService.getInstance());
        if (aClass == null) {
            aClass = JavaPsiFacade.getInstance(Application.project).findClass(className, new ProjectSearchScope());
        } else {
            return aClass;
        }
        if (aClass == null) {
            aClass = findClass(className, GlobalSearchScope.allScope(Application.project));
        }
        return aClass;
    }

    public static PsiClass findClass(String className, GlobalSearchScope searchScope) {
        return JavaPsiFacade.getInstance(Application.project).findClass(className, searchScope);
    }

    public static PsiClass[] findClassWithShortName(String name) {
        PsiClass[] classes = PsiShortNamesCache.getInstance(Application.project).getClassesByName(name, SearchScopeService.getInstance());
        if (classes.length == 0) {
            classes = findClassWithShortName(name, new ProjectSearchScope());
        } else {
            return classes;
        }
        if (classes.length == 0) {
            classes = PsiShortNamesCache.getInstance(Application.project).getClassesByName(name, GlobalSearchScope.allScope(Application.project));
        }
        return classes;
    }

    public static PsiClass[] findClassWithShortName(String name, GlobalSearchScope searchScope) {
        return PsiShortNamesCache.getInstance(Application.project).getClassesByName(name, searchScope);
    }

    public static PsiClass loadClassWithShortName(String name){
        PsiClass[] classes = findClassWithShortName(name);
        if (classes.length == 0) {
            return null;
        }
        return classes[0];
    }

    public static void getParameterDescription(PsiDocTag tag, HashMap<String, String> desc) {
        String paramName = getParamName(tag);
        String parameterDescription = getParameterDescription(tag);
        if (!"".equals(paramName) && !StringUtil.EMPTY.equals(parameterDescription)) {
            desc.put(paramName, parameterDescription);
        }

    }

    public static String getParamName(PsiDocTag tag) {
        String paramName = StringUtil.EMPTY;
        PsiDocTagValue valueElement = tag.getValueElement();
        if (valueElement != null) {
            paramName = valueElement.getText();
        }
        return paramName;
    }

    public static String getParameterDescription(PsiDocTag tag) {
        String paramName = getParamName(tag);
        StringBuilder builder = new StringBuilder();
        PsiElement[] dataElements = tag.getDataElements();
        for (PsiElement psiElement : dataElements) {
            String paramDesc = psiElement.getText().trim();
            if (!paramDesc.equals(paramName) && !StringUtil.EMPTY.equals(paramDesc)) {
                builder.append(paramDesc);
            }
        }
        return builder.toString();
    }

    public static String getClassUrl(PsiAnnotation psiAnnotation) {
        if (psiAnnotation != null) {
            PsiAnnotationMemberValue classUrl = psiAnnotation.findAttributeValue("value");
            if (classUrl != null) {
                String replace = classUrl.getText().replace("\"", StringUtil.EMPTY);
                if (!StringUtil.EMPTY.equals(replace)) {
                    replace = replace.startsWith("/") ? replace : "/" + replace;
                    return replace;
                }
            }
        }
        return StringUtil.EMPTY;
    }

    public static String getDescription(PsiDocComment comment) {
        if (comment != null) {
            PsiElement[] descriptionElements = comment.getDescriptionElements();
            StringBuilder methodDescription = new StringBuilder();
            for (PsiElement element : descriptionElements) {
                if (!StringUtil.EMPTY.equals(element.getText().trim())) {
                    methodDescription.append(element.getText().trim());
                }
            }
            return methodDescription.toString();
        }
        return StringUtil.EMPTY;
    }

    public static String getMethodRequestType(PsiAnnotation annotation) {
        String methodAnnotation = annotation.getQualifiedName();
        String type = StringUtil.EMPTY;
        if (methodAnnotation != null) {
            switch (methodAnnotation) {
                case Annotations.REQUESTMAPPING:
                    PsiAnnotationMemberValue requestMethod = annotation.findAttributeValue("method");
                    if (requestMethod != null) {
                        type = requestMethod.getText();
                    }
                    break;
                case Annotations.POSTMAPPING:
                    type = "POST";
                    break;
                case Annotations.DELETEMAPPING:
                    type = "DELETE";
                    break;
                case Annotations.PUTMAPPING:
                    type = "PUT";
                    break;
                default:
                    type = "GET";
                    break;
            }
        }
        return type;
    }

    public static String getMethodRequestUrl(PsiAnnotation annotation) {
        PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
        if (value != null && !"{}".equals(value.getText())) {
            return trims(value.getText().trim());
        } else {
            value = annotation.findAttributeValue("path");
            if (value != null) {
                return trims(value.getText().trim());
            }
        }
        return StringUtil.EMPTY;
    }

    public static PsiType getSuperType(PsiType psiType) {
        PsiType result = PsiUtil.extractIterableTypeParameter(psiType, true);
        while (result != null) {
            psiType = result;
            result = PsiUtil.extractIterableTypeParameter(psiType, true);
        }
        return psiType;
    }

    private static String trims(String str) {
        return str.replace("\"", StringUtil.EMPTY);
    }

    public static String getFieldsValue(String moduleName, String text) {
        PsiClass psiclass = PSIUtil.findClass(moduleName);
        if (psiclass == null) {
            psiclass = PSIUtil.findClass(moduleName, new ProjectSearchScope());
        }
        if (psiclass == null) {
            NotifyService.notifyWarning(moduleName + " not found");
            return null;
        }
        PsiField[] allFields = psiclass.getAllFields();
        for (PsiField psiField : allFields) {
            if (psiField.getName().trim().equals(text)) {
                PsiElement[] elements = psiField.getChildren();
                for (PsiElement element : elements) {
                    if (element instanceof PsiLiteralExpression) {
                        return element.getText();
                    }
                }
            }
        }
        return null;
    }

    public static Integer getElementOffset(String element, PsiClass psiClass) {
        PsiMethod[] methods = psiClass.getMethods();
        for (PsiMethod method : methods) {
            PsiAnnotation[] annotations = method.getAnnotations();
            for (PsiAnnotation annotation : annotations) {
                if (annotation.hasQualifiedName(ProjectKeys.CMD)) {
                    PsiAnnotationMemberValue memberValue = annotation.findAttributeValue("Id");
                    if (memberValue != null) {
                        String value = memberValue.getText();
                        if (value != null && value.length() != 0) {
                            int index = value.indexOf(".");
                            if (index > -1) {
                                value = value.substring(index + 1);
                            }
                            if (value.equals(element)) {
                                return annotation.getTextOffset();
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static PsiMethod getMethodByName(PsiClass aClass, String methodName) {
        PsiMethod[] methods = aClass.getMethods();
        for (PsiMethod method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public static String getFieldValue(PsiClass cacheClass, String value) {
        PsiField psiField = getField(cacheClass, value);
        assert psiField != null;
        return getFiledValue(psiField);
    }

    private static String getFiledValue(PsiField psiField, String value) {
        if (psiField == null) {
            return StringUtil.EMPTY;
        }
        String name = psiField.getName();
        if (!name.equals(value)) {
            return StringUtil.EMPTY;
        }
        PsiElement[] elements = psiField.getChildren();
        for (PsiElement element : elements) {
            if (element instanceof PsiLiteralExpression) {
                return element.getText();
            }
        }
        return StringUtil.EMPTY;
    }

    public static String getFiledValue(PsiElement element) {
        if (element == null) return null;
        if (element instanceof PsiJavaToken) {
            if (StringUtil.EQUAL == element.getText().charAt(0)) {
                PsiElement nextSibling = element.getNextSibling();
                return ProjectKeys.EQUAL + nextSibling.getNextSibling().getText();
            }
        }
        PsiElement[] children = element.getChildren();
        for (PsiElement child : children) {
            String filedValue = getFiledValue(child);
            if (filedValue != null) {
                return filedValue;
            }
        }
        return null;
    }

    public static String getFiledValue(PsiField psiField) {
        PsiElement[] elements = psiField.getChildren();
        for (PsiElement element : elements) {
            if (element instanceof PsiLiteralExpression) {
                return element.getText();
            }
        }
        return StringUtil.EMPTY;
    }

    public static PsiField getField(PsiClass cacheClass, String filedName) {
        PsiField[] allFields = cacheClass.getAllFields();
        for (PsiField psiField : allFields) {
            String name = psiField.getName();
            if (name.equals(filedName)) {
                return psiField;
            }
        }
        return null;
    }

    /**
     * get class all fields
     *
     * @param aClass target class
     * @return List<String>
     */
    public static List<String> getClassPrimaryInfo(PsiClass aClass) {
        PsiField[] allFields = aClass.getAllFields();
        ArrayList<String> result = Lists.newArrayList();
        for (PsiField field : allFields) {
            if (field.hasAnnotation(Annotations.FILED_IGNORE)) {
                continue;
            }
            result.add(field.getType().getPresentableText() + StringUtil.SPACE + field.getName());
        }
        return result;
    }

    public static PsiField createField(PsiElement element) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiType psiType = factory.createTypeFromText("HitState", element);
        PsiField field = factory.createField("HSE", psiType);
        // PsiUtil.setModifierProperty(field, PsiModifier.PRIVATE, true);
        return field;
    }

    public static PsiMethod createMethod(PsiElement element) {
        String content = "actionStoreHelper";
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiType psiType = factory.createTypeFromText("HitState", element);
        PsiMethod method = factory.createMethod(content, psiType);
        return method;
    }

    public static void addComment(PsiElement element, String strComment) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(Application.project).getElementFactory();
        PsiComment comment = factory.createCommentFromText(strComment, null);
        element.addBefore(comment, element.getFirstChild());
    }

    public static String getFilePrimaryInfo(String content, String annotation) {
        if (content.isEmpty()) {
            return content;
        }
        PsiClass aClass = BuildUtil.createClass(content);
        if (aClass == null) {
            return content;
        }
        ArrayList<Param> fieldsList = Lists.newArrayList();
        getClassAllFields(aClass, fieldsList);
        if (annotation != null) {
            fieldsList.removeIf(e -> annotation.equals(e.getAnnotation()));
        }
        return getFieldsInfo(fieldsList);

    }

    public static <T extends Param> String getFieldsInfo(List<T> fieldsList) {
        StringBuilder builder = new StringBuilder();
        String commentStart = "/**\n";
        String comment = " * ";
        String commentEnd = " */\n";
        for (Param param : fieldsList) {
            builder.append(commentStart).append(comment).append(param.getDescription()).append(StringUtil.NEW_LINE).append(commentEnd).append(param.getTypeAlias()).append(StringUtil.SPACE).append(param.getName()).append(StringUtil.NEW_LINE);
        }
        return builder.toString();
    }

    @Nullable
    public static String getClassValueOf(PsiClass[] classes) {
        PsiClass aClass = classes[0];
        if (aClass == null) {
            return null;
        }
        return getClassValueOf(aClass);
    }

    @Nullable
    public static String getClassValueOf(PsiClass aClass) {
        PsiClass superClass = aClass.getSuperClass();
        List<PsiField> fields = Lists.newArrayList();
        if (superClass != null) {
            for (PsiField psiField : aClass.getAllFields()) {
                boolean shouldAdd = true;
                for (PsiField allField : superClass.getAllFields()) {
                    if (psiField.getName().equals(allField.getName())) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (shouldAdd) {
                    fields.add(psiField);
                }
            }
        }
        return TemplateUtil.valueOf(aClass.getName(), fields);
    }

    public static void getClassField(PsiClass psiClass, Collection<Field> result) {
        if (psiClass == null) return;
        if (Application.isBasicType(psiClass.getName())) {
            return;
        }
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            PsiType type = field.getType();
            if (Application.isBasicType(type.getPresentableText())) {
                Field param = new Field(field.getName());
                param.setType(type);
                param.setDescription(getDescription(field.getDocComment()));
                result.add(param);
            }
        }
    }

    public static List<Field> getClassField(PsiClass aClass) {
        List<Field> list = Lists.newArrayList();
        getClassField(aClass, list);
        return list;
    }


    public static PsiFieldMember buildFieldMember(final PsiField field, final PsiClass clazz) {
        return new PsiFieldMember(field, TypeConversionUtil.getSuperClassSubstitutor(clazz, clazz, PsiSubstitutor.EMPTY));
    }

    public static PsiFieldMember[] buildFieldMember(PsiClass psiClass) {
        if (psiClass == null) {
            return null;
        }
        PsiField[] fields = psiClass.getFields();
        PsiFieldMember[] result = new PsiFieldMember[fields.length];
        for (int i = 0; i < fields.length; i++) {
            result[i] = buildFieldMember(fields[i], psiClass);
        }
        return result;
    }

    public static Set<Param> loadSuperFields(Collection<Param> elements) {
        Set<Param> superParams = Sets.newHashSet();
        for (Param element : elements) {
            String type = element.getType().getPresentableText();
            if (Application.isBasicType(type)) {
                continue;
            }
            PsiClass aClass = loadClassWithShortName(type);
            if (aClass == null) {
                continue;
            }
            PsiField[] fields = aClass.getAllFields();
            for (PsiField field : fields) {
                Param param = new Param(field.getName());
                param.setType(field.getType());
                param.setName(param.getGetString());
                param.setDescription(getDescription(field.getDocComment()));
                superParams.add(param);
            }
        }
        return superParams;
    }
}
