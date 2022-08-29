package cn.nyaaar.partridgemngservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(cn.hutool.extra.spring.SpringUtil.class)
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(value = "cn/nyaaar/partridgemngservice/mapper")
public class PartridgeMngServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartridgeMngServiceApplication.class, args);
    }

}
