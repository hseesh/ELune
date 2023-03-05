package com.yatoufang.templet;

/**
 * @author GongHuang（hse）
 * @since 2021/11/20 20:48
 */
public interface Expression {

    String END = "$END$";
    String IF_NULL = "if(%s == null)";
    String IF_FAIL = "if(%s.isFail())";
    String RETURN_NULL = "return null;";
    String RETURN_RESULT = "return %s;";
    String RETURN_STATUS_CODE = "return %s.valueOf(%s.statusCode);";
    String RETURN_INVALID_PARAM = " return %s.valueOf(INVALID_PARAM);";
    String RETURN_CONFIG_NOT_FOUND = " return %s.valueOf(CONFIG_NOT_FOUND);";
    String LOG_CONFIG_NOT_FOUND = "LOGGER.error(\"%s not fund %s\", %s);";
    String CONFIG_LIST_ALL = "Collection<%s> %s = dataConfig.listAll(this, %s.class);";
    String CONFIG_FOR_LOOP = "for (%s config : %s)";

    String OPTION_NULL = "Optional.ofNullable(%s)";
    String GET_SOME = "%s.get(%s);";
    String COMMENT = "/**\n" + " *  %s\n" + "*/";

    String RESULT_OF = "TResult<%s>";

    String COLLECTION_TYPE = "Collection<%s>";

    String MAP_TYPE = "Map<%s, %s>";

    String MAP_DES_TYPE = "<%s, %s> ";


}
