import config.AppConfig;
import config.DaoAliasConfig;
import config.DynamicDaoConfig;
import config.MultipleDaoConfig;
import config.PrimaryDaoConfig;
import metier.IMetier;
import metier.MetierMultiple;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        String strategy = args.length == 0 ? "property" : args[0];
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            if ("multiple".equals(strategy)) {
                context.register(MultipleDaoConfig.class);
                context.refresh();
                MetierMultiple metier = context.getBean(MetierMultiple.class);
                System.out.println("Sources : " + metier.getDaoMap().keySet());
                System.out.println("Résultat final : " + metier.calcul());
                return;
            }

            context.register(AppConfig.class);
            switch (strategy) {
                case "primary":
                    context.register(PrimaryDaoConfig.class);
                    break;
                case "alias":
                    context.register(DaoAliasConfig.class);
                    break;
                case "property":
                    context.register(DynamicDaoConfig.class);
                    break;
                case "profile:dev":
                    context.getEnvironment().setActiveProfiles("dev");
                    break;
                case "profile:prod":
                    context.getEnvironment().setActiveProfiles("prod");
                    break;
                default:
                    throw new IllegalArgumentException("Stratégie inconnue : " + strategy);
            }
            context.refresh();
            IMetier metier = context.getBean(IMetier.class);
            System.out.println("Résultat final : " + metier.calcul());
        }
    }
}
