package com.ngsliuji.ngsaicodemother.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ngsliuji.ngsaicodemother.model.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author Dell
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2026-03-13 20:13:58
 * @Entity User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




