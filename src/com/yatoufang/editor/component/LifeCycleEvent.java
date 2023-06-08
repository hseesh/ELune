package com.yatoufang.editor.component;

/**
 * @author GongHuang(hse)
 * @since 2023/6/8
 */
public interface LifeCycleEvent {

    void onCreate();

    void onInitialize();

    void onSynchronized(String name, String alias);

    void onReload();
}
