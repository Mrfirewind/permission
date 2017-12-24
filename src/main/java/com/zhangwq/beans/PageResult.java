package com.zhangwq.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class PageResult<T> {

    List<T> data = Lists.newArrayList();

    private int total = 0;


}
