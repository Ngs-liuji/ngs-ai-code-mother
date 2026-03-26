package com.ngsliuji.ngsaicodemother.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ngsliuji.ngsaicodemother.common.DeleteRequest;
import com.ngsliuji.ngsaicodemother.model.dto.app.*;
import com.ngsliuji.ngsaicodemother.model.entity.App;
import com.ngsliuji.ngsaicodemother.model.vo.AppVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Dell
 * @description 针对表【app(应用)】的数据库操作Service
 * @createDate 2026-03-13 20:14:50
 */
public interface AppService extends IService<App> {
    /**
     * 创建应用（用户）
     *
     * @param appAddRequest 创建请求
     * @param request       HttpServletRequest
     * @return 新应用 id
     */
    Long createApp(AppAddRequest appAddRequest, HttpServletRequest request);

    /**
     * 修改自己的应用名称（仅 appName）
     *
     * @param updateNameRequest 修改请求
     * @param request            HttpServletRequest
     * @return 是否成功
     */
    boolean updateMyAppName(AppUpdateNameRequest updateNameRequest, HttpServletRequest request);

    /**
     * 删除自己的应用
     *
     * @param deleteRequest 删除请求
     * @param request       HttpServletRequest
     * @return 是否成功
     */
    boolean deleteMyApp(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 查看自己的应用详情
     *
     * @param id      应用 id
     * @param request HttpServletRequest
     * @return 应用详情
     */
    AppVO getMyAppVOById(long id, HttpServletRequest request);

    /**
     * 分页查询自己的应用列表（支持根据名称查询）
     *
     * @param queryRequest 查询请求
     * @param request       HttpServletRequest
     * @return 应用封装分页数据
     */
    Page<AppVO> listMyAppsByPage(AppQueryByUserRequest queryRequest, HttpServletRequest request);

    /**
     * 分页查询精选应用列表（支持根据名称查询）
     *
     * @param queryRequest 查询请求
     * @param request       HttpServletRequest
     * @return 应用封装分页数据
     */
    Page<AppVO> listMySelectedAppsByPage(AppQuerySelectedRequest queryRequest, HttpServletRequest request);

    /**
     * 管理员：根据 id 删除任意应用
     */
    boolean adminDeleteApp(long id);

    /**
     * 管理员：根据 id 更新任意应用（appName/cover/priority）
     */
    boolean adminUpdateApp(AppAdminUpdateRequest updateRequest);

    /**
     * 管理员：分页查询应用列表（支持根据除时间外的任何字段查询）
     */
    Page<AppVO> adminListAppsByPage(AppAdminQueryRequest queryRequest);

    /**
     * 管理员：根据 id 查看应用详情
     */
    AppVO adminGetAppVOById(long id);

    /**
     * 获取用户侧查询条件
     */
    QueryWrapper<App> getMyQueryWrapper(long userId, AppQueryByUserRequest queryRequest);

    /**
     * 获取用户侧精选查询条件
     */
    QueryWrapper<App> getMySelectedQueryWrapper(long userId, AppQuerySelectedRequest queryRequest);

    /**
     * 获取管理员侧查询条件
     */
    QueryWrapper<App> getAdminQueryWrapper(AppAdminQueryRequest queryRequest);

    /**
     * App 实体转 VO
     */
    AppVO getAppVO(App app);

    /**
     * App 实体列表转 VO 列表
     */
    List<AppVO> getAppVOList(List<App> appList);
}
