/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// Room.java

import java.util.List; // لاستخدام قائمة السجناء

public class Room {
    private int roomId;
    private Department department; // علاقة مع Department
    private String typeOfRoom;
    private int capacity;
    // يمكنك إضافة قائمة بالسجناء هنا إذا أردت تمثيل العلاقة Many-to-Many أو One-to-Many
    // private List<Prisoner> prisoners;

    public Room() {
        // Default constructor
    }

    public Room(int roomId, Department department, String typeOfRoom, int capacity) {
        this.roomId = roomId;
        this.department = department;
        this.typeOfRoom = typeOfRoom;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getTypeOfRoom() {
        return typeOfRoom;
    }

    public void setTypeOfRoom(String typeOfRoom) {
        this.typeOfRoom = typeOfRoom;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
@Override
public String toString() {
    return "غرفة رقم " + this.roomId + " (النوع: " + this.typeOfRoom + ")"; // نص وصفي
}
}