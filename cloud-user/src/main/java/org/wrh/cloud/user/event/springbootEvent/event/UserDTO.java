package org.wrh.cloud.user.event.springbootEvent.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author wuruohong
 * @date 2022-06-19 10:50
 */
@Getter
@Setter
@ToString
public class UserDTO extends ApplicationEvent {
    private Integer userId;
    private String name;
    private Integer age;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public UserDTO(Object source) {
        super(source);
    }
}
