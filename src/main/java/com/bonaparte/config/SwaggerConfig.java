package com.bonaparte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangmingquan on 2018/9/11.
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket demoApi() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Authorization").description("i").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("elastic")
                .globalOperationParameters(aParameters)
                .genericModelSubstitutes(DeferredResult.class)
//              .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .paths(PathSelectors.any())//过滤的接口
                .build()
                .apiInfo(demoApiInfo());
    }

    private ApiInfo demoApiInfo() {
        return new ApiInfoBuilder()
                .title("波拿巴技术")//大标题
                .description("波拿巴技术")//详细描述
                .version("1.0")//版本
                .termsOfServiceUrl("NO terms of service")
                .contact(new Contact("bonaparte", "http://www.bonaparte.cn/", ""))//作者
                .license("The Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }
}
