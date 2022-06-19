package org.wrh.cloud.user.event.javaEvent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuruohong
 * @date 2022-06-19 10:41
 *
 * Java 中的事件机制 *
 * Java中提供了基本的事件处理基类： *
 * EventObject：所有事件状态对象都将从其派生的根类；
 * EventListener：所有事件侦听器接口必须扩展的标记接口
 */
@Slf4j
public class EventExperiment {
    public static void main(String[] args) {
        List<DoorListener> list = new ArrayList<>();
        list.add(new OpenDoorListener());
        list.add(new CloseDoorListener());

        for (DoorListener doorListener : list) {
            doorListener.doorEvent(new DoorEvent(-1,-1));
            doorListener.doorEvent(new DoorEvent(1,1));
        }
    }
}
