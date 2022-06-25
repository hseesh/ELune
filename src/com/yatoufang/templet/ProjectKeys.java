package com.yatoufang.templet;

/**
 * @author GongHuang（hse）
 * @since 2021/11/20 20:48
 */
public interface ProjectKeys {

    String MODULE_NAME = "cn.daxiang.shared.ModuleName";

    String CONFIG_KEY = "cn.daxiang.shared.GlobalConfigKey";

    String GATE_WAY = "cn.daxiang.lyltd.gameserver.core.router.GatewayRouterHandlerImpl";

    String CONFIG_PATH = "core/dataconfig/model";

    String MULTI_ENTITY = "MultiEntity";

    String SINGLE_ENTITY = "SingleEntity";

    String VALUE_OF = "valueOf";

    String MODULE = "module";
    String GET_MODULE = "getModule";

    String MODEL = "model";

    String CORE = "core";
    String GAME_SERVER = "gameserver";
    String WORLD_SERVER = "worldserver";
    String BATTLE_SERVER = "battleserver";
    String DATABASE = "database";
    String PROJECT = "lyltd";

    String DAO = "dao";

    String VO = "vO";
    String VO_ALIAS = "VO";

    String IMPL = "impl";

    String HELPER = "helper";

    String REQUEST = "request";

    String SERIAL_UID = "serialVersionUID";

    String DELETE = "delete";
    String RESULT = "result";

    String REWARD = "reward";

    String RESPONSE = "response";
    String FACADE = "facade";

    String PUSH = "push";
    String CMD = "cmd";
    String HANDLER = "handler";
    String ACTOR_ID = "actorId";
    String UPDATE_TIME = "updateTime";
    String TABLE = "table";
    String CONFIG = "config";

    String JAVA = ".java";

    String EQUAL = " = ";
    String IS = "is";
    String GET = "get";
    String NEW = " new ";
    String RETURN = "return";
    String NULL = "null";

    String GET_VALUE_METHOD = "getValue";
    String INITIALIZE_METHOD = "initialize";

    String SINGLE_ENTITY_TEMPLATE = "/templates/SingleDaoTemplate.vm";
    String MULTI_ENTITY_TEMPLATE = "/templates/MultiDaoTemplate.vm";
    String MULTI_ENTITY_IMPL_TEMPLATE = "/templates/MultiDaoImplTemplate.vm";
    String SINGLE_ENTITY_IMPL_TEMPLATE = "/templates/SingleDaoImplTemplate.vm";
    String SINGLE_TEMPLATE = "/templates/SingleEntityTemplate.vm";
    String MULTI_TEMPLATE = "/templates/MultiEntityTemplate.vm";

    String PUSH_HELP_TEMPLATE = "/templates/PushHelperTemplate.vm";
    String ENTITY_VO_TEMPLATE = "/templates/EntityVO.vm";
    String ENTITY_REWARD_RESPONSE_TEMPLATE = "/templates/EntityRewardResultResponse.vm";
    String ENTITY_RESPONSE_TEMPLATE = "/templates/EntityResponse.vm";
    String ENTITY_DELETE_RESPONSE_TEMPLATE = "/templates/EntityDeleteResponse.vm";
    String ENTITY_CMD_TEMPLATE = "/templates/EntityCmd.vm";
    String ENTITY_HANDLER_TEMPLATE = "/templates/EntityHandler.vm";
    String ENTITY_FACADE_TEMPLATE = "/templates/EntityFacadeTemplate.vm";
    String ENTITY_FACADE_IMPL_TEMPLATE = "/templates/EntityFacadeImplTemplate.vm";

    String CONFIG_TEMPLATE = "/templates/ConfigTemplate.vm";
    String ENTITY_TEMPLATE = "/templates/Entity.vm";
    String HANDLER_TEMPLATE = "/templates/protocol/Handler.vm";
    String CMD_TEMPLATE = "/templates/protocol/Cmd.vm";
    String REQUEST_TEMPLATE = "/templates/protocol/Request.vm";
}
