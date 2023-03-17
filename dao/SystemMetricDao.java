package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.SystemMetric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 资源度量指标DAO
 *
 * @author honggang.liu
 */
@Mapper
public interface SystemMetricDao {

    /**
     * 保存
     *
     * @param systemMetric 资源
     * @return 影响行数
     */
    int save(SystemMetric systemMetric);

    /**
     * 批量保存
     *
     * @param systemMetrics 资源集合
     * @return 行数
     */
    int saveList(@Param("systemMetrics") List<SystemMetric> systemMetrics);

    /**
     * 移除
     *
     * @param id ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 移除
     *
     * @param clearBeforeTime 清理时间
     * @param limit           处理条数
     * @return 影响行数
     */
    int removeBy(@Param("clearBeforeTime") Date clearBeforeTime,
                 @Param("limit") int limit);

    /**
     * 查找
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @param ts       时间戳
     * @param ip       ip
     * @param port     端口
     * @return 记录数
     */
    SystemMetric queryByTs(@Param("app") String app,
                           @Param("ip") String ip,
                           @Param("port") Integer port,
                           @Param("resource") String resource,
                           @Param("ts") Date ts);

    /**
     * 分页查找
     *
     * @param offset   偏移量
     * @param pageSize 分页大小
     * @param app      应用名称
     * @param resource 资源名称
     * @param ip       ip地址
     * @param port     端口
     * @return 记录数
     */
    List<SystemMetric> pageList(@Param("offset") int offset,
                                @Param("pageSize") int pageSize,
                                @Param("app") String app,
                                @Param("resource") String resource,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime);

    /**
     * 查询Top5Cpu
     *
     * @param app       应用名称
     * @param resource  资源名称
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 记录数
     */
    List<SystemMetric> queryTop3Cpu(@Param("app") String app,
                                    @Param("resource") String resource,
                                    @Param("startTime") Date startTime,
                                    @Param("endTime") Date endTime);


    /**
     * 分页查找
     *
     * @param offset   偏移量
     * @param pageSize 分页大小
     * @param app      应用名称
     * @param resource 资源名称
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("app") String app,
                      @Param("resource") String resource,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime);

    /**
     * 查找
     *
     * @param app       应用名称
     * @param resource  资源名称
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 记录数
     */
    List<SystemMetric> queryAppMetric(@Param("app") String app,
                                      @Param("resource") String resource,
                                      @Param("startTime") Date startTime,
                                      @Param("endTime") Date endTime
    );
}
