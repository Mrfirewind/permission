package com.zhangwq.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.zhangwq.dao.SysDeptMapper;
import com.zhangwq.dto.DeptLevelDto;
import com.zhangwq.model.SysDept;
import com.zhangwq.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class SysTreeService implements ISysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public List<DeptLevelDto> getDeptTree() {
        List<SysDept> sysDepts = sysDeptMapper.getAllDept();
        List<DeptLevelDto> deptLevelDtos = Lists.newArrayList();
        for (SysDept sysDept : sysDepts) {
            deptLevelDtos.add(DeptLevelDto.adapt(sysDept));
        }
        return deptListToTree(deptLevelDtos);
    }

    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtos) {
        if (CollectionUtils.isEmpty(deptLevelDtos)) {
            return Lists.newArrayList();
        }

        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();

        for (DeptLevelDto deptLevelDto : deptLevelDtos) {
            levelDeptMap.put(deptLevelDto.getLevel(), deptLevelDto);
            if (LevelUtil.ROOT.equals(deptLevelDto.getLevel())) {
                rootList.add(deptLevelDto);
            }
        }
        //按照seq从小到大排序
        Collections.sort(rootList, deptSeqComparator);
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    public void transformDeptTree(List<DeptLevelDto> deptLevelDtos, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
        for (DeptLevelDto deptLevelDto : deptLevelDtos) {
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            //获取下层的数据
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                Collections.sort(tempDeptList, deptSeqComparator);
                //设置下一层的部门
                deptLevelDto.setDeptLevelDtos(tempDeptList);
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }

        }
    }

    private static final Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
