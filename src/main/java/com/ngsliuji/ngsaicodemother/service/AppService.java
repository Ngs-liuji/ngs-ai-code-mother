package com.ngsliuji.ngsaicodemother.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ngsliuji.ngsaicodemother.model.dto.app.AppQueryRequest;
import com.ngsliuji.ngsaicodemother.model.entity.App;
import com.ngsliuji.ngsaicodemother.model.vo.AppVO;

import java.util.List;

/**
 * @author ngs_liuji
 * @description 针对表【app(应用)】的数据库操作Service
 * @createDate 2026-03-13 20:14:50
 */
public interface AppService extends IService<App> {

    /**
     * App 实体转 VO
     */
    AppVO getAppVO(App app);

    /**
     * App 实体列表转 VO 列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    QueryWrapper<App> getQueryWrapper(AppQueryRequest appQueryRequest);
}
