package mu.mns.camunda.config;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public abstract class AbstractTestContainersConfig {
    private static final Logger log = LoggerFactory.getLogger(AbstractTestContainersConfig.class);

    // Define the MySQL Testcontainer
    @Container
    protected static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("camunda")
            .withUsername("test")
            .withPassword("test")
            .withCommand("--transaction-isolation=READ-COMMITTED", "--innodb_print_all_deadlocks=1")
            .withLogConsumer(new Slf4jLogConsumer(log));  // Attach a log consumer to track container logs
    ; // Set the isolation level to READ_COMMITTED

    @BeforeAll
    public static void setUpContainer() {
        System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());
        System.setProperty("spring.datasource.driver-class-name", mysql.getDriverClassName());

        // Ensure Camunda updates the schema
        System.setProperty("camunda.bpm.database.schema-update", "true");
    }
}
