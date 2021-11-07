package com.yatoufang.test;

/**
 * 游戏服模块分类(通用于所有服务器)
 * <pre>
 * 00 - 10 用途：用于维护、控制台、测试等
 * 11 - 99 用途：逻辑模块
 * </pre>
 * @author CharonWang
 */
public interface ModuleName {

	/**
	 * 基础模块
	 */
	byte BASE = 1;

	// ----------------------------------------
	/**
	 * 用户
	 * <pre>
	 * 帐号相关
	 * 角色相关
	 * </pre>
	 */
	byte USER = 11;
	/**
	 * 英雄
	 * 
	 * <pre>
	 * 英雄相关
	 * </pre>
	 */
	byte HERO = 12;

	/**
	 * 战斗
	 * 
	 * <pre>
	 * 战斗相关
	 * </pre>
	 */
	byte BATTLE = 13;

	/**
	 * 物品
	 * 
	 * <pre>
	 * 物品相关
	 * 背包相关
	 * </pre>
	 */
	byte GOODS = 14;
	/**
	 * 关卡
	 * 
	 * <pre>
	 * 关卡相关
	 * </pre>
	 */
	byte STORY = 15;

	/**
	 * 装备
	 * 
	 * <pre>
	 * 装备相关
	 * </pre>
	 */
	byte EQUIPMENT = 16;

	/**
	 * 阵容
	 * <pre>
	 * 阵容相关
	 * </pre>
	 */
	byte LINEUP = 17;
	/**
	 * 抽奖
	 * 
	 * <pre>
	 * 抽奖相关
	 * </pre>
	 */
	byte GACHA = 18;
	/**
	 * 竞技场
	 * 
	 * <pre>
	 * 竞技场相关
	 * </pre>
	 */
	byte ARENA = 19;
	/**
	 * 试炼
	 * 
	 * <pre>
	 * 试炼相关
	 * </pre>
	 */
	byte ENDLESS = 20;
	/**
	 * 商店
	 * 
	 * <pre>
	 * 商店相关
	 * </pre>
	 */
	byte STORE = 21;
	/**
	 * 聊天
	 * 
	 * <pre>
	 * 聊天相关
	 * </pre>
	 */
	byte CHAT = 22;
	/**
	 * 邮件
	 * 
	 * <pre>
	 * 邮件相关
	 * </pre>
	 */
	byte MAIL = 23;
	/**
	 * 符文
	 * 
	 * <pre>
	 * 符文相关
	 * </pre>
	 */
	byte RUNE = 24;
	/**
	 * 世界BOSS
	 * 
	 * <pre>
	 * 世界BOSS相关
	 * </pre>
	 */
	byte WORLDBOSS = 25;
	/**
	 * 副本
	 * 
	 * <pre>
	 * 各种副本相关
	 * </pre>
	 */
	byte DUNGEON = 26;
	/**
	 * 图鉴
	 * <pre>
	 * 图鉴相关
	 * </pre>
	 */
	byte MANUAL = 27;
	/**
	 * 任务
	 * 
	 * <pre>
	 * 任务相关
	 * </pre>
	 */
	byte TASK = 28;
	/**
	 * 扩展
	 * 
	 * <pre>
	 * 扩展相关
	 * </pre>
	 */
	byte EXTENSION = 29;

	/**
	 * 特权
	 * 
	 * <pre>
	 * 特权相关
	 * </pre>
	 */
	byte PREROGATIVE = 30;
	/**
	 * 活动
	 * 
	 * <pre>
	 * 活动相关
	 * </pre>
	 */
	byte ACTIVITY = 31;
	/**
	 * 国家
	 * 
	 * <pre>
	 * 国家相关
	 * </pre>
	 */
	byte NATION = 33;
	/**
	 * 签到
	 * 
	 * <pre>
	 * 签到相关
	 * </pre>
	 */
	byte SIGN_IN = 34;
	/**
	 * 攻城战
	 * 
	 * <pre>
	 * 攻城战相关
	 * </pre>
	 */
	byte SIEGE = 35;

	/**
	 * 好友
	 * <pre>
	 * 好友相关
	 * </pre>
	 */
	byte FRIEND = 36;
	/**
	 * 云游四海
	 * <pre>
	 * 云游四海相关
	 * </pre>
	 */
	byte ROAM = 37;
	/**
	 * 飞升之路
	 * <pre>
	 * 飞升之路相关
	 * </pre>
	 */
	byte STAR_UP_ROAD = 38;
	/**
	 * 系统
	 * 
	 * <pre>
	 * 系统相关
	 * </pre>
	 */
	byte SYSTEM = 39;

	/**
	 * 王者之路
	 * <pre>
	 * 王者之路相关
	 * </pre>
	 */
	byte VIE = 40;
	/**
	 * 庄园
	 * <pre>
	 * 庄园相关
	 * </pre>
	 */
	byte MANOR = 41;
	/**
	 * 玲珑塔
	 * <pre>
	 * 玲珑塔相关
	 * </pre>
	 */
	byte PAGODA = 42;

	/**
     * 邪神试炼
	 * 
	 * <pre>
     * 邪神试炼相关
	 * </pre>
	 */
    byte EVIL_GOD = 43;
	/**
	 * 修仙
	 * <pre>
	 * 修仙相关
	 * </pre>
	 */
	byte IMMORTAL = 44;
	/**
	 * 许愿池(寻宝)
	 * 
	 * <pre>
	 * 许愿池(寻宝)相关
	 * </pre>
	 */
	byte VOW = 45;
	/**
	 * 国家BOSS
	 * <pre>
	 * 国家BOSS相关
	 * </pre>
	 */
	byte NATIONBOSS = 46;

	/**
	 * 全民BOSS
	 * <pre>
	 * 全民BOSS相关
	 * </pre>
	 */
	byte WHOLE_BOSS = 47;

	/**
	 * 红包
	 * <pre>
	 * 红包相关
	 * </pre>
	 */
	byte RED_ENVELOPE = 48;

	/**
	 * 科技
	 * <pre>
	 * 科技相关
	 * </pre>
	 */
	byte TECHNOLOGY = 49;
	/**
	 * 宝物
	 * <pre>
	 * 宝物相关
	 * </pre>
	 */
	byte TREASURE = 50;
	/**
	 * 仙魔录
	 * <pre>
	 * 仙魔录相关
	 * </pre>
	 */
	byte MAGIC_RECORD = 51;
	/**
	 * 灭神殿(仙府秘境)
	 * <pre>
	 * 灭神殿相关
	 * </pre>
	 */
	byte DESTROY_TEMPLE = 52;
	/**
	 * 兑换码
	 * <pre>
	 * 兑换码相关
	 * </pre>
	 */
	byte EXCHANGE = 53;
	/**
	 * 占矿(紫霄仙殿)
	 * <pre>
	 * 占矿相关
	 * </pre>
	 */
	byte PURPLE_FAIRULAND = 54;

    /**
     * 降妖打宝
     *
     * <pre>
     * 降妖打宝相关
     * </pre>
     */
    byte DEMON_TREASURE = 55;

    /**
     * 远古回廊
     *
     * <pre>
     * 远古回廊相关
     * </pre>
     */
    byte ANCIENT_CORRIDOR = 56;

	/**
	 * 北斗星阵
	 *
	 * <pre>
	 * 北斗星阵相关
	 * </pre>
	 */
	byte DIPPER = 57;

	/**
	 * 仙境探险
	 *
	 * <pre>
	 * 仙境探险相关
	 * </pre>
	 */
	byte WONDERLAND = 58;


    /**
     * 跨服竞技
     *
     * <pre>
     * 跨服竞技相关
     * </pre>
     */
    byte CROSS_ARENA = 59;

    /**
     * 军团圣兽
     * <pre>
     * 军团圣兽相关
     * </pre>
     */
    byte HOLY_BEAST = 60;
    /**
     * 通天塔
     * <pre>
     * 通天塔相关
     * </pre>
     */
    byte BABEL = 61;
    /**
     * 称号系统
     * <pre>
     * 称号相关
     * </pre>
     */
    byte TITLE = 62;
    /**
     * 巅峰竞技场
     * <pre>
     * 巅峰竞技场相关
     * </pre>
     */
    byte CROSS_CUP = 63;
    /**
     * 仙盟对决
     * <pre>
     * 仙盟对决相关
     * </pre>
     */
    byte NATION_DUEL = 64;
    /**
     * 封神争霸
     * <pre>
     * 封神争霸相关
     * </pre>
     */
    byte LEGEND = 65;
	/**
	 * 元神
	 * <pre>
	 * 元神相关
	 * </pre>
	 */
	byte SPIRIT = 66;
	/**
	 * 八阵图
	 * <pre>
	 * 八阵图相关
	 * </pre>
	 */
	byte EIGHT_DIAGRAM = 67;
    /**
     * 神宠
     * <pre>
     * 神宠相关
     * </pre>
     */
    byte PET = 68;
	/**
	 * 仙岛迷宫
	 * <pre>
	 * 仙岛迷宫相关
	 * </pre>
	 */
	byte MAZE = 69;
    /**
     * 战棋
     * <pre>
     * 战棋相关
     * </pre>
     */
    byte CHESS = 70;
    /**
     * 仙位
     * <pre>
     * 仙位相关
     * </pre>
     */
    byte DIVINE_POSITION = 71;
    /**
     * 混战阎罗
     * <pre>
     * 混战阎罗相关
     * </pre>
     */
    byte TANGLED_HELL = 72;
    /**
     * 拍卖行
     * <pre>
     * 拍卖行相关
     * </pre>
     */
    byte AUCTION = 73;
    /**
     * 74.组队
     * <pre>
     * 组队相关
     * </pre>
     */
    byte TEAM = 74;
    /**
     * 75.组队在线塔防
	 * <pre>
	 * 组队在线塔防
	 * </pre>
	 */
	byte TEAM_ONLINE_TD = 75;
	/**
	 * 76.金将录
	 */
	byte GOLDEN_RECORD = 76;
	/**
	 * 77.取经之路
	 * <pre>
	 * 仙位相关
	 * </pre>
	 */
	byte SCRIPTURE_ROAD = 77;

	/**
	 * 78.三界会战
	 * <pre>
	 *  三界会战相关
	 * </pre>
	 */
	byte NATION_TRAILOKYA = 78;

	/**
	 * 羲和神像
	 *
	 * <pre>
	 *     神像相关
	 * </pre>
	 */
	byte IDOL = 79;

	/**
	 * 圣魂
	 *
	 * <pre>
	 *     圣魂相关
	 * </pre>
	 */
	byte HOLY_SOUL = 80;


	/**
	 * 英雄评论
	 *
	 * <pre>
	 *     英雄评论相关
	 * </pre>
	 */
	byte HERO_COMMENT = 81;


}