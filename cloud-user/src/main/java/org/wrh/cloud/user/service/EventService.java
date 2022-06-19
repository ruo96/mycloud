package org.wrh.cloud.user.service;

import org.wrh.cloud.common.dto.ReturnResult;

/**
 * @author wuruohong
 * @date 2022-06-19 10:14
 */
public interface EventService {

    ReturnResult publishEvent(String msg);
}
