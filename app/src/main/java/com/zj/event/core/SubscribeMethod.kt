package com.zj.event.core

import java.lang.reflect.Method

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/6
 * 描述：注册类中的注册信息
 *
 */
class SubscribeMethod(
    //注册方法
    var method: Method,
    //线程类型
    var threadModel: ThreadModel,
    //参数类型
    var eventType: Class<*>
)