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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        List<String> fields = Lists.newArrayList();
        List<SerializeLayer> infos = Lists.newArrayList();
        List<Field> classField = PSIUtil.getClassField(aClass);
        List<String> methods = Arrays.stream(aClass.getMethods()).map(PsiMethod::getName).collect(Collectors.toList());
        for (Field field : classField) {
            if (!field.getTypeAlias().equals(String.class.getName())) {
                fields.add(field.getName());
                continue;
            }
            SerializeLayer serializeLayer = new SerializeLayer();
            boolean flag = StringUtil.getAllCharacter(field.getDescription(), serializeLayer.getNames());
            if (serializeLayer.isEmpty()) {
                continue;
            }
            for (int i = 0; i < serializeLayer.getNames().size(); i++) {
                String name = serializeLayer.getNames().get(i);
                if (name.contains(ProjectKeys.KEY)) {
                    serializeLayer.addTypes(String.class.getSimpleName());
                } else {
                    if (i == serializeLayer.getNames().size() - 1 && serializeLayer.getNames().size() > 1) {
                        Field firstFields = PSIUtil.getFirstFields(name);
                        if (firstFields != null) {
                            if (firstFields.getTypeAlias().equals(long.class.getName())) {
                                serializeLayer.getTypes().set(serializeLayer.getTypes().size() - 1, long.class.getSimpleName());
                            } else {
                                serializeLayer.getTypes().add(int.class.getSimpleName());
                            }
                            serializeLayer.setLastKey(firstFields.getName());
                        } else {
                            serializeLayer.getTypes().add(int.class.getSimpleName());
                        }
                    } else {
                        serializeLayer.getTypes().add(int.class.getSimpleName());
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
                if (fields.contains(field.getName())) {
                    continue;
                }
                aClass.add(field);
            }
        });
        int index = 0;
        for (String method : methods) {
            maps.remove(method.trim());
        }
        for (String value : maps.values()) {
            executor.schedule(() -> {
                WriteCommandAction.runWriteCommandAction(Application.project, () -> {
                    PsiMethod method = BuildUtil.createMethod(aClass, value);
                    aClass.add(method);
                });
            }, (long) Calendar.HOUR * Calendar.ZONE_OFFSET * index++, TimeUnit.MILLISECONDS);
        }

    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
