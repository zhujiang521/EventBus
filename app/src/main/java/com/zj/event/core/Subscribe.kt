package com.zj.event.core

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/6
 * 描述：EventBus接收方法注解
 *
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class Subscribe(val threadModel: ThreadModel = ThreadModel.POSTING)