package DB;

import Classes.*;
import java.sql.SQLException;

public interface DBManagerInterface {
    int getCount(String tableName);
    CustomLinkedList<?> getAll(String type) throws SQLException;
    CustomLinkedList<Prisoner> getAllPrisoners() throws SQLException;
    CustomLinkedList<Staff> getAllStaff() throws SQLException;
    CustomLinkedList<Department> getAllDepartments() throws SQLException;
    CustomLinkedList<Crime> getAllCrimes() throws SQLException;
    CustomLinkedList<Room> getAllRooms() throws SQLException;
    CustomLinkedList<Visitor> getAllVisitors() throws SQLException;
    CustomLinkedList<Visit> getAllVisits() throws SQLException;
    CustomLinkedList<MedicalRecord> getAllMedicalRecords() throws SQLException;
    CustomLinkedList<Incident> getAllIncidents() throws SQLException;

    void addPrisoner(Prisoner p) throws SQLException;
    void addStaff(Staff s) throws SQLException;
    void addDepartment(String name, int capacity) throws SQLException;
    void addRoom(int departmentId, String type, int capacity) throws SQLException;
    void addCrime(String type, String sentence) throws SQLException;
    void addVisitor(Visitor v) throws SQLException;
    void addVisit(Visit v) throws SQLException;
    void addMedicalRecord(MedicalRecord r) throws SQLException;
    void addIncident(Incident i) throws SQLException;

    void updateDepartment(Department d) throws SQLException;
    void updateCrime(Crime c) throws SQLException;
    void updatePrisoner(Prisoner p) throws SQLException;
    void updateStaff(Staff s) throws SQLException;
    void updateRoom(Room r) throws SQLException;
    void updateVisitor(Visitor v) throws SQLException;

    void delete(String type, int id) throws SQLException;
}
