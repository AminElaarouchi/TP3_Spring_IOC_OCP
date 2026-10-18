package dao;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("daoProd")
@Profile("prod")
@Primary
public class DaoImplProd implements IDao {
    @Override
    public double getValue() {
        System.out.println("[DAO] Profil de production");
        return 100.0;
    }
}
