package com.yatoufang.entity;


import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.StringUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author hse
 * @Date: 2021/1/13
 */
public class Method {

    private String description;
    private String url;
    private String requestMethod;
    private String returnType;
    private String returnValue;
    private String returnDescription = "";
    private String name;
    private String requestExample;
    private String responseExample;
    private List<Param> params;
    private transient String argumentList;
    private transient String recommendName;


    public String getResponseExample() {
        return responseExample;
    }

    public void setResponseExample(String responseExample) {
        this.responseExample = responseExample;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getReviseUrl() {
        if (url.startsWith("/")) {
            return url.substring(1);
        }
        return url;
    }

    public void setUrl(String url) {
        //handle Restfull method
        int index = url.indexOf("{");
        if (index > 0) {
            url = url.substring(0, index);
        }
        this.url = url;
    }

    public void setParamDescription(HashMap<String, String> description) {
        if (description.size() > 0) {
            for (Param param : params) {
                String alias = param.getAlias();
                String desc = description.get(alias);
                if (desc != null) {
                    param.setDescription(desc);
                }
            }
        }
    }


    public void setRequestMethod(String requestMethod) {
        if (requestMethod != null) {
            if (requestMethod.contains(".")) {
                String[] split = requestMethod.split("\\.");
                this.requestMethod = split[1];
            } else {
                this.requestMethod = requestMethod;
            }
        }
    }

    public void setRequestExample(String requestExample) {

        if ("POST".equals(requestMethod) || "PUT".equals(requestMethod)) {
            this.requestExample = requestExample;
        } else {
            StringBuilder urlBuilder = new StringBuilder(requestExample);
            if (params.size() > 0) {
                urlBuilder.append("?");
                for (int i = 0; i < params.size(); i++) {
                    Param param = params.get(i);
                    urlBuilder.append(param.getName().trim());
                    urlBuilder.append("=");
                    //@RequestMapping defaultValue attr is "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n"
                    String defaultValue = param.getValue();
                    if (defaultValue == null || !defaultValue.startsWith("\\n")) {
                        urlBuilder.append(param.getName());
                    }
                    if (i != params.size() - 1) {
                        urlBuilder.append("&");
                    }

                }
            }
            this.requestExample = urlBuilder.toString();
        }
    }


    public String getRequestMethod() {
        return "Type: " + requestMethod;
    }

    public String getRequestType() {
        return requestMethod;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnDescription() {
        return returnDescription;
    }

    public void setReturnDescription(String returnDescription) {
        this.returnDescription = returnDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (returnType != null) {
            String result = this.name.replace(ProjectKeys.GET, StringUtil.EMPTY);
            recommendName = StringUtil.toLowerCaseForFirstChar(result);
            if (returnType.contains(ProjectKeys.RESULT_OF)) {
                recommendName += ProjectKeys.RESULT_OF;
            }
        }
    }

    public String getRequestExample() {
        return requestExample;
    }


    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }


    public String getReturnList() {
        return "| " + returnValue + " | " + returnType + " | " + returnDescription + " |";
    }

    public String getRecommendName() {
        return recommendName;
    }

    public void setRecommendName(String recommendName) {
        this.recommendName = recommendName;
    }

    public String getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(String argumentList) {
        this.argumentList = argumentList;
    }

    @Override
    public String toString() {
        return "Method{" +
                "description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", returnType='" + returnType + '\'' +
                ", returnValue='" + returnValue + '\'' +
                ", returnDescription='" + returnDescription + '\'' +
                ", name='" + name + '\'' +
                ", requestExample='" + requestExample + '\'' +
                ", params=" + (params == null ? "" : params.toString()) +
                '}';
    }

}
