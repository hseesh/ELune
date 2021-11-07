package com.yatoufang.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解
 * @author CharonWang
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Table {
	/**
	 * 表名
	 * @return
	 */
	String name();

	/**
	 * 存储级别 {@code DBQueueType}
	 * @return
	 */
	DBQueueType type();
}
