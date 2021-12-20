package com.yatoufang.test.filetype;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author GongHuang（hse）
 * @since 2021/12/20
 */
public class MindMapFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(MindMapFileType.INSTANCE, MindMapFileType.DEFAULT_EXTENSION);
    }
}
