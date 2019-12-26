package com.muye.compensate.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class MybatisConfig {

    @javax.annotation.Resource
    private DataSource dataSource;

    @Bean
    @ConditionalOnMissingBean //当容器没有指定的Bean的情况下创建该对象
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        //设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        //设置mybatis的主配置文件
        ResourcePatternResolver resourcePatternResolver=new PathMatchingResourcePatternResolver();
        Resource mybatisXml=resourcePatternResolver.getResource("classpath:mybatis/mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(mybatisXml);
        // 配置mapper的扫描，找到所有的mapper.xml映射文件
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mybatis/mapper/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        //设置别名包
        sqlSessionFactoryBean.setTypeAliasesPackage("com.muye.compensate.DO");
        return sqlSessionFactoryBean;
    }
}
