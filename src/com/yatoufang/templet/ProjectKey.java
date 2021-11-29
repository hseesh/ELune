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

    String MODEL = "model";

    String CORE = "core";

    String DAO = "dao";

    String VO  = "vO";

    String IMPL = "impl";

    String HELPER = "helper";

    String REQUEST = "request";

    String DELETE = "delete";

    String REWARD = "reward";

    String RESPONSE = "response";

    String PUSH = "push";

    String JAVA = ".java";

    String SINGLE_ENTITY_TEMPLATE = "/templates/SingleDaoTemplate.vm";
    String MULTI_ENTITY_TEMPLATE = "/templates/MultiDaoTemplate.vm";
    String MULTI_ENTITY_IMPL_TEMPLATE = "/templates/MultiDaoImplTemplate.vm";
    String SINGLE_ENTITY_IMPL_TEMPLATE = "/templates/SingleDaoImplTemplate.vm";

    String PUSH_HELP_TEMPLATE = "/templates/PushHelperTemplate.vm";
    String ENTITY_VO_TEMPLATE = "/templates/EntityVO.vm";
    String ENTITY_REWARD_RESPONSE_TEMPLATE = "/templates/EntityRewardResultResponse.vm";
    String ENTITY_RESPONSE_TEMPLATE = "/templates/EntityResponse.vm";
    String ENTITY_DELETE_RESPONSE_TEMPLATE = "/templates/EntityDeleteResponse.vm";
    String ENTITY_CMD_TEMPLATE = "/templates/EntityCmd.vm";
    String ENTITY_HANDLER_TEMPLATE = "/templates/EntityHandler.vm";

    String CMD = "cmd";
    String HANDLER = "handler";
}
