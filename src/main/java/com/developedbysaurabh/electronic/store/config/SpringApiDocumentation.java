package com.developedbysaurabh.electronic.store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApiDocumentation {

    @Bean
    public OpenAPI openAPI(){

        String schemeName = "bearerScheme";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(schemeName)
                )
                .components(new Components().addSecuritySchemes(schemeName,new SecurityScheme()
                        .name(schemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer"))
                )
                .info(new Info()
                        .title("Electronic Store API's")
                        .description("This is Electronic Store Project api Developed By Saurabh")
                        .version("1.0")
                        .contact(new Contact().name("Saurabh").email("saurabhshiradkar@gmail.com"))
                        .license(new License().name("License Name"))
                ).externalDocs(new ExternalDocumentation().url("https://github.com/saurabhshiradkar/ElectronicStore"))
                ;
    }


}












