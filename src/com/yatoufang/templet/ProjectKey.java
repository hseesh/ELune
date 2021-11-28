package com.yatoufang.templet;

/**
 * @author GongHuang（hse）
 * @since 2021/11/20 20:48
 */
public interface ProjectKey {

    String MODULE_NAME = "cn.daxiang.shared.ModuleName";

    String CONFIG_KEY = "cn.daxiang.shared.GlobalConfigKey";

    String MULTI_ENTITY = "MultiEntity";

    String SINGLE_ENTITY = "SingleEntity";

    String VALUE_OF = "valueOf";

    String MODULE = "module";

    String CORE = "core";

    String DAO = "dao";

    String IMPL = "impl";

    String HELPER = "helper";

    String REQUEST = "request";

    String RESPONSE = "response";

    String JAVA = ".java";

    String SINGLE_ENTITY_TEMPLATE = "/templates/SingleDaoTemplate.vm";
    String MULTI_ENTITY_TEMPLATE = "/templates/MultiDaoTemplate.vm";
    String MULTI_ENTITY_IMPL_TEMPLATE = "/templates/MultiDaoImplTemplate.vm";
    String SINGLE_ENTITY_IMPL_TEMPLATE = "/templates/SingleDaoImplTemplate.vm";

}
