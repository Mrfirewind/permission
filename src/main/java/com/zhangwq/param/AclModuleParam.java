package com.zhangwq.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AclModuleParam {

    private Integer id;

    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2,max = 20,message = "权限模块长度需要在2到20个字之间")
    private String name;

    private Integer parentId = 0;

    private String level;

    @NotNull(message = "权限模块展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "权限模块状态不能为空")
    @Max(value = 1,message = "权限模块状态不合法")
    @Min(value = 0,message = "权限模块状态不合法")
    private Integer status;

    @Length(max = 200,message = "权限模块备注需要在200个字以内")
    private String remark;

}
