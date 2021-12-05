package com.yatoufang.config;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.yatoufang.templet.Application;
import org.jetbrains.annotations.NotNull;

/**
 * @author hse
 * @date 2021/5/8 0008
 */
public class ProjectSearchScope extends GlobalSearchScope {
    private final ProjectFileIndex index;

    public ProjectSearchScope() {
        super(Application.project);
        this.index = ProjectRootManager.getInstance(Application.project).getFileIndex();
    }

    public ProjectSearchScope(Project project) {
        super(project);
        this.index = ProjectRootManager.getInstance(project).getFileIndex();
    }


    @Override
    public boolean isSearchInModuleContent(@NotNull Module aModule) {
        return false;
    }

    @Override
    public boolean isSearchInLibraries() {
        return false;
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return index.isInSourceContent(file);
    }
}
