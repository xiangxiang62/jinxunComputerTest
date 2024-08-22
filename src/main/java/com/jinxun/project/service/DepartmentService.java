package com.jinxun.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jinxun.project.model.dto.department.DepartmentQueryRequest;
import com.jinxun.project.model.entity.Department;

/**
 * Department服务
 *
 * @author xiangxiang
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 校验数据
     *
     * @param department 数据
     * @param add 对创建的数据进行校验
     */
    void validDepartment(Department department, boolean add);

    /**
     * 获取查询条件
     *
     * @param departmentQueryRequest 查询条件
     * @return QueryWrapper
     */
    QueryWrapper<Department> getQueryWrapper(DepartmentQueryRequest departmentQueryRequest);



    /**
     * 分页获取Department封装
     *
     * @param departmentPage 分页数据
     * @return Page<DepartmentVO>
     */
    Page<Department> getDepartmentPage(Page<Department> departmentPage);
}
