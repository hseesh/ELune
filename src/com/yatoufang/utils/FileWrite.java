package com.yatoufang.utils;

import com.intellij.openapi.util.io.FileUtil;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.NotifyKeys;

import java.io.File;
import java.io.IOException;

/**
 * @author GongHuang（hse）
 * @since 2022/1/20
 */
public class FileWrite {

    public static void write(String file, String path, boolean printInfo, boolean notifyInfo) {
        if (path.isEmpty()) {
            return;
        }
        ConsoleService consoleService = ConsoleService.getInstance();
        File tableFile = new File(path);
        try {
            FileUtil.writeToFile(tableFile, file);
            if(printInfo){
                consoleService.print(tableFile.getCanonicalPath() + NotifyKeys.CREATED);
            }else if(notifyInfo){
                NotifyService.notify(tableFile.getCanonicalPath() + NotifyKeys.CREATED);
            }
        } catch (IOException e) {
           if(printInfo){
               consoleService.printError(ExceptionUtil.getExceptionInfo(e));
           }
        }
    }

}
