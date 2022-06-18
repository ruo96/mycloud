package org.wrh.cloud.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * okhttp相关配置
 *
 * @author sunbo
 * Created at 2017/11/28 下午8:29
 */

@Component
// @ConfigurationProperties(prefix = "okhttp")
@Data
public class OkhttpConfig implements Serializable {

    private String connectTimeout;

    private String readTimeout;

    private String keepAliveDuration;

    private String maxIdleConnections;

}
