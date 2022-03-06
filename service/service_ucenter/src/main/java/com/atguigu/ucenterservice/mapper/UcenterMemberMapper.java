package com.atguigu.ucenterservice.mapper;

import com.atguigu.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author peanut
 * @since 2022-02-26
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    int countRegisterDay(@Param("day") String day);
}
