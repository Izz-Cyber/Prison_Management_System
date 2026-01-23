package Classes;

import java.util.Date;

public class MedicalRecord {

    private int medicalRecordId;
    private Prisoner prisoner; // <-- إضافة العلاقة الناقصة
    private String conditions;
    private String medication;
    private Date recordDate;

    public MedicalRecord() {
    }

    public MedicalRecord(int medicalRecordId, Prisoner prisoner, String conditions, String medication, Date recordDate) {
        this.medicalRecordId = medicalRecordId;
        this.prisoner = prisoner;
        this.conditions = conditions;
        this.medication = medication;
        this.recordDate = recordDate;
    }

    // --- Getters and Setters ---
    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public Prisoner getPrisoner() {
        return prisoner;
    } // <-- إضافة Getter

    public void setPrisoner(Prisoner prisoner) {
        this.prisoner = prisoner;
    } // <-- إضافة Setter

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return "سجل رقم " + medicalRecordId;
    }
}
