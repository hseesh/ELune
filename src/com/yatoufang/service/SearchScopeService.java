package com.yatoufang.service;

import com.yatoufang.config.ProjectSearchScope;
import org.w3c.dom.events.Event;

/**
 * @author GongHuang（hse）
 * @since 2021/12/16
 */
public class SearchScopeService {

    private static ProjectSearchScope service;

    public static ProjectSearchScope getInstance() {
        if(service == null){
            service = new ProjectSearchScope();
        }

        return service;
    }


}
