package com.yatoufang.service;

import com.intellij.openapi.util.io.FileUtil;
import com.yatoufang.config.AppSettingService;
import com.yatoufang.utils.DataUtil;
import com.yatoufang.utils.StringUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author User
 */
public class VelocityService {

    private static VelocityService instance;

    private VelocityEngine velocityEngine;

    public static VelocityService getInstance() {
        if (instance == null) {
            instance = new VelocityService();
            instance.init();
        }
        return instance;
    }

    private void init() {
        velocityEngine = new VelocityEngine();
        Thread currentThread = Thread.currentThread();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            currentThread.setContextClassLoader(getClass().getClassLoader());
            velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
            velocityEngine.init();
        } finally {
            currentThread.setContextClassLoader(classLoader);
        }
    }

    public String execute(String filePath, Object object) {
        VelocityContext context = new VelocityContext();
        AppSettingService settingService = AppSettingService.getInstance();
        context.put("now", DataUtil.now());
        context.put("table", object);
        context.put("author", settingService.author);
        return execute(filePath, context);
    }


    @Nullable
    private String execute(String filePath, VelocityContext context) {
        StringWriter stringWriter = new StringWriter();
        String text = StringUtil.EMPTY;
        try {
            InputStream resourceAsStream = VelocityService.class.getResourceAsStream(filePath);
            if (resourceAsStream == null) {
                return null;
            }
            text = FileUtil.loadTextAndClose(resourceAsStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        velocityEngine.evaluate(context, stringWriter, "myLog", text);
        return stringWriter.toString();
    }
}

