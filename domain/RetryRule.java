package com.shopee.seamiter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 重试规则
 *
 * @author honggang.liu
 */
public class RetryRule implements Serializable {

    public static final int NeverRetryPolicy = 0;

    public static final int SimpleRetryPolicy = 1;

    public static final int TimeoutRtyPolicy = 2;

    public static final int MaxAttemptsRetryPolicy = 3;

    public static final int ErrorClassifierRetryPolicy = 4;

    public static final int AlwaysRetryPolicy = 5;

    public static final int CompositeRetryPolicy = 6;

    public static final int CustomPolicyRtyPolicy = 7;

    public static final int NoBackOffPolicy = 0;

    public static final int FixedBackOffPolicy = 1;

    public static final int ExponentialBackOffPolicy = 2;

    public static final int ExponentialRandomBackOffPolicy = 3;

    public static final int UniformRandomBackoffPolicy = 4;


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
     * 重试策略相关参数
     * <p>
     * 重试策略类型,该参数为空时是，失败立即重试，重试的时候阻塞线程
     * NeverRetryPolicy 仅仅第一次重试，之后不允许重试，默认策略，
     * SimpleRetryPolicy 简单重试策略，失败后直接重试，没有休眠等
     * TimeoutRtyPolicy 超时重试策略， 只有在没有超时的情况下才进行重试，超时后则退出重试
     * MaxAttemptsRetryPolicy 设置最大重试次数的重试策略，耗尽后则不再重试
     * ErrorClassifierRetryPolicy 自定义错误分类器的重试策略
     * AlwaysRetryPolicy 一直重试，直到成功
     * CompositeRetryPolicy 组合重试策略，按照一定的组合顺序进行重试
     * CustomPolicyRtyPolicy 用户自定义重试策略
     */
    private Integer retryPolicy;
    /**
     * 最大重试次数，此值只有在RetryPolicyType为MaxAttemptsRetryPolicy/SimpleRetryPolicy时才生效，默认为3,包括第一次失败
     */
    private Integer retryMaxAttempts;
    /**
     * 超时时间，此值只有在RetryPolicyType为TimeoutRtyPolicy时才生效，默认为1000毫秒
     */
    private Integer retryTimeout;

    /**
     * 回退策略相关参数
     * <p>
     * 回退策略
     * <p>
     * NoBackOffPolicy 不回退，立即重试
     * FixedBackOffPolicy 休眠规定时长的回退策略
     * ExponentialBackOffPolicy 指数退避策略回退，每次回退会在上一次基础上乘N倍
     * ExponentialRandomBackOffPolicy 使用随机倍率的回退
     * UniformRandomBackoffPolicy 均匀随机回退
     */
    private Integer backoffPolicy;
    /**
     * 固定时长间隔的回退策略,在BackoffPolicyType为FixedBackOffPolicy才有值，默认为1000毫秒
     ***/
    private Integer fixedBackOffPeriodInMs;
    /**
     * **指数回退策略时参数，包含ExponentialBackOffPolicy/ExponentialRandomBackOffPolicy**
     * 延迟时间，单位毫秒，默认值1000，即默认延迟1秒。
     * 当未设置multiplier时，表示每隔backoffDelay的时间重试，
     * 直到重试次数到达maxAttempts设置的最大允许重试次数。
     * 当设置了multiplier参数时，该值作为幂运算的初始值。
     */
    private Integer backoffDelay;
    /**
     * 两次重试间最大间隔时间。
     * 当设置multiplier参数后，下次延迟时间根据是上次延迟时间乘以multiplier得出的，
     * 这会导致两次重试间的延迟时间越来越长，该参数限制两次重试的最大间隔时间
     * 当间隔时间大于该值时，计算出的间隔时间将会被忽略，使用上次的重试间隔时间
     */
    private Integer backoffMaxDelay;
    /**
     * 作为乘数用于计算下次延迟时间。公式：delay = delay * multiplier
     */
    private Integer backoffMultiplier = 0;

    /**
     * **均匀随机策略参数**
     * 最小回退间隔，默认500ms
     */
    private Integer uniformMinBackoffPeriod;

    /**
     * 最大回退间隔，默认1500ms
     */
    private Integer uniformMaxBackoffPeriod;

    /**
     * **异常分类**
     * ExactMatch    // 精确匹配
     * PrefixMatch   // 前缀匹配
     * SuffixMatch   // 后缀匹配
     * ContainMatch  // 包含匹配
     * RegularMatch  // 正则匹配
     * AnyMatch      // 只要不为空，则匹配
     */
    private Integer errorMatcher;

    /**
     * 需要重试的异常,默认为空。当参数exclude也为空时，所有异常都将要求重试
     */
    private String includeExceptions;

    /**
     * 不需要重试的异常。默认为空，当参include也为空时，所有异常都将要求重试
     */
    private String excludeExceptions;

    /**
     * grpc rsp key
     */
    private String grpcRspKey;

    /**
     * grpc rsp values
     */
    private String grpcRspValues;

    /**
     * 是否打开
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

    public Integer getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(Integer retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public Integer getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public void setRetryMaxAttempts(Integer retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public Integer getRetryTimeout() {
        return retryTimeout;
    }

    public void setRetryTimeout(Integer retryTimeout) {
        this.retryTimeout = retryTimeout;
    }

    public Integer getBackoffPolicy() {
        return backoffPolicy;
    }

    public void setBackoffPolicy(Integer backoffPolicy) {
        this.backoffPolicy = backoffPolicy;
    }

    public Integer getFixedBackOffPeriodInMs() {
        return fixedBackOffPeriodInMs;
    }

    public void setFixedBackOffPeriodInMs(Integer fixedBackOffPeriodInMs) {
        this.fixedBackOffPeriodInMs = fixedBackOffPeriodInMs;
    }

    public Integer getBackoffDelay() {
        return backoffDelay;
    }

    public void setBackoffDelay(Integer backoffDelay) {
        this.backoffDelay = backoffDelay;
    }

    public Integer getBackoffMaxDelay() {
        return backoffMaxDelay;
    }

    public void setBackoffMaxDelay(Integer backoffMaxDelay) {
        this.backoffMaxDelay = backoffMaxDelay;
    }

    public Integer getBackoffMultiplier() {
        return backoffMultiplier;
    }

    public void setBackoffMultiplier(Integer backoffMultiplier) {
        this.backoffMultiplier = backoffMultiplier;
    }

    public Integer getUniformMinBackoffPeriod() {
        return uniformMinBackoffPeriod;
    }

    public void setUniformMinBackoffPeriod(Integer uniformMinBackoffPeriod) {
        this.uniformMinBackoffPeriod = uniformMinBackoffPeriod;
    }

    public Integer getUniformMaxBackoffPeriod() {
        return uniformMaxBackoffPeriod;
    }

    public void setUniformMaxBackoffPeriod(Integer uniformMaxBackoffPeriod) {
        this.uniformMaxBackoffPeriod = uniformMaxBackoffPeriod;
    }

    public Integer getErrorMatcher() {
        return errorMatcher;
    }

    public void setErrorMatcher(Integer errorMatcher) {
        this.errorMatcher = errorMatcher;
    }

    public String getIncludeExceptions() {
        return includeExceptions;
    }

    public void setIncludeExceptions(String includeExceptions) {
        this.includeExceptions = includeExceptions;
    }

    public String getExcludeExceptions() {
        return excludeExceptions;
    }

    public void setExcludeExceptions(String excludeExceptions) {
        this.excludeExceptions = excludeExceptions;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
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

    public String getGrpcRspKey() {
        return grpcRspKey;
    }

    public void setGrpcRspKey(String grpcRspKey) {
        this.grpcRspKey = grpcRspKey;
    }

    public String getGrpcRspValues() {
        return grpcRspValues;
    }

    public void setGrpcRspValues(String grpcRspValues) {
        this.grpcRspValues = grpcRspValues;
    }

    @Override
    public String toString() {
        return "RetryRule{" +
                "id='" + id + '\'' +
                ", app='" + app + '\'' +
                ", limitApp='" + limitApp + '\'' +
                ", resource='" + resource + '\'' +
                ", retryPolicy=" + retryPolicy +
                ", retryMaxAttempts=" + retryMaxAttempts +
                ", retryTimeout=" + retryTimeout +
                ", backoffPolicy=" + backoffPolicy +
                ", fixedBackOffPeriodInMs=" + fixedBackOffPeriodInMs +
                ", backoffDelay=" + backoffDelay +
                ", backoffMaxDelay=" + backoffMaxDelay +
                ", backoffMultiplier=" + backoffMultiplier +
                ", uniformMinBackoffPeriod=" + uniformMinBackoffPeriod +
                ", uniformMaxBackoffPeriod=" + uniformMaxBackoffPeriod +
                ", errorMatcher=" + errorMatcher +
                ", includeExceptions='" + includeExceptions + '\'' +
                ", excludeExceptions='" + excludeExceptions + '\'' +
                ", grpcRspKey='" + grpcRspKey + '\'' +
                ", grpcRspValues='" + grpcRspValues + '\'' +
                ", open=" + open +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
