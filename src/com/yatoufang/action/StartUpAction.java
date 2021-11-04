package com.yatoufang.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.yatoufang.templet.Application;
import org.jetbrains.annotations.NotNull;

/**
 * @author hse
 * @date 2021/5/29 0029
 */
public class StartUpAction implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        Application.project = project;
    }

}
