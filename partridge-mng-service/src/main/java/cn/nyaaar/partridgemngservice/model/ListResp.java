package cn.nyaaar.partridgemngservice.model;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "返回查询列表")
public class ListResp<T> {

    private List<T> list;

    private Long pages;

    private Long current;
}
