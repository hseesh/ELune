package com.yatoufang.entity;

import java.util.Date;

/**
 * @author hse
 * @date 2021/4/14 0014
 */
public class MyCookie{

    public String name;
    public String value;
    public String cookieComment;
    public String cookieDomain;
    public Date cookieExpiryDate;
    public String cookiePath;
    public boolean isSecure;
    public int cookieVersion;

    public MyCookie(String name, String value, String cookieComment, String cookieDomain, Date cookieExpiryDate, String cookiePath, boolean isSecure, int cookieVersion) {
        this.name = name;
        this.value = value;
        this.cookieComment = cookieComment;
        this.cookieDomain = cookieDomain;
        this.cookieExpiryDate = cookieExpiryDate;
        this.cookiePath = cookiePath;
        this.isSecure = isSecure;
        this.cookieVersion = cookieVersion;
    }
}
