package com.yatoufang.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.yatoufang.config.service.PackagingSettingsService;
import com.yatoufang.ui.PackagingSettingsWindows;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @Author: Gary
 * @Date 2022-04-26 11:39
 * @Description:
 */
public class PackagingSettingsConfigurable implements Configurable {

    private PackagingSettingsWindows component;

    public PackagingSettingsConfigurable() {
        this.component = new PackagingSettingsWindows();
    }

    @Override
    public String getDisplayName() {
        return "Packaging Setting";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return component.init();
    }

    @Override
    public boolean isModified() {
        PackagingSettingsService service = PackagingSettingsService.getInstance();
        return !component.getUrlText().equals(service.url) || !component.getTokenText().equals(service.token);
    }

    @Override
    public void apply() throws ConfigurationException {
        PackagingSettingsService service = PackagingSettingsService.getInstance();
        service.url = component.getUrlText();
        service.token = component.getTokenText();
        service.loadState(service);
    }

    @Override
    public void disposeUIResources() {
        component = null;
        Configurable.super.disposeUIResources();
    }
}
