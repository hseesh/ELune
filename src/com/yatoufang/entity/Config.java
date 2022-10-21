package com.yatoufang.entity;

import com.yatoufang.preference.ConfigPreferenceService;
import com.yatoufang.utils.StringUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        ConfigPreferenceService service = ConfigPreferenceService.getInstance();
        System.out.println(params);
        Map<String, ConfigParam> maps = sets.stream().collect(Collectors.toMap(ConfigParam::getName, v -> v));
        if (indexLists.size() > 0) {
            for (int i = 0; i < this.params.size(); i++) {
                String s = indexLists.get(i);
                if (s.length() >= 2) {
                    ConfigParam param = maps.get(params.get(i));
                    if (param != null) {
                        paramList.add(param);
                    } else {
                        ConfigParam configParam = new ConfigParam(params.get(i), "String");
                        service.action(configParam);
                        paramList.add(configParam);
                    }
                }
            }

        } else {
            for (String paramName : this.params) {
                ConfigParam param = maps.get(paramName);
                if (param == null) {
                    ConfigParam configParam = new ConfigParam(paramName, "String");
                    service.action(configParam);
                    paramList.add(configParam);
                } else {
                    paramList.add(param);
                }
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
            builder.append(param.getReferenceExpression()).append("\n");
        }
        if (fileNameAlias == null || fileNameAlias.isEmpty()) {
            fileNameAlias = StringUtil.toLowerCaseWithUnderLine(fileName);
        }
        paramList.addAll(temp);
        initialize = builder.toString();
    }

    @Override
    public String toString() {
        return "Config{" + "fileName='" + fileName + '\'' + ", fileNameAlias='" + fileNameAlias + '\'' + ", params=" + params + ", indexLists=" + indexLists + '}';
    }

}

