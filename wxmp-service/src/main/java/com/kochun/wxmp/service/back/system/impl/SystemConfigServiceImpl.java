package com.kochun.wxmp.service.back.system.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kochun.wxmp.core.entity.system.SystemConfig;
import com.kochun.wxmp.core.service.SystemConfigService;
import com.kochun.wxmp.mapper.system.SystemConfigMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统配置 服务实现类
 * </p>
 *
 * @author kochun
 * @since 2019-08-26
 */
@Service(version = "1.0.0")
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Resource
    SystemConfigMapper systemConfigMapper;

    /**
     * 通过key获得配置信息
     *
     * @param key
     * @return SystemConfig
     * @author kochun
     * @date 2019/8/26 15:54
     **/
    @Override
    public SystemConfig getByKey(String key) {
        QueryWrapper<SystemConfig> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("key",key);

        return systemConfigMapper.selectOne(queryWrapper);
    }

    /**
     * 获得系统配置信息，转为map集合，varName：key  varValue:value
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author kochun
     * @date 2019/8/26 16:34
     **/
    @Override
    public Map<String, String> getAllConfigs() {
        List<SystemConfig> lists=systemConfigMapper.selectList(null);
        Map<String, String> configsMap = new HashMap<>();
        lists.forEach((config) -> configsMap.put(config.getVarName(), config.getVarValue()));
        return null;
    }


}