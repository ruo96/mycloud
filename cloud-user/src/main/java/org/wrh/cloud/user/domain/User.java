package org.wrh.cloud.user.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author wuruohong
 * @date 2022-06-17 12:53
 */
@Data
@Builder
public class User {
    private String name;
    private Integer age;
    private String sex;
}
