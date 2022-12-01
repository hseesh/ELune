package com.yatoufang.config.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2022/1/23
 */
@State(
        name = "com.yatoufang.elune.app.base.setting",
        storages = {@Storage("com.yatoufang.elune.app.base.setting.xml")}
)
public class AppSettingService implements PersistentStateComponent<AppSettingService> {

    public String author;
    public String dataConfigPath;

    public static AppSettingService getInstance(){
        AppSettingService service = ServiceManager.getService(AppSettingService.class);
        if (service == null) {
            service = new AppSettingService();
        }
        return service;
    }

    @Override
    public void loadState(@NotNull AppSettingService service){
        XmlSerializerUtil.copyBean(service, this);
    }

   @Override
   public AppSettingService getState(){ return this; }
}
