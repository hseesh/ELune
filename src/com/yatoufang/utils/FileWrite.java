package com.yatoufang.utils;

import com.google.gson.Gson;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ParameterizedTypeImpl;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyKeys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author GongHuang（hse）
 * @since 2022/1/20
 */
public class FileWrite {

    public static void write(String file, String path, boolean printInfo, boolean notifyInfo) {
        if (path.isEmpty() || file.isEmpty()) {
            return;
        }
        File tableFile = new File(path);
        try {
            FileUtil.writeToFile(tableFile, file);
            if (printInfo) {
                ConsoleService consoleService = ConsoleService.getInstance();
                consoleService.print(tableFile.getCanonicalPath() + NotifyKeys.CREATED);
            } else if (notifyInfo) {
                NotifyService.notify(tableFile.getCanonicalPath() + NotifyKeys.CREATED);
            }
        } catch (IOException e) {
            if (printInfo) {
                ConsoleService consoleService = ConsoleService.getInstance();
                consoleService.printError(ExceptionUtil.getExceptionInfo(e));
            }
        }
    }

    public static <T> Collection<T> loadConfig(Class<?> tClass, Class<T> aClass, String filePath) {
        if (filePath == null) {
            return Collections.emptyList();
        }
        try {
            InputStream resourceAsStream = tClass.getResourceAsStream(filePath);
            if (resourceAsStream != null) {
                String config = FileUtil.loadTextAndClose(resourceAsStream);
                if (!config.isEmpty()) {
                    Type listType = new ParameterizedTypeImpl(List.class, new Class[] {aClass});
                    return new Gson().fromJson(config, listType);
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public static String getSelectedPath() {
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(fileChooserDescriptor, Application.project, null);
        for (VirtualFile virtualFile : virtualFiles) {
            return virtualFile.getPath();
        }
        return null;
    }
}
