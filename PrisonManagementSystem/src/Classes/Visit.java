/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// Visit.java

import java.util.Date;

public class Visit {
    private int visitId;
    private Prisoner prisoner; // علاقة مع Prisoner
    private Visitor visitor; // علاقة مع Visitor
    private Date visitDate;
    private String status;

    public Visit() {
        // Default constructor
    }

    public Visit(int visitId, Prisoner prisoner, Visitor visitor, Date visitDate, String status) {
        this.visitId = visitId;
        this.prisoner = prisoner;
        this.visitor = visitor;
        this.visitDate = visitDate;
        this.status = status;
    }

    // Getters and Setters
    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public Prisoner getPrisoner() {
        return prisoner;
    }

    public void setPrisoner(Prisoner prisoner) {
        this.prisoner = prisoner;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Visit{" +
               "visitId=" + visitId +
               ", prisonerName=" + (prisoner != null ? prisoner.getFullName() : "N/A") +
               ", visitorName=" + (visitor != null ? visitor.getFullName() : "N/A") +
               ", visitDate=" + visitDate +
               ", status='" + status + '\'' +
               '}';
    }
}