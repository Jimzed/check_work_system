package com.qushihan.check_work_system.app.controller.student;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qushihan.check_work_system.clazz.api.ClazzService;
import com.qushihan.check_work_system.clazz.dto.ClazzDto;
import com.qushihan.check_work_system.student.dto.GetStudentBySearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qushihan.check_work_system.app.util.PrintWriterUtil;
import com.qushihan.check_work_system.inf.util.TransitionUtil;
import com.qushihan.check_work_system.student.api.StudentService;
import com.qushihan.check_work_system.student.dto.StudentDetailRequest;
import com.qushihan.check_work_system.student.dto.StudentDto;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClazzService clazzService;

    /**
     * 学生详情
     *
     * @param studentDetailRequest
     * @param request
     * @param response
     */
    @RequestMapping("/studentDetail")
    public void studentDetail(@RequestBody StudentDetailRequest studentDetailRequest, HttpServletRequest request,
            HttpServletResponse response) {
        Long clazzId = TransitionUtil.stringToLong(studentDetailRequest.getClazzId());
        List<StudentDto> studentDtos = studentService.getByClazzId(clazzId);
        List<ClazzDto> clazzDtos = clazzService.queryAllClazz();
        request.getServletContext().setAttribute("studentDtos", studentDtos);
        request.getServletContext().setAttribute("clazzIdForStudent", clazzId);
        request.getServletContext().setAttribute("searchClazzDtos", clazzDtos);
        PrintWriterUtil.print("学生详情查询成功", response);
    }

    /**
     * 通过学生名称查询学生
     *
     * @param getStudentBySearchRequest
     * @param request
     * @param response
     */
    @RequestMapping("/getStudentBySearch")
    public void getStudentBySearch(@RequestBody GetStudentBySearchRequest getStudentBySearchRequest, HttpServletRequest request, HttpServletResponse response) {
        String searchStudntName = getStudentBySearchRequest.getSearchStudentName();
        List<StudentDto> studentDtosForSearch = studentService.getBySearchStudentName(searchStudntName);
        List<Long> studentIds = studentDtosForSearch.stream()
                .map(StudentDto::getStudentId)
                .collect(Collectors.toList());
        Long clazzId = (Long) request.getServletContext().getAttribute("clazzIdForStudent");
        List<StudentDto> studentDtos = studentService.getByClazzId(clazzId);
        studentDtos = studentDtos.stream()
                .filter(studentDto -> studentIds.contains(studentDto.getStudentId()))
                .collect(Collectors.toList());
        request.getServletContext().setAttribute("studentDtos", studentDtos);
        PrintWriterUtil.print("查询成功", response);
    }
}
