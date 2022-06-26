package com.yatoufang.entity;

import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2021/11/4 20:56
 */
public class TcpMethod {
    private String methodName;

    private String alias;

    private String content;

    private String moduleCode;

    private String cmdCode;

    private String push;

    private String request;

    private String response;

    private String description;

    private String valueOf;

    private final List<Param> params = Lists.newArrayList();

    public TcpMethod(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public TcpMethod(String methodName, String description) {
        this.methodName = methodName;
        this.description = description;
        this.push = ProjectKeys.PUSH;
        this.request = ProjectKeys.REQUEST;
        this.response = ProjectKeys.RESPONSE;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = StringUtil.getChineseCharacter(methodName);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = StringUtil.getCustomerJson(content);
    }

    public List<Param> getParams() {
        return params;
    }

    public void add(Param param) {
        this.params.add(param);
        this.valueOf = DataUtil.valueOf(params);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void addAll(Collection<Param> params) {
        this.params.addAll(params);
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValueOf() {
        return valueOf;
    }

    public void setValueOf(String valueOf) {
        this.valueOf = valueOf;
    }

    @Override
    public String toString() {
        return "td.ServerDelegate.sendMessage({\n" + "  module : " + moduleCode + ", cmd : " + cmdCode + (content.isEmpty() ? StringUtil.EMPTY : ", ") + content + "\n" + "})";
    }
}
