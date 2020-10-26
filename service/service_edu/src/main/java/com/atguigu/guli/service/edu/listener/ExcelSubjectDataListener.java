package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.excelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<excelSubjectData> {

    private SubjectMapper subjectMapper;
    public ExcelSubjectDataListener() {

    }
    public ExcelSubjectDataListener(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    // 遍历数据
    @Override
    public void invoke(excelSubjectData data, AnalysisContext context) {
      log.info("解析到一条记录",data);
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();
        log.info("levelOneTitle: {}", levelOneTitle);
        log.info("levelTwoTitle: {}", levelTwoTitle);
        String parentId;
        Subject subjectLevelOne = this.getByTitle(levelOneTitle);
        // 判断一级列表是否重复
        if (subjectLevelOne == null){
            // 组装subject
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);
            // 执行插入操作
            subjectMapper.insert(subject);
            parentId = subject.getId();
        }
        else {
       parentId =   subjectLevelOne.getId();
        }
        // 判断一级列表是否重复
        Subject subjectLevelTwo = this.getByTitle(levelTwoTitle);
        if (subjectLevelTwo == null){
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }
    }
    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("全部数据解析完成");

    }

    /**
     * 根据一级判断是否重复存在
     * @param title 一级类别
     * @return
     */
    private Subject getByTitle(String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id","0");
        return subjectMapper.selectOne(queryWrapper);

    }


    /**
     * 根据一级判断二级列表数据是否重复存在
     * @param title 一级类别
     * @param parentId 一级id
     * @return
     */
    private Subject getByTitle(String title,String parentId){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id",parentId);
        return subjectMapper.selectOne(queryWrapper);

    }
}
