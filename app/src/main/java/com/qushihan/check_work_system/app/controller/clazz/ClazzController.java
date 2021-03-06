package com.qushihan.check_work_system.app.controller.clazz;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qushihan.check_work_system.clazz.dto.ClazzDto;
import com.qushihan.check_work_system.clazz.dto.GetClazzBySearchRequest;
import com.qushihan.check_work_system.clazz.enums.CreateClazzStatus;
import com.qushihan.check_work_system.clazz.enums.DeleteClazzStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qushihan.check_work_system.app.util.PrintWriterUtil;
import com.qushihan.check_work_system.clazz.api.ClazzService;
import com.qushihan.check_work_system.clazz.dto.CreateClazzRequest;
import com.qushihan.check_work_system.clazz.dto.DeleteClazzRequest;
import com.qushihan.check_work_system.inf.util.TransitionUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clazz")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 创建班级
     *
     * @param createClazzRequest
     * @param response
     */
    @PostMapping("/create")
    public void createClazz(@RequestBody CreateClazzRequest createClazzRequest, HttpServletRequest request, HttpServletResponse response) {
        String clazzName = createClazzRequest.getClazzName();
        String createMessge = clazzService.createClazz(clazzName);
        if (createMessge.equals(CreateClazzStatus.CREATE_SUCCESS.getMessage())) {
            List<ClazzDto> clazzDtos = clazzService.queryAllClazz();
            request.getServletContext().setAttribute("clazzDtos", clazzDtos);
            request.getServletContext().setAttribute("searchClazzDtos", clazzDtos);
        }
        PrintWriterUtil.print(createMessge, response);
    }

    /**
     * 删除班级
     *
     * @param deleteClazzRequest
     * @param response
     */
    @PostMapping("/delete")
    public void deleteClazz(@RequestBody DeleteClazzRequest deleteClazzRequest, HttpServletRequest request, HttpServletResponse response) {
        Long clazzId = TransitionUtil.stringToLong(deleteClazzRequest.getClazzId());
        String deleteMessge = clazzService.deleteClazz(clazzId);
        if (deleteMessge.equals(DeleteClazzStatus.DELETE_SUCCESS.getMessage())) {
            List<ClazzDto> clazzDtos = clazzService.queryAllClazz();
            request.getServletContext().setAttribute("clazzDtos", clazzDtos);
            request.getServletContext().setAttribute("searchClazzDtos", clazzDtos);
        }
        PrintWriterUtil.print(deleteMessge, response);
    }

    /**
     * 通过班级名称查询班级
     *
     * @param getClazzBySearchRequest
     * @param request
     * @param response
     */
    @RequestMapping("/getClazzBySearch")
    public void getClazzBySearch(@RequestBody GetClazzBySearchRequest getClazzBySearchRequest, HttpServletRequest request, HttpServletResponse response) {
        String searchClazzName = getClazzBySearchRequest.getSearchClazzName();
        List<ClazzDto> searchClazzDtos = clazzService.getBySearchClazzName(searchClazzName);
        List<Long> clazzIds = searchClazzDtos.stream()
                .map(ClazzDto::getClazzId)
                .collect(Collectors.toList());
        List<ClazzDto> clazzDtos = clazzService.queryAllClazz();
        clazzDtos = clazzDtos.stream()
                .filter(clazzDto -> clazzIds.contains(clazzDto.getClazzId()))
                .collect(Collectors.toList());
        request.getServletContext().setAttribute("searchClazzDtos", clazzDtos);
        PrintWriterUtil.print("查询成功", response);
    }
}
