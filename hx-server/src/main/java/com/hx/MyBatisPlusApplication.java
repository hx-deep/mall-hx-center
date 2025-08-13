package com.hx;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

import java.sql.Types;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
/**
 * @Package: com.hx
 * @ClassName: MyBatisPlusApplication.java
 * @Description:
 * @author: zjg
 * @date: 2024/7/26
 */


public class MyBatisPlusApplication {
    public static void main(String[] args) {
        // 生成的代码文件包输出根目录
        String outPath = "E:\\hx\\mall-hx-center\\mall-hx-center\\hx-server\\src\\main\\java\\com\\hx\\";

        // 数据库
        String sqlUrl = "jdbc:mysql://394.11.841.181:3306/hw_scanner?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String sqlUserName = "dev";
        String sqlPassword = "123456";

        // 需要生成的数据表名,多表用逗号隔开（生成的代码会按此数据表字段进行生成对应文件）
        String tables = "file_convert_log,file_convert_record,file_convert_task";
        /**
         * 数据库配置
         */
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(sqlUrl, sqlUserName, sqlPassword);
        /**
         * 全局配置
         */
        fastAutoGenerator.globalConfig(builder -> {
            builder.author("YYYY")    // 设置作者
                    .outputDir(outPath)
                    .disableOpenDir() // 禁止自动打开输出目录
                    .commentDate("yyyy-MM-dd HH:mm:ss"); //注释日期
        });
        /**
         * 数据库配置
         * 兼容旧版本转换成Integer
         */
        fastAutoGenerator.dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
            if (typeCode == Types.SMALLINT) {
                // 自定义类型转换
                return DbColumnType.INTEGER;
            }
            return typeRegistry.getColumnType(metaInfo);
        }));
        /**
         * 包配置
         */
        fastAutoGenerator.packageConfig(builder -> {
            builder.parent("com.linlong.lecture.management") // 设置父包名 - 生成文件夹的父文件（默认在根目录生成，java文件夹下的整个路径）
                    .moduleName("")               // 设置父包模块名
                    .entity("domain.entity")       // entity 包名 （前面model.代表在entity上面会再生成个父文件夹）
                    .service("model.service")     // service 包名
                    .serviceImpl("model.impl")    // impl 包名
                    .mapper("repository.inchat.mapper")       // mapper 包名
                    .xml("model.mapperXml")       // Mapper XML 包名
                    .controller("controller");    // Controller 包名（该生成后的必须与项目启动类Application.java路径同层）
        });
        /**
         * 策略配置
         * entityBuilder().naming: 默认下划线转驼峰命
         * 重点注意：可以通过不同文件夹的策略配置设置enableFileOverride()确定是否覆盖原文件，防止自定义代码被覆盖
         */
        fastAutoGenerator.strategyConfig(builder -> {
            // 设置需要生成的表名,多个表名用逗号隔开如："aa,bb"
            builder.addInclude(tables)
                    .entityBuilder()               // 【Entity 策略配置】
                    .enableTableFieldAnnotation() // 【Entity 策略配置】 开启生成实体时生成字段注解
                    .enableFileOverride()         // 【Entity 策略配置】 开启覆盖已生成文件
                    .enableLombok()               // 【Entity 策略配置】 开启 lombok 模型
                    .controllerBuilder()          // 【Controller 策略配置】
                    .enableRestStyle()            // 【Controller 策略配置】 开启 @RestController注解
                    .mapperBuilder()              // 【mapper 策略配置】
                    .enableMapperAnnotation()     // 【mapper 策略配置】 开启 @Mapper 注解
                    .enableBaseResultMap()        // 【mapper 策略配置】 开启 BaseResultMap
                    .enableBaseColumnList();      // 【mapper 策略配置】 开启 BaseColumnList

        });
        /**
         * 生成代码模板
         */
        fastAutoGenerator.templateEngine(new FreemarkerTemplateEngine()); // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        /**
         * 运行生成
         */
        fastAutoGenerator.execute();
    }
}