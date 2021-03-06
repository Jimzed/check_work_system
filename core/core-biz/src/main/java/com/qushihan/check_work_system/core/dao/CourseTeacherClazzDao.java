package com.qushihan.check_work_system.core.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.qushihan.check_work_system.core.mapper.auto.CourseTeacherClazzMapper;
import com.qushihan.check_work_system.core.model.auto.CourseTeacherClazz;
import com.qushihan.check_work_system.core.model.auto.CourseTeacherClazzExample;
import com.qushihan.check_work_system.inf.enums.FieldIsdelStatus;

@Repository
public class CourseTeacherClazzDao {

    @Autowired
    private CourseTeacherClazzMapper courseTeacherClazzMapper;

    /**
     * 新插入一条记录进入课程教师班级表
     *
     * @param courseTeacherClazz
     *
     * @return
     */
    public int InsertCourseTeacherClazz(CourseTeacherClazz courseTeacherClazz) {
        if (!Optional.ofNullable(courseTeacherClazz).isPresent()) {
            return 0;
        }
        return courseTeacherClazzMapper.insertSelective(courseTeacherClazz);
    }

    /**
     * 通过课程id教师id班级id查询课程教师班级记录
     *
     * @param courseId
     * @param teacherId
     * @param clazzId
     *
     * @return
     */
    public List<CourseTeacherClazz> queryCourseTeacherClazzListByCourseIdAndTeacherIdAndClazzId(Long courseId,
            Long teacherId, Long clazzId) {
        if (!Optional.ofNullable(courseId).isPresent()) {
            return Collections.EMPTY_LIST;
        }
        if (!Optional.ofNullable(teacherId).isPresent()) {
            return Collections.EMPTY_LIST;
        }
        if (!Optional.ofNullable(clazzId).isPresent()) {
            return Collections.EMPTY_LIST;
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        criteria.andTeacherIdEqualTo(teacherId);
        criteria.andClazzIdEqualTo(clazzId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return courseTeacherClazzMapper.selectByExample(courseTeacherClazzExample);
    }

    /**
     * 通过教师id查询课程教师班级记录
     *
     * @return
     */
    public List<CourseTeacherClazz> getByTeacherId(Long teacherId) {
        if (!Optional.ofNullable(teacherId).isPresent()) {
            return Collections.emptyList();
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andTeacherIdEqualTo(teacherId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return courseTeacherClazzMapper.selectByExample(courseTeacherClazzExample);
    }

    /**
     * 通过课程教师班级id去查询课程教师班级列表
     *
     * @param courseTeacherClazzId
     *
     * @return
     */
    public List<CourseTeacherClazz> queryCourseTeacherClazzListByCourseTeacherClazzId(Long courseTeacherClazzId) {
        if (!Optional.ofNullable(courseTeacherClazzId).isPresent()) {
            return Collections.EMPTY_LIST;
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andCourseTeacherClazzIdEqualTo(courseTeacherClazzId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return courseTeacherClazzMapper.selectByExample(courseTeacherClazzExample);
    }

    /**
     * 作业数量字段变更
     *
     * @param courseTeacherClazzId
     * @param courseTeacherClazz
     *
     * @return
     */
    public int workCountUpdate(Long courseTeacherClazzId, CourseTeacherClazz courseTeacherClazz) {
        if (!Optional.ofNullable(courseTeacherClazzId).isPresent()) {
            return 0;
        }
        if (!Optional.ofNullable(courseTeacherClazz).isPresent()) {
            return 0;
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andCourseTeacherClazzIdEqualTo(courseTeacherClazzId);
        return courseTeacherClazzMapper.updateByExampleSelective(courseTeacherClazz, courseTeacherClazzExample);
    }

    /**
     * 通过课程id查询课程教师班级记录
     *
     * @return
     */
    public List<CourseTeacherClazz> getByCourseId(Long courseId) {
        if (!Optional.ofNullable(courseId).isPresent()) {
            return Collections.emptyList();
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return courseTeacherClazzMapper.selectByExample(courseTeacherClazzExample);
    }

    /**
     * 通过班级id查询课程教师班级记录
     *
     * @return
     */
    public List<CourseTeacherClazz> getByClazzId(Long clazzId) {
        if (!Optional.ofNullable(clazzId).isPresent()) {
            return Collections.emptyList();
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        criteria.andClazzIdEqualTo(clazzId);
        return courseTeacherClazzMapper.selectByExample(courseTeacherClazzExample);
    }

    /**
     * 通过课程教师班级id查询课程教师班级记录
     *
     * @param courseTeacherClazzId
     * @return
     */
    public List<CourseTeacherClazz> getByCourseTeacherClazzId(Long courseTeacherClazzId) {
        if (!Optional.ofNullable(courseTeacherClazzId).isPresent()) {
            return Collections.emptyList();
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andCourseTeacherClazzIdEqualTo(courseTeacherClazzId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return courseTeacherClazzMapper.selectByExample(courseTeacherClazzExample);
    }

    /**
     * 通过课程教师班级id更改课程教师班级记录
     *
     * @param courseTeacherClazz
     * @return
     */
    public int updateByCourseTeacherClazzId(CourseTeacherClazz courseTeacherClazz) {
        if (!Optional.ofNullable(courseTeacherClazz).isPresent()) {
            return 0;
        }
        CourseTeacherClazzExample courseTeacherClazzExample = new CourseTeacherClazzExample();
        CourseTeacherClazzExample.Criteria criteria = courseTeacherClazzExample.createCriteria();
        criteria.andCourseTeacherClazzIdEqualTo(courseTeacherClazz.getCourseTeacherClazzId());
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return courseTeacherClazzMapper.updateByExampleSelective(courseTeacherClazz, courseTeacherClazzExample);
    }

}
