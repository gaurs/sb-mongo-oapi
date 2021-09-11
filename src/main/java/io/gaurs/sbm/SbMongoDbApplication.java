package io.gaurs.sbm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class SbMongoDbApplication{

    public static void main(String[] args){
        SpringApplication.run(SbMongoDbApplication.class, args);
    }

    @Bean
    Jackson2ObjectMapperBuilder objectMapperBuilder(){
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
                .simpleDateFormat("dd-MM-yyyy")
                .indentOutput(true)
                .failOnUnknownProperties(false)
                .configure(initObjectMapper());
        return builder;

    }

    private ObjectMapper initObjectMapper(){
        return new ObjectMapper()
                .registerModules(new ParameterNamesModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${spring.application.name}") String title,
                                 @Value("${application.description}") String appDescription, @Value("${application.version}") String appVersion){
        return new OpenAPI().info(new Info()
                .title(title)
                .version(appVersion)
                .description(appDescription)
                .termsOfService("https://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}
