package Classes;

public class Prisoner extends Person {

    private int prisonerId;
    private String nationality;
    private Crime crime;
    private MedicalRecord medicalRecord;
    private Department department;

    public Prisoner() {
        super();
    }

    // ... (باقي الكود كما هو) ...
    public int getPrisonerId() {
        return prisonerId;
    }

    public void setPrisonerId(int prisonerId) {
        this.prisonerId = prisonerId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Crime getCrime() {
        return crime;
    }

    public void setCrime(Crime crime) {
        this.crime = crime;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return getFullName(); // <-- التصحيح هنا: يعيد الاسم فقط من الكلاس الأب
    }
}
