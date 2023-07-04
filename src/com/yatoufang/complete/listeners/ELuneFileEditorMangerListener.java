package com.yatoufang.complete.listeners;

import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.yatoufang.complete.handler.CodeCompleteHandler;
import com.yatoufang.entity.Method;
import com.yatoufang.entity.Param;
import com.yatoufang.service.ConsoleService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * @author GongHuang(hse)
 * @since 2023/6/3
 */
public class ELuneFileEditorMangerListener implements FileEditorManagerListener {

    public static long LAST_CHANGE_TIME;

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        if (DumbService.isDumb(event.getManager().getProject())) {
            return;
        }
        if (System.currentTimeMillis() - LAST_CHANGE_TIME < 100) {
            return;
        }
        VirtualFile file = event.getNewFile();
        if (file != null) {
            if (file.getName().endsWith(ProjectKeys.JAVA)) {
                PsiFile psiFile = PsiManager.getInstance(event.getManager().getProject()).findFile(file);
                if (!(psiFile instanceof PsiJavaFile)) {
                    return;
                }
                PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
                PsiClass[] classes = psiJavaFile.getClasses();
                if (classes.length == 0) {
                    return;
                }
                Application.project = psiFile.getProject();
                CodeCompleteHandler.CONTEXT.clearCache();
                for (PsiClass aClass : classes) {
                    if (aClass.isEnum() || aClass.isInterface()) {
                        continue;
                    }
                    parser(aClass);
                }
            }
        }
        LAST_CHANGE_TIME = System.currentTimeMillis();
    }

    public void parser(PsiClass psiClass) {
        for (PsiField field : psiClass.getFields()) {
            String fieldName = field.getName().toLowerCase(Locale.ROOT);
            PsiClass fieldClass = PSIUtil.findClass(field.getType().getCanonicalText());
            if (fieldClass == null) {
                continue;
            }
            String className = fieldClass.getName();
            if (className == null) {
                continue;
            }
            if (fieldName.endsWith(ProjectKeys.DAO)) {
                PsiMethod[] methods = fieldClass.getMethods();
                for (PsiMethod method : methods) {
                    PsiType returnType = method.getReturnType();
                    if (returnType == null) {
                        continue;
                    }
                    String metaType = StringUtil.getMetaType(returnType.getPresentableText());
                    if (className.contains(metaType) && method.getName().contains(ProjectKeys.GET)) {
                        Method dataBase = PSIUtil.parser(method);
                        if (returnType.getCanonicalText().endsWith(String.valueOf(StringUtil.GRATE_THEN))) {
                            CodeCompleteHandler.CONTEXT.setDataBaseList(dataBase);
                        } else {
                            CodeCompleteHandler.CONTEXT.setDataBase(dataBase);
                        }
                    }
                }
                continue;
            }
            if (fieldName.endsWith(ProjectKeys.FACADE)) {
                continue;
            }
            List<Param> classAllFields = PSIUtil.getClassAllFields(fieldClass);
            for (Param param : classAllFields) {
                param.setRequired(false);
                String getString = param.getGetString();
                param.setGetString(field.getName() + StringUtil.POINT + getString);
                CodeCompleteHandler.CONTEXT.update(param.getName(), param);
            }
        }
        String className = psiClass.getName();
        if (className == null) {
            return;
        }
        for (PsiMethod method : psiClass.getMethods()) {
            PsiType returnType = method.getReturnType();
            if (returnType == null) {
                continue;
            }
            String metaType = StringUtil.getMetaType(returnType.getPresentableText());
            if (className.contains(metaType)) {
                Method dataBase = PSIUtil.parser(method);
                Method contextDataBase = CodeCompleteHandler.CONTEXT.getDataBase();
                if (contextDataBase == null) {
                    continue;
                }
                String dataBaseReturnType = contextDataBase.getReturnType();
                if (dataBaseReturnType.contains(metaType) && !dataBaseReturnType.endsWith(List.class.getSimpleName())) {
                    CodeCompleteHandler.CONTEXT.setDataBase(dataBase);
                }
            }
            if (returnType.getPresentableText().contains(ProjectKeys.RESULT_OF)) {
                continue;
            }
            Method dataBase = PSIUtil.parser(method);
            CodeCompleteHandler.CONTEXT.update(method.getName(), dataBase);
        }
        ConsoleService.getInstance().printInfo(CodeCompleteHandler.CONTEXT.toString());
    }

}
