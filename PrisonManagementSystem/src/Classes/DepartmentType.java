/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// DepartmentType.java

public class DepartmentType {
    private int departmentTypeId;
    private String name;
    private String description;
    private String rules;

    public DepartmentType() {
        // Default constructor
    }

    public DepartmentType(int departmentTypeId, String name, String description, String rules) {
        this.departmentTypeId = departmentTypeId;
        this.name = name;
        this.description = description;
        this.rules = rules;
    }

    // Getters and Setters
    public int getDepartmentTypeId() {
        return departmentTypeId;
    }

    public void setDepartmentTypeId(int departmentTypeId) {
        this.departmentTypeId = departmentTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "DepartmentType{" +
               "departmentTypeId=" + departmentTypeId +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", rules='" + rules + '\'' +
               '}';
    }
}