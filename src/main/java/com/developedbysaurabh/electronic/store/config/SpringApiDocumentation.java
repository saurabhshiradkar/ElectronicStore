package com.developedbysaurabh.electronic.store.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApiDocumentation {

    public OpenAPI openAPI(){
        return new OpenAPI()
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
