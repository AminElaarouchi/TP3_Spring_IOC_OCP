package config;

import dao.IDao;
import java.util.List;
import java.util.Map;
import metier.MetierMultiple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** Contexte indépendant pour l'extension d'agrégation. */
@Configuration
@ComponentScan("dao")
public class MultipleDaoConfig {
    @Bean
    public MetierMultiple metierMultiple(List<IDao> daos, Map<String, IDao> daoMap) {
        return new MetierMultiple(daos, daoMap);
    }
}
