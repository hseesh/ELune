package com.yatoufang.complete.model.context;

import com.google.common.collect.Maps;
import com.intellij.psi.*;
import com.yatoufang.entity.Method;
import com.yatoufang.entity.Param;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.*;

/**
 * @author GongHuang(hse)
 * @since 2023/6/1
 */
public class CodeCompleteTrigger {

    private Method method;
    private String className;
    private PsiElement element;
    private PsiParameter[] arguments;
    private final List<Method> methods = Lists.newArrayList();
    private List<PsiVariable> variables = Lists.newArrayList();
    private List<PsiExpression> expressions = Lists.newArrayList();
    private final List<String> expressionList = Lists.newArrayList();
    private final Map<String, Collection<Param>> referenceVariables = Maps.newHashMap();

    public boolean shouldCreateDB() {
        return variables.size() == arguments.length;
    }

    public boolean shouldFillArgument() {
        return element instanceof PsiExpressionList || element instanceof PsiJavaToken;
    }

    public boolean shouldCheckParam() {
        return expressions.size() == 0 && arguments.length > 1;
    }

    public Param getPrepareArgument() {
        for (PsiParameter argument : arguments) {
            if (expressionList.contains(argument.getText())) {
                continue;
            }
            return new Param(argument.getName(), argument.getType());
        }
        return null;
    }

    public List<Param> getFillParam() {
        PsiElement parent = element.getParent();
        while (parent != null) {
            if (parent instanceof PsiMethodCallExpression) {
                PsiReferenceExpression methodExpression = ((PsiMethodCallExpression) parent).getMethodExpression();
                PsiElement resolve = methodExpression.resolve();
                if (resolve instanceof PsiMethod) {
                    PsiMethod method = (PsiMethod) resolve;
                    List<Param> result = Lists.newArrayList();
                    for (PsiParameter parameter : method.getParameterList().getParameters()) {
                        result.add(new Param(parameter.getName(), parameter.getType()));
                    }
                    return result;
                }
            }else if(parent instanceof PsiNewExpression){
                PsiJavaCodeReferenceElement classReference = ((PsiNewExpression) parent).getClassReference();
                if (classReference == null) {
                    continue;
                }
                PsiElement resolve = classReference.resolve();
                if (resolve instanceof PsiClass) {
                    PsiClass aClass = (PsiClass) resolve;
                    List<Param> result = Lists.newArrayList();
                    PsiMethod[] constructors = aClass.getConstructors();
                    for (PsiMethod constructor : constructors) {
                        for (PsiParameter parameter : constructor.getParameterList().getParameters()) {
                            result.add(new Param(parameter.getName(), parameter.getType()));
                        }
                        return result;
                    }
                }
            }
            parent = parent.getParent();
        }
        return Collections.emptyList();
    }

    public Param getPrepareParam() {
        if (variables.size() > 0) {
            PsiVariable psiVariable = variables.get(variables.size() - 1);
            return new Param(psiVariable.getName(), psiVariable.getType());
        }
        return null;
    }

    public void addMethods(Method id) {
        methods.add(id);
    }

    public List<PsiVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<PsiVariable> variables) {
        this.variables = variables;
    }

    public PsiParameter[] getArguments() {
        return arguments;
    }

    public void setArguments(PsiParameter[] arguments) {
        this.arguments = arguments;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addVariables(PsiVariable variable) {
        variables.add(variable);
    }

    public List<PsiExpression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<PsiExpression> expressions) {
        this.expressions = expressions;
    }

    public PsiElement getElement() {
        return element;
    }

    public void setElement(PsiElement element) {
        this.element = element;
    }

    public void addExpressions(PsiExpression id) {
        expressions.add(id);
        expressionList.add(id.getText());
    }

    public String getPrepareKey(String trigger) {
        for (PsiExpression expression : expressions) {
            if (expression.getText().contains(trigger)) {
                return ProjectKeys.RESULT;
            }
        }
        return ProjectKeys.RESULT_OF;
    }

    public void addReferenceVariables(Collection<Param> params) {
        for (Param param : params) {
            Collection<Param> paramCollection = referenceVariables.computeIfAbsent(param.getName(), k -> Lists.newArrayList());
            paramCollection.add(param);
        }
    }

    public Collection<Param> getReferenceVariables(String type) {
        return referenceVariables.computeIfAbsent(type, k -> Lists.newArrayList());
    }

    public Param getReferenceVariables(String type, String name) {
        Collection<Param> params = referenceVariables.get(type);
        if (params == null) {
            return null;
        }
        double similarValue = 0;
        Param result = null;
        for (Param param : params) {
            double levenshtein = DataUtil.calculateLevenshtein(param.getAlias(), name);
            if (levenshtein > 0  && levenshtein >= similarValue) {
                result = param;
                similarValue = levenshtein;
            }
        }
        return result;
    }

    public void clearCache() {
        methods.clear();
        expressionList.clear();
        referenceVariables.clear();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (PsiExpression expression : expressions) {
            builder.append(expression.getText()).append(StringUtil.SPACE);
        }
        return "CodeCompleteTrigger{" + "method=" + method + ", className='" + className + '\'' + "\narguments=" + Arrays.toString(arguments) + "\nvariables=" + variables
            + "\nmethods=" + methods + "\nexpressions=" + builder + '}';
    }
}
