package com.yatoufang.preference;

import com.yatoufang.entity.ConfigParam;
import com.yatoufang.preference.impl.config.*;

/**
 * @author GongHuang（hse）
 * @since 2022/1/2 0002
 */
public class ConfigPreferenceService {

    private static ConfigPreferenceService instance;

    private static ConfigPreferenceHandler handler;

    public static ConfigPreferenceService getInstance() {
        if (instance == null) {
            instance = new ConfigPreferenceService();
        }
        return instance;
    }

    ConfigPreferenceService() {
        UniqueIdHandler uniqueIdHandler = new UniqueIdHandler();
        AttributeHandler attributeHandler = new AttributeHandler();
        AttributeTHandler attributeTHandler = new AttributeTHandler();
        CostHandler costHandler = new CostHandler();
        RewardsHandler rewardsHandler = new RewardsHandler();
        LimitHandler limitHandler = new LimitHandler();

        uniqueIdHandler.handler = limitHandler;
        limitHandler.handler = rewardsHandler;
        rewardsHandler.handler = attributeTHandler;
        attributeTHandler.handler = attributeHandler;
        attributeHandler.handler = costHandler;

        handler = uniqueIdHandler;
    }

    public ConfigParam action(ConfigParam configParam) {
        handler.invoke(configParam);
        return configParam;
    }

    public static void main(String[] args) {
        ConfigPreferenceService service = getInstance();
        ConfigParam param = new ConfigParam("id");
        service.action(param);
        System.out.println("param = " + param);
    }

}
