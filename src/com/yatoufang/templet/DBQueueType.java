package com.yatoufang.templet;

/**
 * DB队列类型
 * @author CharonWang
 *
 */
public enum DBQueueType {

	/**
	 * 不入队列实体（直接入库）
	 */
	NONE,
	/**
	 * 核心实体（用于1对1表结构, 主键是角色id的表）
	 */
	IMPORTANT,
	/**
	 * 普通实体（用于1对多的表结构, 主键是自增的表）
	 */
	DEFAULT;
}
