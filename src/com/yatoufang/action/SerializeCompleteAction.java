package com.yatoufang.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.complete.SerializeLayer;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.ProjectKeys;
import com.yatoufang.thread.ELuneScheduledThreadPoolExecutor;
import com.yatoufang.utils.*;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author GongHuang（hse）
 * @since 2023/3/30
 */
public class SerializeCompleteAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        PsiClass aClass = DataUtil.getClass(anActionEvent);
        if (aClass == null) {
            return;
        }
        List<SerializeLayer> infos = Lists.newArrayList();
        List<Field> classField = PSIUtil.getClassField(aClass);
        for (Field field : classField) {
            SerializeLayer serializeLayer = new SerializeLayer();
            if (!field.getTypeAlias().equals(String.class.getName())) {
                continue;
            }
            boolean flag = StringUtil.getAllCharacter(field.getDescription(), serializeLayer.getNames());
            if (serializeLayer.isEmpty()) {
                continue;
            }
            for (int i = 0; i < serializeLayer.getNames().size(); i++) {
                String name = serializeLayer.getNames().get(i);
                if (name.contains(ProjectKeys.KEY)) {
                    serializeLayer.addTypes(String.class.getSimpleName());
                } else {
                    if (i == serializeLayer.getNames().size() - 1) {
                        Field firstFields = PSIUtil.getFirstFields(name);
                        if (firstFields != null) {
                            if (firstFields.getTypeAlias().equals(Long.class.getName())) {
                                serializeLayer.getTypes().add(Long.class.getSimpleName());
                            } else {
                                serializeLayer.getTypes().add(Integer.class.getSimpleName());
                            }
                            serializeLayer.setLastKey(firstFields.getName());
                        } else {
                            serializeLayer.getTypes().add(Integer.class.getSimpleName());
                        }
                    } else {
                        serializeLayer.getTypes().add(Integer.class.getSimpleName());
                    }
                }
            }
            serializeLayer.setDescription(field.getDescription());
            serializeLayer.setName(field.getName());
            serializeLayer.setCollection(flag);
            infos.add(serializeLayer);
        }

        List<String> variables = Lists.newArrayList();
        Map<String, String> maps = Maps.newHashMap();
        for (SerializeLayer serializeLayer : infos) {
            TemplateUtil.buildOperatorMethod(serializeLayer, maps);
            String buildMap = TemplateUtil.buildMap(serializeLayer);
            if (buildMap.isEmpty()) {
                continue;
            }
            variables.add(buildMap);
        }
        ELuneScheduledThreadPoolExecutor executor = ELuneScheduledThreadPoolExecutor.getInstance();
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            for (String variable : variables) {
                PsiField field = BuildUtil.createField(aClass, variable);
                aClass.add(field);
            }
        });
        for (String value : maps.values()) {
            executor.schedule(() -> {
                WriteCommandAction.runWriteCommandAction(Application.project, () -> {
                    PsiMethod method = BuildUtil.createMethod(aClass, value);
                    aClass.add(method);
                });
            }, Calendar.HOUR * Calendar.ZONE_OFFSET * Calendar.APRIL, TimeUnit.MILLISECONDS);
        }

    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
