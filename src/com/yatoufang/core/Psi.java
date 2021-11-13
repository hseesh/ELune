package com.yatoufang.core;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtil;
import com.yatoufang.entity.Param;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectSearchScope;
import com.yatoufang.templet.Annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * this plugin is base on IntelliJ PSI,The Program Structure Interface, the layer
 * in the IntelliJ Platform
 * <p>
 * Consider using PsiViewer plugin , This plugin will show you the PSI tree structure
 * or see https://plugins.jetbrains.com/docs/intellij/psi.html to learn more about PSI
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
public class Psi {


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
            System.out.println("param = " + param);
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
        myParams.removeIf(next -> "serialVersionUID".equals(next.getName()));
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
        System.out.println(paramsDetail.toString());
        ArrayList<Param> myParams = new ArrayList<>();
        for (Param param : paramsDetail) {
            getClassFields(param, myParams, null);
        }
        myParams.removeIf(next -> "serialVersionUID".equals(next.getName()));
        myParams = removeDuplicates(myParams);
        return myParams;
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
            if ("serialVersionUID".equals(field.getName())) {
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
        return JavaPsiFacade.getInstance(Application.project).findClass(className, new ProjectSearchScope(Application.project));
    }


    public static PsiClass findClass(String className, GlobalSearchScope searchScope) {
        return JavaPsiFacade.getInstance(Application.project).findClass(className, searchScope);
    }


    public static void getParameterDescription(PsiDocTag tag, HashMap<String, String> desc) {
        String paramName = getParamName(tag);
        String parameterDescription = getParameterDescription(tag);
        if (!"".equals(paramName) && !"".equals(parameterDescription)) {
            desc.put(paramName, parameterDescription);
        }

    }

    public static String getParamName(PsiDocTag tag) {
        String paramName = "";
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
            if (!paramDesc.equals(paramName) && !"".equals(paramDesc)) {
                builder.append(paramDesc);
            }
        }
        return builder.toString();
    }


    public static String getClassUrl(PsiAnnotation psiAnnotation) {
        if (psiAnnotation != null) {
            PsiAnnotationMemberValue classUrl = psiAnnotation.findAttributeValue("value");
            if (classUrl != null) {
                String replace = classUrl.getText().replace("\"", "");
                if (!"".equals(replace)) {
                    replace = replace.startsWith("/") ? replace : "/" + replace;
                    return replace;
                }
            }
        }
        return "";
    }


    public static String getDescription(PsiDocComment comment) {
        if (comment != null) {
            PsiElement[] descriptionElements = comment.getDescriptionElements();
            StringBuilder methodDescription = new StringBuilder();
            for (PsiElement element : descriptionElements) {
                if (!"".equals(element.getText().trim())) {
                    methodDescription.append(element.getText().trim());
                }
            }
            return methodDescription.toString();
        }
        return "";
    }

    public static String getMethodRequestType(PsiAnnotation annotation) {
        String methodAnnotation = annotation.getQualifiedName();
        String type = "";
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
        return "";
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
        return str.replace("\"", "");
    }


}