package com.jinxun.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinxun.project.common.ErrorCode;
import com.jinxun.project.exception.BusinessException;
import com.jinxun.project.exception.ThrowUtils;
import com.jinxun.project.mapper.OrderMapper;
import com.jinxun.project.model.dto.order.OrderTableQueryRequest;
import com.jinxun.project.model.entity.OrderTable;
import com.jinxun.project.model.enums.OrderTypeEnum;
import com.jinxun.project.model.vo.OrderVO;
import com.jinxun.project.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Order服务实现
 *
 * @author xiangxiang
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderTable> implements OrderService {

    /**
     * 校验数据
     *
     * @param order
     * @param add   对创建的数据进行校验
     */
    @Override
    public void validOrder(OrderTable order, boolean add) {

        if (order == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long order_no = order.getOrder_no();
        Integer order_type = order.getOrder_type();
        String title = order.getTitle();
        String content = order.getContent();
        ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR,"标题不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(content), ErrorCode.PARAMS_ERROR,"内容不能为空");
        ThrowUtils.throwIf(OrderTypeEnum.getEnumByValue(order_type) == null, ErrorCode.PARAMS_ERROR,"订单状态不合法");
        // 创建数据时，参数不能为空
        if (add) {
            QueryWrapper<OrderTable> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no", order_no);
            OrderTable orderById = this.getOne(queryWrapper);

            // 检查 orderById 是否为 null，若不为 null，抛出异常
            if (orderById != null) {
                ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, "订单已存在");
            }
        }

    }

    /**
     * 获取查询条件
     *
     * @param orderQueryRequest 工单查询请求
     * @return
     */
    @Override
    public QueryWrapper<OrderTable> getQueryWrapper(OrderTableQueryRequest orderQueryRequest) {


        QueryWrapper<OrderTable> queryWrapper = new QueryWrapper<>();
        if (orderQueryRequest == null) {
            return queryWrapper;
        }

        Long id = orderQueryRequest.getId();
        Long order_no = orderQueryRequest.getOrder_no();
        Integer order_type = orderQueryRequest.getOrder_type();
        String title = orderQueryRequest.getTitle();
        String content = orderQueryRequest.getContent();
        Integer handle_dept_id = orderQueryRequest.getHandle_dept_id();
        Integer is_overdue = orderQueryRequest.getIs_overdue();
        Long userId = orderQueryRequest.getUserId();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);

        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(order_no), "order_no", order_no);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(order_type), "order_type", order_type);
        queryWrapper.eq(ObjectUtils.isNotEmpty(handle_dept_id), "handle_dept_id", handle_dept_id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(is_overdue), "is_overdue", is_overdue);
        return queryWrapper;
    }

    /**
     * 分页获取Order封装
     *
     * @param orderPage 分页对象
     * @return 分页对象
     */
    @Override
    public Page<OrderVO> getOrderVOPage(Page<OrderTable> orderPage) {
        List<OrderTable> orderList = orderPage.getRecords();
        Page<OrderVO> orderVOPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        if (ObjectUtils.isEmpty(orderList)) {
            return orderVOPage;
        }
        // 对象列表 => 封装对象列表
        List<OrderVO> orderVOList = orderList.stream().map(order -> OrderVO.objToVo(order)).collect(Collectors.toList());

        orderVOPage.setRecords(orderVOList);
        return orderVOPage;
    }

}
