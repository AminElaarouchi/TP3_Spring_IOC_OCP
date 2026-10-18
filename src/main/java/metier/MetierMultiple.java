package metier;

import dao.IDao;
import java.util.List;
import java.util.Map;

/** Extension optionnelle : agréger toutes les sources enregistrées. */
public class MetierMultiple implements IMetier {
    private final List<IDao> daos;
    private final Map<String, IDao> daoMap;

    public MetierMultiple(List<IDao> daos, Map<String, IDao> daoMap) {
        this.daos = List.copyOf(daos);
        this.daoMap = Map.copyOf(daoMap);
    }

    public Map<String, IDao> getDaoMap() {
        return daoMap;
    }

    @Override
    public double calcul() {
        return daos.stream().mapToDouble(IDao::getValue).sum();
    }
}
