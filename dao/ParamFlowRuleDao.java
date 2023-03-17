package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.ParamFlowRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 降级规则Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface ParamFlowRuleDao {
    /**
     * 参数流控规则保存
     *
     * @param paramFlowRule 参数流控规则
     * @return 影响行数
     */
    int save(ParamFlowRule paramFlowRule);

    /**
     * 更新
     *
     * @param paramFlowRule 参数流控规则
     * @return 影响行数
     */
    int update(ParamFlowRule paramFlowRule);

    /**
     * 移除规则
     *
     * @param id 规则ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 根据ID加载参数流控规则
     *
     * @param id 主键ID
     * @return 流控规则
     */
    ParamFlowRule load(@Param("id") Long id);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param metricType     流控类型
     * @param permissionApps 授权应用
     * @return 记录数
     */
    List<ParamFlowRule> pageList(@Param("offset") int offset,
                                 @Param("pageSize") int pageSize,
                                 @Param("app") String app,
                                 @Param("resource") String resource,
                                 @Param("metricType") Integer metricType,
                                 @Param("permissionApps") List<String> permissionApps);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param metricType     流控类型
     * @param permissionApps 授权应用
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("app") String app,
                      @Param("resource") String resource,
                      @Param("metricType") Integer metricType,
                      @Param("permissionApps") List<String> permissionApps);

    /**
     * 根据应用名称查询热点参数流控规则
     *
     * @param app  应用名称
     * @param open 是否开启
     * @return 规则集合
     */
    List<ParamFlowRule> queryByApp(@Param("app") String app, @Param("open") Boolean open);

    /**
     * 判断app是否存在热点参数规则
     *
     * @param app 应用名称
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean exist(@Param("app") String app);

    /**
     * 参数规则加载
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 规则
     */
    ParamFlowRule loadBy(@Param("app") String app, @Param("resource") String resource);


    /**
     * 判断app对应的resource是否存在流控规则
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean existBy(@Param("app") String app, @Param("resource") String resource);

}
