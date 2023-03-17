package com.shopee.seamiter.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * admin配置加载
 *
 * @author honggang.liu
 */
@Component
public class AdminConfig implements InitializingBean, DisposableBean {

    private static AdminConfig adminConfig = null;

    /**
     * 缓存管理器
     */
    @Resource
    private CacheManager cacheManager;

    /**
     * conf
     */
    @Value("${sea.i18n}")
    private String i18n;

    /**
     * 环境信息
     */
    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 日志保留时间
     */
    @Value("${metric.retentiondays}")
    private Integer retentionDays;

    public String getEnv() {
        return env;
    }


    // ---------------------- Scheduler ----------------------

    private Scheduler scheduler;

    @Override
    public void destroy() throws Exception {
        scheduler.destroy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;

        scheduler = new Scheduler();
        scheduler.init();
    }

    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    public static AdminConfig getAdminConfig() {
        return adminConfig;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public Integer getRetentionDays() {
        return retentionDays;
    }
}
