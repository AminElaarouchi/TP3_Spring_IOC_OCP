package dao;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("daoDev")
@Profile("dev")
@Primary
public class DaoImplDev implements IDao {
    @Override
    public double getValue() {
        System.out.println("[DAO] Profil de développement");
        return 150.0;
    }
}
