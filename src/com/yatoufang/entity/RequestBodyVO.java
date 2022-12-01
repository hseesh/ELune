package com.yatoufang.entity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Gary
 * @Date 2022-04-26 20:00
 * @Description:
 */
public class RequestBodyVO {
    /**
     * Copyright 2022 json.cn
     */

    private String token;
    private String ref;
    private Map<String, String> variables;

    public RequestBodyVO(String token, String ref, Map<String, String> variables) {
        this.token = token;
        this.ref = ref;
        this.variables = variables;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    @Override
    public String toString() {
        return "RequestBodyVO{" + "token='" + token + '\'' + ", ref='" + ref + '\'' + ", variables=" + variables + '}';
    }

    public static void main(String[] args) {
        Map<String, String> variables = new HashMap<>();
        variables.put("CI_COMMIT_MESSAGE", "发布版本:game\\n123");
        RequestBodyVO release = new RequestBodyVO("67d3c9c14215b83a0d25f20e94bac9", "release", variables);
        Gson gson = new Gson();
        String s = gson.toJson(release);
        System.out.println(s);
    }

}
