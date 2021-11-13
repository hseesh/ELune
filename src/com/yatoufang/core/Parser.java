package com.yatoufang.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtil;
import com.yatoufang.entity.HttpState;
import com.yatoufang.entity.Method;
import com.yatoufang.entity.ReferenceBox;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyService;
import com.yatoufang.templet.Annotations;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author hse
 * @Date: 2021/1/23
 */
public class Parser {


    public Method action(PsiMethod psiMethod, String baseUrl, String classUrl) {
        Method method = new Method();
        HashSet<String> mapSet = new HashSet<>();
        ArrayList<String> mapKey = new ArrayList<>();
        ArrayList<PsiType> mapType = new ArrayList<>();
        HashMap<String, String> paramDesc = new HashMap<>();

        psiMethod.accept(new JavaRecursiveElementWalkingVisitor() {

            @Override
            public void visitDocComment(PsiDocComment comment) {
                method.setDescription(Psi.getDescription(comment));
                PsiDocTag[] tags = comment.getTags();
                for (PsiDocTag tag : tags) {
                    switch (tag.getName()) {
                        case "param":
                            Psi.getParameterDescription(tag, paramDesc);
                            break;
                        case "return":
                            method.setReturnDescription(Psi.getParameterDescription(tag));
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void visitParameterList(PsiParameterList list) {
                method.setParams(Psi.getParamList(list));
            }

            @Override
            public void visitAnnotation(PsiAnnotation annotation) {
                if (annotation.hasQualifiedName(Annotations.REQUESTMAPPING)
                        || annotation.hasQualifiedName(Annotations.POSTMAPPING)
                        || annotation.hasQualifiedName(Annotations.GETMAPPING)
                        || annotation.hasQualifiedName(Annotations.DELETEMAPPING)
                        || annotation.hasQualifiedName(Annotations.PUTMAPPING)) {
                    String methodUrl = Psi.getMethodRequestUrl(annotation);
                    methodUrl = methodUrl.startsWith("/") ? methodUrl : "/" + methodUrl;
                    method.setUrl(classUrl + methodUrl);
                    method.setRequestMethod(Psi.getMethodRequestType(annotation));
                }
            }


            @Override
            public void visitReturnStatement(PsiReturnStatement statement) {
                PsiExpression returnValue = statement.getReturnValue();
                if (returnValue != null) {
                    // for complex return statement
                    returnValue.accept(new JavaRecursiveElementWalkingVisitor() {
                        @Override
                        public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                            String text = expression.getText();
                            if (text.indexOf(".put(") > 0) {
                                PsiExpressionList argumentList = expression.getArgumentList();
                                PsiExpression[] expressions = argumentList.getExpressions();
                                if (expressions.length == 2) {
                                    if (mapSet.add(expressions[0].getText())) {
                                        mapKey.add(expressions[0].getText().replace("\"", ""));
                                        mapType.add(expressions[1].getType());
                                    }
                                }
                            }
                        }
                    });
                }

            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                //Determines the map type's expression is not rigorous by string comparison and parameter number matching
                String text = expression.getText();
                if (text.indexOf(".put(") > 0) {// indexOf is faster than KMP ,regular expression and String.contaions in this case
                    PsiExpressionList argumentList = expression.getArgumentList();
                    PsiExpression[] expressions = argumentList.getExpressions();
                    if (expressions.length == 2) {
                        if (mapSet.add(expressions[0].getText())) {
                            mapKey.add(expressions[0].getText().replace("\"", ""));
                            mapType.add(expressions[1].getType());
                        }
                    }
                }
            }
        });

        PsiType returnType = psiMethod.getReturnType();
        ReferenceBox example = new ReferenceBox();

        example.setMapKey(mapKey);
        example.setMapValueType(mapType);
        if (returnType != null) {
            method.setResponseExample(getResponseExample(returnType, example));
        }

        PsiType returnValueType = psiMethod.getReturnType();
        if (returnValueType != null) {
            method.setReturnValue(returnValueType.getPresentableText());
            method.setReturnType(returnValueType.getCanonicalText());
        }
        method.setName(psiMethod.getName());
        method.setParamDescription(paramDesc);

        method.setRequestExample(baseUrl + method.getUrl());
        return method;
    }

    /**
     * get info for http request
     *
     * @param psiMethod target method
     * @param classUrl  class URI
     * @return HttpState
     */
    public HttpState action(PsiMethod psiMethod, String classUrl) {
        HttpState httpState = new HttpState();
        psiMethod.accept(new JavaRecursiveElementWalkingVisitor() {

            @Override
            public void visitParameterList(PsiParameterList list) {
                //unknown bugs for JavaRecursiveElementWalkingVisitor, this code fragment should be call only once instead of twice or more with using other params
                if (httpState.getParams() == null) {
                    httpState.setParams(Psi.getParamArray(list));
                }
                httpState.setBodyType(Psi.getContentType(list));
            }


            @Override
            public void visitAnnotation(PsiAnnotation annotation) {
                if (annotation.hasQualifiedName(Annotations.REQUESTMAPPING)
                        || annotation.hasQualifiedName(Annotations.POSTMAPPING)
                        || annotation.hasQualifiedName(Annotations.GETMAPPING)
                        || annotation.hasQualifiedName(Annotations.DELETEMAPPING)
                        || annotation.hasQualifiedName(Annotations.PUTMAPPING)) {
                    String methodUrl = Psi.getMethodRequestUrl(annotation);
                    methodUrl = methodUrl.startsWith("/") ? methodUrl : "/" + methodUrl;
                    httpState.setShortUrl(classUrl + methodUrl);
                    httpState.setMethod(Psi.getMethodRequestType(annotation));
                }
            }

            @Override
            public void visitCallExpression(PsiCallExpression callExpression) {
                super.visitCallExpression(callExpression);
            }
        });


        if (httpState.getDescription() == null) {
            httpState.setDescription(psiMethod.getName());
        }
        httpState.loadLocalData();
        return httpState;
    }


    /**
     * get Response Example
     * <p>
     * it's necessary to differentiate basically array complex object various situations
     *
     * @param psiType return type
     * @param example Reference record for Map
     * @return ResponseExample
     */
    private String getResponseExample(PsiType psiType, ReferenceBox example) {
        psiType = Psi.getSuperType(psiType);

        String defaultValue = getDefaultValue(psiType.getPresentableText());
        if ("com.yatoufang.hse".equals(defaultValue)) {
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = new JsonWriter(stringWriter);
            try {
                writer.beginObject();
                buildJson(psiType, writer, example, true, 0);
                writer.endObject();
                String s = stringWriter.toString();
                JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                defaultValue = gson.toJson(jsonObject);
                return defaultValue;
            } catch (IOException e) {
                NotifyService.notifyError("My API Document ResponseExample Builder");
                e.printStackTrace();
            }
        } else {
            return defaultValue;
        }
        return "";
    }

    /**
     * create response example Json text
     *
     * @param psiType       psiType current recursion's param type
     *                      used for write default value and load customerClass superClass's fields
     * @param writer        JsonWriter,Json text builder
     * @param example       map (key,value) call expression record
     * @param isSingleValue whether the return value is a single value
     *                      if return true then it's no need to build Json text
     *                      This param decide whether to use JsonWriter.beginObject() method when build json text
     * @param recursionDeep recursion Deep counter, recursionDeep max value == 2 for a nested object
     * @throws IOException JsonWriter nested exception
     */
    private void buildJson(PsiType psiType, JsonWriter writer, ReferenceBox example, boolean isSingleValue, int recursionDeep) throws IOException {
        psiType = Psi.getSuperType(psiType);

        String defaultValue = getDefaultValue(psiType.getPresentableText());
        if (!"com.yatoufang.hse".equals(defaultValue)) {
            writer.value(defaultValue);
        } else {
            PsiClass entity = PsiUtil.resolveClassInClassTypeOnly(psiType);
            if (entity != null) {
                String targetClass = entity.getQualifiedName();
                if (targetClass != null && targetClass.startsWith(Application.basePackage)) {
                    PsiField[] fields = entity.getFields();
                    ArrayList<PsiField> classFields = new ArrayList<>();
                    for (PsiField field : fields) {

                        if (psiType.getCanonicalText().equals(Psi.getSuperType(field.getType()).getCanonicalText())) {
                            if (++recursionDeep > 1) {
                                continue;
                            }
                        }

                        if (!"serialVersionUID".equals(field.getName())) {
                            classFields.add(field);
                        }
                    }
                    if (classFields.size() > 0) {
                        if (isSingleValue) {
                            for (PsiField psiField : classFields) {
                                writer.name(psiField.getName());
                                buildJson(psiField.getType(), writer, example, false, recursionDeep);
                            }
                        } else {
                            writer.beginObject();
                            for (PsiField psiField : classFields) {
                                writer.name(psiField.getName());
                                buildJson(psiField.getType(), writer, example, false, recursionDeep);
                            }
                            writer.endObject();
                        }
                    } else {
                        traversalMapReference(writer, example);
                    }
                } else {
                    if (traversalMapReference(writer, example)) {
                        if (!isSingleValue) {
                            writer.value("");
                        }
                    }
                }
            } else {
                writer.value("");
            }
        }
    }

    /**
     * traversal Map Reference
     *
     * @return JsonWriter ask every key have a value ,if return true,it's mean need to fill in missing values
     * @throws IOException JsonWriter nested exception
     */
    private boolean traversalMapReference(JsonWriter writer, ReferenceBox example) throws IOException {
        ArrayList<String> mapCase = example.getMapKey();
        if (mapCase != null && mapCase.size() > 0) {
            Iterator<String> keys = mapCase.iterator();
            Iterator<PsiType> types = example.getMapValueType().iterator();
            while (keys.hasNext()) {
                PsiType tem = types.next();
                String key = keys.next();
                keys.remove();
                types.remove();
                writer.name(key);
                buildJson(tem, writer, example, false, 0);
            }
            return false;
        }
        return true;
    }

    /**
     * the basically type's default value
     *
     * @param type basically type
     * @return String
     */
    private String getDefaultValue(String type) {
        type = type.replace("[]", "");
        String result;
        switch (type) {
            case "int":
            case "long":
            case "short":
            case "Integer":
            case "Long":
            case "Short":
            case "BigDecimal":
                result = Application.getInteger();
                break;
            case "double":
            case "float":
            case "Double":
            case "Float":
                result = Application.getDecimal();
                break;
            case "boolean":
            case "Boolean":
                result = "false";
                break;
            case "byte":
            case "Byte":
                result = "1";
                break;
            case "char":
            case "Char":
            case "String":
                result = Application.getWord();
                break;
            case "Date":
            case "Time":
            case "Datetime":
            case "Timestamp":
            case "LocalDate":
            case "LocalDateTime":
                result = "2021-01-01";
                break;
            case "Object":
                result = "Object";
                break;
            case "void":
                result = "void";
                break;
            default:
                result = "com.yatoufang.hse";
                break;
        }
        return result;
    }

}
