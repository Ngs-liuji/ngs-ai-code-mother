package com.ngsliuji.ngsaicodemother.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ngsliuji.ngsaicodemother.model.dto.user.UserQueryRequest;
import com.ngsliuji.ngsaicodemother.model.entity.User;
import com.ngsliuji.ngsaicodemother.model.vo.LoginUserVO;
import com.ngsliuji.ngsaicodemother.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Dell
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2026-03-13 20:13:58
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userName
     * @param userPassword
     * @param checkPassword
     * @return 新用户 id
     */

    long userRegister(String userName, String userPassword, String checkPassword);


    /**
     * 获取加密后的密码
     *
     * @param userPassword
     */
    String getEncryptePassword(String userPassword);


    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 用户注销(退出登录)
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */

    public UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息列表
     *
     * @param userList
     * @return
     */
    public List<UserVO> getUserVOList(List<User> userList);


    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
