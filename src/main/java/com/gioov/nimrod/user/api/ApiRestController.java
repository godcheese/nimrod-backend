package com.gioov.nimrod.user.api;

import com.gioov.common.web.exception.BaseResponseException;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.common.vue.antd.ApiEntityAsAntdTable;
import com.gioov.nimrod.common.vue.antd.ApiEntityAsAntdTable;
import com.gioov.nimrod.system.service.DictionaryService;
import com.gioov.nimrod.user.User;
import com.gioov.nimrod.user.entity.ApiEntity;
import com.gioov.nimrod.user.entity.ApiEntity;
import com.gioov.nimrod.user.mapper.*;
import com.gioov.nimrod.user.mapper.ApiMapper;
import com.gioov.nimrod.user.service.ApiService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.List;

import static com.gioov.nimrod.user.service.UserService.SYSTEM_ADMIN;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@RestController
@RequestMapping(value = User.Api.API, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiRestController {

    private static final String API = "/API/SYSTEM/API";

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;

    @Autowired
    private ViewPageApiMapper viewPageApiMapper;

    @Autowired
    private ViewPageComponentApiMapper viewPageComponentApiMapper;

    /**
     * 指定 API 分类 id ，分页获取所有 API
     *
     * @param page          页
     * @param rows          每页显示数量
     * @param apiCategoryId API 分类 id
     * @return ResponseEntity<Pagination<ApiEntity>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/PAGE_ALL_BY_API_CATEGORY_ID')")
    @GetMapping(value = "/page_all_by_api_category_id/{apiCategoryId}")
    public ResponseEntity<Pagination<ApiEntity>> pageAllByApiCategoryId(@RequestParam Integer page, @RequestParam Integer rows, @PathVariable Long apiCategoryId) {
        return new ResponseEntity<>(apiService.pageAllByApiCategoryId(apiCategoryId, page, rows), HttpStatus.OK);
    }

    /**
     * 新增 API
     *
     * @param name          API 名称
     * @param url           请求地址（url）
     * @param authority     权限（authority）
     * @param apiCategoryId API 分类 id
     * @param sort          排序
     * @param remark        备注
     * @return ResponseEntity<ApiEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/ADD_ONE')")
    @PostMapping(value = "/add_one")
    public ResponseEntity<ApiEntity> addOne(@RequestParam String name, @RequestParam String url, @RequestParam String authority, @RequestParam Long apiCategoryId, @RequestParam Long sort, @RequestParam String remark) throws BaseResponseException {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setName(name);
        apiEntity.setUrl(url);
        apiEntity.setAuthority(authority);
        apiEntity.setApiCategoryId(apiCategoryId);
        apiEntity.setSort(sort);
        apiEntity.setRemark(remark);
        ApiEntity apiEntity1 = apiService.insertOne(apiEntity);
        return new ResponseEntity<>(apiEntity1, HttpStatus.OK);
    }

    /**
     * 保存 API
     *
     * @param id     API id
     * @param name   API 名称
     * @param url    请求地址（url）
     * @param sort   排序
     * @param remark 备注
     * @return ResponseEntity<ApiEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/SAVE_ONE')")
    @PostMapping(value = "/save_one")
    public ResponseEntity<ApiEntity> saveOne(@RequestParam Long id, @RequestParam String name, @RequestParam String url, @RequestParam String authority, @RequestParam Long apiCategoryId, @RequestParam Long sort, @RequestParam String remark) {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setId(id);
        apiEntity.setName(name);
        apiEntity.setUrl(url);
        apiEntity.setAuthority(authority);
        apiEntity.setApiCategoryId(apiCategoryId);
        apiEntity.setSort(sort);
        apiEntity.setRemark(remark);
        ApiEntity apiEntity1 = apiService.updateOne(apiEntity);
        return new ResponseEntity<>(apiEntity1, HttpStatus.OK);
    }

    /**
     * 指定 API id list ，批量删除 API
     *
     * @param idList API id list
     * @return ResponseEntity<Integer>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/DELETE_ALL')")
    @PostMapping(value = "/delete_all")
    public ResponseEntity<Integer> deleteAll(@RequestParam("id[]") List<Long> idList) {
        return new ResponseEntity<>(apiService.deleteAll(idList), HttpStatus.OK);
    }

    /**
     * 指定 API id ， 获取 API 信息
     *
     * @param id API id
     * @return ResponseEntity<ApiEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/ONE')")
    @GetMapping(value = "/one/{id}")
    public ResponseEntity<ApiEntity> getOne(@PathVariable Long id) {
        return new ResponseEntity<>(apiService.getOne(id), HttpStatus.OK);
    }

    /**
     * 获取当前用户的菜单

     * @return ResponseEntity<Pagination<VueMenu>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/PAGE_ALL_AS_ANTD_TABLE_BY_ROLE_ID_AND_API_CATEGORY_ID_LIST')")
    @GetMapping(value = "/page_all_as_antd_table_by_role_id_and_api_category_id_list")
    public ResponseEntity<Pagination<ApiEntityAsAntdTable>> pageAllAsAntdTableByRoleIdAndApiCategoryIdList(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam Long roleId, @RequestParam List<Long> apiCategoryIdList) {
        Pagination<ApiEntityAsAntdTable> pagination = new Pagination<>();
        List<ApiEntityAsAntdTable> apiEntityAsAntdTableResultList = new ArrayList<>(0);

//        if(sorterField != null && !"".equals(sorterField) && sorterOrder != null && !"".equals(sorterOrder)) {
//            sorterField = StringUtil.camelToUnderline(sorterField);
//            String orderBy = sorterField + " " + sorterOrder;
//            PageHelper.startPage(page, rows, orderBy);
//        } else {
        PageHelper.startPage(page, rows);
//        }
        Page<ApiEntity> apiEntityPage = apiMapper.pageAllByApiCategoryIdList(apiCategoryIdList);
        Integer isOrtNotIs = Integer.valueOf(String.valueOf(dictionaryService.get("IS_OR_NOT", "IS")));
        Integer isOrtNotNot = Integer.valueOf(String.valueOf(dictionaryService.get("IS_OR_NOT", "NOT")));
        for(ApiEntity apiEntity : apiEntityPage.getResult()) {
            ApiEntityAsAntdTable apiEntityAsAntdTable = new ApiEntityAsAntdTable();
            apiEntityAsAntdTable.setId(apiEntity.getId());
            apiEntityAsAntdTable.setName(apiEntity.getName());
            apiEntityAsAntdTable.setUrl(apiEntity.getUrl());
            apiEntityAsAntdTable.setAuthority(apiEntity.getAuthority());
            apiEntityAsAntdTable.setApiCategoryId(apiEntity.getApiCategoryId());
            apiEntityAsAntdTable.setSort(apiEntity.getSort());
            apiEntityAsAntdTable.setRemark(apiEntity.getRemark());
            apiEntityAsAntdTable.setGmtModified(apiEntity.getGmtModified());
            apiEntityAsAntdTable.setGmtCreated(apiEntity.getGmtCreated());
            // 判断当前角色是否被授权此视图菜单
            apiEntityAsAntdTable.setIsGranted(roleAuthorityMapper.getOneByRoleIdAndAuthority(roleId, apiEntity.getAuthority()) != null ? isOrtNotIs : isOrtNotNot);
            apiEntityAsAntdTableResultList.add(apiEntityAsAntdTable);
        }
        pagination.setRows(apiEntityAsAntdTableResultList);
        pagination.setTotal(apiEntityPage.getTotal());
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    /**
     * 获取当前用户的菜单

     * @return ResponseEntity<Pagination<VueMenu>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/PAGE_ALL_AS_ANTD_TABLE_BY_API_CATEGORY_ID_LIST')")
    @GetMapping(value = "/page_all_as_antd_table_by_api_category_id_list")
    public ResponseEntity<Pagination<ApiEntityAsAntdTable>> pageAllAsAntdTableByApiCategoryIdList(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam List<Long> apiCategoryIdList) {
        Pagination<ApiEntityAsAntdTable> pagination = new Pagination<>();
        List<ApiEntityAsAntdTable> apiEntityAsAntdTableResultList = new ArrayList<>(0);

//        if(sorterField != null && !"".equals(sorterField) && sorterOrder != null && !"".equals(sorterOrder)) {
//            sorterField = StringUtil.camelToUnderline(sorterField);
//            String orderBy = sorterField + " " + sorterOrder;
//            PageHelper.startPage(page, rows, orderBy);
//        } else {
        PageHelper.startPage(page, rows);
//        }
        Page<ApiEntity> apiEntityPage = apiMapper.pageAllByApiCategoryIdList(apiCategoryIdList);
        for(ApiEntity apiEntity : apiEntityPage.getResult()) {
            ApiEntityAsAntdTable apiEntityAsAntdTable = new ApiEntityAsAntdTable();
            apiEntityAsAntdTable.setId(apiEntity.getId());
            apiEntityAsAntdTable.setName(apiEntity.getName());
            apiEntityAsAntdTable.setUrl(apiEntity.getUrl());
            apiEntityAsAntdTable.setAuthority(apiEntity.getAuthority());
            apiEntityAsAntdTable.setApiCategoryId(apiEntity.getApiCategoryId());
            apiEntityAsAntdTable.setSort(apiEntity.getSort());
            apiEntityAsAntdTable.setRemark(apiEntity.getRemark());
            apiEntityAsAntdTable.setGmtModified(apiEntity.getGmtModified());
            apiEntityAsAntdTable.setGmtCreated(apiEntity.getGmtCreated());
            apiEntityAsAntdTableResultList.add(apiEntityAsAntdTable);
        }
        pagination.setRows(apiEntityAsAntdTableResultList);
        pagination.setTotal(apiEntityPage.getTotal());
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    /**
     * 指定角色 id、API 权限（authority），批量授权
     *
     * @param roleId        角色 id
     * @param apiIdList 权限（authority） list
     * @return ResponseEntity<List < String>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/GRANT_ALL_BY_ROLE_ID_AND_API_ID_LIST')")
    @PostMapping(value = "/grant_all_by_role_id_and_api_id_list")
    public ResponseEntity<List<String>> grantAllByRoleIdAndApiCategoryIdList(@RequestParam Long roleId, @RequestParam("apiIdList[]") List<Long> apiIdList) {
        return new ResponseEntity<>(apiService.grantAllByRoleIdAndApiIdList(roleId, apiIdList), HttpStatus.OK);
    }

    /**
     * 指定角色 id、API 权限（authority），批量授权
     *
     * @param roleId        角色 id
     * @param apiIdList 权限（authority） list
     * @return ResponseEntity<List < String>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/REVOKE_ALL_BY_ROLE_ID_AND_API_ID_LIST')")
    @PostMapping(value = "/revoke_all_by_role_id_and_api_id_list")
    public ResponseEntity<List<String>> revokeAllByRoleIdAndApiIdList(@RequestParam Long roleId, @RequestParam("apiIdList[]") List<Long> apiIdList) {
        return new ResponseEntity<>(apiService.revokeAllByRoleIdAndApiIdList(roleId, apiIdList), HttpStatus.OK);
    }

    /**
     * 获取当前用户的菜单
     * @return ResponseEntity<Pagination<VueMenu>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/PAGE_ALL_AS_ANTD_TABLE_BY_PAGE_ID_AND_API_CATEGORY_ID_LIST')")
    @GetMapping(value = "/page_all_as_antd_table_by_page_id_and_api_category_id_list")
    public ResponseEntity<Pagination<ApiEntityAsAntdTable>> pageAllAsAntdTableByPageIdAndApiCategoryIdList(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam Long pageId, @RequestParam List<Long> apiCategoryIdList) {
        Pagination<ApiEntityAsAntdTable> pagination = new Pagination<>();
        List<ApiEntityAsAntdTable> apiEntityAsAntdTableResultList = new ArrayList<>(0);

//        if(sorterField != null && !"".equals(sorterField) && sorterOrder != null && !"".equals(sorterOrder)) {
//            sorterField = StringUtil.camelToUnderline(sorterField);
//            String orderBy = sorterField + " " + sorterOrder;
//            PageHelper.startPage(page, rows, orderBy);
//        } else {
        PageHelper.startPage(page, rows);
//        }
        Page<ApiEntity> apiEntityPage = apiMapper.pageAllByApiCategoryIdList(apiCategoryIdList);
        Integer isOrtNotIs = Integer.valueOf(String.valueOf(dictionaryService.get("IS_OR_NOT", "IS")));
        Integer isOrtNotNot = Integer.valueOf(String.valueOf(dictionaryService.get("IS_OR_NOT", "NOT")));
        for(ApiEntity apiEntity : apiEntityPage.getResult()) {
            ApiEntityAsAntdTable apiEntityAsAntdTable = new ApiEntityAsAntdTable();
            apiEntityAsAntdTable.setId(apiEntity.getId());
            apiEntityAsAntdTable.setName(apiEntity.getName());
            apiEntityAsAntdTable.setUrl(apiEntity.getUrl());
            apiEntityAsAntdTable.setAuthority(apiEntity.getAuthority());
            apiEntityAsAntdTable.setApiCategoryId(apiEntity.getApiCategoryId());
            apiEntityAsAntdTable.setSort(apiEntity.getSort());
            apiEntityAsAntdTable.setRemark(apiEntity.getRemark());
            apiEntityAsAntdTable.setGmtModified(apiEntity.getGmtModified());
            apiEntityAsAntdTable.setGmtCreated(apiEntity.getGmtCreated());
            // 判断当前角色是否被授权此视图菜单
            apiEntityAsAntdTable.setIsGranted(viewPageApiMapper.getOneByPageIdAndApiId(pageId, apiEntity.getId()) != null ? isOrtNotIs : isOrtNotNot);
            apiEntityAsAntdTableResultList.add(apiEntityAsAntdTable);
        }
        pagination.setRows(apiEntityAsAntdTableResultList);
        pagination.setTotal(apiEntityPage.getTotal());
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    /**
     * 获取当前用户的菜单
     * @return ResponseEntity<Pagination<VueMenu>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + API + "/PAGE_ALL_AS_ANTD_TABLE_BY_PAGE_COMPONENT_ID_AND_API_CATEGORY_ID_LIST')")
    @GetMapping(value = "/page_all_as_antd_table_by_page_component_id_and_api_category_id_list")
    public ResponseEntity<Pagination<ApiEntityAsAntdTable>> pageAllAsAntdTableByPageComponentIdAndApiCategoryIdList(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam Long pageComponentId, @RequestParam List<Long> apiCategoryIdList) {
        Pagination<ApiEntityAsAntdTable> pagination = new Pagination<>();
        List<ApiEntityAsAntdTable> apiEntityAsAntdTableResultList = new ArrayList<>(0);

//        if(sorterField != null && !"".equals(sorterField) && sorterOrder != null && !"".equals(sorterOrder)) {
//            sorterField = StringUtil.camelToUnderline(sorterField);
//            String orderBy = sorterField + " " + sorterOrder;
//            PageHelper.startPage(page, rows, orderBy);
//        } else {
        PageHelper.startPage(page, rows);
//        }
        Page<ApiEntity> apiEntityPage = apiMapper.pageAllByApiCategoryIdList(apiCategoryIdList);
        Integer isOrtNotIs = Integer.valueOf(String.valueOf(dictionaryService.get("IS_OR_NOT", "IS")));
        Integer isOrtNotNot = Integer.valueOf(String.valueOf(dictionaryService.get("IS_OR_NOT", "NOT")));
        for(ApiEntity apiEntity : apiEntityPage.getResult()) {
            ApiEntityAsAntdTable apiEntityAsAntdTable = new ApiEntityAsAntdTable();
            apiEntityAsAntdTable.setId(apiEntity.getId());
            apiEntityAsAntdTable.setName(apiEntity.getName());
            apiEntityAsAntdTable.setUrl(apiEntity.getUrl());
            apiEntityAsAntdTable.setAuthority(apiEntity.getAuthority());
            apiEntityAsAntdTable.setApiCategoryId(apiEntity.getApiCategoryId());
            apiEntityAsAntdTable.setSort(apiEntity.getSort());
            apiEntityAsAntdTable.setRemark(apiEntity.getRemark());
            apiEntityAsAntdTable.setGmtModified(apiEntity.getGmtModified());
            apiEntityAsAntdTable.setGmtCreated(apiEntity.getGmtCreated());
            // 判断当前角色是否被授权此视图菜单
            apiEntityAsAntdTable.setIsGranted(viewPageComponentApiMapper.getOneByPageComponentIdAndApiId(pageComponentId, apiEntity.getId()) != null ? isOrtNotIs : isOrtNotNot);
            apiEntityAsAntdTableResultList.add(apiEntityAsAntdTable);
        }
        pagination.setRows(apiEntityAsAntdTableResultList);
        pagination.setTotal(apiEntityPage.getTotal());
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

}
