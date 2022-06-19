package com.yatoufang.action;

import com.yatoufang.service.TranslateService;

class Test {

    public static void main(String[] args) {
        TranslateService instance = TranslateService.getInstance();
        String result = instance.action("羽化登仙");
        System.out.println(result);
    }

}
