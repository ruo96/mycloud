package org.wrh.cloud.user.event.javaEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wuruohong
 * @date 2022-06-19 10:38
 */
@Slf4j
public class CloseDoorListener implements DoorListener {
    @Override
    public void doorEvent(DoorEvent doorEvent) {
        if (doorEvent.getState() == -1) {
            log.info("[event-close]>>> door close!");
        }
    }
}
