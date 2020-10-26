package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.feign.EduCourseService;
import com.atguigu.guli.service.trade.feign.UcenterMemberService;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.utils.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author wjh
 * @since 2020-09-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private EduCourseService eduCourseService;

    @Resource
    private UcenterMemberService ucenterMemberService;

    @Override
    public String saveOrder(String courseId, String memberId) {
        //查询当前用户是否已有当前课程的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if(orderExist != null){
            return orderExist.getId(); //如果订单已存在，则直接返回订单id
        }
        //查询课程信息
        CourseDto courseDto = eduCourseService.getCourseDtoById(courseId);
        if (courseDto == null) {
            throw new GuLiExcpetion(ResultCodeEnum.PARAM_ERROR);
        }
        //查询用户信息
        MemberDto memberDto = ucenterMemberService.getMemberDtoByMemberId(memberId);
        if (memberDto == null) {
            throw new GuLiExcpetion(ResultCodeEnum.PARAM_ERROR);
        }

        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));//分
        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());
        order.setStatus(0);//未支付
        order.setPayType(1);//微信支付
        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String id) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId).eq("member_id",id);
        Order order = baseMapper.selectOne(queryWrapper);
        return order;
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String id) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",courseId).eq("member_id",id).eq("status",1);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count.intValue() > 0;
    }

    @Override
    public List<Order> selectByMemberId(String id) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.eq("member_id", id);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean removeById(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", orderId)
                .eq("member_id", memberId);
        return this.remove(queryWrapper);
    }
}