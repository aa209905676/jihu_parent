package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author wjh
 * @since 2020-09-24
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String id);

    Order getByOrderId(String orderId, String id);

    Boolean isBuyByCourseId(String courseId, String id);

    List<Order> selectByMemberId(String id);

    boolean removeById(String orderId, String memberId);
}
