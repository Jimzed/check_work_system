package com.qushihan.check_work_system.work.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.qushihan.check_work_system.inf.enums.FieldIsdelStatus;
import com.qushihan.check_work_system.work.mapper.auto.WorkMapper;
import com.qushihan.check_work_system.work.model.auto.Work;
import com.qushihan.check_work_system.work.model.auto.WorkExample;

@Repository
public class WorkDao {

    @Autowired
    private WorkMapper workMapper;

    /**
     * 通过CourseTeacherClazzId查询作业列表
     *
     * @param courseTeacherClazzId
     *
     * @return
     */
    public List<Work> getByCourseTeacherClazzId(Long courseTeacherClazzId) {
        if (!Optional.ofNullable(courseTeacherClazzId).isPresent()) {
            return Collections.emptyList();
        }
        WorkExample workExample = new WorkExample();
        WorkExample.Criteria criteria = workExample.createCriteria();
        criteria.andCourseTeacherClazzIdEqualTo(courseTeacherClazzId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return workMapper.selectByExample(workExample);
    }

    /**
     * 创建作业
     *
     * @param work
     */
    public int createWork(Work work) {
        if (!Optional.ofNullable(work).isPresent()) {
            return 0;
        }
        return workMapper.insertSelective(work);
    }

    /**
     * 通过作业题目和作业内容查询作业列表
     *
     * @param workTitle
     * @param workContent
     *
     * @return
     */
    public List<Work> getByWorkTitleAndWorkContent(String workTitle, String workContent) {
        if (StringUtils.isEmpty(workTitle)) {
            return Collections.emptyList();
        }
        if (StringUtils.isEmpty(workContent)) {
            return Collections.emptyList();
        }
        WorkExample workExample = new WorkExample();
        WorkExample.Criteria criteria = workExample.createCriteria();
        criteria.andWorkTitleEqualTo(workTitle);
        criteria.andWorkContentEqualTo(workContent);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return workMapper.selectByExample(workExample);
    }

    /**
     * 通过作业id更改作业记录
     *
     * @param work
     *
     * @return
     */
    public int updateByWorkId(Work work) {
        if (!Optional.ofNullable(work).isPresent()) {
            return 0;
        }
        WorkExample workExample = new WorkExample();
        WorkExample.Criteria criteria = workExample.createCriteria();
        criteria.andWorkIdEqualTo(work.getWorkId());
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return workMapper.updateByExampleSelective(work, workExample);
    }

    /**
     * 通过作业题目名称搜索作业
     *
     * @param searchWorkTitle
     * @return
     */
    public List<Work> getBySearchWorkTitle(String searchWorkTitle) {
        WorkExample workExample = new WorkExample();
        WorkExample.Criteria criteria = workExample.createCriteria();
        if (StringUtils.isNotEmpty(searchWorkTitle)) {
            criteria.andWorkTitleLike(searchWorkTitle + "%");
        }
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return workMapper.selectByExample(workExample);
    }

    /**
     * 通过作业id获取作业记录
     *
     * @param workId
     * @return
     */
    public Work getByWorkId(Long workId)  {
        if (!Optional.ofNullable(workId).isPresent()) {
            return new Work();
        }
        WorkExample workExample = new WorkExample();
        WorkExample.Criteria criteria = workExample.createCriteria();
        criteria.andWorkIdEqualTo(workId);
        criteria.andIsdelEqualTo(FieldIsdelStatus.ISDEL_FALSE.getIsdel());
        return workMapper.selectByExample(workExample).stream()
                .findFirst()
                .orElse(new Work());
    }
}
