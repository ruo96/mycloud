package org.wrh.cloud.user.event.javaEvent;

import java.util.EventListener;

/**
 * @author wuruohong
 * @date 2022-06-19 10:37
 */
public interface DoorListener extends EventListener {

    void doorEvent(DoorEvent doorEvent);
}
