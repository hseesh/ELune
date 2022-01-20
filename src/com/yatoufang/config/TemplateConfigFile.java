package com.yatoufang.config;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.yatoufang.entity.ConfigParam;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author GongHuang（hse）
 * @since 2022/1/19
 */
@State(
        name = "com.yatoufang.config.template.params",
        storages = {@Storage("com.yatoufang.template.params.xml")}
)
public class TemplateConfigFile {

   public Set<ConfigParam> params = new HashSet<>();

    public static TemplateConfigFile getInstance(){
        TemplateConfigFile service = ServiceManager.getService(TemplateConfigFile.class);
        if (service == null) {
            service = new TemplateConfigFile();
        }
        return service;
    }

    public Map<String,ConfigParam> getMaps(){
        return params.stream().collect(Collectors.toMap(ConfigParam::getName, v -> v));
    }

    public TemplateConfigFile getFile(){return  this;}

    public void loadFile(@NotNull TemplateConfigFile file) {
        XmlSerializerUtil.copyBean(file, this);
    }
}
