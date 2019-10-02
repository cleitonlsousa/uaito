package com.uaito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class ServerConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://uaidb.cmwkswbuwbmj.sa-east-1.rds.amazonaws.com:5432/dbtourneament");
        ds.setUsername("serpent");
        ds.setPassword("135rdBD2019");

        return ds;
    }

}
