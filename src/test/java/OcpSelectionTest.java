import config.AppConfig;
import config.DaoAliasConfig;
import config.DynamicDaoConfig;
import config.MultipleDaoConfig;
import config.PrimaryDaoConfig;
import metier.IMetier;
import metier.MetierMultiple;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OcpSelectionTest {
    @Test
    public void proprietesChangentLeDaoSansChangerLeMetier() {
        Map<String, Double> scenarios = Map.of(
                "dao", 200.0,
                "dao2", 300.0,
                "daoFile", 360.0,
                "daoApi", 440.0);
        for (Map.Entry<String, Double> scenario : scenarios.entrySet()) {
            try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
                context.getEnvironment().getPropertySources().addFirst(
                        new MapPropertySource("test", Map.of("dao.target", scenario.getKey())));
                context.register(AppConfig.class, DynamicDaoConfig.class);
                context.refresh();
                assertEquals(scenario.getValue(), context.getBean(IMetier.class).calcul(), 0.001);
            }
        }
    }

    @Test
    public void primarySelectionneLeDaoDeProduction() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                AppConfig.class, PrimaryDaoConfig.class)) {
            assertEquals(200.0, context.getBean(IMetier.class).calcul(), 0.001);
        }
    }

    @Test
    public void aliasSelectionneLeDaoApi() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                AppConfig.class, DaoAliasConfig.class)) {
            assertEquals(440.0, context.getBean(IMetier.class).calcul(), 0.001);
        }
    }

    @Test
    public void profilsSelectionnentLeurDao() {
        assertEquals(300.0, calculAvecProfil("dev"), 0.001);
        assertEquals(200.0, calculAvecProfil("prod"), 0.001);
    }

    @Test
    public void extensionAgregeLesQuatreDao() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                MultipleDaoConfig.class)) {
            MetierMultiple metier = context.getBean(MetierMultiple.class);
            assertEquals(4, metier.getDaoMap().size());
            assertTrue(metier.getDaoMap().containsKey("daoApi"));
            assertEquals(650.0, metier.calcul(), 0.001);
        }
    }

    private double calculAvecProfil(String profil) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles(profil);
            context.register(AppConfig.class);
            context.refresh();
            return context.getBean(IMetier.class).calcul();
        }
    }
}
