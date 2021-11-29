package com.yatoufang.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.yatoufang.entity.Param;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hse
 * @Date: 2021/2/3
 */
//@State(
//        name = "com.yatoufang.config.VariableTemplate",
//        storages = {@Storage("com.yatoufang.VariableTemplate.xml")}
//)
public class VariableTemplateState  {

    public List<Param> params;

    public static VariableTemplateState getInstance() {
        VariableTemplateState service = ServiceManager.getService(VariableTemplateState.class);
        service = service == null ? new VariableTemplateState() : service;
        if(service.params == null || service.params.size() == 0){
            service.params = initData();
        }
        return service;
    }


    public VariableTemplateState getState() {
        return this;
    }


    public void loadState(@NotNull VariableTemplateState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    private static List<Param> initData() {
        ArrayList<Param> params = new ArrayList<>();
        params.add(new Param("startTime","String",false,"","start time"));
        params.add(new Param("endTime","String",false,"","end time"));
        params.add(new Param("page","Integer",false,"1","page number"));
        params.add(new Param("limit","Integer",false,"20","page size"));
        return params;
    }

}
