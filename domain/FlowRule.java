package com.shopee.seamiter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 流控规则
 *
 * @author honggang.liu
 */
public class FlowRule implements Serializable {

    /**
     * id
     */
    private String id;
    /**
     * 应用
     */
    private String app;

    /**
     * limitApp
     */
    private String limitApp;
    /**
     * 资源名称
     */
    private String resource;
    /**
     * 0为线程数;1为qps
     */
    private Integer grade;
    /**
     * 阈值
     */
    private Double threshold;
    /**
     * 0为直接限流;1为关联限流;2为链路限流
     ***/
    private Integer relationStrategy;
    /**
     * 关联资源名称
     */
    private String refResource;
    /**
     * 0. default, 1. warm up, 2. rate limiter
     */
    private Integer controlBehavior;
    /**
     * 预热时间
     */
    private Integer warmUpPeriodSec = 0;
    /**
     * max queueing time in rate limiter behavior
     */
    private Integer maxQueueingTimeMs;

    /**
     * token计算策略
     * 0：直接，直接返回设置的阈值
     * 1：warm up，
     * 2:内存自适应
     * If the watermark is less than Rule.MemLowWaterMarkBytes, the threshold is Rule.LowMemUsageThreshold.
     * If the watermark is greater than Rule.MemHighWaterMarkBytes, the threshold is Rule.HighMemUsageThreshold.
     * Otherwise, the threshold is ((watermark - MemLowWaterMarkBytes)/(MemHighWaterMarkBytes - MemLowWaterMarkBytes)) *
     * (HighMemUsageThreshold - LowMemUsageThreshold) + LowMemUsageThreshold.
     */
    private Integer tokenCalculateStrategy;

    /**
     * 低内存阈值
     */
    private Integer lowMemUsageThreshold;

    /**
     * 低内存水位
     */
    private Integer memLowWaterMarkBytes;

    /**
     * 高内存阈值
     */
    private Integer highMemUsageThreshold;

    /**
     * 高内存水位
     */
    private Integer memHighWaterMarkBytes;

    /**
     * 是否为集群模式
     */
    private boolean clusterMode;
    /**
     * Flow rule config for cluster mode.
     */
    private ClusterFlowConfig clusterConfig;

    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 更新时间
     */
    private Date gmtModified;


    /**
     * 是否开启
     * <p>
     * 打开是否开启开关：在规则创建完成时，规则立即生效。
     * 关闭是否开启开关：在规则创建完成时，规则不生效。您可以后续编辑，手动打开开关，使规则生效。
     */
    private Boolean open;

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

    public String getLimitApp() {
        return limitApp;
    }

    public void setLimitApp(String limitApp) {
        this.limitApp = limitApp;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Integer getRelationStrategy() {
        return relationStrategy;
    }

    public void setRelationStrategy(Integer relationStrategy) {
        this.relationStrategy = relationStrategy;
    }

    public String getRefResource() {
        return refResource;
    }

    public void setRefResource(String refResource) {
        this.refResource = refResource;
    }

    public Integer getControlBehavior() {
        return controlBehavior;
    }

    public void setControlBehavior(Integer controlBehavior) {
        this.controlBehavior = controlBehavior;
    }

    public Integer getWarmUpPeriodSec() {
        return warmUpPeriodSec;
    }

    public void setWarmUpPeriodSec(Integer warmUpPeriodSec) {
        this.warmUpPeriodSec = warmUpPeriodSec;
    }

    public Integer getMaxQueueingTimeMs() {
        return maxQueueingTimeMs;
    }

    public void setMaxQueueingTimeMs(Integer maxQueueingTimeMs) {
        this.maxQueueingTimeMs = maxQueueingTimeMs;
    }

    public boolean isClusterMode() {
        return clusterMode;
    }

    public void setClusterMode(boolean clusterMode) {
        this.clusterMode = clusterMode;
    }

    public ClusterFlowConfig getClusterConfig() {
        return clusterConfig;
    }

    public void setClusterConfig(ClusterFlowConfig clusterConfig) {
        this.clusterConfig = clusterConfig;
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

    public Integer getTokenCalculateStrategy() {
        return tokenCalculateStrategy;
    }

    public void setTokenCalculateStrategy(Integer tokenCalculateStrategy) {
        this.tokenCalculateStrategy = tokenCalculateStrategy;
    }

    public Integer getLowMemUsageThreshold() {
        return lowMemUsageThreshold;
    }

    public void setLowMemUsageThreshold(Integer lowMemUsageThreshold) {
        this.lowMemUsageThreshold = lowMemUsageThreshold;
    }

    public Integer getMemLowWaterMarkBytes() {
        return memLowWaterMarkBytes;
    }

    public void setMemLowWaterMarkBytes(Integer memLowWaterMarkBytes) {
        this.memLowWaterMarkBytes = memLowWaterMarkBytes;
    }

    public Integer getHighMemUsageThreshold() {
        return highMemUsageThreshold;
    }

    public void setHighMemUsageThreshold(Integer highMemUsageThreshold) {
        this.highMemUsageThreshold = highMemUsageThreshold;
    }

    public Integer getMemHighWaterMarkBytes() {
        return memHighWaterMarkBytes;
    }

    public void setMemHighWaterMarkBytes(Integer memHighWaterMarkBytes) {
        this.memHighWaterMarkBytes = memHighWaterMarkBytes;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "FlowRule{" +
                "id='" + id + '\'' +
                ", app='" + app + '\'' +
                ", limitApp='" + limitApp + '\'' +
                ", resource='" + resource + '\'' +
                ", grade=" + grade +
                ", threshold=" + threshold +
                ", relationStrategy=" + relationStrategy +
                ", refResource='" + refResource + '\'' +
                ", controlBehavior=" + controlBehavior +
                ", warmUpPeriodSec=" + warmUpPeriodSec +
                ", maxQueueingTimeMs=" + maxQueueingTimeMs +
                ", tokenCalculateStrategy=" + tokenCalculateStrategy +
                ", lowMemUsageThreshold=" + lowMemUsageThreshold +
                ", memLowWaterMarkBytes=" + memLowWaterMarkBytes +
                ", highMemUsageThreshold=" + highMemUsageThreshold +
                ", memHighWaterMarkBytes=" + memHighWaterMarkBytes +
                ", clusterMode=" + clusterMode +
                ", clusterConfig=" + clusterConfig +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", open=" + open +
                '}';
    }
}
