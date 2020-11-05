package com.udacity.vehicles.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private String apiTitle = "Vehicle Service API";
    private String apiDescription = "A vehicle listing service of cars that may or may not exist with bogus prices, in places you'll never find.";
    private String apiVersion = "0.1";
    private Contact apiContact = new springfox.documentation.service.Contact("David Dickinson", "https://github.com/biscaboy", "biscaboy@pm.me");
    private String apiLicense = "The MIT License";
    private String getApiLicenseUrl = "https://mit-license.org/";
    private String apiTermsOfService = "https://github.com/biscaboy/cars/blob/main/vehicles-api/tos.md";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo getApiInfo() {

        ApiInfo info =  new ApiInfo(
                    apiTitle,
                    apiDescription,
                    apiVersion,
                    apiTermsOfService,
                    apiContact,
                    apiLicense,
                    getApiLicenseUrl,
                    Collections.emptyList());
        return info;
    }

}
