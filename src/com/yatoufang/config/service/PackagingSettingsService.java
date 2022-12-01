package com.yatoufang.config.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Author: Gary
 * @Date 2022-04-26 16:11
 * @Description:
 */
@State(name = "com.yatoufang.elune.app.setting", storages = {@Storage("com.yatoufang.elune.app.setting.xml")})
public class PackagingSettingsService implements PersistentStateComponent<PackagingSettingsService> {

    public String url;
    public String token;

    @Override
    public @Nullable PackagingSettingsService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PackagingSettingsService packagingSettingsService) {
        XmlSerializerUtil.copyBean(packagingSettingsService, this);
    }

    public static PackagingSettingsService getInstance() {
        PackagingSettingsService service = ServiceManager.getService(PackagingSettingsService.class);
        if (service == null) {
            service = new PackagingSettingsService();
        }
        return service;
    }
}
