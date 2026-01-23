package GUI;

import DB.DBManager;
import Classes.*;
// Removed dependency on external JDateChooser to avoid compile-time library requirement
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
// import java.util.List; // تم استبدالها بـ CustomLinkedList
import java.util.Vector;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MainDashboard extends JFrame {

    private DB.DBManagerInterface dbManager;
    private JLabel prisonerCountLabel, staffCountLabel, roomCountLabel, crimeCountLabel;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // تعريف الألوان الجديدة للتصميم المظلم
    private final Color DARK_BACKGROUND = new Color(33, 37, 41); // #212529
    private final Color DARK_CARD_BACKGROUND = new Color(52, 58, 64); // #343a40
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color BORDER_COLOR = new Color(73, 80, 87); // #495057
    private final Color ACCENT_COLOR = new Color(0, 123, 255);

    public MainDashboard() {
        // جرب إجراء اتصال فعلي بقاعدة البيانات أولاً؛ هذا سيحمّل الدرايفر ديناميكياً إذا وُجد jar
        try {
            DB.DBConnection.getConnection().close();
            dbManager = new DBManager();
            setTitle("النظام المتكامل لإدارة السجون - Prison Management System (Connected)");
        } catch (Exception ex) {
            // تشغيل التطبيق في وضع عدم الاتصال باستخدام بيانات توضيحية
            dbManager = new DB.MockDBManager();
            setTitle("النظام المتكامل لإدارة السجون - Prison Management System (Demo Mode)");
            JOptionPane.showMessageDialog(this, "تعذّر الاتصال بقاعدة البيانات. التطبيق يعمل الآن في وضع العرض (demo).", "تنبيه", JOptionPane.INFORMATION_MESSAGE);
        }
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // تطبيق المظهر المظلم على كل مكونات Swing
        setupDarkTheme();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BACKGROUND);
        add(mainPanel);

        JMenuBar menuBar = createCustomMenuBar();
        mainPanel.add(menuBar, BorderLayout.NORTH);

        JPanel dashboardContent = new JPanel(new BorderLayout(20, 20));
        dashboardContent.setBackground(DARK_BACKGROUND);
        dashboardContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.add(dashboardContent, BorderLayout.CENTER);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        cardsPanel.setOpaque(false);

        prisonerCountLabel = new JLabel("0");
        staffCountLabel = new JLabel("0");
        roomCountLabel = new JLabel("0");
        crimeCountLabel = new JLabel("0");

        cardsPanel.add(createDashboardCard("السجناء", prisonerCountLabel, new Color(0, 123, 255)));
        cardsPanel.add(createDashboardCard("الموظفون", staffCountLabel, new Color(255, 193, 7)));
        cardsPanel.add(createDashboardCard("الغرف", roomCountLabel, new Color(40, 167, 69)));
        cardsPanel.add(createDashboardCard("الجرائم", crimeCountLabel, new Color(220, 53, 69)));

        dashboardContent.add(cardsPanel, BorderLayout.NORTH);

        updateDashboardCounts();
    }

    private void setupDarkTheme() {
        UIManager.put("control", DARK_BACKGROUND);
        UIManager.put("info", new Color(128, 128, 128));
        UIManager.put("nimbusBase", new Color(18, 30, 49));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", ACCENT_COLOR);
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", DARK_BACKGROUND);
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", Color.WHITE);
        UIManager.put("nimbusSelectionBackground", ACCENT_COLOR);
        UIManager.put("text", TEXT_COLOR);
        UIManager.put("Panel.background", DARK_BACKGROUND);
        UIManager.put("OptionPane.background", DARK_CARD_BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("Button.background", DARK_CARD_BACKGROUND);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Button.focus", new Color(100, 100, 100));
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", Color.BLACK);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("TextArea.foreground", Color.BLACK);
        UIManager.put("ScrollPane.background", DARK_BACKGROUND);
        UIManager.put("ScrollPane.viewportBorder", BorderFactory.createEmptyBorder());
        UIManager.put("Viewport.background", DARK_BACKGROUND);
    }

    private JMenuBar createCustomMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menuBar.setBackground(DARK_CARD_BACKGROUND);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        addMenu(menuBar, "السجناء", new String[]{"إضافة سجين", "عرض الكل"}, new Runnable[]{this::showAddPrisonerDialog, () -> showListDialog("Prisoner", "عرض كل السجناء")});
        addMenu(menuBar, "الموظفون", new String[]{"إضافة موظف", "عرض الكل"}, new Runnable[]{this::showAddStaffDialog, () -> showListDialog("Staff", "عرض كل الموظفين")});
        addMenu(menuBar, "الأقسام", new String[]{"إضافة قسم", "عرض الكل"}, new Runnable[]{this::showAddDepartmentDialog, () -> showListDialog("Department", "عرض كل الأقسام")});
        addMenu(menuBar, "الغرف", new String[]{"إضافة غرفة", "عرض الكل"}, new Runnable[]{this::showAddRoomDialog, () -> showListDialog("Room", "عرض كل الغرف")});
        addMenu(menuBar, "الجرائم والحوادث", new String[]{"إضافة جريمة", "عرض الجرائم", "إضافة حادثة", "عرض الحوادث"}, new Runnable[]{this::showAddCrimeDialog, () -> showListDialog("Crime", "عرض كل الجرائم"), this::showAddIncidentDialog, () -> showListDialog("Incident", "عرض كل الحوادث")});
        addMenu(menuBar, "الزيارات", new String[]{"إضافة زائر", "عرض الزوار", "تسجيل زيارة", "عرض الزيارات"}, new Runnable[]{this::showAddVisitorDialog, () -> showListDialog("Visitor", "عرض كل الزوار"), this::showAddVisitDialog, () -> showListDialog("Visit", "عرض كل الزيارات")});
        addMenu(menuBar, "السجلات الطبية", new String[]{"إضافة سجل طبي", "عرض السجلات"}, new Runnable[]{this::showAddMedicalRecordDialog, () -> showListDialog("MedicalRecord", "عرض كل السجلات الطبية")});

        return menuBar;
    }

    private void addMenu(JMenuBar menuBar, String title, String[] items, Runnable[] actions) {
        JMenu menu = new JMenu(title);
        menu.setForeground(TEXT_COLOR);
        menu.setFont(new Font("Arial", Font.BOLD, 14));
        for (int i = 0; i < items.length; i++) {
            JMenuItem menuItem = new JMenuItem(items[i]);
            menuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            menuItem.setBackground(DARK_CARD_BACKGROUND);
            menuItem.setForeground(TEXT_COLOR);
            menuItem.setFont(new Font("Arial", Font.PLAIN, 13));
            final int index = i;
            menuItem.addActionListener(e -> actions[index].run());
            menu.add(menuItem);
        }
        menuBar.add(menu);
    }

    // ==================================================================
    // دوال عرض الجداول والنوافذ - معدلة للتصميم المظلم
    // ==================================================================
    private JDialog createStyledDialog(String title, int width, int height) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(DARK_BACKGROUND);
        return dialog;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        panel.setBackground(DARK_BACKGROUND);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        return panel;
    }

    private void addFormField(JPanel panel, String labelText, JComponent component, int gridY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_COLOR);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private void showListDialog(String type, String title) {
        JDialog dialog = createStyledDialog(title, 900, 600);
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(DARK_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dialog.add(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("بحث: ");
        JTextField searchField = new JTextField();
        topPanel.add(searchLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // جعل كل الخلايا غير قابلة للتعديل
            }
        };
        JTable table = new JTable(tableModel);
        setupTableStyle(table);

        loadTableData(tableModel, type, "");

        searchField.addActionListener(e -> loadTableData(tableModel, type, searchField.getText()));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton editButton = new JButton("تعديل المحدد");
        JButton deleteButton = new JButton("حذف المحدد");
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(DARK_CARD_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) table.getValueAt(selectedRow, table.getColumnCount() - 1);
                int confirm = JOptionPane.showConfirmDialog(dialog, "هل أنت متأكد من رغبتك في حذف هذا السجل؟", "تأكيد الحذف", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        dbManager.delete(type, id);
                        loadTableData(tableModel, type, searchField.getText());
                        updateDashboardCounts();
                    } catch (SQLException ex) {
                        showError("خطأ أثناء الحذف.", ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "يرجى تحديد صف لحذفه.", "تنبيه", JOptionPane.WARNING_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) table.getValueAt(selectedRow, table.getColumnCount() - 1);
                boolean success = false;
                switch (type) {
                    case "Department":
                        success = showEditDepartmentDialog(id);
                        break;
                    case "Crime":
                        success = showEditCrimeDialog(id);
                        break;
                    case "Prisoner":
                        success = showEditPrisonerDialog(id);
                        break;
                    case "Staff":
                        success = showEditStaffDialog(id);
                        break;
                    case "Room":
                        success = showEditRoomDialog(id);
                        break;
                    default:
                        JOptionPane.showMessageDialog(dialog, "التعديل غير متاح لهذا القسم حالياً.", "معلومة", JOptionPane.INFORMATION_MESSAGE);
                        return;
                }
                if (success) {
                    loadTableData(tableModel, type, searchField.getText());
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "يرجى تحديد صف لتعديله.", "تنبيه", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void loadTableData(DefaultTableModel tableModel, String type, String searchTerm) {
        try {
            CustomLinkedList<?> data = dbManager.getAll(type); // لاحقاً سنضيف البحث
            tableModel.setRowCount(0);

            switch (type) {
                case "Prisoner":
                    tableModel.setColumnIdentifiers(new String[]{"الجريمة", "القسم", "الجنسية", "العمر", "الاسم الكامل", "ID"});
                    for (Object item : data) {
                        Prisoner p = (Prisoner) item;
                        tableModel.addRow(new Object[]{p.getCrime().getCrimeType(), p.getDepartment().getName(), p.getNationality(), p.getAge(), p.getFullName(), p.getId()});
                    }
                    break;
                case "Staff":
                    tableModel.setColumnIdentifiers(new String[]{"القسم", "الرتبة", "الوظيفة", "العمر", "الاسم الكامل", "ID"});
                    for (Object item : data) {
                        Staff s = (Staff) item;
                        tableModel.addRow(new Object[]{s.getDepartment().getName(), s.getRank(), s.getJobTitle(), s.getAge(), s.getFullName(), s.getId()});
                    }
                    break;
                case "Department":
                    tableModel.setColumnIdentifiers(new String[]{"السعة", "اسم القسم", "ID"});
                    for (Object item : data) {
                        Department d = (Department) item;
                        tableModel.addRow(new Object[]{d.getCapacity(), d.getName(), d.getDepartmentId()});
                    }
                    break;
                case "Room":
                    tableModel.setColumnIdentifiers(new String[]{"القسم", "السعة", "نوع الغرفة", "ID"});
                    for (Object item : data) {
                        Room r = (Room) item;
                        tableModel.addRow(new Object[]{r.getDepartment().getName(), r.getCapacity(), r.getTypeOfRoom(), r.getRoomId()});
                    }
                    break;
                case "Crime":
                    tableModel.setColumnIdentifiers(new String[]{"مدة الحكم", "نوع الجريمة", "ID"});
                    for (Object item : data) {
                        Crime c = (Crime) item;
                        tableModel.addRow(new Object[]{c.getSentence(), c.getCrimeType(), c.getCrimeId()});
                    }
                    break;
                case "Visitor":
                    tableModel.setColumnIdentifiers(new String[]{"صلة القرابة", "رقم الهوية", "العمر", "الاسم الكامل", "ID"});
                    for (Object item : data) {
                        Visitor v = (Visitor) item;
                        tableModel.addRow(new Object[]{v.getRelationship(), v.getIdCard(), v.getAge(), v.getFullName(), v.getId()});
                    }
                    break;
                case "Visit":
                    tableModel.setColumnIdentifiers(new String[]{"الحالة", "تاريخ الزيارة", "اسم الزائر", "اسم السجين", "ID"});
                    for (Object item : data) {
                        Visit v = (Visit) item;
                        tableModel.addRow(new Object[]{v.getStatus(), sdf.format(v.getVisitDate()), v.getVisitor().getFullName(), v.getPrisoner().getFullName(), v.getVisitId()});
                    }
                    break;
                case "MedicalRecord":
                    tableModel.setColumnIdentifiers(new String[]{"الأدوية", "الحالة الصحية", "تاريخ التسجيل", "اسم السجين", "ID"});
                    for (Object item : data) {
                        MedicalRecord mr = (MedicalRecord) item;
                        tableModel.addRow(new Object[]{mr.getMedication(), mr.getConditions(), sdf.format(mr.getRecordDate()), mr.getPrisoner().getFullName(), mr.getMedicalRecordId()});
                    }
                    break;
                case "Incident":
                    tableModel.setColumnIdentifiers(new String[]{"الخطورة", "التاريخ", "الوصف", "ID"});
                    for (Object item : data) {
                        Incident i = (Incident) item;
                        tableModel.addRow(new Object[]{i.getSeverity(), sdf.format(i.getDate()), i.getDescription(), i.getIncidentId()});
                    }
                    break;
            }
        } catch (Exception e) {
            showError("خطأ في جلب البيانات", e);
        }
    }

    // ==================================================================
    // دوال الإضافة (ADD) - مكتملة
    // ==================================================================
    private void showAddPrisonerDialog() {
        JDialog dialog = createStyledDialog("إضافة سجين جديد", 500, 400);
        JPanel panel = createFormPanel();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField nationalityField = new JTextField();
        JComboBox<Department> departmentCombo = new JComboBox<>();
        JComboBox<Crime> crimeCombo = new JComboBox<>();
        try {
            dbManager.getAllDepartments().forEach(departmentCombo::addItem);
            dbManager.getAllCrimes().forEach(crimeCombo::addItem);
        } catch (SQLException ex) {
            showError("خطأ في تحميل البيانات اللازمة.", ex);
            return;
        }
        addFormField(panel, "الاسم الكامل:", nameField, 0);
        addFormField(panel, "العمر:", ageField, 1);
        addFormField(panel, "الجنسية:", nationalityField, 2);
        addFormField(panel, "القسم:", departmentCombo, 3);
        addFormField(panel, "الجريمة:", crimeCombo, 4);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                Prisoner p = new Prisoner();
                p.setFullName(nameField.getText());
                p.setAge(Integer.parseInt(ageField.getText()));
                p.setNationality(nationalityField.getText());
                p.setDepartment((Department) departmentCombo.getSelectedItem());
                p.setCrime((Crime) crimeCombo.getSelectedItem());
                dbManager.addPrisoner(p);
                JOptionPane.showMessageDialog(dialog, "تمت إضافة السجين بنجاح!");
                updateDashboardCounts();
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة السجين", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddStaffDialog() {
        JDialog dialog = createStyledDialog("إضافة موظف جديد", 500, 350);
        JPanel panel = createFormPanel();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField jobTitleField = new JTextField();
        JTextField rankField = new JTextField();
        JComboBox<Department> departmentCombo = new JComboBox<>();
        try {
            dbManager.getAllDepartments().forEach(departmentCombo::addItem);
        } catch (SQLException ex) {
            showError("خطأ في تحميل الأقسام.", ex);
            return;
        }
        addFormField(panel, "الاسم الكامل:", nameField, 0);
        addFormField(panel, "العمر:", ageField, 1);
        addFormField(panel, "المسمى الوظيفي:", jobTitleField, 2);
        addFormField(panel, "الرتبة:", rankField, 3);
        addFormField(panel, "القسم:", departmentCombo, 4);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                Staff s = new Staff();
                s.setFullName(nameField.getText());
                s.setAge(Integer.parseInt(ageField.getText()));
                s.setJobTitle(jobTitleField.getText());
                s.setRank(rankField.getText());
                s.setDepartment((Department) departmentCombo.getSelectedItem());
                dbManager.addStaff(s);
                JOptionPane.showMessageDialog(dialog, "تمت إضافة الموظف بنجاح!");
                updateDashboardCounts();
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة الموظف", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddDepartmentDialog() {
        JDialog dialog = createStyledDialog("إضافة قسم جديد", 400, 250);
        JPanel panel = createFormPanel();
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();
        addFormField(panel, "اسم القسم:", nameField, 0);
        addFormField(panel, "السعة:", capacityField, 1);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                dbManager.addDepartment(nameField.getText(), Integer.parseInt(capacityField.getText()));
                JOptionPane.showMessageDialog(dialog, "تمت إضافة القسم بنجاح!");
                updateDashboardCounts();
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة القسم", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddRoomDialog() {
        JDialog dialog = createStyledDialog("إضافة غرفة جديدة", 400, 300);
        JPanel panel = createFormPanel();
        JComboBox<Department> departmentCombo = new JComboBox<>();
        try {
            dbManager.getAllDepartments().forEach(departmentCombo::addItem);
        } catch (SQLException ex) {
            showError("خطأ في تحميل الأقسام.", ex);
            return;
        }
        JTextField typeField = new JTextField("عادي");
        JTextField capacityField = new JTextField();
        addFormField(panel, "القسم:", departmentCombo, 0);
        addFormField(panel, "نوع الغرفة:", typeField, 1);
        addFormField(panel, "السعة:", capacityField, 2);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                Department selectedDept = (Department) departmentCombo.getSelectedItem();
                dbManager.addRoom(selectedDept.getDepartmentId(), typeField.getText(), Integer.parseInt(capacityField.getText()));
                JOptionPane.showMessageDialog(dialog, "تمت إضافة الغرفة بنجاح!");
                updateDashboardCounts();
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة الغرفة", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddCrimeDialog() {
        JDialog dialog = createStyledDialog("إضافة جريمة جديدة", 400, 250);
        JPanel panel = createFormPanel();
        JTextField typeField = new JTextField();
        JTextField sentenceField = new JTextField();
        addFormField(panel, "نوع الجريمة:", typeField, 0);
        addFormField(panel, "مدة الحكم:", sentenceField, 1);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                dbManager.addCrime(typeField.getText(), sentenceField.getText());
                JOptionPane.showMessageDialog(dialog, "تمت إضافة الجريمة بنجاح!");
                updateDashboardCounts();
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة الجريمة", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddVisitorDialog() {
        JDialog dialog = createStyledDialog("إضافة زائر جديد", 500, 350);
        JPanel panel = createFormPanel();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField idCardField = new JTextField();
        JTextField relationshipField = new JTextField();
        addFormField(panel, "الاسم الكامل:", nameField, 0);
        addFormField(panel, "العمر:", ageField, 1);
        addFormField(panel, "رقم الهوية:", idCardField, 2);
        addFormField(panel, "صلة القرابة:", relationshipField, 3);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                Visitor v = new Visitor();
                v.setFullName(nameField.getText());
                v.setAge(Integer.parseInt(ageField.getText()));
                v.setIdCard(Integer.parseInt(idCardField.getText()));
                v.setRelationship(relationshipField.getText());
                dbManager.addVisitor(v);
                JOptionPane.showMessageDialog(dialog, "تمت إضافة الزائر بنجاح!");
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة الزائر", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddVisitDialog() {
        JDialog dialog = createStyledDialog("تسجيل زيارة جديدة", 500, 350);
        JPanel panel = createFormPanel();
        JComboBox<Prisoner> prisonerCombo = new JComboBox<>();
        JComboBox<Visitor> visitorCombo = new JComboBox<>();
        JTextField dateField = new JTextField(sdf.format(new Date()));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"مجدولة", "مكتملة", "ملغاة"});
        try {
            dbManager.getAllPrisoners().forEach(prisonerCombo::addItem);
            dbManager.getAllVisitors().forEach(visitorCombo::addItem);
        } catch (SQLException ex) {
            showError("خطأ في تحميل بيانات السجناء أو الزوار.", ex);
            return;
        }
        addFormField(panel, "السجين:", prisonerCombo, 0);
        addFormField(panel, "الزائر:", visitorCombo, 1);
        addFormField(panel, "تاريخ الزيارة (yyyy-MM-dd):", dateField, 2);
        addFormField(panel, "الحالة:", statusCombo, 3);
        JButton addButton = new JButton("تسجيل");
        addButton.addActionListener(e -> {
            try {
                Visit v = new Visit();
                v.setPrisoner((Prisoner) prisonerCombo.getSelectedItem());
                v.setVisitor((Visitor) visitorCombo.getSelectedItem());
                try {
                    v.setVisitDate(sdf.parse(dateField.getText()));
                } catch (Exception ex) {
                    showError("صيغة التاريخ غير صحيحة. الرجاء استخدام yyyy-MM-dd.", ex);
                    return;
                }
                v.setStatus((String) statusCombo.getSelectedItem());
                dbManager.addVisit(v);
                JOptionPane.showMessageDialog(dialog, "تم تسجيل الزيارة بنجاح!");
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في تسجيل الزيارة", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddMedicalRecordDialog() {
        JDialog dialog = createStyledDialog("إضافة سجل طبي", 500, 400);
        JPanel panel = createFormPanel();
        JComboBox<Prisoner> prisonerCombo = new JComboBox<>();
        try {
            dbManager.getAllPrisoners().forEach(prisonerCombo::addItem);
        } catch (SQLException ex) {
            showError("خطأ في تحميل بيانات السجناء.", ex);
            return;
        }
        JTextArea conditionsArea = new JTextArea(3, 20);
        JTextArea medicationArea = new JTextArea(3, 20);
        JTextField dateField = new JTextField(sdf.format(new Date()));
        addFormField(panel, "السجين:", prisonerCombo, 0);
        addFormField(panel, "الحالة الصحية:", new JScrollPane(conditionsArea), 1);
        addFormField(panel, "الأدوية:", new JScrollPane(medicationArea), 2);
        addFormField(panel, "تاريخ التسجيل (yyyy-MM-dd):", dateField, 3);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                MedicalRecord mr = new MedicalRecord();
                mr.setPrisoner((Prisoner) prisonerCombo.getSelectedItem());
                mr.setConditions(conditionsArea.getText());
                mr.setMedication(medicationArea.getText());
                try {
                    mr.setRecordDate(sdf.parse(dateField.getText()));
                } catch (Exception ex) {
                    showError("صيغة التاريخ غير صحيحة. الرجاء استخدام yyyy-MM-dd.", ex);
                    return;
                }
                dbManager.addMedicalRecord(mr);
                JOptionPane.showMessageDialog(dialog, "تمت إضافة السجل الطبي بنجاح!");
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة السجل الطبي", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddIncidentDialog() {
        JDialog dialog = createStyledDialog("إضافة حادثة", 500, 350);
        JPanel panel = createFormPanel();
        JTextArea descriptionArea = new JTextArea(4, 20);
        JTextField dateField = new JTextField(sdf.format(new Date()));
        JComboBox<String> severityCombo = new JComboBox<>(new String[]{"منخفضة", "متوسطة", "عالية"});
        addFormField(panel, "وصف الحادثة:", new JScrollPane(descriptionArea), 0);
        addFormField(panel, "تاريخ الحادثة (yyyy-MM-dd):", dateField, 1);
        addFormField(panel, "مستوى الخطورة:", severityCombo, 2);
        JButton addButton = new JButton("إضافة");
        addButton.addActionListener(e -> {
            try {
                Incident i = new Incident();
                i.setDescription(descriptionArea.getText());
                try {
                    i.setDate(sdf.parse(dateField.getText()));
                } catch (Exception ex) {
                    showError("صيغة التاريخ غير صحيحة. الرجاء استخدام yyyy-MM-dd.", ex);
                    return;
                }
                i.setSeverity((String) severityCombo.getSelectedItem());
                dbManager.addIncident(i);
                JOptionPane.showMessageDialog(dialog, "تمت إضافة الحادثة بنجاح!");
                dialog.dispose();
            } catch (Exception ex) {
                showError("خطأ في إضافة الحادثة", ex);
            }
        });
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(addButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ==================================================================
    // دوال التعديل (EDIT) - مكتملة الآن
    // ==================================================================
    private boolean showEditDepartmentDialog(int id) {
        try {
            // جلب البيانات الحالية للقسم
            Department deptToEdit = null;
            for (Department d : dbManager.getAllDepartments()) {
                if (d.getDepartmentId() == id) {
                    deptToEdit = d;
                    break;
                }
            }
            if (deptToEdit == null) {
                showError("لم يتم العثور على القسم المحدد.", null);
                return false;
            }

            JDialog dialog = createStyledDialog("تعديل القسم", 400, 250);
            JPanel panel = createFormPanel();
            JTextField nameField = new JTextField(deptToEdit.getName());
            JTextField capacityField = new JTextField(String.valueOf(deptToEdit.getCapacity()));
            addFormField(panel, "اسم القسم:", nameField, 0);
            addFormField(panel, "السعة:", capacityField, 1);

            JButton saveButton = new JButton("حفظ التعديلات");
            final Department finalDept = deptToEdit; // نسخة نهائية للاستخدام داخل لامدا
            saveButton.addActionListener(e -> {
                try {
                    finalDept.setName(nameField.getText());
                    finalDept.setCapacity(Integer.parseInt(capacityField.getText()));
                    dbManager.updateDepartment(finalDept);
                    JOptionPane.showMessageDialog(dialog, "تم تعديل القسم بنجاح!");
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("خطأ في حفظ التعديلات.", ex);
                }
            });

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(saveButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            return true;

        } catch (Exception ex) {
            showError("خطأ في فتح نافذة التعديل.", ex);
            return false;
        }
    }

    private boolean showEditCrimeDialog(int id) {
        try {
            Crime crimeToEdit = null;
            for (Crime c : dbManager.getAllCrimes()) {
                if (c.getCrimeId() == id) {
                    crimeToEdit = c;
                    break;
                }
            }
            if (crimeToEdit == null) {
                showError("لم يتم العثور على الجريمة المحددة.", null);
                return false;
            }

            JDialog dialog = createStyledDialog("تعديل الجريمة", 400, 250);
            JPanel panel = createFormPanel();
            JTextField typeField = new JTextField(crimeToEdit.getCrimeType());
            JTextField sentenceField = new JTextField(crimeToEdit.getSentence());
            addFormField(panel, "نوع الجريمة:", typeField, 0);
            addFormField(panel, "مدة الحكم:", sentenceField, 1);

            JButton saveButton = new JButton("حفظ التعديلات");
            final Crime finalCrime = crimeToEdit;
            saveButton.addActionListener(e -> {
                try {
                    finalCrime.setCrimeType(typeField.getText());
                    finalCrime.setSentence(sentenceField.getText());
                    dbManager.updateCrime(finalCrime);
                    JOptionPane.showMessageDialog(dialog, "تم تعديل الجريمة بنجاح!");
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("خطأ في حفظ التعديلات.", ex);
                }
            });

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(saveButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            return true;

        } catch (Exception ex) {
            showError("خطأ في فتح نافذة التعديل.", ex);
            return false;
        }
    }

    private boolean showEditPrisonerDialog(int id) {
        try {
            Prisoner prisonerToEdit = null;
            for (Prisoner p : dbManager.getAllPrisoners()) {
                if (p.getId() == id) {
                    prisonerToEdit = p;
                    break;
                }
            }
            if (prisonerToEdit == null) {
                showError("لم يتم العثور على السجين المحدد.", null);
                return false;
            }

            JDialog dialog = createStyledDialog("تعديل بيانات السجين", 500, 400);
            JPanel panel = createFormPanel();
            JTextField nameField = new JTextField(prisonerToEdit.getFullName());
            JTextField ageField = new JTextField(String.valueOf(prisonerToEdit.getAge()));
            JTextField nationalityField = new JTextField(prisonerToEdit.getNationality());
            JComboBox<Department> departmentCombo = new JComboBox<>();
            JComboBox<Crime> crimeCombo = new JComboBox<>();

            CustomLinkedList<Department> depts = dbManager.getAllDepartments();
            CustomLinkedList<Crime> crimes = dbManager.getAllCrimes();
            depts.forEach(departmentCombo::addItem);
            crimes.forEach(crimeCombo::addItem);

            // تحديد الخيارات الحالية
            departmentCombo.setSelectedItem(prisonerToEdit.getDepartment());
            crimeCombo.setSelectedItem(prisonerToEdit.getCrime());

            addFormField(panel, "الاسم الكامل:", nameField, 0);
            addFormField(panel, "العمر:", ageField, 1);
            addFormField(panel, "الجنسية:", nationalityField, 2);
            addFormField(panel, "القسم:", departmentCombo, 3);
            addFormField(panel, "الجريمة:", crimeCombo, 4);

            JButton saveButton = new JButton("حفظ التعديلات");
            final Prisoner finalPrisoner = prisonerToEdit;
            saveButton.addActionListener(e -> {
                try {
                    finalPrisoner.setFullName(nameField.getText());
                    finalPrisoner.setAge(Integer.parseInt(ageField.getText()));
                    finalPrisoner.setNationality(nationalityField.getText());
                    finalPrisoner.setDepartment((Department) departmentCombo.getSelectedItem());
                    finalPrisoner.setCrime((Crime) crimeCombo.getSelectedItem());
                    dbManager.updatePrisoner(finalPrisoner);
                    JOptionPane.showMessageDialog(dialog, "تم تعديل بيانات السجين بنجاح!");
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("خطأ في حفظ التعديلات.", ex);
                }
            });

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(saveButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            return true;

        } catch (Exception ex) {
            showError("خطأ في فتح نافذة التعديل.", ex);
            return false;
        }
    }

    private boolean showEditStaffDialog(int id) {
        try {
            Staff staffToEdit = null;
            for (Staff s : dbManager.getAllStaff()) {
                if (s.getId() == id) {
                    staffToEdit = s;
                    break;
                }
            }
            if (staffToEdit == null) {
                showError("لم يتم العثور على الموظف المحدد.", null);
                return false;
            }

            JDialog dialog = createStyledDialog("تعديل بيانات الموظف", 500, 350);
            JPanel panel = createFormPanel();
            JTextField nameField = new JTextField(staffToEdit.getFullName());
            JTextField ageField = new JTextField(String.valueOf(staffToEdit.getAge()));
            JTextField jobTitleField = new JTextField(staffToEdit.getJobTitle());
            JTextField rankField = new JTextField(staffToEdit.getRank());
            JComboBox<Department> departmentCombo = new JComboBox<>();

            dbManager.getAllDepartments().forEach(departmentCombo::addItem);
            departmentCombo.setSelectedItem(staffToEdit.getDepartment());

            addFormField(panel, "الاسم الكامل:", nameField, 0);
            addFormField(panel, "العمر:", ageField, 1);
            addFormField(panel, "المسمى الوظيفي:", jobTitleField, 2);
            addFormField(panel, "الرتبة:", rankField, 3);
            addFormField(panel, "القسم:", departmentCombo, 4);

            JButton saveButton = new JButton("حفظ التعديلات");
            final Staff finalStaff = staffToEdit;
            saveButton.addActionListener(e -> {
                try {
                    finalStaff.setFullName(nameField.getText());
                    finalStaff.setAge(Integer.parseInt(ageField.getText()));
                    finalStaff.setJobTitle(jobTitleField.getText());
                    finalStaff.setRank(rankField.getText());
                    finalStaff.setDepartment((Department) departmentCombo.getSelectedItem());
                    dbManager.updateStaff(finalStaff);
                    JOptionPane.showMessageDialog(dialog, "تم تعديل بيانات الموظف بنجاح!");
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("خطأ في حفظ التعديلات.", ex);
                }
            });

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(saveButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            return true;

        } catch (Exception ex) {
            showError("خطأ في فتح نافذة التعديل.", ex);
            return false;
        }
    }

    private boolean showEditRoomDialog(int id) {
        try {
            Room roomToEdit = null;
            for (Room r : dbManager.getAllRooms()) {
                if (r.getRoomId() == id) {
                    roomToEdit = r;
                    break;
                }
            }
            if (roomToEdit == null) {
                showError("لم يتم العثور على الغرفة المحددة.", null);
                return false;
            }

            JDialog dialog = createStyledDialog("تعديل الغرفة", 400, 300);
            JPanel panel = createFormPanel();
            JComboBox<Department> departmentCombo = new JComboBox<>();
            dbManager.getAllDepartments().forEach(departmentCombo::addItem);
            departmentCombo.setSelectedItem(roomToEdit.getDepartment());

            JTextField typeField = new JTextField(roomToEdit.getTypeOfRoom());
            JTextField capacityField = new JTextField(String.valueOf(roomToEdit.getCapacity()));

            addFormField(panel, "القسم:", departmentCombo, 0);
            addFormField(panel, "نوع الغرفة:", typeField, 1);
            addFormField(panel, "السعة:", capacityField, 2);

            JButton saveButton = new JButton("حفظ التعديلات");
            final Room finalRoom = roomToEdit;
            saveButton.addActionListener(e -> {
                try {
                    finalRoom.setDepartment((Department) departmentCombo.getSelectedItem());
                    finalRoom.setTypeOfRoom(typeField.getText());
                    finalRoom.setCapacity(Integer.parseInt(capacityField.getText()));
                    dbManager.updateRoom(finalRoom);
                    JOptionPane.showMessageDialog(dialog, "تم تعديل الغرفة بنجاح!");
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("خطأ في حفظ التعديلات.", ex);
                }
            });

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(saveButton, BorderLayout.SOUTH);
            dialog.setVisible(true);
            return true;

        } catch (Exception ex) {
            showError("خطأ في فتح نافذة التعديل.", ex);
            return false;
        }
    }

    // ==================================================================
    // دوال مساعدة للتصميم (Helper Functions for Styling)
    // ==================================================================
    private void setupTableStyle(JTable table) {
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        table.setFillsViewportHeight(true);
        table.setBackground(DARK_CARD_BACKGROUND);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(DARK_BACKGROUND);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        rightRenderer.setForeground(TEXT_COLOR);
        rightRenderer.setBackground(DARK_CARD_BACKGROUND);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_COLOR);
        centerRenderer.setBackground(DARK_CARD_BACKGROUND);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equals("ID") || table.getColumnName(i).equals("العمر") || table.getColumnName(i).equals("السعة")) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            }
        }
    }

    private JPanel createDashboardCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, color),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(DARK_CARD_BACKGROUND);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 50));
        valueLabel.setForeground(TEXT_COLOR);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void updateDashboardCounts() {
        SwingUtilities.invokeLater(() -> {
            prisonerCountLabel.setText(String.valueOf(dbManager.getCount("prisoner")));
            staffCountLabel.setText(String.valueOf(dbManager.getCount("staff")));
            roomCountLabel.setText(String.valueOf(dbManager.getCount("room")));
            crimeCountLabel.setText(String.valueOf(dbManager.getCount("crime")));
        });
    }

    private void showError(String message, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, message, "خطأ", JOptionPane.ERROR_MESSAGE);
    }
}
