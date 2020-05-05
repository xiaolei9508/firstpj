package com.cmft.slas.cmuop.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan("com.cmft.slas.cmuop")
@MapperScan("com.cmft.slas.cmuop.mapper")
@Configuration
public class SlasCMUOPConfiguration implements WebMvcConfigurer {

}
