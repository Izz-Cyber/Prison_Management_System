/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// src/com/yourprojectname/model/Incident.java

import java.util.Date;

public class Incident {
    private int incidentId;
    private int relatedId; // يمكن أن يكون ID لسجين، موظف، أو زائر
    private String description;
    private Date date;
    private String severity; // مستوى الخطورة (e.g., Low, Medium, High)

    public Incident() {
        // Default constructor
    }

    public Incident(int incidentId, int relatedId, String description, Date date, String severity) {
        this.incidentId = incidentId;
        this.relatedId = relatedId;
        this.description = description;
        this.date = date;
        this.severity = severity;
    }

    // Getters and Setters
    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "Incident{" +
               "incidentId=" + incidentId +
               ", relatedId=" + relatedId +
               ", description='" + description + '\'' +
               ", date=" + date +
               ", severity='" + severity + '\'' +
               '}';
    }
}