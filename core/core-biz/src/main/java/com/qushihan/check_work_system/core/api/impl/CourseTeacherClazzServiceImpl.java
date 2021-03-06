package com.qushihan.check_work_system.core.api.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.qushihan.check_work_system.submitwork.api.SubmitWorkService;
import com.qushihan.check_work_system.submitwork.dto.SubmitWorkDto;
import com.qushihan.check_work_system.work.api.WorkService;
import com.qushihan.check_work_system.work.dto.WorkDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.qushihan.check_work_system.clazz.api.ClazzService;
import com.qushihan.check_work_system.clazz.dto.ClazzDto;
import com.qushihan.check_work_system.core.api.CourseTeacherClazzService;
import com.qushihan.check_work_system.core.dao.CourseTeacherClazzDao;
import com.qushihan.check_work_system.core.dto.CourseTeacherClazzDto;
import com.qushihan.check_work_system.core.enums.CreateCourseTeacherClazzStatus;
import com.qushihan.check_work_system.core.enums.DeleteCourseTeacherClazzStatus;
import com.qushihan.check_work_system.core.model.auto.CourseTeacherClazz;
import com.qushihan.check_work_system.core.util.CourseTeacherClazzUtil;
import com.qushihan.check_work_system.course.api.CourseService;
import com.qushihan.check_work_system.course.dto.CourseDto;
import com.qushihan.check_work_system.inf.enums.FieldIsdelStatus;
import com.qushihan.check_work_system.teacher.api.TeacherService;
import com.qushihan.check_work_system.teacher.dto.TeacherDto;

@Service
public class CourseTeacherClazzServiceImpl implements CourseTeacherClazzService {

    @Autowired
    private CourseTeacherClazzDao courseTeacherClazzDao;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private WorkService workService;

    @Autowired
    private SubmitWorkService submitWorkService;

    @Override
    public String createCourseTeacherClazz(Long courseId, Long teacherId, Long clazzId) {
        // 先查重复
        if (!CollectionUtils.isEmpty(
                courseTeacherClazzDao.queryCourseTeacherClazzListByCourseIdAndTeacherIdAndClazzId(courseId, teacherId,
                                                                                                  clazzId))) {
            return CreateCourseTeacherClazzStatus.REPEAT_CREATE_FAIL.getMessage();
        }
        Long courseTeacherClazzId = CourseTeacherClazzUtil.getCourseTeacherClazzId(courseId, teacherId, clazzId);
        CourseTeacherClazz courseTeacherClazz = new CourseTeacherClazz();
        courseTeacherClazz.setCourseTeacherClazzId(courseTeacherClazzId);
        courseTeacherClazz.setCourseId(courseId);
        courseTeacherClazz.setTeacherId(teacherId);
        courseTeacherClazz.setClazzId(clazzId);
        courseTeacherClazzDao.InsertCourseTeacherClazz(courseTeacherClazz);
        return CreateCourseTeacherClazzStatus.CREATE_SUCCESS.getMessage();
    }

    @Override
    public List<CourseTeacherClazzDto> getByTeacherId(Long teacherId) {
        List<CourseTeacherClazz> courseTeacherClazzes = courseTeacherClazzDao.getByTeacherId(teacherId);
        if (CollectionUtils.isEmpty(courseTeacherClazzes)) {
            return Collections.emptyList();
        }
        return groupCourseTeacherClazzDtoList(courseTeacherClazzes);
    }

    /**
     * 进行CourseTeacherClazzDto列表组装
     *
     */
    private List<CourseTeacherClazzDto> groupCourseTeacherClazzDtoList(List<CourseTeacherClazz> courseTeacherClazzes) {
        return courseTeacherClazzes.stream()
                .map(courseTeacherClazz -> {
                    CourseTeacherClazzDto courseTeacherClazzDto = new CourseTeacherClazzDto();
                    BeanUtils.copyProperties(courseTeacherClazz, courseTeacherClazzDto);
                    CourseDto courseDto = courseService.queryCourseDtoByCourseId(courseTeacherClazz.getCourseId());
                    TeacherDto teacherDto = teacherService.queryTeacherDtoByTeacherId(courseTeacherClazz.getTeacherId());
                    ClazzDto clazzDto = clazzService.getByClazzId(courseTeacherClazz.getClazzId());
                    courseTeacherClazzDto.setCourseName(courseDto.getCourseName());
                    courseTeacherClazzDto.setTeacherName(teacherDto.getTeacherName());
                    courseTeacherClazzDto.setClazzName(clazzDto.getClazzName());
                    return courseTeacherClazzDto;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteCourseTeacherClazz(Long courseTeacherClazzId) {
        // 软删除课程教师班级下的所有作业下的所有提交作业
        List<WorkDto> workDtos = workService.getByCourseTeacherClazzId(courseTeacherClazzId);
        List<Long> workIds = workDtos.stream()
                .map(WorkDto::getWorkId)
                .collect(Collectors.toList());
        List<SubmitWorkDto> submitWorkDtos = submitWorkService.getByWorkIds(workIds);
        submitWorkDtos = submitWorkDtos.stream()
                .map(submitWorkDto -> submitWorkDto.setIsdel(FieldIsdelStatus.ISDEL_TRUE.getIsdel()))
                .collect(Collectors.toList());
        submitWorkDtos.forEach(submitWorkDto -> submitWorkService.updateBySubmitWorkId(submitWorkDto));
        // 软删除课程教师班级下的所有作业
        workDtos = workDtos.stream()
                .map(workDto -> workDto.setIsdel(FieldIsdelStatus.ISDEL_TRUE.getIsdel()))
                .collect(Collectors.toList());
        workDtos.forEach(workDto -> workService.updateByWorkId(workDto));
        // 软删除课程教室班级
        CourseTeacherClazzDto courseTeacherClazzDto = getByCourseTeacherClazzId(courseTeacherClazzId);
        courseTeacherClazzDto.setIsdel(FieldIsdelStatus.ISDEL_TRUE.getIsdel());
        updateByCourseTeacherClazzId(courseTeacherClazzDto);
        return DeleteCourseTeacherClazzStatus.DELETE_SUCCESS.getMessage();
    }

    @Override
    public Integer workCountAddOne(Long courseTeacherClazzId) {
        List<CourseTeacherClazz> courseTeacherClazzes = courseTeacherClazzDao.queryCourseTeacherClazzListByCourseTeacherClazzId(
                courseTeacherClazzId);
        CourseTeacherClazz courseTeacherClazz = courseTeacherClazzes.stream().findFirst().orElse(null);
        Long workCount = courseTeacherClazz.getWorkCount();
        courseTeacherClazz.setWorkCount(workCount + 1);
        if (courseTeacherClazz != null) {
            return courseTeacherClazzDao.workCountUpdate(courseTeacherClazzId, courseTeacherClazz);
        }
        return null;
    }

    @Override
    public List<CourseTeacherClazzDto> getByCourseId(Long courseId) {
        List<CourseTeacherClazz> courseTeacherClazzes = courseTeacherClazzDao.getByCourseId(courseId);
        if (CollectionUtils.isEmpty(courseTeacherClazzes)) {
            return Collections.emptyList();
        }
        return groupCourseTeacherClazzDtoList(courseTeacherClazzes);
    }

    @Override
    public List<CourseTeacherClazzDto> getByClazzId(Long clazzId) {
        List<CourseTeacherClazz> courseTeacherClazzes = courseTeacherClazzDao.getByClazzId(clazzId);
        if (CollectionUtils.isEmpty(courseTeacherClazzes)) {
            return Collections.emptyList();
        }
        return groupCourseTeacherClazzDtoList(courseTeacherClazzes);
    }

    @Override
    public CourseTeacherClazzDto getByCourseTeacherClazzId(Long courseTeacherClazzId) {
        List<CourseTeacherClazz> courseTeacherClazzes = courseTeacherClazzDao.getByCourseTeacherClazzId(courseTeacherClazzId);
        if (CollectionUtils.isEmpty(courseTeacherClazzes)) {
            return new CourseTeacherClazzDto();
        }
        List<CourseTeacherClazzDto> courseTeacherClazzDtos = groupCourseTeacherClazzDtoList(courseTeacherClazzes);
        return courseTeacherClazzDtos.stream()
                .findFirst()
                .orElse(new CourseTeacherClazzDto());
    }

    @Override
    public int updateByCourseTeacherClazzId(CourseTeacherClazzDto courseTeacherClazzDto) {
        CourseTeacherClazz courseTeacherClazz = new CourseTeacherClazz();
        BeanUtils.copyProperties(courseTeacherClazzDto, courseTeacherClazz);
        return courseTeacherClazzDao.updateByCourseTeacherClazzId(courseTeacherClazz);
    }
}
