package com.yatoufang.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.util.Query;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.GenericAttributeValue;
import com.yatoufang.utils.PSIUtil;
import com.yatoufang.entity.Param;
import com.yatoufang.entity.TemplateMethod;
import com.yatoufang.templet.Application;
import com.yatoufang.service.NotifyService;
import com.yatoufang.config.ProjectSearchScope;
import com.yatoufang.templet.Annotations;
import com.yatoufang.ui.CodeGeneratorDialog;
import com.yatoufang.xml.Mapper;
import com.yatoufang.xml.Result;
import com.yatoufang.xml.ResultMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author hse
 * @date 2021/6/12 0012
 */
public class CodeGeneratorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(LangDataKeys.EDITOR);
        PsiJavaFile file = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        if (file == null || editor == null) return;

        PsiElement[] metadata = new PsiElement[2];

        if (!obtainData(metadata, editor, file)) {
            return;
        }

        TemplateMethod method = new TemplateMethod(metadata);

        DomService domService = DomService.getInstance();
        ProjectSearchScope searchScope = new ProjectSearchScope(Application.project);
        List<DomFileElement<Mapper>> mapperFiles = domService.getFileElements(Mapper.class, Application.project, searchScope);

        if (mapperFiles.size() == 0) {
            NotifyService.notifyWarning("No Xml File Scanned");
            return;
        }

        ArrayList<String> formObject = new ArrayList<>();
        ArrayList<Param> formFields = new ArrayList<>();
        ArrayList<Param> superFormFields = new ArrayList<>();
        ArrayList<String> services = new ArrayList<>();
        ArrayList<String> daoObjects = new ArrayList<>();

        PsiClass[] classes = file.getClasses();
        for (PsiClass aClass : classes) {
            if (aClass.hasAnnotation(Annotations.SERVICE)) {
                services.add(aClass.getName());
                searchAll(mapperFiles, formObject, formFields, superFormFields, aClass, services, daoObjects);
            } else {
                PsiField[] fields = aClass.getFields();
                for (PsiField field : fields) {
                    PsiType type = field.getType();
                    if (Application.isBasicType(type.getPresentableText())) {
                        continue;
                    }
                    PsiClass targetClass = PSIUtil.findClass(type.getCanonicalText(), searchScope);
                    if (targetClass != null && targetClass.isInterface()) {
                        Query<PsiClass> searchService = ClassInheritorsSearch.search(targetClass, searchScope, true);
                        PsiClass targetService = searchService.findFirst();

                        searchAll(mapperFiles, formObject, formFields, superFormFields, targetService, services, daoObjects);
                    }
                }
            }
        }


        method.setEntity(formObject);
        method.setService(services);
        method.setDao(daoObjects);
        formFields = PSIUtil.removeDuplicates(formFields);
        superFormFields = PSIUtil.removeDuplicates(superFormFields);
        JBPopupFactory instance = JBPopupFactory.getInstance();
        CodeGeneratorDialog dialog = new CodeGeneratorDialog(method, formObject, formFields, superFormFields, file);

        instance.createComponentPopupBuilder(dialog.getRootPanel(), null)
                .setTitle("My Code")
                .setMovable(true)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setCancelButton(new IconButton("Close", AllIcons.Actions.Cancel))
                .setRequestFocus(true)
                .setMinSize(new Dimension(1600, 700))
                .createPopup()
                .showInFocusCenter();
    }

    private void searchAll(List<DomFileElement<Mapper>> mapperFiles, ArrayList<String> formObject, ArrayList<Param> formFields,
                           ArrayList<Param> superFormFields, PsiClass targetService, ArrayList<String> services, ArrayList<String> daoObjects) {
        if (targetService != null && targetService.hasAnnotation(Annotations.SERVICE)) {
            services.add(targetService.getName());
            PsiField[] serviceFields = targetService.getFields();
            for (PsiField serviceField : serviceFields) {
                if (Application.isBasicType(serviceField.getType().getPresentableText())) {
                    continue;
                }
                boolean hasAdded = true;
                String canonicalText = serviceField.getType().getCanonicalText();
                for (DomFileElement<Mapper> mapperFile : mapperFiles) {
                    Mapper mapper = mapperFile.getRootElement();
                    GenericAttributeValue<String> namespace = mapper.getNamespace();
                    String nameId = namespace.getRawText();
                    if (canonicalText.equals(nameId)) {
                        List<ResultMap> resultMaps = mapper.getResultMaps();
                        for (ResultMap resultMap : resultMaps) {
                            GenericAttributeValue<String> mapType = resultMap.getType();
                            if (mapType != null) {
                                PsiClass finalTarget = PSIUtil.findClass(mapType.getValue());
                                if (finalTarget != null) {
                                    formObject.add(finalTarget.getName());
                                    if (hasAdded) {
                                        daoObjects.add(serviceField.getName());
                                        hasAdded = false;
                                    }
                                    PSIUtil.getClassFields(finalTarget, formFields, superFormFields, false, serviceField.getType());
                                    if (formFields.size() == 0 && superFormFields.size() == 0) {
                                        loadFields(formFields, resultMap.getTableId(), resultMap.getColumnInfo());
                                    } else {
                                        merge(formFields, resultMap.getTableId(), resultMap.getColumnInfo());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void loadFields(ArrayList<Param> formFields, List<Result> tableId, List<Result> columnInfo) {
        for (Result result : tableId) {
            formFields.add(new Param(result.getProperty().getRawText(), result.getColumn().getRawText(), result.getJdbcType().getRawText()));
        }
        for (Result result : columnInfo) {
            formFields.add(new Param(result.getProperty().getRawText(), result.getColumn().getRawText(), result.getJdbcType().getRawText()));
        }
    }

    private void merge(ArrayList<Param> formFields, List<Result> tableId, List<Result> columnInfo) {
        for (Param formField : formFields) {
            String name = formField.getName();
            for (Result result : tableId) {
                if (name.equals(result.getProperty().getRawText())) {
                    formField.setAlias(result.getColumn().getRawText());
                }
            }
            for (Result result : columnInfo) {
                if (name.equals(result.getProperty().getRawText())) {
                    formField.setAlias(result.getColumn().getRawText());
                }
            }
        }
    }


    private boolean obtainData(PsiElement[] metadata, Editor editor, PsiJavaFile file) {
        int elementCount = 0;
        HashSet<String> container = new HashSet<>();
        int startPoint = editor.getSelectionModel().getSelectionStart();
        int endPoint = editor.getSelectionModel().getSelectionEnd() - 1;

        for (int i = startPoint; i < endPoint; i++) {
            PsiElement element = file.findElementAt(i);
            if (element != null && !(element instanceof PsiWhiteSpace)) {
                if (container.add(element.getText())) {
                    if (elementCount < 2) {
                        metadata[elementCount++] = element;
                    } else {
                        return false;
                    }
                }
            }
        }
        for (String element : container) {
            System.out.println("element = " + element);
        }

        return true;
    }
}
