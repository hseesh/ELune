package com.yatoufang.entity;

import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.Collection;

/**
 * @author GongHuang（hse）
 * @since 2021/11/4 20:56
 */
public class TcpMethod {
    private String methodName;

    private String content;

    private String moduleCode;

    private String cmdCode;

    private final Collection<Param> params = Lists.newArrayList();

    public TcpMethod(String moduleCode) {
        this.moduleCode = moduleCode;
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

    public Collection<Param> getParams() {
        return params;
    }

    public void add(Param param) {
        this.params.add(param);
    }

    @Override
    public String toString() {
        return "td.ServerDelegate.sendMessage({\n" +
                "  module : " + moduleCode + ", cmd : " + cmdCode + (content.isEmpty() ? StringUtil.EMPTY : ", ") + content + "\n" +
                "})";
    }
}
