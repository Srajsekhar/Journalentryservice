package net.engineeringdigest.journalApp.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration
public class Transactionconfig {
    @Bean
    public PlatformTransactionManager add(MongoDatabaseFactory dbfactory){
        return new MongoTransactionManager(dbfactory);
    }
}
