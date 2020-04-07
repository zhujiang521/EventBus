// EventBusService.aidl
package com.zj.event.bean;

// Declare any non-default types here with import statements
import com.zj.event.bean.Request;
import com.zj.event.bean.Responce;

interface EventBusService {

    Responce send(in Request request);

}
