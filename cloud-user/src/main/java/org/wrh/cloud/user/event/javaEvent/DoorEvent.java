package org.wrh.cloud.user.event.javaEvent;

import lombok.Getter;
import lombok.Setter;

import java.util.EventObject;

/**
 * @author wuruohong
 * @date 2022-06-19 10:34
 * 创建事件对象
 */
@Getter
@Setter
public class DoorEvent extends EventObject {

    int state;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DoorEvent(Object source) {
        super(source);
    }

    public DoorEvent(Object source, int state) {
        super(source);
        this.state = state;
    }
}
