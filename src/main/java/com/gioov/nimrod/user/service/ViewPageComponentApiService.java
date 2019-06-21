package com.gioov.nimrod.user.service;

import java.util.List;
import java.util.Map;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
public interface ViewPageComponentApiService {

    /**
     * 是否关联 API
     *
     * @param pageComponentId 视图页面组件 id
     * @param apiId           API id
     * @return Map<String, Object>
     */
    Map<String, Object> isAssociatedByPageComponentIdAndApiId(Long pageComponentId, Long apiId);

    /**
     * 批量关联 API
     *
     * @param pageComponentId 视图页面组件 id
     * @param apiIdList       API id list
     * @return List<Long>
     */
    List<Long> associateAllByPageComponentIdAndApiIdList(Long pageComponentId, List<Long> apiIdList);

    /**
     * 指定视图页面组件 id、API id ，批量撤销关联
     *
     * @param pageComponentId 视图页面组件 id
     * @param apiIdList       API id list
     * @return int
     */
    int revokeAssociateAllByPageComponentIdAndApiIdList(Long pageComponentId, List<Long> apiIdList);

}
