package config;

import dao.IDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** Déclare le DAO de production comme choix par défaut. */
@Configuration
public class PrimaryDaoConfig {
    @Bean
    @Primary
    public IDao primaryDao(@Qualifier("dao") IDao dao) {
        return dao;
    }
}
