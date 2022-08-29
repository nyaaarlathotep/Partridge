package cn.nyaaar.partridgemngservice.config.mybatis.extend;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

/**
 * 扩展Mybatis generator生成数字类型字段时对于number(*)会映射为BigDecimal的问题
 */
public class NumberConvertor extends MySqlTypeConvert implements ITypeConvert {

    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        //System.out.println(fieldType);

        String toLow = fieldType.toLowerCase();
        if (toLow.contains("number")) {
            return toNumberType(toLow);
        }

        return super.processTypeConvert(globalConfig, fieldType);
    }


    private static IColumnType toNumberType(String typeName) {
        if (typeName.equals("number(1)")) {
            return DbColumnType.BOOLEAN;
        } else if (typeName.matches("number\\([0-9]\\)")) {
            return DbColumnType.INTEGER;
        } else if (typeName.matches("number\\([1-3][0-9]\\)")) {
            return DbColumnType.LONG;
        } else if (typeName.equals("number")) {
            return DbColumnType.INTEGER;
        }
        return DbColumnType.BIG_DECIMAL;
    }

}
