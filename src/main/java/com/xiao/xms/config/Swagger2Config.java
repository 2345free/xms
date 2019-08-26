package com.xiao.xms.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@EnableSwagger2
@Configuration
@RequiredArgsConstructor
public class Swagger2Config {

    private final TypeResolver typeResolver;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalParams())
                .alternateTypeRules(newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(PageRequest.class)))
                .securitySchemes(newArrayList(apiKey()))
                .securityContexts(newArrayList(securityContext()));
    }

    private List<Parameter> globalParams() {
        return newArrayList(new ParameterBuilder()
                .name("access_token")
                .description("jwt token")
                .modelRef(new ModelRef("string"))
                .parameterType("query")
                .required(false)
                .build());
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(new SecurityReference("Authorization", authorizationScopes));
    }

    @ApiModel
    @Data
    private static class PageRequest {
        @ApiModelProperty(value = "第page页,从0开始计数", example = "0")
        private Integer page;

        @ApiModelProperty(value = "每页数据数量", example = "15")
        private Integer size;

        @ApiModelProperty(value = "按属性排序,格式:属性(,asc|desc)")
        private List<String> sort;
    }
}
