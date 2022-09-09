package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.nyaaar.partridgemngservice.model.QueryData;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public QueryData<${entity}> findListByPage(${entity} where, Integer page, Integer pageCount){
        IPage<${entity}> wherePage = new Page<>(page, pageCount);

        IPage<${entity}> iPage = baseMapper.selectPage(wherePage, Wrappers.query(where));

        return new QueryData<>(iPage);
    }

    @Override
    public List<${entity}> findList(${entity} where){

        return baseMapper.selectList( Wrappers.query(where));
    }



    @Override
    public Integer add(${entity} ${entity?uncap_first}){
 
        return baseMapper.insert(${entity?uncap_first});
    }

    @Override
    public Integer delete(Integer id){
    
        return baseMapper.deleteById(id);
    }

    @Override
    public Integer updateData(${entity} ${entity?uncap_first}){
    
        return baseMapper.updateById(${entity?uncap_first});
    }

    @Override
    public ${entity} findById(Integer id){
    
        return baseMapper.selectById(id);
    }
}
</#if>
