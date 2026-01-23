import DB.DBManager;
import DB.MockDBManager;
import DB.DBManagerInterface;
import Classes.*;

import java.util.Date;

public class TestAll {
    public static void main(String[] args) {
        DBManagerInterface db;
        boolean usingMock = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            db = new DBManager();
            System.out.println("Using real DBManager (JDBC)");
        } catch (ClassNotFoundException e) {
            db = new MockDBManager();
            usingMock = true;
            System.out.println("MySQL driver not found — using MockDBManager (demo mode)");
        }

        try {
            String[] types = new String[]{"Prisoner","Staff","Department","Crime","Room","Visitor","Visit","MedicalRecord","Incident"};
            for (String t : types) {
                try {
                    CustomLinkedList<?> list = db.getAll(t);
                    System.out.println(t + ": count=" + (list==null?0:list.size()));
                    if (list!=null && list.size()>0) {
                        Object first = list.get(0);
                        System.out.println("  sample: " + first.toString());
                    }
                } catch (Exception ex) {
                    System.out.println("  failed to getAll("+t+"): " + ex.getMessage());
                }
            }

            System.out.println("\n--- Inserting test prisoner ---");
            Prisoner p = new Prisoner();
            String testName = "TEST_AUTOMATION_" + System.currentTimeMillis();
            p.setFullName(testName);
            p.setAge(99);
            p.setNationality("Testland");

            // pick first department & crime if available
            CustomLinkedList<Department> depts = db.getAllDepartments();
            CustomLinkedList<Crime> crimes = db.getAllCrimes();
            if (depts!=null && depts.size()>0) p.setDepartment(depts.get(0));
            if (crimes!=null && crimes.size()>0) p.setCrime(crimes.get(0));

            db.addPrisoner(p);
            System.out.println("Added test prisoner: " + testName);

            // verify
            CustomLinkedList<Prisoner> prisoners = db.getAllPrisoners();
            int foundId = -1;
            for (Prisoner q : prisoners) {
                if (testName.equals(q.getFullName())) {
                    foundId = q.getId();
                    System.out.println("Found inserted prisoner with id=" + foundId + " name=" + q.getFullName());
                    break;
                }
            }
            if (foundId==-1) System.out.println("Inserted prisoner not found in getAllPrisoners() — maybe DB didn't persist yet.");

            if (!usingMock && foundId!=-1) {
                System.out.println("Deleting test prisoner id="+foundId);
                db.delete("Prisoner", foundId);
                System.out.println("Deleted test prisoner.");
            } else if (usingMock) {
                System.out.println("Mock mode — no persistent deletion necessary.");
            }

            System.out.println("\nDone.");
        } catch (Exception e) {
            System.err.println("TestAll failed:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
