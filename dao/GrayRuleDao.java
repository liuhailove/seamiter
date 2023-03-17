package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.GrayRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 灰度规则Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface GrayRuleDao {
    /**
     * 灰度规则保存
     *
     * @param grayRule 灰度规则
     * @return 影响行数
     */
    int save(GrayRule grayRule);

    /**
     * 灰度规则更新
     *
     * @param grayRule 灰度规则
     * @return 影响行数
     */
    int update(GrayRule grayRule);

    /**
     * 移除规则
     *
     * @param id 规则ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param ruleName       规则名称
     * @param routerStrategy 路由策略
     * @param permissionApps 授权应用
     * @return 记录数
     */
    List<GrayRule> pageList(@Param("offset") int offset,
                            @Param("pageSize") int pageSize,
                            @Param("app") String app,
                            @Param("resource") String resource,
                            @Param("ruleName") String ruleName,
                            @Param("routerStrategy") Integer routerStrategy,
                            @Param("open") Boolean open,
                            @Param("permissionApps") List<String> permissionApps);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resource       资源名称
     * @param ruleName       规则名称
     * @param routerStrategy 路由策略
     * @param permissionApps 授权应用
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("app") String app,
                      @Param("resource") String resource,
                      @Param("ruleName") String ruleName,
                      @Param("routerStrategy") Integer routerStrategy,
                      @Param("open") Boolean open,
                      @Param("permissionApps") List<String> permissionApps);

    /**
     * 根据应用名称查询灰度规则
     *
     * @param app  应用名称
     * @param open 是否开启
     * @return 规则集合
     */
    List<GrayRule> queryByApp(@Param("app") String app, @Param("open") Boolean open);

    /**
     * 判断app是否存在灰度规则
     *
     * @param app 应用名称
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean exist(@Param("app") String app);

    /**
     * 判断app对应的resource是否存在灰度规则
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean existBy(@Param("app") String app, @Param("resource") String resource);

    /**
     * 规则ID
     *
     * @param id 主键ID
     * @return 流控规则
     */
    GrayRule load(@Param("id") Long id);

    /**
     * 灰度规则加载
     *
     * @param app      应用名称
     * @param resource 资源名称
     * @return 规则
     */
    GrayRule loadBy(@Param("app") String app, @Param("resource") String resource);
}
