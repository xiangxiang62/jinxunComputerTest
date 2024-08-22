package com.jinxun.project.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinxun.project.model.dto.order.OrderTableQueryRequest;
import com.jinxun.project.model.entity.OrderTable;
import com.jinxun.project.model.vo.OrderVO;

/**
 * Order服务
 *
 * @author xiangxiang
 */
public interface OrderService extends IService<OrderTable> {

    /**
     * 校验数据
     *
     * @param order 数据
     * @param add 对创建的数据进行校验
     */
    void validOrder(OrderTable order, boolean add);

    /**
     * 获取查询条件
     *
     * @param orderQueryRequest 查询条件
     * @return QueryWrapper
     */
    QueryWrapper<OrderTable> getQueryWrapper(OrderTableQueryRequest orderQueryRequest);



    /**
     * 分页获取Order封装
     *
     * @param orderPage 分页数据
     * @return Page<OrderVO>
     */
    Page<OrderVO> getOrderVOPage(Page<OrderTable> orderPage);
}
