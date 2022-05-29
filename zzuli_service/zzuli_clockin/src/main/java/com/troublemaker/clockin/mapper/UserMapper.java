package com.troublemaker.clockin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.troublemaker.clockin.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
