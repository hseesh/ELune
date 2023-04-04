package com.yatoufang.templet;

/**
 * @author GongHuang（hse）
 * @since 2021/11/20 20:48
 */
public interface ProjectKeys {

    String MODULE_NAME = "cn.daxiang.shared.ModuleName";

    String CONFIG_KEY = "cn.daxiang.shared.GlobalConfigKey";

    String CONFIG_ADAPTER = "ConfigServiceAdapter";

    String MODEL_ADAPTER = "ModelAdapter";

    String IDENTITY_KEY = "cn.daxiang.framework.identity.IdentiyKey";

    String GATE_WAY = "cn.daxiang.lyltd.gameserver.core.router.GatewayRouterHandlerImpl";

    String CONFIG_PATH = "core/dataconfig/model";

    String MULTI_ENTITY = "MultiEntity";

    String SINGLE_ENTITY = "SingleEntity";

    String VALUE_OF = "valueOf";

    String MODULE = "module";
    String GET_MODULE = "getModule";

    String MODEL = "model";

    String CORE = "core";

    String KEY = "Key";

    String UPDATE = "update";

    String RANK_KEY = "Rank";

    String CACHE_KEY = "cacheKey";

    String SHARD = "shard";
    String GAME_SERVER = "gameserver";
    String WORLD_SERVER = "worldserver";
    String BATTLE_SERVER = "battleserver";
    String DATABASE = "database";
    String PROJECT = "lyltd";

    String LINK = "@link";

    String DAO = "dao";

    String ACTIVITY = "activity";

    String VO = "vO";
    String VO_ALIAS = "VO";

    String IMPL = "impl";

    String HELPER = "helper";

    String REQUEST = "request";

    String SERIAL_UID = "serialVersionUID";

    String DELETE = "delete";
    String RESULT = "result";
    String RESULT_OF = "Result";

    String T_RESULT = "TResult";

    String METHOD_FIND_CONFIG = "findConfig";
    String METHOD_FIND_KEY = "findKey";
    String METHOD_BUILD_KEY = "build";

    String COST = "cost";

    String REWARD = "reward";
    String ID = "id";
    String ATTRIBUTE = "attribute";
    String DEFAULT_NODE = "defaultNode";
    String ATTRIBUTE_PERCENT = "attributeTTPercent";

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

    String ENUM = "type";

    String GET_VALUE_METHOD = "getValue";
    String CLEAN_METHOD = "clean";
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

    String ENUM_TEMPLATE = "/templates/Enum.vm";
    String HANDLER_TEMPLATE = "/templates/protocol/Handler.vm";
    String CMD_TEMPLATE = "/templates/protocol/Cmd.vm";
    String REQUEST_TEMPLATE = "/templates/protocol/Request.vm";
    String FACADE_TEMPLATE = "/templates/protocol/Facade.vm";
    String FACADE_IMPL_TEMPLATE = "/templates/protocol/FacadeImpl.vm";

    String PATH_CONFIG_FIELDS = "/config/config_fields.json";
    String PATH_CONFIG_ACCESS_BUILD = "/config/access_build.json";
}
