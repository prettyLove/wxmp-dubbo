package com.kochun.wxmp.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

//注解标示,这是一个配置类,@Configuation注解包含了@Component注解
//可以不用在使用@Component注解标记这是个bean了
@Configuration
//注解开启 swagger2 功能
@EnableSwagger2
@EnableWebMvc
public class Swagger2Config implements WebMvcConfigurer {

    /**
     * 将Swagger2 的swagger-ui.html加入资源路径下,否则Swagger2静态页面不能访问。要想使资源能够访问，可以有两种方法
     * 一：需要配置类继承WebMvcConfigurationSupport 类，实现addResourceHandlers方法。
     *      但是实现了WebMvcConfigurationSupport以后，Spring Boot的 WebMvc自动配置就会失效，具体表现比如访问不到
     *      静态资源（js，css等）了，这是因为WebMvc的自动配置都在WebMvcAutoConfiguration类中，但是类中有这个注解
     *      @ConditionalOnMissingBean({WebMvcConfigurationSupport.class})，意思是一旦在容器中检测到
     *      WebMvcConfigurationSupport这个类，WebMvcAutoConfiguration类中的配置都不生效。
     *      所以一旦我们自己写的配置类继承了WebMvcConfigurationSupport，相当于容器中已经有了WebMvcConfigurationSupport，
     *      所有默认配置都不会生效，都得自己在配置文件中配置。
     * 二：继承WebMvcConfigurer接口，这里采用此方法 网上有人说使用该方法会导致date编译等问题，可能在配置文件中得到解决，
     *      本人没有碰到，不多做解释
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
    @Bean
    public Docket createRestApi() {
        ParameterBuilder builder  = new ParameterBuilder();
        Parameter parameter = builder
                // 从cookie中获取token
                .parameterType("header") //参数类型支持header, cookie, body, query etc
                .name("accessToken") //参数名
                .defaultValue("") //默认值
                .description("请输入token")
                .modelRef(new ModelRef("string")) //指定参数值的类型
                .required(false).build(); //非必需，这里是全局配置，然而在登陆的时候是不用验证的

        List<Parameter> pars = new ArrayList<Parameter>();
//        builder .name("accessToken").description("Token").modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build();

//        pars.add(builder .build());
        pars.add(parameter);
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.kochun.wxmp.back.controller")).paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("KOCHUN-后台接口文档与测试").description("API 使用说明").version("1.0").build();
    }

}
