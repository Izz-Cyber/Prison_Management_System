package DB;

import Classes.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DBManager implements DBManagerInterface {

    // ... (دوال الإضافة وجلب العدد تبقى كما هي) ...
    
public void addPrisoner(Prisoner prisoner) throws SQLException {

    int personId = 0;

    String sqlPerson = "INSERT INTO person (fullName, age) VALUES (?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmtPerson = conn.prepareStatement(sqlPerson, Statement.RETURN_GENERATED_KEYS)) {

        pstmtPerson.setString(1, prisoner.getFullName());
        pstmtPerson.setInt(2, prisoner.getAge());

        int affectedRows = pstmtPerson.executeUpdate();

        if (affectedRows > 0) {
            try (ResultSet generatedKeys = pstmtPerson.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personId = generatedKeys.getInt(1); 
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("فشل في إضافة الشخص الأساسي إلى جدول person");
        e.printStackTrace();
        throw e; 
    }

    
    if (personId == 0) {
        throw new SQLException("فشلت عملية إنشاء الشخص الأساسي، لم يتم الحصول على ID.");
    }

    
    String sqlPrisoner = "INSERT INTO prisoner (id, nationality, crimeId, departmentId) VALUES (?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmtPrisoner = conn.prepareStatement(sqlPrisoner)) {

        pstmtPrisoner.setInt(1, personId); 
        pstmtPrisoner.setString(2, prisoner.getNationality());
        pstmtPrisoner.setInt(3, prisoner.getCrime().getCrimeId()); 
        pstmtPrisoner.setInt(4, prisoner.getDepartment().getDepartmentId());

        pstmtPrisoner.executeUpdate();

        System.out.println("تمت إضافة السجين بنجاح بالـ ID: " + personId);

    } catch (SQLException e) {
        System.err.println("فشل في إضافة السجين إلى جدول prisoner");
        e.printStackTrace();
        
        throw e; 
    }
}



    public void addStaff(Staff staff) throws SQLException {
    int personId = 0;

    // --- الخطوة 1 و 2: إضافة الشخص في جدول 'person' والحصول على الـ ID ---
    String sqlPerson = "INSERT INTO person (fullName, age) VALUES (?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmtPerson = conn.prepareStatement(sqlPerson, Statement.RETURN_GENERATED_KEYS)) {

        pstmtPerson.setString(1, staff.getFullName());
        pstmtPerson.setInt(2, staff.getAge());
        int affectedRows = pstmtPerson.executeUpdate();

        if (affectedRows > 0) {
            try (ResultSet generatedKeys = pstmtPerson.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personId = generatedKeys.getInt(1);
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("فشل في إضافة الشخص الأساسي للموظف.");
        e.printStackTrace();
        throw e;
    }

    if (personId == 0) {
        throw new SQLException("فشلت عملية إنشاء الشخص الأساسي للموظف، لم يتم الحصول على ID.");
    }

    // --- الخطوة 3: إضافة بيانات الموظف في جدول 'staff' ---
    String sqlStaff = "INSERT INTO staff (id, jobTitle, rank, shift, departmentId, role) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmtStaff = conn.prepareStatement(sqlStaff)) {

        pstmtStaff.setInt(1, personId); // استخدام الـ ID الذي تم إنشاؤه
        pstmtStaff.setString(2, staff.getJobTitle());
        pstmtStaff.setString(3, staff.getRank());
        pstmtStaff.setString(4, staff.getShift());
        pstmtStaff.setInt(5, staff.getDepartment().getDepartmentId()); // افتراض وجود كائن Department
        pstmtStaff.setString(6, staff.getRole());

        pstmtStaff.executeUpdate();
        System.out.println("تمت إضافة الموظف بنجاح بالـ ID: " + personId);

    } catch (SQLException e) {
        System.err.println("فشل في إضافة بيانات الموظف.");
        e.printStackTrace();
        // (اختياري لكن موصى به): حذف الـ person الذي تم إنشاؤه للتو
        throw e;
    }

    }

    public void addDepartment(String name, int capacity) throws SQLException {
        String sql = "INSERT INTO department (name, capacity) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, capacity);
            ps.executeUpdate();
        }
    }

    public void addRoom(int departmentId, String type, int capacity) throws SQLException {
        String sql = "INSERT INTO room (departmentId, typeOfRoom, capacity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ps.setString(2, type);
            ps.setInt(3, capacity);
            ps.executeUpdate();
        }
    }

    public void addCrime(String type, String sentence) throws SQLException {
        String sql = "INSERT INTO crime (crimeType, sentence) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setString(2, sentence);
            ps.executeUpdate();
        }
    }
    
    // انسخ هذه الدالة وضعها في كلاس DBManager.java

// =======================================================================
//  دالة إضافة زائر جديد (ADD VISITOR) - نسخة محدثة لـ BIGINT/long
// =======================================================================
public void addVisitor(Visitor visitor) throws SQLException {
    int personId = 0;

    // --- الخطوة 1: إضافة السجل في جدول 'person' ---
    String sqlPerson = "INSERT INTO person (fullName, age) VALUES (?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmtPerson = conn.prepareStatement(sqlPerson, Statement.RETURN_GENERATED_KEYS)) {

        pstmtPerson.setString(1, visitor.getFullName());
        pstmtPerson.setInt(2, visitor.getAge());
        int affectedRows = pstmtPerson.executeUpdate();

        // --- الخطوة 2: الحصول على الـ ID الذي تم إنشاؤه تلقائياً ---
        if (affectedRows > 0) {
            try (ResultSet generatedKeys = pstmtPerson.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personId = generatedKeys.getInt(1);
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("!!! خطأ في الخطوة 1: فشل في إضافة الشخص الأساسي للزائر.");
        e.printStackTrace();
        throw e;
    }

    if (personId == 0) {
        throw new SQLException("فشلت عملية إنشاء الشخص الأساسي للزائر، لم يتم الحصول على ID.");
    }

    // --- الخطوة 3: إضافة بيانات الزائر في جدول 'visitor' ---
    String sqlVisitor = "INSERT INTO visitor (id, idCard, relationship) VALUES (?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmtVisitor = conn.prepareStatement(sqlVisitor)) {

        pstmtVisitor.setInt(1, personId);

        // ***** التعديل الأساسي هنا *****
        // استخدام setLong ليتوافق مع نوع البيانات BIGINT في قاعدة البيانات و long في جافا
        pstmtVisitor.setLong(2, visitor.getIdCard());

        pstmtVisitor.setString(3, visitor.getRelationship());

        pstmtVisitor.executeUpdate();
        System.out.println(">> نجاح: تمت إضافة الزائر بالكامل بالـ ID رقم: " + personId);

    } catch (SQLException e) {
        System.err.println("!!! خطأ في الخطوة 3: فشل في إضافة بيانات الزائر.");
        e.printStackTrace();
        throw e;
    }
}

    public void addVisit(Visit visit) throws SQLException {
        String sql = "INSERT INTO visit (prisonerId, visitorId, visitDate, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, visit.getPrisoner().getId());
            ps.setInt(2, visit.getVisitor().getId());
            ps.setDate(3, new java.sql.Date(visit.getVisitDate().getTime()));
            ps.setString(4, visit.getStatus());
            ps.executeUpdate();
        }
    }

    public void addMedicalRecord(MedicalRecord record) throws SQLException {
        String sql = "INSERT INTO medical_record (prisonerId, conditions, medication, recordDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, record.getPrisoner().getId());
            ps.setString(2, record.getConditions());
            ps.setString(3, record.getMedication());
            ps.setDate(4, new java.sql.Date(record.getRecordDate().getTime()));
            ps.executeUpdate();
        }
    }

    public void addIncident(Incident incident) throws SQLException {
        String sql = "INSERT INTO incident (description, incidentDate, severity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, incident.getDescription());
            ps.setDate(2, new java.sql.Date(incident.getDate().getTime()));
            ps.setString(3, incident.getSeverity());
            ps.executeUpdate();
        }
    }

    // ==================================================================
    // دوال التعديل (UPDATE) - مكتملة الآن
    // ==================================================================
    public void updateDepartment(Department dept) throws SQLException {
        String sql = "UPDATE department SET name = ?, capacity = ? WHERE departmentId = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept.getName());
            ps.setInt(2, dept.getCapacity());
            ps.setInt(3, dept.getDepartmentId());
            ps.executeUpdate();
        }
    }
    
    public void updateCrime(Crime crime) throws SQLException {
        String sql = "UPDATE crime SET crimeType = ?, sentence = ? WHERE crimeId = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, crime.getCrimeType());
            ps.setString(2, crime.getSentence());
            ps.setInt(3, crime.getCrimeId());
            ps.executeUpdate();
        }
    }
    
    public void updatePrisoner(Prisoner prisoner) throws SQLException {
    // الاستعلام الأول: تحديث الجدول الأب 'person'
    String sqlPerson = "UPDATE person SET fullName = ?, age = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sqlPerson)) {

        pstmt.setString(1, prisoner.getFullName());
        pstmt.setInt(2, prisoner.getAge());
        pstmt.setInt(3, prisoner.getId()); // شرط WHERE
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.err.println("فشل في تحديث بيانات الشخص الأساسية.");
        e.printStackTrace();
        throw e;
    }

    // الاستعلام الثاني: تحديث الجدول الابن 'prisoner'
    String sqlPrisoner = "UPDATE prisoner SET nationality = ?, departmentId = ?, crimeId = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sqlPrisoner)) {

        pstmt.setString(1, prisoner.getNationality());
        pstmt.setInt(2, prisoner.getDepartment().getDepartmentId()); // تأكد من الحصول على الـ ID الصحيح من واجهة المستخدم
        pstmt.setInt(3, prisoner.getCrime().getCrimeId()); // تأكد من الحصول على الـ ID الصحيح من واجهة المستخدم
        pstmt.setInt(4, prisoner.getId()); // شرط WHERE
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.err.println("فشل في تحديث بيانات السجين.");
        e.printStackTrace();
        throw e;
    }

    System.out.println("تم تحديث بيانات السجين بنجاح.");
    // من الأفضل استخدام Transactions هنا لضمان تنفيذ كلا التحديثين معاً
}
    
    public void updateStaff(Staff staff) throws SQLException {
    // الاستعلام الأول: تحديث 'person'
    String sqlPerson = "UPDATE person SET fullName = ?, age = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sqlPerson)) {
        pstmt.setString(1, staff.getFullName());
        pstmt.setInt(2, staff.getAge());
        pstmt.setInt(3, staff.getId());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.err.println("فشل في تحديث البيانات الأساسية للموظف.");
        e.printStackTrace();
        throw e;
    }

    // الاستعلام الثاني: تحديث 'staff'
    String sqlStaff = "UPDATE staff SET jobTitle = ?, rank = ?, shift = ?, departmentId = ?, role = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sqlStaff)) {
        pstmt.setString(1, staff.getJobTitle());
        pstmt.setString(2, staff.getRank());
        pstmt.setString(3, staff.getShift());
        pstmt.setInt(4, staff.getDepartment().getDepartmentId());
        pstmt.setString(5, staff.getRole());
        pstmt.setInt(6, staff.getId());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.err.println("فشل في تحديث بيانات وظيفة الموظف.");
        e.printStackTrace();
        throw e;
    }
    System.out.println("تم تحديث بيانات الموظف بنجاح.");
}
    
    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE room SET departmentId = ?, typeOfRoom = ?, capacity = ? WHERE roomId = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, room.getDepartment().getDepartmentId());
            ps.setString(2, room.getTypeOfRoom());
            ps.setInt(3, room.getCapacity());
            ps.setInt(4, room.getRoomId());
            ps.executeUpdate();
        }
    }
    
    // انسخ هذه الدالة وضعها في كلاس DBManager.java

// =======================================================================
//  دالة تعديل بيانات زائر (UPDATE VISITOR) - نسخة محدثة لـ BIGINT/long
// =======================================================================
public void updateVisitor(Visitor visitor) throws SQLException {

    // --- الاستعلام الأول: تحديث الجدول الأب 'person' (الاسم والعمر) ---
    String sqlPerson = "UPDATE person SET fullName = ?, age = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sqlPerson)) {

        pstmt.setString(1, visitor.getFullName());
        pstmt.setInt(2, visitor.getAge());
        pstmt.setInt(3, visitor.getId()); // شرط WHERE لتحديد الشخص الصحيح
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.err.println("!!! فشل في تحديث البيانات الأساسية للزائر (جدول person).");
        e.printStackTrace();
        throw e; // رمي الخطأ لإعلام واجهة المستخدم
    }

    // --- الاستعلام الثاني: تحديث الجدول الابن 'visitor' (رقم الهوية والقرابة) ---
    String sqlVisitor = "UPDATE visitor SET idCard = ?, relationship = ? WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sqlVisitor)) {

        // ***** التعديل الأساسي هنا *****
        // استخدام setLong ليتوافق مع نوع البيانات BIGINT
        pstmt.setLong(1, visitor.getIdCard());
        
        pstmt.setString(2, visitor.getRelationship());
        pstmt.setInt(3, visitor.getId()); // شرط WHERE لتحديد الزائر الصحيح
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.err.println("!!! فشل في تحديث بيانات الزائر (جدول visitor).");
        e.printStackTrace();
        throw e; // رمي الخطأ لإعلام واجهة المستخدم
    }

    System.out.println(">> نجاح: تم تحديث بيانات الزائر بالكامل بنجاح.");
    // ملاحظة: من الأفضل استخدام Transactions هنا لضمان تنفيذ كلا التحديثين معاً أو فشلهما معاً
}


    // ==================================================================
    // دوال جلب كل البيانات (GET ALL) - مع تصحيح السجلات الطبية
    // ==================================================================
    public CustomLinkedList<?> getAll(String type) throws SQLException {
        switch (type) {
            case "Prisoner": return getAllPrisoners();
            case "Staff": return getAllStaff();
            case "Department": return getAllDepartments();
            case "Crime": return getAllCrimes();
            case "Room": return getAllRooms();
            case "Visitor": return getAllVisitors();
            case "Visit": return getAllVisits();
            case "MedicalRecord": return getAllMedicalRecords();
            case "Incident": return getAllIncidents();
            default: return new CustomLinkedList<>();
        }
    }

    public CustomLinkedList<Prisoner> getAllPrisoners() throws SQLException {
    CustomLinkedList<Prisoner> prisoners = new CustomLinkedList<>();
    // جملة SQL الجديدة مع JOINs متعددة
    String sql = "SELECT p.id, p.fullName, p.age, pr.nationality, " +
                 "d.name AS departmentName, c.crimeType AS crimeType " +
                 "FROM person p " +
                 "JOIN prisoner pr ON p.id = pr.id " +
                 "LEFT JOIN department d ON pr.departmentId = d.departmentId " +
                 "LEFT JOIN crime c ON pr.crimeId = c.crimeId";

    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Prisoner prisoner = new Prisoner();
            prisoner.setId(rs.getInt("id"));
            prisoner.setFullName(rs.getString("fullName"));
            prisoner.setAge(rs.getInt("age"));
            prisoner.setNationality(rs.getString("nationality"));

            // إنشاء كائنات كاملة الآن
            Department dept = new Department();
            dept.setName(rs.getString("departmentName")); // <-- التعديل هنا
            prisoner.setDepartment(dept);

            Crime crime = new Crime();
            crime.setCrimeType(rs.getString("crimeType")); // <-- التعديل هنا
            prisoner.setCrime(crime);

            prisoners.add(prisoner);
        }
    } // ... (بقية الكود كما هو)
    return prisoners;
}
    
    public CustomLinkedList<Staff> getAllStaff() throws SQLException {
    CustomLinkedList<Staff> staffList = new CustomLinkedList<>();
    String sql = "SELECT p.id, p.fullName, p.age, s.jobTitle, s.rank, s.shift, s.role, d.name AS departmentName " +
                 "FROM person p " +
                 "JOIN staff s ON p.id = s.id " +
                 "LEFT JOIN department d ON s.departmentId = d.departmentId";

    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Staff staff = new Staff();
            staff.setId(rs.getInt("id"));
            staff.setFullName(rs.getString("fullName"));
            staff.setAge(rs.getInt("age"));
            staff.setJobTitle(rs.getString("jobTitle"));
            staff.setRank(rs.getString("rank"));
            staff.setShift(rs.getString("shift"));
            staff.setRole(rs.getString("role"));

            Department dept = new Department();
            dept.setName(rs.getString("departmentName"));
            staff.setDepartment(dept);

            staffList.add(staff);
        }
    } catch (SQLException e) {
        System.err.println("خطأ في جلب قائمة الموظفين!");
        e.printStackTrace();
        throw e;
    }
    return staffList;
}
    
    public CustomLinkedList<Department> getAllDepartments() throws SQLException {
        CustomLinkedList<Department> list = new CustomLinkedList<>();
        String sql = "SELECT * FROM department ORDER BY name";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Department d = new Department();
                d.setDepartmentId(rs.getInt("departmentId"));
                d.setName(rs.getString("name"));
                d.setCapacity(rs.getInt("capacity"));
                list.add(d);
            }
        }
        return list;
    }
    
    public CustomLinkedList<Crime> getAllCrimes() throws SQLException {
        CustomLinkedList<Crime> list = new CustomLinkedList<>();
        String sql = "SELECT * FROM crime ORDER BY crimeType";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Crime c = new Crime();
                c.setCrimeId(rs.getInt("crimeId"));
                c.setCrimeType(rs.getString("crimeType"));
                c.setSentence(rs.getString("sentence"));
                list.add(c);
            }
        }
        return list;
    }
    
    public CustomLinkedList<Room> getAllRooms() throws SQLException {
        CustomLinkedList<Room> list = new CustomLinkedList<>();
        String sql = "SELECT r.*, d.name as departmentName FROM room r LEFT JOIN department d ON r.departmentId = d.departmentId";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Room r = new Room();
                r.setRoomId(rs.getInt("roomId"));
                r.setTypeOfRoom(rs.getString("typeOfRoom"));
                r.setCapacity(rs.getInt("capacity"));
                
                Department dept = new Department();
                dept.setDepartmentId(rs.getInt("departmentId"));
                dept.setName(rs.getString("departmentName"));
                r.setDepartment(dept);
                
                list.add(r);
            }
        }
        return list;
    }
    
    // انسخ هذه الدالة وضعها في كلاس DBManager.java

// =======================================================================
//  دالة جلب كل الزوار (GET ALL VISITORS) - نسخة محدثة لـ BIGINT/long
// =======================================================================
public CustomLinkedList<Visitor> getAllVisitors() throws SQLException {
    // إنشاء قائمة فارغة لتخزين كائنات الزوار
    CustomLinkedList<Visitor> visitorList = new CustomLinkedList<>();

    // استعلام SQL الذي يدمج البيانات من جدولي person و visitor
    String sql = "SELECT p.id, p.fullName, p.age, v.idCard, v.relationship " +
                 "FROM person p " +
                 "JOIN visitor v ON p.id = v.id";

    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        // المرور على كل صف في النتائج
        while (rs.next()) {
            // إنشاء كائن Visitor جديد لكل صف
            Visitor visitor = new Visitor();

            // قراءة البيانات من الصف الحالي وتعيينها للكائن
            visitor.setId(rs.getInt("id"));
            visitor.setFullName(rs.getString("fullName"));
            visitor.setAge(rs.getInt("age"));

            // ***** التعديل الأساسي هنا *****
            // استخدام getLong() لقراءة قيمة BIGINT من قاعدة البيانات
            visitor.setIdCard((int) rs.getLong("idCard"));

            visitor.setRelationship(rs.getString("relationship"));

            // إضافة الكائن المكتمل إلى القائمة
            visitorList.add(visitor);
        }
    } catch (SQLException e) {
        System.err.println("!!! خطأ في جلب قائمة الزوار من قاعدة البيانات!");
        e.printStackTrace();
        throw e;
    }

    // إرجاع القائمة التي تحتوي على جميع الزوار
    return visitorList;
}
    
    public CustomLinkedList<Visit> getAllVisits() throws SQLException {
        CustomLinkedList<Visit> list = new CustomLinkedList<>();
        String sql = "SELECT v.*, p.fullName as prisonerName, vis.fullName as visitorName " +
                     "FROM visit v " +
                     "JOIN prisoner p ON v.prisonerId = p.id " +
                     "JOIN visitor vis ON v.visitorId = vis.id";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Visit v = new Visit();
                v.setVisitId(rs.getInt("visitId"));
                
                Prisoner p = new Prisoner();
                p.setId(rs.getInt("prisonerId"));
                p.setFullName(rs.getString("prisonerName"));
                v.setPrisoner(p);
                
                Visitor visitor = new Visitor();
                visitor.setId(rs.getInt("visitorId"));
                visitor.setFullName(rs.getString("visitorName"));
                v.setVisitor(visitor);
                
                v.setVisitDate(rs.getDate("visitDate"));
                v.setStatus(rs.getString("status"));
                list.add(v);
            }
        }
        return list;
    }

    // انسخ هذه الدالة وضعها في DBManager.java

// =======================================================================
//  دالة جلب كل السجلات الطبية (GET ALL MEDICAL RECORDS)
// =======================================================================
public CustomLinkedList<MedicalRecord> getAllMedicalRecords() throws SQLException {
    CustomLinkedList<MedicalRecord> records = new CustomLinkedList<>();

    // جملة SQL التي تربط 3 جداول: medical_record -> prisoner -> person
    String sql = "SELECT " +
                 "  mr.medicalRecordId, " +
                 "  mr.conditions, " +
                 "  mr.medication, " +
                 "  mr.recordDate, " +
                 "  p.fullName AS prisonerName, " + // جلب اسم السجين
                 "  p.id AS prisonerId " +          // جلب ID السجين
                 "FROM " +
                 "  medical_record mr " +
                 "JOIN " +
                 "  prisoner pr ON mr.prisonerId = pr.id " +
                 "JOIN " +
                 "  person p ON pr.id = p.id";

    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            // إنشاء كائن للسجل الطبي
            MedicalRecord record = new MedicalRecord();
            record.setMedicalRecordId(rs.getInt("medicalRecordId"));
            record.setConditions(rs.getString("conditions"));
            record.setMedication(rs.getString("medication"));
            record.setRecordDate(rs.getDate("recordDate"));

            // إنشاء كائن سجين وهمي (dummy) لتخزين الاسم والرقم
            // هذا يتطلب أن كلاس MedicalRecord يحتوي على كائن Prisoner
            Prisoner prisoner = new Prisoner();
            prisoner.setId(rs.getInt("prisonerId"));
            prisoner.setFullName(rs.getString("prisonerName"));

            // تعيين كائن السجين للسجل الطبي
            record.setPrisoner(prisoner); // افترض أن لديك دالة setPrisoner(Prisoner p)

            records.add(record);
        }
    } catch (SQLException e) {
        System.err.println("!!! خطأ في جلب السجلات الطبية!");
        e.printStackTrace();
        throw e;
    }

    return records;
}
    
    public CustomLinkedList<Incident> getAllIncidents() throws SQLException {
        CustomLinkedList<Incident> list = new CustomLinkedList<>();
        String sql = "SELECT * FROM incident";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Incident i = new Incident();
                i.setIncidentId(rs.getInt("incidentId"));
                i.setDescription(rs.getString("description"));
                i.setDate(rs.getDate("incidentDate"));
                i.setSeverity(rs.getString("severity"));
                list.add(i);
            }
        }
        return list;
    }

    // ==================================================================
    // دالة الحذف (DELETE) - مع معالجة الأخطاء
    // ==================================================================
    public void delete(String type, int id) throws SQLException {
        String tableName = type.toLowerCase();
        String primaryKeyName = "";

        // تحديد اسم المفتاح الأساسي لكل جدول
        switch(tableName) {
            case "prisoner":
            case "staff":
            case "visitor":
                primaryKeyName = "id";
                break;
            default:
                primaryKeyName = tableName + "Id";
                break;
        }

        String sql = "DELETE FROM " + tableName + " WHERE " + primaryKeyName + " = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "تم الحذف بنجاح.", "نجاح", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLIntegrityConstraintViolationException e) {
            // عرض رسالة خطأ واضحة للمستخدم
            showError("لا يمكن حذف هذا السجل لوجود سجلات أخرى مرتبطة به.\n" +
                      "مثال: لا يمكن حذف قسم يحتوي على سجناء، أو جريمة مسجلة على سجين.", null);
        } catch (SQLException e) {
            showError("حدث خطأ أثناء الحذف.", e);
            throw e;
        }
    }
    
    // ==================================================================
    // دوال مساعدة
    // ==================================================================
    public int getCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName.toLowerCase();
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            showError("خطأ في جلب العدد من جدول " + tableName, e);
        }
        return 0;
    }

    private void showError(String message, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, message, "خطأ", JOptionPane.ERROR_MESSAGE);
    }
}