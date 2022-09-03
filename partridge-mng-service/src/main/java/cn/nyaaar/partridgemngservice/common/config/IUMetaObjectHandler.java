package cn.nyaaar.partridgemngservice.common.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 自定义 MetaObjectHandler 类
 *
 * @author yuegenhua
 * @Version $Id: IUMetaObjectHandler.java, v 0.1 2022-02 11:06 yuegenhua Exp $$
 */
@Slf4j
@Component
public class IUMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdTime", DateUtil.date(), metaObject);
        this.setFieldValByName("updatedTime", DateUtil.date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedTime", DateUtil.date(), metaObject);
    }
}