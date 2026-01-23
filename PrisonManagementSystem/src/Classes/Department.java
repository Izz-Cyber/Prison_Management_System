/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// Department.java

import java.util.List; // لاستخدام قائمة الغرف والموظفين

public class Department {
    private int departmentId;
    private String name;
    private int capacity;
    private DepartmentType departmentType; // علاقة مع DepartmentType
    // يمكنك إضافة قوائم للغرف والموظفين هنا إذا أردت تمثيل العلاقات One-to-Many في Java
    // private List<Room> rooms;
    // private List<Staff> staffMembers;

    public Department() {
        // Default constructor
    }

    public Department(int departmentId, String name, int capacity, DepartmentType departmentType) {
        this.departmentId = departmentId;
        this.name = name;
        this.capacity = capacity;
        this.departmentType = departmentType;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public DepartmentType getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(DepartmentType departmentType) {
        this.departmentType = departmentType;
    }
@Override
public String toString() {
    return this.name; // فقط الاسم
}
}