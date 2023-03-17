package com.shopee.seamiter.domain;

import java.io.Serializable;

/**
 * 权重标签
 *
 * @author honggang.liu
 */
public class GrayWeight implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 灰度规则ID
     */
    private Long grayRuleId;

    /**
     * 生效地址
     * 配置是否只对某几个特定实例生效。
     * 所有实例：addresses: ["0.0.0.0"] 或addresses: ["0.0.0.0:*"] 具体由side值决定。
     * 指定实例：addersses[实例地址列表]。
     */
    private String effectiveAddresses;

    /**
     * 目标target
     */
    private String targetResource;

    /**
     * 目标版本
     */
    private String targetVersion;

    /**
     * 权重
     */
    private Double weight;

    /**
     * 是否打开
     */
    private Boolean open;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGrayRuleId() {
        return grayRuleId;
    }

    public void setGrayRuleId(Long grayRuleId) {
        this.grayRuleId = grayRuleId;
    }

    public String getEffectiveAddresses() {
        return effectiveAddresses;
    }

    public void setEffectiveAddresses(String effectiveAddresses) {
        this.effectiveAddresses = effectiveAddresses;
    }

    public String getTargetResource() {
        return targetResource;
    }

    public void setTargetResource(String targetResource) {
        this.targetResource = targetResource;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "GrayWeight{" +
                "id=" + id +
                ", grayRuleId=" + grayRuleId +
                ", effectiveAddresses='" + effectiveAddresses + '\'' +
                ", targetResource='" + targetResource + '\'' +
                ", targetVersion='" + targetVersion + '\'' +
                ", weight=" + weight +
                ", open=" + open +
                '}';
    }
}
