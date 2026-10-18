package config;

import dao.IDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** Alias sélectionné dans la configuration, sans modifier MetierImpl. */
@Configuration
public class DaoAliasConfig {
    @Bean
    @Primary
    public IDao daoAlias(@Qualifier("daoApi") IDao api) {
        return api;
    }
}
