package com.gioov.nimrod.system.api;

import com.gioov.common.web.exception.BaseResponseException;
import com.gioov.nimrod.common.Common;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.system.System;
import com.gioov.nimrod.system.entity.DictionaryCategoryEntity;
import com.gioov.nimrod.system.entity.DictionaryEntity;
import com.gioov.nimrod.system.mapper.DictionaryMapper;
import com.gioov.nimrod.system.service.DictionaryCategoryService;
import com.gioov.nimrod.system.service.DictionaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gioov.nimrod.user.service.UserService.SYSTEM_ADMIN;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@RestController
@RequestMapping(value = System.Api.DICTIONARY, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DictionaryRestController {

    private static final String DICTIONARY = "/API/SYSTEM/DICTIONARY";

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private Common common;

    @Autowired
    private DictionaryCategoryService dictionaryCategoryService;

    @Autowired
    private DictionaryMapper dictionaryMapper;

    /**
     * 指定父级数据字典分类 id ，获取所有数据字典
     *
     * @param page                 页
     * @param rows                 每页显示数量
     * @param dictionaryCategoryId 数据字典分类 id
     * @return ResponseEntity<Pagination<DictionaryEntity>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/PAGE_ALL_BY_DICTIONARY_CATEGORY_ID')")
    @GetMapping(value = "/page_all_by_dictionary_category_id/{dictionaryCategoryId}")
    public ResponseEntity<Pagination<DictionaryEntity>> pageAllByDictionaryCategoryId(@RequestParam Integer page, @RequestParam Integer rows, @PathVariable Long dictionaryCategoryId) {
        return new ResponseEntity<>(dictionaryService.pageAllByDictionaryCategoryId(dictionaryCategoryId, page, rows), HttpStatus.OK);
    }

    /**
     * 新增数据字典
     *
     * @param key                  数据字典键
     * @param keyName              数据字典键名
     * @param valueName            数据字典值名
     * @param valueSlug            数据字典值别名
     * @param dictionaryCategoryId 数据字典分类 id
     * @param value                数据字典值
     * @param enabled             是否启用
     * @param sort                 排序
     * @param remark               备注
     * @return ResponseEntity<DictionaryEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/ADD_ONE')")
    @PostMapping(value = "/add_one")
    public ResponseEntity<DictionaryEntity> addOne(@RequestParam String keyName, @RequestParam String key, @RequestParam String valueName, @RequestParam String valueSlug, @RequestParam String value, @RequestParam Long dictionaryCategoryId, @RequestParam Integer enabled, @RequestParam Long sort, @RequestParam String remark) {
        DictionaryEntity dictionaryEntity = new DictionaryEntity();
        dictionaryEntity.setKeyName(keyName);
        dictionaryEntity.setKey(key);
        dictionaryEntity.setValueName(valueName);
        dictionaryEntity.setValueSlug(valueSlug);
        dictionaryEntity.setValue(value);
        dictionaryEntity.setDictionaryCategoryId(dictionaryCategoryId);
        dictionaryEntity.setEnabled(enabled);
        dictionaryEntity.setSort(sort);
        dictionaryEntity.setRemark(remark);
        DictionaryEntity dictionaryEntity1 = dictionaryService.insertOne(dictionaryEntity);
        return new ResponseEntity<>(dictionaryEntity1, HttpStatus.OK);
    }

    /**
     * 保存数据字典
     *
     * @param id        数据字典 id
     * @param key       数据字典键
     * @param keyName   数据字典键名
     * @param valueName 数据字典值名
     * @param valueSlug 数据字典值别名
     * @param value     数据字典值
     * @param sort      排序
     * @param remark    备注
     * @return ResponseEntity<DictionaryEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/SAVE_ONE')")
    @PostMapping(value = "/save_one")
    public ResponseEntity<DictionaryEntity> saveOne(@RequestParam Long id, @RequestParam String keyName, @RequestParam String key, @RequestParam String valueName, @RequestParam String valueSlug, @RequestParam String value, @RequestParam Long dictionaryCategoryId, @RequestParam Integer enabled, @RequestParam Long sort, @RequestParam String remark) {
        DictionaryEntity dictionaryEntity = dictionaryService.getOne(id);
        dictionaryEntity.setKeyName(keyName);
        dictionaryEntity.setKey(key);
        dictionaryEntity.setValueName(valueName);
        dictionaryEntity.setValueSlug(valueSlug);
        dictionaryEntity.setValue(value);
        dictionaryEntity.setDictionaryCategoryId(dictionaryCategoryId);
        dictionaryEntity.setEnabled(enabled);
        dictionaryEntity.setSort(sort);
        dictionaryEntity.setRemark(remark);
        DictionaryEntity dictionaryEntity1 = dictionaryService.updateOne(dictionaryEntity);
        return new ResponseEntity<>(dictionaryEntity1, HttpStatus.OK);
    }

    /**
     * 指定数据字典 id ， 批量删除数据字典
     *
     * @param idList 数据字典 id list
     * @return ResponseEntity<Integer>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/DELETE_ALL')")
    @PostMapping("/delete_all")
    public ResponseEntity<Integer> deleteAll(@RequestParam("id[]") List<Long> idList) {
        return new ResponseEntity<>(dictionaryService.deleteAll(idList), HttpStatus.OK);
    }

    /**
     * 指定数据字典 id ，获取数据字典信息
     *
     * @param id 数据字典 id
     * @return ResponseEntity<DictionaryEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/ONE')")
    @GetMapping(value = "/one/{id}")
    public ResponseEntity<DictionaryEntity> getOne(@PathVariable Long id) {
        return new ResponseEntity<>(dictionaryService.getOne(id), HttpStatus.OK);
    }

    /**
     * 获取所有数据字典
     *
     * @return ResponseEntity<Map<String, Map<String, Object>>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/LIST_ALL')")
    @RequestMapping("/list_all")
    public ResponseEntity<Map<String, Map<String, Object>>> listAll() {
        return new ResponseEntity<>(dictionaryService.keyValueMap(), HttpStatus.OK);
    }

    /**
     * 指定数据字典键 ，从内存中获取所有数据字典
     *
     * @param key 数据字典键
     * @return ResponseEntity<List<DictionaryEntity>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/LIST_ALL_BY_KEY')")
    @RequestMapping("/list_all_by_key/{key}")
    public ResponseEntity<List<DictionaryEntity>> listAllByKey(@PathVariable String key) {
        return new ResponseEntity<>(dictionaryService.get(key), HttpStatus.OK);
    }

    /**
     * 同步数据字典到内存中
     *
     * @return ResponseEntity<HttpStatus>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/SYNC_TO_MEMORY')")
    @PostMapping(value = "/sync_to_memory")
    public ResponseEntity<HttpStatus> syncToMemory() {
        common.initialize();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 保存数据字典
     *
     * @return ResponseEntity<DictionaryEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/MOVE_TO_DICTIONARY_CATEGORY')")
    @PostMapping(value = "/move_to_dictionary_category")
    public ResponseEntity<Integer> moveToDictionaryCategory(@RequestParam Long dictionaryCategoryId, @RequestParam(required = false, name = "dictionaryId[]") List<Long> dictionaryIdList, @RequestParam(required = false, name = "dictionaryCategoryId[]") List<Long> dictionaryCategoryIdList) {
        DictionaryEntity dictionaryEntity;
        DictionaryCategoryEntity dictionaryCategoryEntity;

        int index = 0;
        if(dictionaryIdList != null) {
            for (Long dictionaryId : dictionaryIdList) {
                dictionaryEntity = dictionaryService.getOne(dictionaryId);
                dictionaryEntity.setDictionaryCategoryId(dictionaryCategoryId);
                dictionaryService.updateOne(dictionaryEntity);
                index++;
            }
        } else if(dictionaryCategoryIdList != null) {
            for (Long mDictionaryCategoryId : dictionaryCategoryIdList) {
                dictionaryCategoryEntity = dictionaryCategoryService.getOne(mDictionaryCategoryId);
                dictionaryCategoryEntity.setParentId(dictionaryCategoryId);
                dictionaryCategoryService.updateOne(dictionaryCategoryEntity);
                index++;
            }
        }

        return new ResponseEntity<>(index, HttpStatus.OK);
    }


    /**
     * 指定数据字典 id ，导出数据字典信息
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param idList
     * @throws BaseResponseException
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/ONE')")
    @GetMapping(value = "/export_all_by_dictionary_category_id_list")
    public void exportAllByDictionaryCategoryIdList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("id") List<Long> idList) throws BaseResponseException {
        dictionaryService.exportAllByDictionaryCategoryIdList(httpServletRequest, httpServletResponse, idList);
    }

    /**
     * 导入数据字典
     *
     * @param file       文件
     * @param categoryId 数据字典分类 id
     * @return ResponseEntity<DictionaryEntity>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/ONE')")
    @PostMapping(value = "/import_all_by_dictionary_category_id")
    public void importAllByDictionaryCategoryId(@RequestParam MultipartFile file, @RequestParam Long categoryId) throws BaseResponseException {
        dictionaryService.importAllByDictionaryCategoryId(file, categoryId);
    }

    /**
     * 获取当前用户的菜单

     * @return ResponseEntity<Pagination<VueMenu>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + DICTIONARY + "/PAGE_ALL_AS_ANTD_TABLE_BY_API_CATEGORY_ID_LIST')")
    @GetMapping(value = "/page_all_by_dictionary_category_id_list")
    public ResponseEntity<Pagination<DictionaryEntity>> pageAllByDictionaryCategoryIdList(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam List<Long> dictionaryCategoryIdList) {
        Pagination<DictionaryEntity> pagination = new Pagination<>();

//        if(sorterField != null && !"".equals(sorterField) && sorterOrder != null && !"".equals(sorterOrder)) {
//            sorterField = StringUtil.camelToUnderline(sorterField);
//            String orderBy = sorterField + " " + sorterOrder;
//            PageHelper.startPage(page, rows, orderBy);
//        } else {
        PageHelper.startPage(page, rows);
//        }
        Page<DictionaryEntity> dictionaryEntityPage = dictionaryMapper.pageAllByDictionaryCategoryIdList(dictionaryCategoryIdList);
        pagination.setRows(dictionaryEntityPage.getResult());
        pagination.setTotal(dictionaryEntityPage.getTotal());
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }
}
