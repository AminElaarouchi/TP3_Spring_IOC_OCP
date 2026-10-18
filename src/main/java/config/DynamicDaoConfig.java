package config;

import dao.IDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** Choix du DAO par app.properties, -Ddao.target ou DAO_TARGET. */
@Configuration
public class DynamicDaoConfig {
    @Bean
    @Primary
    public IDao dynamicDao(@Value("${dao.target}") String target, ApplicationContext context) {
        if (!context.containsBean(target)) {
            throw new IllegalArgumentException("Cible DAO inconnue : " + target);
        }
        return context.getBean(target, IDao.class);
    }
}
