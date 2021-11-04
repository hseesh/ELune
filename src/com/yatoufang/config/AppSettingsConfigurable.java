package com.yatoufang.config;


import com.intellij.openapi.options.Configurable;
import com.yatoufang.ui.AppSettingsComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author hse
 * @Date: 2021/2/3 0003
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    public AppSettingsConfigurable() {

    }

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Custom Template";
    }


    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setRequestExampleState(settings.requestExample);
        mySettingsComponent.setResponseFieldsState(settings.responseFields);
        mySettingsComponent.setResponseExample(settings.responseExample);
        //mySettingsComponent.setResponseExample(settings.hostInfo);
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean requestExampleSate = mySettingsComponent.getRequestExampleSate();
        boolean responseFieldsState = mySettingsComponent.getResponseFieldsState();
        boolean responseExampleState = mySettingsComponent.getResponseExampleState();
       // String hostInfo = mySettingsComponent.getHostInfo();
        return !requestExampleSate == settings.requestExample || !responseFieldsState == settings.responseFields
                || !responseExampleState == settings.responseExample; // || !(hostInfo.equals(settings.hostInfo));
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.requestExample = mySettingsComponent.getRequestExampleSate();
        settings.responseFields = mySettingsComponent.getResponseFieldsState();
        settings.responseExample = mySettingsComponent.getResponseExampleState();
        //settings.hostInfo = mySettingsComponent.getHostInfo();
    }


    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }


}

