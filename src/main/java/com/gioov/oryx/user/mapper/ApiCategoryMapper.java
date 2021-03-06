package com.gioov.oryx.user.mapper;

import com.gioov.tile.mybatis.CrudMapper;
import com.gioov.oryx.user.entity.ApiCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@Component("apiCategoryMapper")
@Mapper
public interface ApiCategoryMapper extends CrudMapper<ApiCategoryEntity, Long> {

//    /**
//     * 分页获取所有父级 id 为 null 的 API 分类
//     * @return List<ApiCategoryEntity>
//     */
//    Page<ApiCategoryEntity> pageAllByParentIdIsNull();

    /**
     * 指定父级 API 分类 id，获取所有 API 分类
     * @param parentId 父级 API 分类 id
     * @return List<ApiCategoryEntity>
     */
    List<ApiCategoryEntity> listAllByParentId(@Param("parentId") Long parentId);

    /**
     * 指定父级 API 分类 id，获取所有 API 分类
     * @param parentId 父级 API 分类 id
     * @return ApiCategoryEntity
     */
    ApiCategoryEntity getOneByParentId(@Param("parentId") Long parentId);

}
