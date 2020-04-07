package com.zj.event.core

import android.os.Handler
import android.os.Looper
import java.lang.RuntimeException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/6
 * 描述：EventBus
 *
 */
object EventBus {

    // 所有未解注册缓存
    private val cacheMap: MutableMap<Any, List<SubscribeMethod>> = HashMap()
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var executorService: ExecutorService = Executors.newCachedThreadPool()

    /**
     * 注册
     * @param subscriber 注册的Activity
     */
    fun register(subscriber: Any) {
        var subscribeMethods = cacheMap[subscriber]
        // 如果已经注册，就不需要注册
        if (subscribeMethods == null) {
            subscribeMethods = getSubscribeList(subscriber);
            cacheMap[subscriber] = subscribeMethods;
        }
    }

    private fun getSubscribeList(subscriber: Any): List<SubscribeMethod> {
        val list: MutableList<SubscribeMethod> = arrayListOf()
        var aClass = subscriber.javaClass
        while (aClass != null) {
            //判断分类是在那个包下，（如果是系统的就不需要）
            val name = aClass.name
            if (name.startsWith("java.") ||
                name.startsWith("javax.") ||
                name.startsWith("android.") ||
                name.startsWith("androidx.")
            ) {
                break
            }

            val declaredMethods = aClass.declaredMethods
            declaredMethods.forEach {
                val annotation = it.getAnnotation(Subscribe::class.java) ?: return@forEach
                //检测是否合格
                val parameterTypes = it.parameterTypes
                if (parameterTypes.size != 1) {
                    throw RuntimeException("EventBus只能接收一个参数")
                }
                //符合要求
                val threadModel = annotation.threadModel

                val subscribeMethod = SubscribeMethod(
                    method = it,
                    threadModel = threadModel,
                    eventType = parameterTypes[0]
                )

                list.add(subscribeMethod)

            }
            aClass = aClass.superclass as Class<Any>
        }
        return list
    }

    /**
     * 发送消息
     * @param obj 具体消息
     */
    fun post(obj: Any) {
        val keys = cacheMap.keys
        val iterator = keys.iterator()
        while (iterator.hasNext()) {
            // 拿到注册类
            val next = iterator.next()
            //获取类中所有添加注解的方法
            val list = cacheMap[next]
            list?.forEach {
                //判断这个方法是否应该接收事件
                if (it.eventType.isAssignableFrom(obj::class.java)) {
                    when (it.threadModel) {
                        ThreadModel.POSTING -> {
                            //默认情况，不进行线程切换，post方法是什么线程，接收方法就是什么线程
                            invoke(it, next, obj)
                        }
                        // 接收方法在主线程执行的情况
                        ThreadModel.MAIN, ThreadModel.MAIN_ORDERED -> {
                            // Post方法在主线程执行的情况
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(it, next, obj)
                            } else {
                                // 在子线程中接收，主线程中接收消息
                                handler.post { invoke(it, next, obj) }
                            }
                        }
                        //接收方法在子线程的情况
                        ThreadModel.ASYNC -> {
                            //Post方法在主线程的情况
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                executorService.execute(Runnable { invoke(it, next, obj) })
                            } else {
                                //Post方法在子线程的情况
                                invoke(it, next, obj)
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 执行接收消息方法
     * @param it 需要接收消息的方法
     * @param next 注册类
     * @param obj 接收的参数（即post的参数）
     */
    private fun invoke(it: SubscribeMethod, next: Any, obj: Any) {
        val method = it.method
        method.invoke(next, obj)
    }

    /**
     * 取消注册
     * @param subscriber /
     */
    fun unRegister(subscriber: Any) {
        val list = cacheMap[subscriber]
        //如果获取到
        if (list != null) {
            cacheMap.remove(subscriber)
        }

    }

}