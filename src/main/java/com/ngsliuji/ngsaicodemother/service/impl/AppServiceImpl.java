package com.ngsliuji.ngsaicodemother.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ngsliuji.ngsaicodemother.exception.BusinessException;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import com.ngsliuji.ngsaicodemother.mapper.AppMapper;
import com.ngsliuji.ngsaicodemother.model.dto.app.*;
import com.ngsliuji.ngsaicodemother.model.entity.App;
import com.ngsliuji.ngsaicodemother.model.entity.User;
import com.ngsliuji.ngsaicodemother.model.vo.AppVO;
import com.ngsliuji.ngsaicodemother.service.AppService;
import com.ngsliuji.ngsaicodemother.service.UserService;
import com.ngsliuji.ngsaicodemother.common.DeleteRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dell
 * @description 针对表【app(应用)】的数据库操作Service实现
 * @createDate 2026-03-13 20:14:50
 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
        implements AppService {

    @Resource
    private UserService userService;

    @Override
    public Long createApp(AppAddRequest appAddRequest, HttpServletRequest request) {
        if (appAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 1.校验参数
        String initPrompt = appAddRequest.getInitPrompt();
        ThrowIfBlank(initPrompt, "initPrompt不能为空");

        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        // 2.构造实体
        App app = new App();
        app.setUserId(userId);
        app.setInitPrompt(initPrompt);
        app.setCover(appAddRequest.getCover());

        // appName 不是必填，但为了前端展示，这里给一个兜底值
        String appName = appAddRequest.getAppName();
        app.setAppName(StrUtil.isNotBlank(appName) ? appName : "未命名");

        Integer priority = appAddRequest.getPriority();
        app.setPriority(ObjUtil.defaultIfNull(priority, 0));

        boolean saveResult = this.save(app);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建应用失败");
        }
        return app.getId();
    }

    @Override
    public boolean updateMyAppName(AppUpdateNameRequest updateNameRequest, HttpServletRequest request) {
        if (updateNameRequest == null || updateNameRequest.getId() == null || updateNameRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StrUtil.isBlank(updateNameRequest.getAppName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "appName不能为空");
        }

        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        App app = this.getById(updateNameRequest.getId());
        ThrowIfNotFound(app);

        if (!userId.equals(app.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "只能修改自己的应用");
        }

        app.setAppName(updateNameRequest.getAppName());
        app.setEditTime(new Date());
        boolean result = this.updateById(app);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return true;
    }

    @Override
    public boolean deleteMyApp(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        App app = this.getById(deleteRequest.getId());
        ThrowIfNotFound(app);
        if (!userId.equals(app.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "只能删除自己的应用");
        }
        return this.removeById(deleteRequest.getId());
    }

    @Override
    public AppVO getMyAppVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        App app = this.getById(id);
        ThrowIfNotFound(app);
        if (!userId.equals(app.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "只能查看自己的应用");
        }
        return getAppVO(app);
    }

    @Override
    public Page<AppVO> listMyAppsByPage(AppQueryByUserRequest queryRequest, HttpServletRequest request) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        long current = queryRequest.getPageNum();
        long pageSize = queryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<App> appPage = this.page(new Page<>(current, pageSize),
                getMyQueryWrapper(userId, queryRequest));

        Page<AppVO> voPage = new Page<>(current, pageSize, appPage.getTotal());
        voPage.setRecords(getAppVOList(appPage.getRecords()));
        return voPage;
    }

    @Override
    public Page<AppVO> listMySelectedAppsByPage(AppQuerySelectedRequest queryRequest, HttpServletRequest request) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();

        long current = queryRequest.getPageNum();
        long pageSize = queryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<App> appPage = this.page(new Page<>(current, pageSize),
                getMySelectedQueryWrapper(userId, queryRequest));

        Page<AppVO> voPage = new Page<>(current, pageSize, appPage.getTotal());
        voPage.setRecords(getAppVOList(appPage.getRecords()));
        return voPage;
    }

    @Override
    public boolean adminDeleteApp(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        App app = this.getById(id);
        ThrowIfNotFound(app);
        return this.removeById(id);
    }

    @Override
    public boolean adminUpdateApp(AppAdminUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null || updateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean hasUpdate =
                updateRequest.getAppName() != null
                        || updateRequest.getCover() != null
                        || updateRequest.getPriority() != null;
        ThrowIfNotFound(hasUpdate, "请求中至少需要包含一个可更新字段");

        App app = this.getById(updateRequest.getId());
        ThrowIfNotFound(app);

        if (updateRequest.getAppName() != null) {
            ThrowIfBlank(updateRequest.getAppName(), "appName不能为空");
            app.setAppName(updateRequest.getAppName());
        }
        if (updateRequest.getCover() != null) {
            app.setCover(updateRequest.getCover());
        }
        if (updateRequest.getPriority() != null) {
            app.setPriority(updateRequest.getPriority());
        }

        app.setEditTime(new Date());
        boolean result = this.updateById(app);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return true;
    }

    @Override
    public Page<AppVO> adminListAppsByPage(AppAdminQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long current = queryRequest.getPageNum();
        long pageSize = queryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<App> appPage = this.page(new Page<>(current, pageSize),
                getAdminQueryWrapper(queryRequest));

        Page<AppVO> voPage = new Page<>(current, pageSize, appPage.getTotal());
        voPage.setRecords(getAppVOList(appPage.getRecords()));
        return voPage;
    }

    @Override
    public AppVO adminGetAppVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        App app = this.getById(id);
        ThrowIfNotFound(app);
        return getAppVO(app);
    }

    @Override
    public QueryWrapper<App> getMyQueryWrapper(long userId, AppQueryByUserRequest queryRequest) {
        if (userId <= 0 || queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String appName = queryRequest.getAppName();

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.like(StrUtil.isNotBlank(appName), "appName", appName);

        if (StrUtil.isNotEmpty(queryRequest.getSortField())) {
            queryWrapper.orderBy(StrUtil.isNotEmpty(queryRequest.getSortField()),
                    queryRequest.getSortOrder().equals("ascend"), queryRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("createTime");
        }
        return queryWrapper;
    }

    @Override
    public QueryWrapper<App> getMySelectedQueryWrapper(long userId, AppQuerySelectedRequest queryRequest) {
        if (userId <= 0 || queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String appName = queryRequest.getAppName();

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.gt("priority", 0);
        queryWrapper.like(StrUtil.isNotBlank(appName), "appName", appName);

        if (StrUtil.isNotEmpty(queryRequest.getSortField())) {
            queryWrapper.orderBy(StrUtil.isNotEmpty(queryRequest.getSortField()),
                    queryRequest.getSortOrder().equals("ascend"), queryRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("priority").orderByDesc("createTime");
        }
        return queryWrapper;
    }

    @Override
    public QueryWrapper<App> getAdminQueryWrapper(AppAdminQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(ObjUtil.isNotNull(queryRequest.getId()), "id", queryRequest.getId());
        queryWrapper.like(StrUtil.isNotBlank(queryRequest.getAppName()), "appName", queryRequest.getAppName());
        queryWrapper.like(StrUtil.isNotBlank(queryRequest.getCover()), "cover", queryRequest.getCover());
        queryWrapper.like(StrUtil.isNotBlank(queryRequest.getInitPrompt()), "initPrompt", queryRequest.getInitPrompt());
        queryWrapper.like(StrUtil.isNotBlank(queryRequest.getCodeGenType()), "codeGenType", queryRequest.getCodeGenType());
        queryWrapper.eq(StrUtil.isNotBlank(queryRequest.getDeployKey()), "deployKey", queryRequest.getDeployKey());
        queryWrapper.eq(ObjUtil.isNotNull(queryRequest.getPriority()), "priority", queryRequest.getPriority());
        queryWrapper.eq(ObjUtil.isNotNull(queryRequest.getUserId()), "userId", queryRequest.getUserId());
        queryWrapper.eq(ObjUtil.isNotNull(queryRequest.getIsDelete()), "isDelete", queryRequest.getIsDelete());

        if (StrUtil.isNotEmpty(queryRequest.getSortField())) {
            queryWrapper.orderBy(StrUtil.isNotEmpty(queryRequest.getSortField()),
                    queryRequest.getSortOrder().equals("ascend"), queryRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("createTime");
        }
        return queryWrapper;
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtils.copyProperties(app, appVO);
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        List<AppVO> voList = new ArrayList<>(appList.size());
        for (App app : appList) {
            voList.add(getAppVO(app));
        }
        return voList;
    }

    private void ThrowIfBlank(String value, String message) {
        if (StrUtil.isBlank(value)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, message);
        }
    }

    private void ThrowIfNotFound(Object obj) {
        if (obj == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
    }

    private void ThrowIfNotFound(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, message);
        }
    }
}




