package com.inventain.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Andrey
 */
@Configuration
@EnableWebMvc
@ComponentScan({"com.inventain.test"})
public class AppConfig extends WebMvcConfigurerAdapter {


}
