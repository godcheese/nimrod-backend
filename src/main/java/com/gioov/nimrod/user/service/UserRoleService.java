package com.gioov.nimrod.user.service;

import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.user.entity.UserRoleEntity;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
public interface UserRoleService {

    /**
     * 指定 API 分类 id ，分页获取所有 API
     *
     * @param page 页
     * @param rows 每页显示数量
     * @return Pagination<UserRoleEntity>
     */
    Pagination<UserRoleEntity> pageAll(Integer page, Integer rows);

    /**
     * 新增角色
     *
     * @param userRoleEntity UserRoleEntity
     * @return UserRoleEntity
     */
    UserRoleEntity insertOne(UserRoleEntity userRoleEntity);

    /**
     * 指定角色 id list ，批量删除角色
     *
     * @param userId     用户 id
     * @param roleIdList 角色 id list
     * @return 已删除角色个数
     */
    int deleteAllByUserIdAndRoleIdList(Long userId, List<Long> roleIdList);

}
