package Main;

import GUI.MainDashboard;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // استخدام مظهر عصري للواجهة إن أمكن
      
        
        // تشغيل الواجهة الرئيسية
        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}