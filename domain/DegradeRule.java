package com.shopee.seamiter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 降级规则
 *
 * @author honggang.liu
 */
public class DegradeRule implements Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 应用
     */
    private String app;
    /**
     * 资源
     */
    private String resource;
    /**
     * limit app 来源应用
     */
    private String limitApp;
    /**
     * 阈值
     * Threshold count. The exact meaning depends on the field of grade.
     * <ul>
     *     <li>In average RT mode, it means the maximum response time(RT) in milliseconds.</li>
     *     <li>In exception ratio mode, it means exception ratio which between 0.0 and 1.0.</li>
     *     <li>In exception count mode, it means exception count</li>
     * <ul/>
     */
    private Double threshold;
    /**
     * Recovery timeout (in ms) when circuit breaker opens. After the timeout, the circuit breaker will transform to half-open state for trying a few requests.
     */
    private Integer retryTimeoutMs;
    /**
     * 熔断策略
     * Circuit breaking strategy (0: average RT, 1: exception ratio, 2: exception count).
     */
    private Integer strategy;
    /**
     * 最小请求数量
     * Minimum number of requests (in an active statistic time span) that can trigger circuit breaking.
     */
    private Integer minRequestAmount;
    /**
     * 慢请求阈值
     * The threshold of slow request ratio in RT mode.
     */
    private Double slowRatioThreshold;
    /**
     * 统计间隔
     * The interval statistics duration in millisecond.
     */
    private Integer statIntervalMs;

    /**
     * 探针数量
     */
    private Integer probeNum;

    /**
     * 是否开启
     * <p>
     * 打开是否开启开关：在规则创建完成时，规则立即生效。
     * 关闭是否开启开关：在规则创建完成时，规则不生效。您可以后续编辑，手动打开开关，使规则生效。
     */
    private Boolean open;

    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 更新时间
     */
    private Date gmtModified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getLimitApp() {
        return limitApp;
    }

    public void setLimitApp(String limitApp) {
        this.limitApp = limitApp;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Integer getRetryTimeoutMs() {
        return retryTimeoutMs;
    }

    public void setRetryTimeoutMs(Integer retryTimeoutMs) {
        this.retryTimeoutMs = retryTimeoutMs;
    }

    public Integer getStrategy() {
        return strategy;
    }

    public void setStrategy(Integer strategy) {
        this.strategy = strategy;
    }

    public Integer getMinRequestAmount() {
        return minRequestAmount;
    }

    public void setMinRequestAmount(Integer minRequestAmount) {
        this.minRequestAmount = minRequestAmount;
    }

    public Double getSlowRatioThreshold() {
        return slowRatioThreshold;
    }

    public void setSlowRatioThreshold(Double slowRatioThreshold) {
        this.slowRatioThreshold = slowRatioThreshold;
    }

    public Integer getStatIntervalMs() {
        return statIntervalMs;
    }

    public void setStatIntervalMs(Integer statIntervalMs) {
        this.statIntervalMs = statIntervalMs;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getProbeNum() {
        return probeNum;
    }

    public void setProbeNum(Integer probeNum) {
        this.probeNum = probeNum;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "DegradeRule{" +
                "id='" + id + '\'' +
                ", app='" + app + '\'' +
                ", resource='" + resource + '\'' +
                ", limitApp='" + limitApp + '\'' +
                ", threshold=" + threshold +
                ", retryTimeoutMs=" + retryTimeoutMs +
                ", strategy=" + strategy +
                ", minRequestAmount=" + minRequestAmount +
                ", slowRatioThreshold=" + slowRatioThreshold +
                ", statIntervalMs=" + statIntervalMs +
                ", probeNum=" + probeNum +
                ", open=" + open +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
