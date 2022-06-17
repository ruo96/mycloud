package org.wrh.cloud.user.service;

import org.wrh.cloud.common.dto.ReturnResult;

/**
 * @author wuruohong
 * @date 2022-06-17 23:33
 */
public interface DynamicConfigService {

    /**
     * 获取动态配置
     * @return
     */
    ReturnResult getDynamicConfig();
}
