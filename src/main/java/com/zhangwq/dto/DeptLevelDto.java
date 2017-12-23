package com.zhangwq.dto;

import com.google.common.collect.Lists;
import com.zhangwq.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString

public class DeptLevelDto extends SysDept {
    private List<DeptLevelDto> deptLevelDtos = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto deptLevelDto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, deptLevelDto);
        return deptLevelDto;
    }
}
