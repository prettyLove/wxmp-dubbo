package com.kochun.wxmp.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kochun.wxmp.core.entity.system.EmailTemplate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kochun
 * @since 2019-08-26
 */
public interface EmailTemplateService extends IService<EmailTemplate> {
    EmailTemplate getByOperation(String operation);
}
