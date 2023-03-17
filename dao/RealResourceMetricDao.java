package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.RealResourceMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 资源度量直白哦DAO
 *
 * @author honggang.liu
 */
@Mapper
public interface RealResourceMetricDao {

    /**
     * 保存
     *
     * @param flowResource 资源
     * @return 影响行数
     */
    int save(RealResourceMetric resourceMetric);

    /**
     * 更新
     *
     * @param resourceMetric 资源
     * @return 影响行数
     */
    int update(RealResourceMetric resourceMetric);

//    /**
//     * 查询
//     *
//     * @param app      应用名称
//     * @param resource 资源名称
//     * @return 实时资源
//     */
//    RealResourceMetric query(@Param("app") String app, @Param("resource") String resource);

    /**
     * 移除
     *
     * @param id ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 移除已经失活的度量指标
     *
     * @param app      应用
     * @param deadTime 失活时间
     * @return 影响行数
     */
    int removeDead(@Param("app") String app, @Param("deadTime") Date deadTime);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param permissionApps 授权Apps
     * @return 记录数
     */
    List<RealResourceMetric> pageList(@Param("offset") int offset,
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
     * @param permissionApps 授权Apps
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("app") String app,
                      @Param("resource") String resource,
                      @Param("permissionApps") List<String> permissionApps);

    /**
     * 分页查找
     *
     * @param offset    偏移量
     * @param pageSize  分页大小
     * @param app       应用名称
     * @param resource  资源名称
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 记录数
     */
    List<String> resourcePageList(@Param("offset") int offset,
                                  @Param("pageSize") int pageSize,
                                  @Param("app") String app,
                                  @Param("resource") String resource,
                                  @Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime);

    /**
     * 流控指标rest,用于不在线时的更新
     *
     * @param app 应用
     * @return 影响行数
     */
    int reset(@Param("app") String app);


}
