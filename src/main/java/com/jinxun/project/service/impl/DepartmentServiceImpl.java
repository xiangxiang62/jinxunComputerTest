package com.jinxun.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinxun.project.common.ErrorCode;
import com.jinxun.project.exception.BusinessException;
import com.jinxun.project.exception.ThrowUtils;
import com.jinxun.project.mapper.DepartmentMapper;
import com.jinxun.project.model.dto.department.DepartmentQueryRequest;
import com.jinxun.project.model.entity.Department;
import com.jinxun.project.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Department服务实现
 *
 * @author xiangxiang

 */
@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {


    /**
     * 校验数据
     *
     * @param department 部门
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validDepartment(Department department, boolean add) {
        if (department == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String deptName = department.getDept_name();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(deptName), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(deptName)) {
            ThrowUtils.throwIf(deptName.length() > 80, ErrorCode.PARAMS_ERROR, "部门名称过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param departmentQueryRequest 部门查询请求
     * @return
     */
    @Override
    public QueryWrapper<Department> getQueryWrapper(DepartmentQueryRequest departmentQueryRequest) {


        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (departmentQueryRequest == null) {
            return queryWrapper;
        }
        Long userId = departmentQueryRequest.getUserId();
        Integer deptId = departmentQueryRequest.getDept_id();
        String deptName = departmentQueryRequest.getDept_name();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(deptName), "dept_name", deptName);

        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(deptId), "dept_id", deptId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);

        return queryWrapper;
    }



    /**
     * 分页获取Department封装
     *
     * @param departmentPage 分页对象
     * @return 分页对象
     */
    @Override
    public Page<Department> getDepartmentPage(Page<Department> departmentPage) {
        List<Department> departmentList = departmentPage.getRecords();
        Page<Department> departmentPages = new Page<>(departmentPage.getCurrent(), departmentPage.getSize(), departmentPage.getTotal());
        if (ObjectUtils.isEmpty(departmentList)) {
            return departmentPage;
        }
        return departmentPages;
    }

}
