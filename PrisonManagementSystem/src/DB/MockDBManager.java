package DB;

import Classes.*;
import java.sql.SQLException;
import java.util.Date;

/**
 * Mock DB manager to run the application in offline/demo mode when JDBC driver or DB is unavailable.
 */
public class MockDBManager implements DBManagerInterface {

    private CustomLinkedList<Prisoner> prisoners = new CustomLinkedList<>();
    private CustomLinkedList<Staff> staff = new CustomLinkedList<>();
    private CustomLinkedList<Department> departments = new CustomLinkedList<>();
    private CustomLinkedList<Crime> crimes = new CustomLinkedList<>();
    private CustomLinkedList<Room> rooms = new CustomLinkedList<>();
    private CustomLinkedList<Visitor> visitors = new CustomLinkedList<>();
    private CustomLinkedList<Visit> visits = new CustomLinkedList<>();
    private CustomLinkedList<MedicalRecord> records = new CustomLinkedList<>();
    private CustomLinkedList<Incident> incidents = new CustomLinkedList<>();

    private int personIdCounter = 1;
    private int deptIdCounter = 1;
    private int crimeIdCounter = 1;
    private int roomIdCounter = 1;
    private int visitIdCounter = 1;
    private int medicalRecordIdCounter = 1;
    private int incidentIdCounter = 1;

    public MockDBManager() {
        // populate some demo data
        Department d1 = new Department(); d1.setDepartmentId(deptIdCounter++); d1.setName("عام"); d1.setCapacity(50); departments.add(d1);
        Department d2 = new Department(); d2.setDepartmentId(deptIdCounter++); d2.setName("أمني"); d2.setCapacity(30); departments.add(d2);

        Crime c1 = new Crime(); c1.setCrimeId(crimeIdCounter++); c1.setCrimeType("سرقة"); c1.setSentence("سنة"); crimes.add(c1);
        Crime c2 = new Crime(); c2.setCrimeId(crimeIdCounter++); c2.setCrimeType("احتيال"); c2.setSentence("6 أشهر"); crimes.add(c2);

        Prisoner p1 = new Prisoner(); p1.setId(personIdCounter++); p1.setFullName("أحمد علي"); p1.setAge(30); p1.setNationality("مصري"); p1.setDepartment(d1); p1.setCrime(c1); prisoners.add(p1);
        Prisoner p2 = new Prisoner(); p2.setId(personIdCounter++); p2.setFullName("محمد سمير"); p2.setAge(40); p2.setNationality("سوداني"); p2.setDepartment(d2); p2.setCrime(c2); prisoners.add(p2);

        Staff s1 = new Staff(); s1.setId(personIdCounter++); s1.setFullName("خالد حسن"); s1.setAge(35); s1.setJobTitle("حارس"); s1.setRank("A"); s1.setDepartment(d1); staff.add(s1);

        Room r1 = new Room(); r1.setRoomId(roomIdCounter++); r1.setDepartment(d1); r1.setTypeOfRoom("عادي"); r1.setCapacity(10); rooms.add(r1);

        Visitor v1 = new Visitor(); v1.setId(personIdCounter++); v1.setFullName("سامي نور"); v1.setAge(28); v1.setIdCard(123456); v1.setRelationship("أخ"); visitors.add(v1);

        Visit visit = new Visit(); visit.setVisitId(visitIdCounter++); visit.setPrisoner(p1); visit.setVisitor(v1); visit.setVisitDate(new Date()); visit.setStatus("مجدولة"); visits.add(visit);

        MedicalRecord mr = new MedicalRecord(); mr.setMedicalRecordId(medicalRecordIdCounter++); mr.setPrisoner(p1); mr.setConditions("طبيعية"); mr.setMedication("لا شيء"); mr.setRecordDate(new Date()); records.add(mr);

        Incident inc = new Incident(); inc.setIncidentId(incidentIdCounter++); inc.setDescription("شجار بسيط"); inc.setDate(new Date()); inc.setSeverity("منخفضة"); incidents.add(inc);
    }

    public int getCount(String tableName) {
        switch (tableName.toLowerCase()) {
            case "prisoner": return prisoners.size();
            case "staff": return staff.size();
            case "room": return rooms.size();
            case "crime": return crimes.size();
            default: return 0;
        }
    }

    public CustomLinkedList<?> getAll(String type) {
        switch (type) {
            case "Prisoner": return prisoners;
            case "Staff": return staff;
            case "Department": return departments;
            case "Crime": return crimes;
            case "Room": return rooms;
            case "Visitor": return visitors;
            case "Visit": return visits;
            case "MedicalRecord": return records;
            case "Incident": return incidents;
            default: return new CustomLinkedList<>();
        }
    }

    public CustomLinkedList<Department> getAllDepartments() { return departments; }
    public CustomLinkedList<Crime> getAllCrimes() { return crimes; }
    public CustomLinkedList<Prisoner> getAllPrisoners() { return prisoners; }
    public CustomLinkedList<Staff> getAllStaff() { return staff; }
    public CustomLinkedList<Room> getAllRooms() { return rooms; }
    public CustomLinkedList<Visitor> getAllVisitors() { return visitors; }
    public CustomLinkedList<Visit> getAllVisits() { return visits; }
    public CustomLinkedList<MedicalRecord> getAllMedicalRecords() { return records; }
    public CustomLinkedList<Incident> getAllIncidents() { return incidents; }

    // Add operations modify in-memory lists
    public void addPrisoner(Prisoner p) throws SQLException { p.setId(personIdCounter++); prisoners.add(p); }
    public void addStaff(Staff s) throws SQLException { s.setId(personIdCounter++); staff.add(s); }
    public void addDepartment(String name, int capacity) throws SQLException { Department d = new Department(); d.setDepartmentId(deptIdCounter++); d.setName(name); d.setCapacity(capacity); departments.add(d); }
    public void addRoom(int departmentId, String type, int capacity) throws SQLException { Room r = new Room(); r.setRoomId(roomIdCounter++); // find dept
        for (Department d : departments) { if (d.getDepartmentId() == departmentId) { r.setDepartment(d); break; } }
        r.setTypeOfRoom(type); r.setCapacity(capacity); rooms.add(r); }
    public void addCrime(String type, String sentence) throws SQLException { Crime c = new Crime(); c.setCrimeId(crimeIdCounter++); c.setCrimeType(type); c.setSentence(sentence); crimes.add(c); }
    public void addVisitor(Visitor v) throws SQLException { v.setId(personIdCounter++); visitors.add(v); }
    public void addVisit(Visit v) throws SQLException { v.setVisitId(visitIdCounter++); visits.add(v); }
    public void addMedicalRecord(MedicalRecord r) throws SQLException { r.setMedicalRecordId(medicalRecordIdCounter++); records.add(r); }
    public void addIncident(Incident i) throws SQLException { i.setIncidentId(incidentIdCounter++); incidents.add(i); }

    public void updateDepartment(Department d) throws SQLException { for (Department x : departments) { if (x.getDepartmentId() == d.getDepartmentId()) { x.setName(d.getName()); x.setCapacity(d.getCapacity()); return; } } }
    public void updateCrime(Crime c) throws SQLException { for (Crime x : crimes) { if (x.getCrimeId() == c.getCrimeId()) { x.setCrimeType(c.getCrimeType()); x.setSentence(c.getSentence()); return; } } }
    public void updatePrisoner(Prisoner p) throws SQLException { for (Prisoner x : prisoners) { if (x.getId() == p.getId()) { x.setFullName(p.getFullName()); x.setAge(p.getAge()); x.setNationality(p.getNationality()); x.setDepartment(p.getDepartment()); x.setCrime(p.getCrime()); return; } } }
    public void updateStaff(Staff s) throws SQLException { for (Staff x : staff) { if (x.getId() == s.getId()) { x.setFullName(s.getFullName()); x.setAge(s.getAge()); x.setJobTitle(s.getJobTitle()); x.setRank(s.getRank()); x.setDepartment(s.getDepartment()); return; } } }
    public void updateRoom(Room r) throws SQLException { for (Room x : rooms) { if (x.getRoomId() == r.getRoomId()) { x.setDepartment(r.getDepartment()); x.setCapacity(r.getCapacity()); x.setTypeOfRoom(r.getTypeOfRoom()); return; } } }
    public void updateVisitor(Visitor v) throws SQLException { for (Visitor x : visitors) { if (x.getId() == v.getId()) { x.setFullName(v.getFullName()); x.setAge(v.getAge()); x.setIdCard(v.getIdCard()); x.setRelationship(v.getRelationship()); return; } } }

    public void delete(String type, int id) throws SQLException {
        switch(type.toLowerCase()) {
            case "prisoner": for (int i=0;i<prisoners.size();i++) { if (prisoners.get(i).getId()==id) { /* no remove method; rebuild list */ } } break;
            default: break;
        }
    }
}
