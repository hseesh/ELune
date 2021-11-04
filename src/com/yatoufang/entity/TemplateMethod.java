package com.yatoufang.entity;

import com.intellij.psi.PsiElement;

import java.util.Arrays;
import java.util.List;

/**
 * @author hse
 * @date 2021/6/10 0010
 */
public class TemplateMethod {

    private String name;
    private String returnType;
    private String contentType;
    private String annotation;
    private String description;
    private String requestMethod;
    private String accessAuthority;

    private List<Param> params;
    private List<String> entity;
    private List<String> service;
    private List<String> dao;

    private String codeTemplate;

    private String selectedEntity;
    private String selectedService;
    private String selectedDao;

    private final PsiElement[] originElement;

    public TemplateMethod(PsiElement[] originElement) {
        this.originElement = originElement;
        this.name = originElement[1].getText();
        this.returnType = originElement[0].getText();
        this.contentType = "RequestParam";
        this.requestMethod = "GetMapping";
        this.accessAuthority = "public";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getAccessAuthority() {
        return accessAuthority;
    }

    public void setAccessAuthority(String accessAuthority) {
        this.accessAuthority = accessAuthority;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public List<String> getEntity() {
        return entity;
    }

    public void setEntity(List<String> entity) {
        this.entity = entity;
        if(entity != null && entity.size() > 0){
            this.selectedEntity = entity.get(0);
        }
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
        if(service != null && service.size() > 0){
            this.selectedService = service.get(0);
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public PsiElement getOriginElement() {
        return originElement[0];
    }

    public PsiElement getDeleteElement() {
        return originElement[1];
    }

    public List<String> getDao() {
        return dao;
    }

    public void setDao(List<String> dao) {
        this.dao = dao;
        if(dao != null && dao.size() > 0){
            this.selectedDao = dao.get(0);
        }
    }

    public String getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(String selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public String getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(String selectedService) {
        this.selectedService = selectedService;
    }

    public String getSelectedDao() {
        return selectedDao;
    }

    public void setSelectedDao(String selectedDao) {
        this.selectedDao = selectedDao;
    }

    public String getCodeTemplate() {
        return codeTemplate;
    }

    public void setCodeTemplate(String codeTemplate) {
        this.codeTemplate = codeTemplate;
    }

    @Override
    public String toString() {
        return "TemplateMethod{" +
                "name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", contentType='" + contentType + '\'' +
                ", annotation='" + annotation + '\'' +
                ", description='" + description + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", accessAuthority='" + accessAuthority + '\'' +
                ", params=" + params +
                ", entity=" + entity +
                ", service=" + service +
                ", dao=" + dao +
                ", selectedEntity='" + selectedEntity + '\'' +
                ", selectedService='" + selectedService + '\'' +
                ", selectedDao='" + selectedDao + '\'' +
                ", originElement=" + Arrays.toString(originElement) +
                '}';
    }
}
