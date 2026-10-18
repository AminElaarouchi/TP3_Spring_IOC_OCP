# TP3 — Spring IoC et principe OCP

Projet Maven correspondant au [TP 3 du cours Architecture Microservices](https://mliaedu.toubkalit.com/student/courses/7/labs/155). Il montre comment remplacer une source de données sans modifier `MetierImpl` : la classe métier ne dépend que de `IDao` et reçoit sa dépendance par constructeur.

## Prérequis

- JDK 17 ou plus récent
- Maven 3.6 ou plus récent

Le projet utilise Spring Context **5.3.30** comme demandé dans le TP et JUnit **4.13.2** pour les tests.

## Structure

```text
src/main/java/
├── dao/       IDao et les implémentations dao, dao2, daoFile, daoApi
├── metier/    IMetier, MetierImpl et l'extension MetierMultiple
├── config/    AppConfig et une configuration par stratégie
└── Main.java
src/main/resources/app.properties
src/test/java/OcpSelectionTest.java
```

Les quatre sources de données sont **simulées**, conformément aux exemples du TP : elles renvoient respectivement `100.0`, `150.0`, `180.0` et `220.0`. `DaoFile` ne lit pas encore un vrai fichier et `DaoApi` ne fait pas de requête réseau.

## Construire et exécuter

Dans ce dossier :

```powershell
mvn clean package
java -jar target/tp3-spring-ioc-ocp-1.0-SNAPSHOT.jar
```

Par défaut, la stratégie `property` lit `dao.target=dao2` dans `src/main/resources/app.properties`. Le résultat est **300.0**. Une propriété système ou la variable d'environnement `DAO_TARGET` peut remplacer ce choix sans recompilation :

```powershell
java '-Ddao.target=daoApi' -jar target/tp3-spring-ioc-ocp-1.0-SNAPSHOT.jar
$env:DAO_TARGET = 'daoFile'
java -jar target/tp3-spring-ioc-ocp-1.0-SNAPSHOT.jar
Remove-Item Env:DAO_TARGET
```

Les résultats sont respectivement **440.0** et **360.0**.

## Stratégies indépendantes

Chaque lancement active une seule stratégie de sélection. Cela évite plusieurs beans `@Primary` concurrents dans le même contexte Spring.

| Commande après `java -jar target/tp3-spring-ioc-ocp-1.0-SNAPSHOT.jar` | Sélection | Résultat |
| --- | --- | ---: |
| `primary` | `@Primary` dans `PrimaryDaoConfig` → `dao` | 200.0 |
| `alias` | alias `@Bean` → `daoApi` | 440.0 |
| `property` | `dao.target` → `dao2` par défaut | 300.0 |
| `profile:dev` | bean du profil `dev` | 300.0 |
| `profile:prod` | bean du profil `prod` | 200.0 |
| `multiple` | somme des quatre DAO | 650.0 |

Le profil est activé **avant** `context.refresh()` pour que Spring puisse enregistrer le bon bean. La configuration dynamique récupère un bean déjà géré par Spring à partir de son nom, au lieu d'instancier une deuxième implémentation avec `new`. La trace `@PostConstruct` indique la classe réellement injectée.

## Tests

```powershell
mvn test
```

Les tests couvrent les quatre cibles de `dao.target`, l'alias, `@Primary`, les profils et l'agrégation. Les classes métier ne contiennent aucun nom d'implémentation de DAO.

## Importer dans Eclipse

Sélectionnez **File > Import > Maven > Existing Maven Projects**, puis ce dossier. Eclipse chargera `pom.xml` et les dépendances.
