package com.kochun.wxmp.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kochun.wxmp.core.entity.system.SystemConfig;

import java.util.Map;

/**
 * <p>
 * 系统配置 服务类
 * </p>
 *
 * @author kochun
 * @since 2019-08-26
 */
public interface SystemConfigService extends IService<SystemConfig> {
    /**
     * 通过key获得配置信息
     * @author kochun
     * @date 2019/8/26 15:54
     * @param key
     * @return SystemConfig
     **/
    SystemConfig getByKey(String key);


    /**
     *  获得系统配置信息，转为map集合，varName：key  varValue:value
     * @author kochun
     * @date 2019/8/26 16:34
     * @param
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    Map<String, String> getAllConfigs();
}
