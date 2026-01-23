import DB.DBConnection;
import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            Connection c = DBConnection.getConnection();
            if (c != null && !c.isClosed()) {
                System.out.println("DB connection OK");
                c.close();
            } else {
                System.out.println("DB connection returned null or closed");
            }
        } catch (Exception e) {
            System.err.println("DB connection failed:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
