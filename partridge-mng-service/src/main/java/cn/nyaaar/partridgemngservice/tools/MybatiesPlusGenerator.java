package cn.nyaaar.partridgemngservice.tools;


import cn.nyaaar.partridgemngservice.common.config.NumberConvertor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MybatiesPlusGenerator {

    public static void main(String[] args) {

        //生成到的Package
        String parent = "cn.nyaaar.partridgemngservice";
        //多个用逗号分隔, 表名要大写
        String tables = "pr_user";
        //数据源
        String db_url = "jdbc:mysql://localhost:3306/partridge?serverTimezone=Asia/Shanghai";
        String db_DriverName = "com.mysql.cj.jdbc.Driver";
        String db_Username = "root";
        String db_Password = "12345678";


        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());


        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setFileOverride(true); //重新生成时文件是否覆盖
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setAuthor("nyaaar");

        gc.setOpen(false); //生成后是否打开资源管理器
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");


        gc.setIdType(IdType.AUTO); //主键策略

        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
        //gc.setSwagger2(true);//开启Swagger2模式


        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(db_url);
        dsc.setDriverName(db_DriverName);
        dsc.setUsername(db_Username);
        dsc.setPassword(db_Password);
        dsc.setDbType(DbType.MYSQL);
        //number类型主键映射为long
        dsc.setTypeConvert(new NumberConvertor());

        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(""); //模块名
        pc.setParent(parent);
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tables.split(","));//对哪些表生成代码
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        strategy.setTablePrefix(pc.getModuleName() + "_"); //生成实体时去掉表前缀

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(false); //url中驼峰转连字符

        // 自动填充
        TableFill tableFill = new TableFill("created_time", FieldFill.INSERT);
        TableFill tableFill2 = new TableFill("updated_time", FieldFill.INSERT_UPDATE);
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(tableFill);
        tableFillList.add(tableFill2);
        strategy.setTableFillList(tableFillList);

        mpg.setStrategy(strategy);


        // 6、注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-rb");
                this.setMap(map);
            }
        };
        // 排除controller
        cfg.setFileCreate((configBuilder, fileType, filePath) ->
                FileType.CONTROLLER != fileType);

        // 7、调整 xml 生成目录演示
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String mapperParent = parent.replace(".", "/") + "/mapper/";
                return projectPath + "/src/main/java/" + mapperParent + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);


        // 8、 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("crud-templates/entity.java");
//        templateConfig.setMapper("crud-templates/mapper.xml");
        templateConfig.setService("crud-templates/service.java");
        templateConfig.setServiceImpl("crud-templates/serviceImpl.java");
        templateConfig.setController("crud-templates/controller.java");

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        // 6、执行
        mpg.execute();
    }
}
