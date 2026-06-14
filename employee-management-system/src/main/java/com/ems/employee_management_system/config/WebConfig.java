package com.ems.employee_management_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /css/** to both CSS/ and css/ folders
        registry.addResourceHandler("/css/**")
                .addResourceLocations(
                    "classpath:/static/css/",
                    "classpath:/static/CSS/"
                );

        // Map /js/** to both JavaScript/ and js/ folders
        registry.addResourceHandler("/js/**")
                .addResourceLocations(
                    "classpath:/static/js/",
                    "classpath:/static/JavaScript/"
                );

        // Map /images/** to Images/ folder
        registry.addResourceHandler("/images/**")
                .addResourceLocations(
                    "classpath:/static/images/",
                    "classpath:/static/Images/"
                );
    }
}
