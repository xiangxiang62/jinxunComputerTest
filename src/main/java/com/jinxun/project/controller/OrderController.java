package com.jinxun.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jinxun.project.annotation.AuthCheck;
import com.jinxun.project.common.BaseResponse;
import com.jinxun.project.common.DeleteRequest;
import com.jinxun.project.common.ErrorCode;
import com.jinxun.project.common.ResultUtils;
import com.jinxun.project.constant.UserConstant;
import com.jinxun.project.exception.BusinessException;
import com.jinxun.project.exception.ThrowUtils;
import com.jinxun.project.model.dto.order.*;
import com.jinxun.project.model.entity.Department;
import com.jinxun.project.model.entity.OrderTable;
import com.jinxun.project.model.entity.User;
import com.jinxun.project.model.vo.OrderVO;
import com.jinxun.project.service.DepartmentService;
import com.jinxun.project.service.OrderService;
import com.jinxun.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Order接口
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @Resource
    private DepartmentService departmentService;


    /**
     * 创建Order
     *
     * @param orderAddRequest 工单新增请求
     * @param request request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addOrder(@RequestBody OrderTableAddRequest orderAddRequest, HttpServletRequest request) {
        if (orderAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderTable order = new OrderTable();
        BeanUtils.copyProperties(orderAddRequest, order);
        // 数据校验
        orderService.validOrder(order, true);
        User loginUser = userService.getLoginUser(request);
        order.setCreate_user_id(loginUser.getId());
        // 写入数据库
        boolean result = orderService.save(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newOrderId = order.getId();
        return ResultUtils.success(newOrderId);
    }

    /**
     * 删除Order
     *
     * @param deleteRequest 删除请求
     * @param request request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrder(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        OrderTable oldOrder = orderService.getById(id);
        ThrowUtils.throwIf(oldOrder == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldOrder.getCreate_user_id().equals(user.getId()) && !userService.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = orderService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新Order（仅管理员可用）
     *
     * @param orderUpdateRequest 更新工单请求（管理员）
     * @return Boolean
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateOrder(@RequestBody OrderTableUpdateRequest orderUpdateRequest) {
        if (orderUpdateRequest == null || orderUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderTable order = new OrderTable();
        BeanUtils.copyProperties(orderUpdateRequest, order);
        // 数据校验
        orderService.validOrder(order, false);
        // 判断是否存在
        long id = orderUpdateRequest.getId();
        OrderTable oldOrder = orderService.getById(id);
        ThrowUtils.throwIf(oldOrder == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = orderService.updateById(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取Order（封装类）
     *
     * @param id id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<OrderVO> getOrderVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        OrderTable order = orderService.getById(id);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        // 获取封装类
        return ResultUtils.success(orderVO);
    }

    /**
     * 分页获取Order列表（仅管理员可用）
     *
     * @param orderQueryRequest 分页查询请求
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<OrderTable>> listOrderByPage(@RequestBody OrderTableQueryRequest orderQueryRequest) {
        long current = orderQueryRequest.getCurrent();
        long size = orderQueryRequest.getPageSize();
        // 查询数据库
        Page<OrderTable> orderPage = orderService.page(new Page<>(current, size),
                orderService.getQueryWrapper(orderQueryRequest));
        return ResultUtils.success(orderPage);
    }

    /**
     * 分页获取Order列表（封装类）
     *
     * @param orderQueryRequest 分页查询请求
     * @param request pageOrderVO
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<OrderVO>> listOrderVOByPage(@RequestBody OrderTableQueryRequest orderQueryRequest,
                                                               HttpServletRequest request) {
        long current = orderQueryRequest.getCurrent();
        long size = orderQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<OrderTable> orderPage = orderService.page(new Page<>(current, size),
                orderService.getQueryWrapper(orderQueryRequest));
        // 获取封装类
        return ResultUtils.success(orderService.getOrderVOPage(orderPage));
    }


    /**
     * 编辑Order（给用户使用）
     *
     * @param orderEditRequest 编辑工单请求（用户）
     * @param request Boolean
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editOrder(@RequestBody OrderTableEditRequest orderEditRequest, HttpServletRequest request) {
        if (orderEditRequest == null || orderEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderTable order = new OrderTable();
        BeanUtils.copyProperties(orderEditRequest, order);
        // 数据校验
        orderService.validOrder(order, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = orderEditRequest.getId();
        OrderTable oldOrder = orderService.getById(id);
        ThrowUtils.throwIf(oldOrder == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldOrder.getCreate_user_id().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = orderService.updateById(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分派工单
     *
     * @param orderTableFenPaiRequest 分配工单请求
     * @param request Boolean 分配是否成功
     * @return
     */
    @PostMapping("/fenpai")
    public BaseResponse<Boolean> fenpaiOrder(@RequestBody OrderTableFenPaiRequest orderTableFenPaiRequest, HttpServletRequest request) {
        if (orderTableFenPaiRequest == null || orderTableFenPaiRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OrderTable order = new OrderTable();
        BeanUtils.copyProperties(orderTableFenPaiRequest, order);

        User loginUser = userService.getLoginUser(request);

        // 判断是否存在
        long id = orderTableFenPaiRequest.getId();
        OrderTable oldOrder = orderService.getById(id);
        ThrowUtils.throwIf(oldOrder == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅管理员可分派
        if (!oldOrder.getCreate_user_id().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        order.setFenpai_time(LocalDateTime.now());
        // 判断该部门是否存在
        Long handle_dept_id = orderTableFenPaiRequest.getHandle_dept_id();
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id",handle_dept_id);
        Department department = departmentService.getOne(queryWrapper);
        if (department == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"部门不存在");
        }
        order.setHandle_dept_id(orderTableFenPaiRequest.getHandle_dept_id());
        // 操作数据库
        boolean result = orderService.updateById(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 查询指定月份每天的工单总量和超期率
     *
     * @param month 月份
     * @return
     */
    @GetMapping("/search/month")
    public BaseResponse<List<OrderStatisticsDTO>> getOrderStatisticsByMonth(@RequestParam("month") Integer month) {
        if (month == null || month <= 0 || month > 12) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前年份
        int year = LocalDate.now().getYear();

        // 初始化结果列表
        List<OrderStatisticsDTO> statistics = new ArrayList<>();

        // 遍历每一天，计算当天的工单总量和超期率
        for (int day = 1; day <= YearMonth.of(year, month).lengthOfMonth(); day++) {
            LocalDate startDate = LocalDate.of(year, month, day);
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = startDate.plusDays(1).atStartOfDay();

            // 查询当天的所有工单
            QueryWrapper<OrderTable> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("create_time", startDateTime, endDateTime);
            List<OrderTable> orders = orderService.list(queryWrapper);

            // 统计总量和超期的数量
            int totalOrders = orders.size();
            long overdueCount = orders.stream().filter(order -> order.getIs_overdue() == 1).count();

            // 确保超期率为小数点格式
            BigDecimal overdueRate = totalOrders > 0
                    ? BigDecimal.valueOf(overdueCount)
                    .divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            // 添加到结果列表
            statistics.add(new OrderStatisticsDTO(day, totalOrders, overdueRate));
        }

        return ResultUtils.success(statistics);
    }

    /**
     * 每个部门的工单总量和超期率
     *
     * @param month 月份
     * @return
     */
    @GetMapping("/search/department")
    public BaseResponse<List<DepartmentOrderStatisticsDTO>> getDepartmentOrderStatisticsByMonth(@RequestParam("month") Integer month) {
        if (month == null || month <= 0 || month > 12) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前年份
        int year = LocalDate.now().getYear();

        // 计算指定月份的开始时间和结束时间
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(year, month, YearMonth.of(year, month).lengthOfMonth(), 23, 59, 59);

        // 查询所有部门
        List<Department> departments = departmentService.list();

        // 初始化结果列表
        List<DepartmentOrderStatisticsDTO> statistics = new ArrayList<>();

        // 遍历每个部门，统计其在指定月份的工单总量和超期率
        for (Department department : departments) {
            // 查询该部门在指定月份的所有工单
            QueryWrapper<OrderTable> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("handle_dept_id", department.getDept_id());
            queryWrapper.between("create_time", startDateTime, endDateTime);
            List<OrderTable> orders = orderService.list(queryWrapper);

            // 统计总量和超期的数量
            int totalOrders = orders.size();
            long overdueCount = orders.stream().filter(order -> order.getIs_overdue() == 1).count();

            // 计算超期率，使用 BigDecimal 来确保精度
            BigDecimal totalOrdersBD = new BigDecimal(totalOrders);
            BigDecimal overdueCountBD = new BigDecimal(overdueCount);
            BigDecimal overdueRate = totalOrdersBD.compareTo(BigDecimal.ZERO) > 0
                    ? overdueCountBD.divide(totalOrdersBD, 4, RoundingMode.HALF_UP) // 保留四位小数
                    : BigDecimal.ZERO;

            // 添加到结果列表
            statistics.add(new DepartmentOrderStatisticsDTO(department.getDept_id(), department.getDept_name(), totalOrders, overdueRate));
        }

        return ResultUtils.success(statistics);
    }


}
