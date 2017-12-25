package com.zhangwq.dto;

import com.google.common.collect.Lists;
import com.zhangwq.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class AclModuleDto extends SysAclModule {
    private List<AclModuleDto> aclModuleList = Lists.newArrayList();

    public static AclModuleDto adapt(SysAclModule sysAclModule) {
        AclModuleDto aclModuleDto = new AclModuleDto();
        BeanUtils.copyProperties(sysAclModule, aclModuleDto);
        return aclModuleDto;
    }
}
