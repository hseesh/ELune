package com.yatoufang.test;

import cn.daxiang.framework.database.MultiEntity;
import cn.daxiang.framework.database.annotation.Column;
import cn.daxiang.framework.database.annotation.DBQueueType;
import cn.daxiang.framework.database.annotation.Table;
import cn.daxiang.framework.identity.IdentiyKey;
import cn.daxiang.lyltd.gameserver.module.goods.helper.RewardHelper;
import cn.daxiang.shared.reward.RewardObject;
import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * 圣魂相关
 * @author GongHuang
 */
@Table(name = "holy_soul", type = DBQueueType.DEFAULT)
public class HolySoul extends MultiEntity<Long> {
    /**
     * 角色ID
     */
    @Column(fk = true)
    private Long actorId;

    /**
     * 圣魂Id
     */
    @Column(pk = true)
    private Long soulId;

    /**
     * 配置Id
     */
    @Column
    private Integer configId;

    /**
     * 圣魂阶级
     */
    @Column
    private Integer advanceLevel;


    /**
     * 元神祝福养成消耗
     */
    @Column(alias = "advanceCost")
    private Collection<RewardObject> advanceCost = Lists.newArrayList();


    /**
     * 
     */
    public Long getActorId() {
        return actorId;
    }

    /**
     * 
     */
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    /**
     * 圣魂Id
     */
    public Long getSoulId() {
        return soulId;
    }

    /**
     * 圣魂Id
     */
    public void setSoulId(Long soulId) {
        this.soulId = soulId;
    }

    /**
     * 配置Id
     */
    public Integer getConfigId() {
        return configId;
    }

    /**
     * 配置Id
     */
    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    /**
     * 圣魂阶级
     */
    public Integer getAdvanceLevel() {
        return advanceLevel;
    }

    /**
     * 圣魂阶级
     */
    public void setAdvanceLevel(Integer advanceLevel) {
        this.advanceLevel = advanceLevel;
    }


    public Collection<RewardObject> getAdvanceCost() {
        return advanceCost;
    }

    public void setAdvanceCost(Collection<RewardObject> advanceCost) {
        this.advanceCost = advanceCost;
    }

    public void addAdvanceCost(Collection<RewardObject> blessCost) {
        this.advanceCost.addAll(blessCost);
        this.advanceCost = RewardHelper.groupByTypeAndId(this.advanceCost);
    }

    @Override
    public IdentiyKey findPkId() {
        return IdentiyKey.build(this.soulId);
    }

    @Override
    public void setPkId(IdentiyKey pk) {
        this.soulId = pk.getFirstLongId();
    }

    @Override
    public Long findFkId() {
        return this.actorId;
    }

    @Override
    public void setFkId(Long aLong) {
        this.actorId = aLong;
    }
}