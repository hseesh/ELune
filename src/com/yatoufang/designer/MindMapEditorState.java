package com.yatoufang.designer;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2022/3/27 0027
 */
public class MindMapEditorState implements FileEditorState {

    public static final MindMapEditorState DUMMY = new MindMapEditorState();

    @Override
    public boolean canBeMergedWith(@NotNull FileEditorState fileEditorState, @NotNull FileEditorStateLevel fileEditorStateLevel) {
        return false;
    }
}

