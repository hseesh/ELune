package com.yatoufang.complete.model.context;

import com.google.common.collect.Maps;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiVariable;
import com.yatoufang.entity.Config;
import com.yatoufang.entity.Method;
import com.yatoufang.entity.Param;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author GongHuang(hse)
 * @since 2023/6/2
 */
public class CodeCompleteContext {

    private Method dataBase;

    private Method dataBaseList;

    private final Map<String, Config> configMap = Maps.newHashMap();

    private final Map<String, Method> methodMap = Maps.newHashMap();

    private final Map<String, Param> variableNameMap = Maps.newHashMap();

    private final Map<String, Collection<Param>> variableTypeMap = Maps.newHashMap();

    public Config getConfig(String key) {
        return configMap.get(key);
    }

    public Collection<Param> getParam(String type, String name) {
        if (name != null) {
            Param param = variableNameMap.get(name);
            if (type != null) {
                if (type.equals(param.getTypeAlias())) {
                    return Collections.singletonList(param);
                }
            }
        }
        if (type != null) {
            return variableTypeMap.get(type);
        }
        return Collections.emptyList();
    }

    public String getArgumentList(CodeCompleteTrigger triggerContext, List<Param> params) {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        PsiParameter[] arguments = triggerContext.getArguments();
        for (Param param : params) {
            if (index != 1 && index <= params.size()) {
                builder.append(StringUtil.COMMA).append(StringUtil.SPACE);
            }
            index++;
            boolean isSkip = false;
            for (PsiParameter argument : arguments) {
                if (param.getName().equals(argument.getName())) {
                    builder.append(argument.getName());
                    isSkip = true;
                    break;
                }
            }
            if (isSkip) {
                continue;
            }
            for (PsiVariable variable : triggerContext.getVariables()) {
                if (param.getName().equals(variable.getName())) {
                    builder.append(variable.getName());
                    isSkip = true;
                    break;
                }
            }
            if (isSkip) {
                continue;
            }
            for (PsiVariable variable : triggerContext.getVariables()) {
                if (DataUtil.isSameBasicType(param.getTypeAlias(), variable.getType().getPresentableText())) {
                    builder.append(variable.getName());
                    isSkip = true;
                    break;
                }
            }
            if (isSkip) {
                continue;
            }
            Param prepareParam = triggerContext.getReferenceVariables(param.getName(), param.getTypeAlias());
            if (prepareParam != null) {
                builder.append(prepareParam.getName());
                continue;
            }
            Param namePrepare = variableNameMap.get(param.getName());
            if (namePrepare != null) {
                if (namePrepare.getTypeAlias().equals(param.getTypeAlias())) {
                    builder.append(namePrepare.getName());
                    continue;
                }
            }
            Collection<Param> paramCollection = variableTypeMap.get(param.getTypeAlias());
            if (paramCollection != null) {
                for (Param typeParam : paramCollection) {
                    builder.append(typeParam.getName());
                    break;
                }
                continue;
            }
            Method method = methodMap.get(param.getTypeAlias());
            if (method != null) {
                builder.append(method.getName());
                continue;
            }
            double similarValue = 0;
            String result = null;
            for (PsiVariable variable : triggerContext.getVariables()) {
                String name = variable.getName();
                if (name == null) {
                    continue;
                }
                double levenshtein = DataUtil.calculateLevenshtein(param.getName(), name);
                if (levenshtein >= similarValue && variable.getType().getPresentableText().equals(param.getTypeAlias())) {
                    result = name;
                    similarValue = levenshtein;
                }
            }
            if (similarValue > 0) {
                builder.append(result);
            }
        }
        return builder.toString();
    }

    public void update(String key, Method value) {
        methodMap.put(key, value);
    }

    public void update(String key, Config value) {
        configMap.put(key, value);
    }

    public Method getMethod(String key) {
        return methodMap.get(key);
    }

    public void update(String key, Param value) {
        variableNameMap.put(key, value);
        String presentableText = value.getTypeAlias();
        Collection<Param> params = variableTypeMap.computeIfAbsent(presentableText, k -> Sets.newHashSet());
        params.add(value);
    }

    public void clearCache() {
        synchronized (variableTypeMap) {
            List<String> dropKeys = Lists.newArrayList();
            for (Map.Entry<String, Collection<Param>> entry : variableTypeMap.entrySet()) {
                Collection<Param> params = entry.getValue();
                List<Param> dropList = Lists.newArrayList();
                for (Param param : params) {
                    if (param.isRequired()) {
                        continue;
                    }
                    dropList.add(param);
                }
                params.removeAll(dropList);
                if (entry.getValue().isEmpty()) {
                    dropKeys.add(entry.getKey());
                }
            }
            dropKeys.forEach(variableTypeMap::remove);
        }
        synchronized (variableNameMap) {
            variableNameMap.entrySet().removeIf(e -> !e.getValue().isRequired());
        }
        synchronized (methodMap) {
            methodMap.clear();
        }
    }

    public Method getDataBase() {
        return dataBase;
    }

    public void setDataBase(Method dataBase) {
        this.dataBase = dataBase;
    }

    public Method getDataBaseList() {
        return dataBaseList;
    }

    public void setDataBaseList(Method dataBaseList) {
        this.dataBaseList = dataBaseList;
    }

    @Override
    public String toString() {
        return "dataBase=" + dataBase + "\ndataBaseList=" + dataBaseList + "\nconfigMap=" + configMap + "\nmethodMap=" + methodMap + "\nvariableNameMap=" + variableNameMap
            + "\nvariableTypeMap=" + variableTypeMap;
    }
}
