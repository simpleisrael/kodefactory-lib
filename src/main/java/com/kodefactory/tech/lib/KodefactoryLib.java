package com.kodefactory.tech.lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan(basePackages={"com.kodefactory.tech.lib"})
@EnableAutoConfiguration
public class KodefactoryLib {

//    public static void main(String[] args) {
//        SpringApplication.run(KodefactoryLib.class, args);
//    }

}
