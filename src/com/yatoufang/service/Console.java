package com.yatoufang.service;

import com.yatoufang.entity.Table;

/**
 * @author GongHuang（hse）
 * @since 2021/11/19 19:41
 */
public interface Console {

    void init();

    void printHead(String info);

    void print(String info);

    void printInfo(String info);

    void printError(String error);

    void init(Table table,String rootPath);

}
