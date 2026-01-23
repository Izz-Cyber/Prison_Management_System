import DB.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDBSchema {
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement()) {
            System.out.println("Connected to DB: listing tables and counts (if available)");
            try (ResultSet rs = st.executeQuery("SHOW TABLES")) {
                while (rs.next()) {
                    String table = rs.getString(1);
                    System.out.println("Table: " + table);
                    // use a separate statement for counting so the outer ResultSet isn't closed
                    try (Statement st2 = c.createStatement(); ResultSet cr = st2.executeQuery("SELECT COUNT(*) FROM `" + table + "`")) {
                        if (cr.next()) System.out.println("  Rows: " + cr.getInt(1));
                    } catch (Exception e) {
                        System.out.println("  (cannot count rows: " + e.getMessage() + ")");
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Schema check failed:");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
