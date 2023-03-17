package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.Platform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台管理
 *
 * @author honggang.liu
 */
@Mapper
public interface PlatformDao {

    List<Platform> pageList(@Param("offset") int offset,
                            @Param("pagesize") int pagesize,
                            @Param("platformName") String platformName,
                            @Param("env") String env,
                            @Param("region") String region,
                            @Param("platformNames") List<String> platformNames);

    int pageListCount(@Param("offset") int offset,
                      @Param("pagesize") int pagesize,
                      @Param("platformName") String platformName,
                      @Param("env") String env,
                      @Param("region") String region,
                      @Param("platformNames") List<String> platformNames);

    Platform load(@Param("id") int id);

    /**
     * 根据平台名称加载平台
     *
     * @param platformName 平台名称
     * @return 平台
     */
    Platform loadByName(@Param("platformName") String platformName);

    int save(Platform platform);

    int update(Platform platform);

    int delete(@Param("id") int id);

    /**
     * 分页查询
     *
     * @param offset   偏移
     * @param pagesize 分页大小
     * @param env      包含的环境
     * @return 平台明细
     */
    List<Platform> pageListWithEnv(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("env") String env);

    /**
     * 分页统计
     *
     * @param offset   偏移
     * @param pagesize 分页大小
     * @param env      包含的环境
     * @return 平台统计
     */
    int pageListCountWithEnv(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("env") String env);

}
