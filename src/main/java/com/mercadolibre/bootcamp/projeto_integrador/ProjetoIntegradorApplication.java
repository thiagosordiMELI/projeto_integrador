package com.mercadolibre.bootcamp.projeto_integrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
public class ProjetoIntegradorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoIntegradorApplication.class, args);
    }

}
