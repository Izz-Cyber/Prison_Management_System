/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// Crime.java

public class Crime {
    private int crimeId;
    private String crimeType;
    private String sentence;

    public Crime() {
        // Default constructor
    }

    public Crime(int crimeId, String crimeType, String sentence) {
        this.crimeId = crimeId;
        this.crimeType = crimeType;
        this.sentence = sentence;
    }

    // Getters and Setters
    public int getCrimeId() {
        return crimeId;
    }

    public void setCrimeId(int crimeId) {
        this.crimeId = crimeId;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
@Override
public String toString() {
    return this.crimeType; // فقط نوع الجريمة
}
}