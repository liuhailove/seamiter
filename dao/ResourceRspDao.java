package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.ResourceRsp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源Rsp Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface ResourceRspDao {
    /**
     * 保存
     *
     * @param resourceRsp 资源响应
     * @return 影响行数
     */
    int save(ResourceRsp resourceRsp);

    /**
     * 更新
     *
     * @param resourceRsp 资源响应
     * @return 影响行数
     */
    int update(ResourceRsp resourceRsp);

    /**
     * 移除
     *
     * @param id 规则ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 查看
     *
     * @param id 主键ID
     * @return 资源响应
     */
    ResourceRsp load(@Param("id") Long id);

    /**
     * 资源响应
     *
     * @param app          应用名称
     * @param resourceName 资源名称
     * @return 资源响应
     */
    ResourceRsp loadBy(@Param("app") String app, @Param("resourceName") String resourceName);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param permissionApps 授权应用
     * @return 记录数
     */
    List<ResourceRsp> pageList(@Param("offset") int offset,
                               @Param("pageSize") int pageSize,
                               @Param("app") String app,
                               @Param("resource") String resource,
                               @Param("permissionApps") List<String> permissionApps);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param permissionApps 授权应用
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("app") String app,
                      @Param("resource") String resource,
                      @Param("permissionApps") List<String> permissionApps);
}
