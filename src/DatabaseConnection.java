import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseConnection {

    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
    private static final String DB_NAME = "flappy_bird_edu";
    private static final String DB_URL =
        "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final Path SCHEMA_FILE = Paths.get("sql", "schema.sql");

    private static volatile boolean initialized = false;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        ensureDriverLoaded();
        ensureDatabaseExists();

        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        ensureInitialized(connection);
        return connection;
    }

    private static synchronized void ensureInitialized(Connection connection) throws SQLException {
        if (initialized) {
            return;
        }

        String schemaSql = loadSchemaSql();
        String[] statements = schemaSql.split(";");
        try (Statement statement = connection.createStatement()) {
            for (String raw : statements) {
                String sql = raw.trim();
                if (!sql.isEmpty()) {
                    statement.execute(sql);
                }
            }
        }

        initialized = true;
    }

    private static void ensureDatabaseExists() throws SQLException {
        try (Connection connection = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(
                "CREATE DATABASE IF NOT EXISTS " + DB_NAME
                    + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
            );
        } catch (SQLException e) {
            throw new SQLException("Khong the tao hoac ket noi toi database MySQL.", e);
        }
    }

    private static String loadSchemaSql() {
        if (Files.exists(SCHEMA_FILE)) {
            try {
                return Files.readString(SCHEMA_FILE, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.err.println("Khong doc duoc file schema: " + e.getMessage());
            }
        }

        return "CREATE TABLE IF NOT EXISTS players ("
            + "id INT PRIMARY KEY AUTO_INCREMENT,"
            + "name VARCHAR(100) NOT NULL UNIQUE,"
            + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");"
            + "CREATE TABLE IF NOT EXISTS scores ("
            + "id INT PRIMARY KEY AUTO_INCREMENT,"
            + "player_id INT NOT NULL,"
            + "difficulty VARCHAR(20) DEFAULT 'MEDIUM',"
            + "score INT NOT NULL,"
            + "correct_answers INT DEFAULT 0,"
            + "total_questions INT DEFAULT 0,"
            + "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(player_id) REFERENCES players(id)"
            + ");";
    }

    private static void ensureDriverLoaded() throws SQLException {
        try {
            Class.forName(DRIVER_CLASS);
            return;
        } catch (ClassNotFoundException ignored) {
        }

        Path libDir = Paths.get("lib");
        if (!Files.isDirectory(libDir)) {
            throw new SQLException("Khong tim thay thu muc lib de nap MySQL JDBC driver.");
        }

        try {
            List<Path> driverFiles = Files.list(libDir)
                .filter(path -> {
                    String name = path.getFileName().toString().toLowerCase();
                    return name.startsWith("mysql-connector") && name.endsWith(".jar");
                })
                .toList();

            for (Path jarPath : driverFiles) {
                URL jarUrl = jarPath.toUri().toURL();
                URLClassLoader loader = new URLClassLoader(new URL[] { jarUrl }, DatabaseConnection.class.getClassLoader());
                Class<?> driverClass = Class.forName(DRIVER_CLASS, true, loader);
                Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(new DriverShim(driver));
                return;
            }
        } catch (Exception e) {
            throw new SQLException("Khong the nap MySQL JDBC driver tu thu muc lib.", e);
        }

        throw new SQLException("Khong tim thay MySQL JDBC driver. Hay dat mysql-connector-j-*.jar vao thu muc lib.");
    }

    private static class DriverShim implements Driver {
        private final Driver driver;

        DriverShim(Driver driver) {
            this.driver = driver;
        }

        @Override
        public Connection connect(String url, java.util.Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        @Override
        public java.sql.DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }
}
