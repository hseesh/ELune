package com.yatoufang.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.yatoufang.ui.TemplateSettingComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/23
 */
public class AppSettingsConfigurable implements Configurable {

    private TemplateSettingComponent component;

    public AppSettingsConfigurable() {
        component = new TemplateSettingComponent();
    }

    @Override
    public String getDisplayName() {
        return "ELune Setting";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return component.init();
    }

    @Override
    public boolean isModified() {
        AppSettingService service = AppSettingService.getInstance();
        return !component.getAuthor().equals(service.author) &&
                !component.getDataConfigPath().equals(service.dataConfigPath);
    }

    @Override
    public void apply() throws ConfigurationException {
        AppSettingService service = AppSettingService.getInstance();
        service.author = component.getAuthor();
        service.dataConfigPath = component.getDataConfigPath();
        service.loadState(service);
    }

    @Override
    public void disposeUIResources() {
        component = null;
        Configurable.super.disposeUIResources();
    }
}
