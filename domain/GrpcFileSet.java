package com.shopee.seamiter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * grpc方法路径
 *
 * @author honggang.liu
 */
public class GrpcFileSet implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 服务ID
     */
    private Long serviceId;

    /**
     * 用来判断是否发生了变化
     */
    private Integer hashCode;

    /**
     * 协议文件流
     */
    private byte[] protoData;

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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public byte[] getProtoData() {
        return protoData;
    }

    public void setProtoData(byte[] protoData) {
        this.protoData = protoData;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getHashCode() {
        return hashCode;
    }

    public void setHashCode(Integer hashCode) {
        this.hashCode = hashCode;
    }
}
