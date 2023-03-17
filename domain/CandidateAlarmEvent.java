package com.shopee.seamiter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 候选告警时间
 *
 * @author honggang.liu
 */
public class CandidateAlarmEvent implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 应用主键ID
     */
    private Long appId;

    /**
     * 应用名称
     */
    private String app;

    /**
     * 对应的规则ID
     */
    private Long alarmRuleId;

    /**
     * 对应的规则ID
     */
    private Long alarmItemId;

    /**
     * 实际观测阈值
     */
    private Integer observeThreshold;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Long getAlarmItemId() {
        return alarmItemId;
    }

    public void setAlarmItemId(Long alarmItemId) {
        this.alarmItemId = alarmItemId;
    }

    public Integer getObserveThreshold() {
        return observeThreshold;
    }

    public void setObserveThreshold(Integer observeThreshold) {
        this.observeThreshold = observeThreshold;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getAlarmRuleId() {
        return alarmRuleId;
    }

    public void setAlarmRuleId(Long alarmRuleId) {
        this.alarmRuleId = alarmRuleId;
    }
}
