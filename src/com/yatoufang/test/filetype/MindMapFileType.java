package com.yatoufang.test.filetype;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.yatoufang.test.lang.MyLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MindMapFileType extends LanguageFileType {

    public static final MindMapFileType INSTANCE = new MindMapFileType();
    @NonNls
    public static final String DEFAULT_EXTENSION = "mm";

    private MindMapFileType() {
        super(MyLanguage.INSTANCE);
    }

    @Override
    public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
        return "UTF-8";
    }


    @Override
    public String getName() {
        return "Mind Map";
    }


    @Override
    public String getDescription() {
        return "IDEA Mind Map files";
    }


    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.Idea_logo_welcome;
    }
}
