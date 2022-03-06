package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.dto.CourseInfoDO;
import com.atguigu.commonutils.dto.LoginInfoDO;
import com.atguigu.commonutils.dto.TeacherInfoDO;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.MemberClient;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.mapper.OrderMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author peanut
 * @since 2022-03-01
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private MemberClient memberClient;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String saveOrder(String courseId, String memberId) {
        // 调用member模块获取用户信息
        LoginInfoDO loginInfoById = memberClient.getLoginInfoById(memberId);

        // 调用edu模块获取课程信息
        CourseInfoDO courseInfoByCourseId = eduClient.getCourseInfoByCourseId(courseId);

        // 调用edu模块获取讲师Name
        TeacherInfoDO teacherName = eduClient.getTeacherName(courseInfoByCourseId.getTeacherId());

        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo()); // 通过工具类生成订单id
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoByCourseId.getTitle());
        order.setCourseCover(courseInfoByCourseId.getCover());
        order.setTeacherName(teacherName.getName());
        order.setTotalFee(courseInfoByCourseId.getPrice());
        order.setMemberId(memberId);
        order.setMobile(loginInfoById.getMobile());
        order.setNickname(loginInfoById.getNickname());
        order.setStatus(0);
        order.setPayType(1);  // 1代表微信支付，默认为1

        baseMapper.insert(order);

        return order.getOrderNo(); // 返回order_no，注意不是id
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(orderQueryWrapper);
        return order;
    }

    @Override
    public int getOrderByCourseIdAndOrderId(String courseId, String memberId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("course_id", courseId);
        orderQueryWrapper.eq("member_id", memberId);
        // 查询已经支付的订单
        orderQueryWrapper.eq("status", 1);
        return baseMapper.selectCount(orderQueryWrapper);
    }
}
