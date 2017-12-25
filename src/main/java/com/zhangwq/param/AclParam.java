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
public class AclParam {

    private Integer id;

    @NotBlank(message = "权限点名称不能为空")
    @Length(min = 2, max = 20, message = "权限点名称需要在2-20个字之间")
    private String name;

    @NotNull(message = "必须制定权限模块")
    private Integer aclModuleId;

    @Length(min = 6, max = 100, message = "权限点url需要在6-100个字符之间")
    private String url;

    @NotNull(message = "权限点类型不允许为空")
    @Min(value = 1, message = "权限点类型不合法")
    @Max(value = 3, message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "权限点状态不允许为空")
    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 1, message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "必须制定权限点的展示位置")
    private Integer seq;

    @Length(min = 0, max = 200, message = "备注需要在0-200个字符之间")
    private String remark;

}
