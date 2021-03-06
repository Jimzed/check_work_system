package com.qushihan.check_work_system.student.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.qushihan.check_work_system.inf.enums.FieldIsdelStatus;
import com.qushihan.check_work_system.student.mapper.auto.StudentMapper;
import com.qushihan.check_work_system.student.model.auto.Student;
import com.qushihan.check_work_system.student.model.auto.StudentExample;

@Repository
public class StudentDao {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 通过班级id查询学生
     *
     * @param clazzId
     *
     * @return
     */
    public List<Student> getByClazzId(Long clazzId) {
        if (!Optional.ofNullable(clazzId).isPresent()) {
            return Collections.emptyList();
        }
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andClazzIdEqualTo(clazzId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return studentMapper.selectByExample(studentExample);
    }

    /**
     * 通过学生id更新学生信息
     *
     * @param student
     *
     * @return
     */
    public int updateById(Student student) {
        if (!Optional.ofNullable(student).isPresent()) {
            return 0;
        }
        return studentMapper.updateByPrimaryKey(student);
    }

    /**
     * 通过学生id列表查询Student列表
     *
     * @param studentIds
     *
     * @return
     */
    public List<Student> getByStudentIdList(List<Long> studentIds) {
        if (CollectionUtils.isEmpty(studentIds)) {
            return Collections.EMPTY_LIST;
        }
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andStudentIdIn(studentIds);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return studentMapper.selectByExample(studentExample);
    }

    /**
     * 通过学生名称搜索学生
     *
     * @param searchStudentName
     * @return
     */
    public List<Student> getBySearchStudentName(String searchStudentName) {
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        if (StringUtils.isNotEmpty(searchStudentName)) {
            criteria.andStudentNameLike(searchStudentName + "%");
        }
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return studentMapper.selectByExample(studentExample);
    }
}
