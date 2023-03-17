package com.shopee.seamiter.domain;

import java.io.Serializable;

/**
 * 手动触发信息
 *
 * @author honggang.liu
 */
public class TriggerInfo implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 执行参数
     */
    private String executorParam;
    /**
     * 执行参数
     */
    private String addressList;

    /**
     * 执行头信息
     */
    private String metaData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "TriggerInfo{" +
                "id=" + id +
                ", executorParam='" + executorParam + '\'' +
                ", addressList='" + addressList + '\'' +
                ", headers='" + metaData + '\'' +
                '}';
    }
}
