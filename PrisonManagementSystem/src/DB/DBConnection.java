package DB;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DBConnection {

    // رابط الاتصال القديم (الخاطئ)
    // private static final String URL = "jdbc:mysql://localhost:3306/prison_management_system";
    
    // =================================================================================
    // رابط الاتصال الجديد والصحيح الذي يدعم اللغة العربية 100%
    // =================================================================================
    private static final String URL = "jdbc:mysql://localhost:3306/prison_management_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        ensureDriverAvailable();
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void ensureDriverAvailable() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return; // driver already on classpath
        } catch (ClassNotFoundException ignored) {
            // try to find connector jars in common locations and load them dynamically
        }
        List<File> jars = findConnectorJars();
        if (jars.isEmpty()) {
            throw new SQLException("MySQL JDBC Driver not found and no connector jar discovered in project paths.");
        }

        try {
            List<URL> urls = new ArrayList<>();
            for (File f : jars) {
                try {
                    urls.add(f.toURI().toURL());
                } catch (Exception ex) {
                    // ignore this file
                }
            }
            URL[] urlArr = urls.toArray(new URL[0]);
            URLClassLoader loader = new URLClassLoader(urlArr, DBConnection.class.getClassLoader());

            // Try known driver class names and register the driver via a shim to avoid classloader issues
            String[] driverClassNames = new String[]{"com.mysql.cj.jdbc.Driver", "com.mysql.jdbc.Driver"};
            boolean loaded = false;
            for (String driverClass : driverClassNames) {
                try {
                    Class<?> clazz = Class.forName(driverClass, true, loader);
                    Object inst = clazz.getDeclaredConstructor().newInstance();
                    if (inst instanceof Driver) {
                        Driver drv = (Driver) inst;
                        DriverManager.registerDriver(new DriverShim(drv));
                        loaded = true;
                        System.out.println("Loaded and registered MySQL driver from: " + jars.get(0).getAbsolutePath());
                        break;
                    }
                } catch (ClassNotFoundException cnf) {
                    // try next class name
                }
            }
            if (!loaded) {
                throw new SQLException("Failed to find/register a MySQL Driver class inside discovered connector jars.");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw new SQLException("Failed to load MySQL JDBC Driver from discovered jars.", e);
        }
    }

    private static List<File> findConnectorJars() {
        List<File> found = new ArrayList<>();
        Pattern p = Pattern.compile("mysql-connector.*\\.jar", Pattern.CASE_INSENSITIVE);

        // Start points to search: working dir and its parents (up to 4 levels), plus common folders
        List<Path> startPoints = new ArrayList<>();
        String userDir = System.getProperty("user.dir");
        if (userDir != null) startPoints.add(Paths.get(userDir));
        // also try location of this class file
        try {
            Path codeLoc = Paths.get(DBConnection.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            startPoints.add(codeLoc);
            if (codeLoc.getParent() != null) startPoints.add(codeLoc.getParent());
        } catch (Exception ignored) {
        }

        // add some likely folders
        startPoints.add(Paths.get("lib"));
        startPoints.add(Paths.get("src"));
        startPoints.add(Paths.get("."));

        // For each start point, walk a few levels deep and match file names
        for (Path sp : startPoints) {
            if (sp == null) continue;
            if (!Files.exists(sp)) continue;
            try (Stream<Path> stream = Files.walk(sp, 6)) {
                stream.filter(Files::isRegularFile).forEach(pth -> {
                    String nm = pth.getFileName().toString();
                    if (p.matcher(nm).matches()) {
                        found.add(pth.toFile());
                    }
                });
            } catch (IOException ignored) {
            }
            if (!found.isEmpty()) return found;
        }

        // As a last resort, walk up from user.dir to parents and search each parent recursively
        try {
            Path up = Paths.get(System.getProperty("user.dir"));
            for (int i = 0; i < 4 && up != null; i++) {
                try (Stream<Path> stream = Files.walk(up, 5)) {
                    stream.filter(Files::isRegularFile).forEach(pth -> {
                        String nm = pth.getFileName().toString();
                        if (p.matcher(nm).matches()) {
                            found.add(pth.toFile());
                        }
                    });
                } catch (IOException ignored) {
                }
                if (!found.isEmpty()) return found;
                up = up.getParent();
            }
        } catch (Exception ignored) {
        }

        return found;
    }

    // Driver shim to avoid classloader issues when registering drivers loaded from child classloaders
    private static class DriverShim implements Driver {
        private final Driver driver;

        DriverShim(Driver d) {
            this.driver = d;
        }

        public boolean acceptsURL(String u) throws SQLException {
            return driver.acceptsURL(u);
        }

        public java.sql.Connection connect(String u, java.util.Properties p) throws SQLException {
            return driver.connect(u, p);
        }

        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        public java.sql.DriverPropertyInfo[] getPropertyInfo(String u, java.util.Properties p) throws SQLException {
            return driver.getPropertyInfo(u, p);
        }

        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
            try {
                return driver.getParentLogger();
            } catch (AbstractMethodError ame) {
                throw new java.sql.SQLFeatureNotSupportedException(ame);
            }
        }
    }
}