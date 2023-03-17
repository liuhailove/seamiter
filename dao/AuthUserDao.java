package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户管理
 *
 * @author honggang.liu
 */
@Mapper
public interface AuthUserDao {

    List<AuthUser> pageList(@Param("offset") int offset,
                            @Param("pagesize") int pagesize,
                            @Param("username") String username,
                            @Param("roleName") String roleName);

    int pageListCount(@Param("offset") int offset,
                      @Param("pagesize") int pagesize,
                      @Param("username") String username,
                      @Param("roleName") String roleName);

    AuthUser loadByUserName(@Param("username") String username);

    AuthUser load(@Param("id") int id);

    int save(AuthUser authUser);

    int update(AuthUser authUser);

    int updatePwd(AuthUser authUser);

    int delete(@Param("id") int id);

    /**
     * 根据角色名称加载用户
     *
     * @param roleName 角色名称
     * @return 用户
     */
    List<AuthUser> loadByRoleName(@Param("offset") int offset,
                                  @Param("pagesize") int pagesize,
                                  @Param("roleName") String roleName);

    /**
     * 根据角色名称统计用户数量
     *
     * @param roleName 角色名称
     * @return 用户数量
     */
    int loadByRoleNameCount(@Param("roleName") String roleName);

    /**
     * 根据用户组名加载用户
     *
     * @param ugroupName 用户组名
     * @return 用户
     */
    List<AuthUser> loadByUGroupName(@Param("ugroupName") String ugroupName);

    /**
     * 根据应用名称查询授权的用户
     *
     * @param appName 应用名称
     * @return 授权用户
     */
    List<AuthUser> loadByAppName(@Param("appName") String appName);

}
