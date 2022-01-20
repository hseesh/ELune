package com.yatoufang.entity;

import com.yatoufang.config.TemplateConfigFile;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GongHuang（hse）
 * @since 2022/1/20
 */
public class Config {

    private String fileName;

    private String fileNameAlias;

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

    public boolean hasParams() {
        return params.size() > 0;
    }

    public void build() {
        TemplateConfigFile instance = TemplateConfigFile.getInstance();
        Map<String, ConfigParam> maps = instance.getMaps();
        if (indexLists.size() > 0) {
            for (int i = 0; i < this.params.size(); i++) {
                String s = indexLists.get(i);
                if("sc".equals(s) || "cs".equals(s)){
                    ConfigParam param = maps.get(params.get(i));
                    if(param != null){
                        paramList.add(param);
                    }
                }
            }
        }else{
            for (String paramName : this.params) {
                ConfigParam param = maps.get(paramName);
                if(param != null){
                    paramList.add(param);
                }
            }
        }
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

