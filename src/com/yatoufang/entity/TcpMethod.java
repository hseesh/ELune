package com.yatoufang.entity;

import com.yatoufang.utils.StringUtil;

public class TcpMethod {
    private String methodName;

    private String Content;

    private String moduleCode;

    private String cmdCode;

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
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "td.ServerDelegate.sendMessage({" +
                "  moduleCode= " + moduleCode + ", cmdCode= " + cmdCode + (Content.isEmpty() ? StringUtil.EMPTY : ", ") + Content +
                "})";
    }
}
