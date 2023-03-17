package com.shopee.seamiter.domain;

import java.io.Serializable;

/**
 * 推送流控规则锁
 *
 * @author honggang.liu
 */
public class PushRuleLock implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 规则类型
     */
    private String ruleType;

    /**
     * 上次触发时间
     */
    private long triggerLastTime;

    /**
     * 下次触发时间
     */
    private long triggerNextTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public long getTriggerLastTime() {
        return triggerLastTime;
    }

    public void setTriggerLastTime(long triggerLastTime) {
        this.triggerLastTime = triggerLastTime;
    }

    public long getTriggerNextTime() {
        return triggerNextTime;
    }

    public void setTriggerNextTime(long triggerNextTime) {
        this.triggerNextTime = triggerNextTime;
    }
}
