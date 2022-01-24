package com.yatoufang.entity;

import com.yatoufang.config.TemplateConfigFile;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GongHuang（hse）
 * @since 2022/1/20
 */
public class Config {

    private String fileName;

    private String fileNameAlias;

    private String fileDescription;

    private String initialize;

    private List<String> params = Lists.newArrayList();

    private List<ConfigParam> paramList = Lists.newArrayList();

    private List<String> indexLists = Lists.newArrayList();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(List<String> params) {
        this.fileName = params.get(1);
        this.fileNameAlias = params.get(0);
    }

    public String getFileNameAlias() {
        return fileNameAlias;
    }

    public void setFileNameAlias(String fileNameAlias) {
        this.fileNameAlias = fileNameAlias;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public List<String> getIndexLists() {
        return indexLists;
    }

    public void setIndexLists(List<String> indexLists) {
        this.indexLists = indexLists;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getInitialize() {
        return initialize;
    }

    public List<ConfigParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ConfigParam> paramList) {
        this.paramList = paramList;
    }

    public void setInitialize(String initialize) {
        this.initialize = initialize;
    }

    public boolean hasParams() {
        return params.size() > 0;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(List<String> fileDescription) {
        this.fileDescription = fileDescription.get(0);
    }

    public void build(HashSet<ConfigParam> sets) {
        Map<String, ConfigParam> maps = sets.stream().collect(Collectors.toMap(ConfigParam::getName, v -> v));
        if (indexLists.size() > 0) {
            for (int i = 0; i < this.params.size(); i++) {
                String s = indexLists.get(i);
                if(s.length() >= 2){
                    ConfigParam param = maps.get(params.get(i));
                    if(param != null){
                        paramList.add(param);
                    }else{
                        paramList.add(new ConfigParam(params.get(i),"String"));
                    }
                }
            }

        }else{
            for (String paramName : this.params) {
                ConfigParam param = maps.get(paramName);
                paramList.add(Objects.requireNonNullElseGet(param, () -> new ConfigParam(paramName, "String")));
            }
        }
        StringBuilder builder = new StringBuilder();
        List<ConfigParam> temp = Lists.newArrayList();
        for (ConfigParam param : paramList) {
            if (param.getAliaParam() != null) {
                temp.add(param.getAliaParam());
            }
            if (param.getReferenceExpression() == null || param.getReferenceExpression().isEmpty()) {
                continue;
            }
            builder.append(param.getReferenceExpression())
                    .append("\n");
        }
        if(fileNameAlias == null || fileNameAlias.isEmpty()){
            fileNameAlias = StringUtil.toUpperCaseWithUnderLine(fileName);
        }
        paramList.addAll(temp);
        initialize = builder.toString();
    }

    @Override
    public String toString() {
        return "Config{" +
                "fileName='" + fileName + '\'' +
                ", fileNameAlias='" + fileNameAlias + '\'' +
                ", params=" + params +
                ", indexLists=" + indexLists +
                '}';
    }


}

