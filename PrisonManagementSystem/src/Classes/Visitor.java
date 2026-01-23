
package Classes;
// Visitor.java

public class Visitor extends Person {
    private int visitorId;
    private int idCard;
    private String relationship;

    public Visitor() {
        super();
    }

    public Visitor(int personId, String fullName, int age, int visitorId, int idCard, String relationship) {
        super(personId, fullName, age);
        this.visitorId = visitorId;
        this.idCard = idCard;
        this.relationship = relationship;
    }

    // Getters and Setters
    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return getFullName();
    }
}