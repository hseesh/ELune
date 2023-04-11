package com.yatoufang.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.components.JBList;
import com.yatoufang.preference.AccessPreferenceService;
import com.yatoufang.preference.impl.access.CollectionAccessHandler;
import com.yatoufang.preference.model.AccessContentVariable;
import com.yatoufang.service.VelocityService;
import com.yatoufang.templet.Application;
import com.yatoufang.ui.component.TextCellRender;
import com.yatoufang.ui.dialog.TextChooseDialog;
import com.yatoufang.utils.*;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author GongHuang（hse）
 * @since 2022/10/17
 */
public class CreateAccessAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        PsiClass aClass = DataUtil.getClass(event);
        if (aClass == null) {
            return;
        }
        PsiFieldMember[] members = PSIUtil.buildFieldMember(aClass);
        List<PsiFieldMember> psiFieldMembers = SwingUtils.showFieldsDialog(members, true, true);
        List<PsiField> fields = Lists.newArrayList();
        for (PsiFieldMember member : psiFieldMembers) {
            fields.add(member.getElement());
        }
        Map<String, Collection<String>> fieldsMap = Maps.newHashMap();
        AccessPreferenceService preferenceService = AccessPreferenceService.getInstance();
        for (PsiField field : fields) {
            Collection<String> collection = fieldsMap.computeIfAbsent(field.getName(), k -> Lists.newArrayList());
            Collection<String> action = preferenceService.action(field);
            if (action != null) {
                collection.addAll(action);
            }
        }
        CollectionAccessHandler collectionAccessHandler = new CollectionAccessHandler();
        Map<String, AccessContentVariable> variableMap = Maps.newHashMap();
        for (PsiField field : fields) {
            AccessContentVariable variable = AccessContentVariable.valueOf(field.getName());
            String description = PSIUtil.getDescription(field.getDocComment());
            description = StringUtil.collectChineseCharacter(description);
            variable.setDescription(description);
            variableMap.put(field.getName(), variable);
            if (field.getType().getPresentableText().contains(Map.class.getSimpleName())) {
                String text = field.getType().getPresentableText().replace(String.valueOf(StringUtil.SPACE), StringUtil.EMPTY);
                String value = StringUtil.subString(text, String.valueOf(StringUtil.COMMA), String.valueOf(StringUtil.GRATE_THEN));
                String key = StringUtil.subString(text, String.valueOf(StringUtil.LESS_THEN), String.valueOf(StringUtil.COMMA)).trim();
                if (Long.class.getSimpleName().equals(key)) {
                    key = long.class.getSimpleName();
                } else if (Integer.class.getSimpleName().equals(key)) {
                    key = int.class.getSimpleName();
                }
                variable.setKey(key);
                variable.setValue(value);
            }
            String alias = field.getName().replace(Map.class.getSimpleName(), StringUtil.EMPTY).replace(List.class.getSimpleName(), StringUtil.EMPTY);
            variable.setAlias(StringUtil.toUpperCaseForFirstCharacter(alias));
            variable.setType(field.getType().getPresentableText());
            if (collectionAccessHandler.check(field)) {
                String metaType = StringUtil.getMetaType(field.getType().getPresentableText()).trim();
                if (Long.class.getSimpleName().equals(metaType)) {
                    metaType = long.class.getSimpleName();
                } else if (Integer.class.getSimpleName().equals(metaType)) {
                    metaType = int.class.getSimpleName();
                }
                variable.setType(metaType);
            }
        }
        if (variableMap.isEmpty()) {
            return;
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JBList<String> list = new JBList<>(listModel);
        list.setCellRenderer(new TextCellRender());
        VelocityService velocityService = VelocityService.getInstance();
        Set<String> resultSet = Sets.newHashSet();
        for (Map.Entry<String, Collection<String>> entry : fieldsMap.entrySet()) {
            listModel.clear();
            String key = entry.getKey();
            AccessContentVariable variable = variableMap.get(key);
            for (String content : entry.getValue()) {
                String result = velocityService.execute(content, variable, null);
                listModel.addElement(result);
            }
            TextChooseDialog dialog = new TextChooseDialog(list, resultSet, listModel, key);
            dialog.show();
        }
        if (resultSet.isEmpty()) {
            return;
        }
        WriteCommandAction.runWriteCommandAction(Application.project, () -> {
            for (String content : resultSet) {
                PsiMethod method = BuildUtil.createMethod(aClass, content);
                aClass.add(method);
            }
        });
    }
}
