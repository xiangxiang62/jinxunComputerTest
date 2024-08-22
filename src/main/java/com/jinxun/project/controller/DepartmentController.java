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
import com.jinxun.project.model.dto.department.DepartmentAddRequest;
import com.jinxun.project.model.dto.department.DepartmentEditRequest;
import com.jinxun.project.model.dto.department.DepartmentQueryRequest;
import com.jinxun.project.model.dto.department.DepartmentUpdateRequest;
import com.jinxun.project.model.entity.Department;
import com.jinxun.project.model.entity.User;
import com.jinxun.project.service.DepartmentService;
import com.jinxun.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Department接口
 */
@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @Resource
    private UserService userService;


    /**
     * 创建Department
     *
     * @param departmentAddRequest 部门新增请求
     * @param request request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addDepartment(@RequestBody DepartmentAddRequest departmentAddRequest, HttpServletRequest request) {
        if (departmentAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        // 判断是否已经存在
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id",departmentAddRequest.getDept_id());
        Department departmentServiceOne = departmentService.getOne(queryWrapper);
        if (departmentServiceOne != null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"该部门已存在");
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentAddRequest, department);
        // 数据校验
        departmentService.validDepartment(department, true);
        User loginUser = userService.getLoginUser(request);
        department.setCreat_user_id(loginUser.getId());

        // 写入数据库
        boolean result = departmentService.save(department);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        // 返回新写入的数据 id
        long newDepartmentId = department.getDept_id();
        return ResultUtils.success(newDepartmentId);
    }

    /**
     * 删除Department
     *
     * @param deleteRequest 删除请求
     * @param request request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDepartment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Department oldDepartment = departmentService.getById(id);
        if (oldDepartment == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"未找到该数据");
        }
        // 仅本人或管理员可删除
        if (!oldDepartment.getCreat_user_id().equals(user.getId()) && !userService.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = departmentService.removeById(id);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 更新Department（仅管理员可用）
     *
     * @param departmentUpdateRequest 更细请求，仅管理员
     * @return Boolean
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateDepartment(@RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        if (departmentUpdateRequest == null || departmentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentUpdateRequest, department);
        // 数据校验
        departmentService.validDepartment(department, false);
        // 判断是否存在
        long id = departmentUpdateRequest.getId();
        Department oldDepartment = departmentService.getById(id);
        if (oldDepartment == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"该数据不存在");
        }
        // 操作数据库
        boolean result = departmentService.updateById(department);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取Department（封装类）
     *
     * @param id id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<Department> getDepartmentVOById(long id ) {
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
        }
        // 查询数据库
        Department department = departmentService.getById(id);
        if (department == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"该条数据不存在");
        }
        // 获取封装类
        return ResultUtils.success(department);
    }

    /**
     * 分页获取Department列表（仅管理员可用）
     *
     * @param departmentQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Department>> listDepartmentByPage(@RequestBody DepartmentQueryRequest departmentQueryRequest) {
        long current = departmentQueryRequest.getCurrent();
        long size = departmentQueryRequest.getPageSize();
        // 查询数据库
        Page<Department> departmentPage = departmentService.page(new Page<>(current, size),
                departmentService.getQueryWrapper(departmentQueryRequest));
        return ResultUtils.success(departmentPage);
    }

    /**
     * 编辑Department（给用户使用）
     *
     * @param departmentEditRequest 编辑请求（用户）
     * @param request request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editDepartment(@RequestBody DepartmentEditRequest departmentEditRequest, HttpServletRequest request) {
        if (departmentEditRequest == null || departmentEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentEditRequest, department);
        // 数据校验
        departmentService.validDepartment(department, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id",departmentEditRequest.getDept_id());
        Department departmentServiceOne = departmentService.getOne(queryWrapper);
        if (departmentServiceOne == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"数据不存在");
        }

        // 仅本人或管理员可编辑
        if (!departmentServiceOne.getCreat_user_id().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = departmentService.updateById(department);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
        }
        return ResultUtils.success(true);
    }

}
