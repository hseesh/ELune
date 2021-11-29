package com.yatoufang.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


/**
 * @author hse
 * @Date: 2021/2/3
 */
//@State(
//        name = "com.yatoufang.config.CodeTemplate",
//        storages = {@Storage("com.yatoufang.CodeTemplate.xml")}
//)
public class CodeTemplateState{

    public HashMap<String, String> codeMap;

    public static CodeTemplateState getInstance() {
        CodeTemplateState service = ServiceManager.getService(CodeTemplateState.class);
        service = service == null ? new CodeTemplateState() : service;
        if (service.codeMap == null || service.codeMap.size() == 0) {
            service.codeMap = initData();
        }
        service.codeMap = initData();
        return service;
    }


    public CodeTemplateState getState() {
        return this;
    }

    public void loadState(@NotNull CodeTemplateState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    private static HashMap<String, String> initData() {

        HashMap<String, String> codeMap = new HashMap<>();
        codeMap.put("DEMO_ONE","@$requestMethod(\"/$methodName\")\n" +
                "    @ApiOperation(\"\")\n" +
                "    $accessAuthority $returnType $methodName(\n" +
                "#foreach($param in $params)\n" +
                "                     @$contentType(value = \"$param.name\", required = false) $param.typeAlias $param.name,\n" +
                "#end" +

                ") {\n" +
                "\n" +
                "        Page<$selectedEntity> page = new Page<>(1, 20);\n" +
                "\n" +
                "        QueryWrapper<$selectedEntity> queryWrapper = new QueryWrapper<>();\n" +
                "#foreach($param in $params)\n" +
                "        queryWrapper.eq($param.name != null, \"$param.alias\", $param.name);\n" +
                "#end" +
                "        IPage<$selectedEntity> pageList = $selectedService.page(page, queryWrapper);\n" +
                "        return R.ok().put(\"data\", pageList.getRecords()).put(\"total\", pageList.getTotal());\n" +
                "    } ");

        return codeMap;

    }

}
