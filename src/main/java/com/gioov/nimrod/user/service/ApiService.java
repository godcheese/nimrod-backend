package com.gioov.nimrod.user.service;

import com.gioov.common.web.exception.BaseResponseException;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.user.entity.ApiEntity;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
public interface ApiService {

    /**
     * 指定 API 分类 id ，分页获取所有 API
     *
     * @param apiCategoryId API 分类 id
     * @param page          页
     * @param rows          每页显示数量
     * @return Pagination<ApiEntity>
     */
    Pagination<ApiEntity> pageAllByApiCategoryId(Long apiCategoryId, Integer page, Integer rows);

    /**
     * 新增 API
     *
     * @param apiEntity ApiEntity
     * @return ApiEntity
     * @throws BaseResponseException BaseResponseException
     */
    ApiEntity insertOne(ApiEntity apiEntity) throws BaseResponseException;

    /**
     * 保存 API
     *
     * @param apiEntity ApiEntity
     * @return ApiEntity
     */
    ApiEntity updateOne(ApiEntity apiEntity);

    /**
     * 指定 API id list ，批量删除 API
     *
     * @param idList API id list
     * @return 已删除 API 个数
     */
    int deleteAll(List<Long> idList);

    /**
     * 指定 API id ，获取 API 信息
     *
     * @param id API id
     * @return ApiEntity
     */
    ApiEntity getOne(Long id);

    /**
     * 指定角色 id、API 权限（authority），批量授权
     *
     * @param roleId        角色 id
     * @param apiIdList 权限（authority） list
     * @return List<String>
     */
    List<String> grantAllByRoleIdAndApiIdList(Long roleId, List<Long> apiIdList);

    /**
     * 指定角色 id、API 权限（authority），批量撤销授权
     *
     * @param roleId        角色 id
     * @param apiIdList 权限（authority） list
     * @return List<String>
     */
    List<String> revokeAllByRoleIdAndApiIdList(Long roleId, List<Long> apiIdList);

}
