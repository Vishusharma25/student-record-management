import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StudentManagementSystem extends JFrame implements ActionListener {

    // ==========================================
    // CONFIGURATION SECTION - EDIT BEFORE RUNNING
    // ==========================================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = ""; // <-- PUT YOUR MYSQL PASSWORD HERE

    // UI Components
    JLabel jtitle, studentName, studentID, studentGrade, dobLabel, genderLabel, contactLabel, emailLabel;
    JTextField jstudentName, jstudentID, jstudentGrade, dobField, contactField, emailField, searchField;
    JRadioButton maleRadio, femaleRadio;
    ButtonGroup genderGroup;
    JButton addStudent, reset, deleteRecord, searchButton;
    JTable studentTable;
    DefaultTableModel tableModel;
    
    private Connection connection;

    public StudentManagementSystem() {
        setTitle("Student Management System by Group 5");
        setLayout(null);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window on screen

        // Title
        jtitle = new JLabel("STUDENT MANAGEMENT SYSTEM");
        jtitle.setBounds(250, 10, 700, 50);
        jtitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        add(jtitle);

        // Labels
        setupLabel(studentName = new JLabel("Student Name"), 80);
        setupLabel(studentID = new JLabel("Student ID"), 120);
        setupLabel(studentGrade = new JLabel("Student Grade"), 160);
        setupLabel(dobLabel = new JLabel("Date of Birth (dd-mm-yyyy)"), 200);
        setupLabel(genderLabel = new JLabel("Gender"), 240);
        setupLabel(contactLabel = new JLabel("Contact Number"), 280);
        setupLabel(emailLabel = new JLabel("Email"), 320);

        // Input Fields
        add(jstudentName = new JTextField()); jstudentName.setBounds(250, 80, 200, 30);
        add(jstudentID = new JTextField()); jstudentID.setBounds(250, 120, 200, 30);
        add(jstudentGrade = new JTextField()); jstudentGrade.setBounds(250, 160, 200, 30);
        add(dobField = new JTextField()); dobField.setBounds(250, 200, 200, 30);
        
        add(contactField = new JTextField()); contactField.setBounds(250, 280, 200, 30);
        add(emailField = new JTextField()); emailField.setBounds(250, 320, 200, 30);

        // Radio Buttons
        maleRadio = new JRadioButton("Male"); maleRadio.setBounds(250, 240, 80, 30);
        femaleRadio = new JRadioButton("Female"); femaleRadio.setBounds(340, 240, 100, 30);
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        add(maleRadio); add(femaleRadio);

        // Buttons
        add(addStudent = new JButton("Add Student")); addStudent.setBounds(650, 150, 150, 30);
        add(reset = new JButton("Reset")); reset.setBounds(650, 200, 150, 30);
        add(deleteRecord = new JButton("Delete Record")); deleteRecord.setBounds(650, 250, 150, 30);

        // Search Section
        add(searchField = new JTextField()); searchField.setBounds(50, 360, 300, 30);
        add(searchButton = new JButton("Search by ID")); searchButton.setBounds(360, 360, 150, 30);

        // Table
        String[] columnNames = { "Student Name", "Student ID", "Grade", "DOB", "Gender", "Contact", "Email" };
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(50, 400, 860, 150);
        add(scrollPane);

        // Listeners
        addStudent.addActionListener(this);
        reset.addActionListener(this);
        deleteRecord.addActionListener(this);
        searchButton.addActionListener(this);

        // Database Init
        connectToDatabase();
        loadStudentDataFromDatabase();

        // Cleanup on close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDatabaseConnection();
            }
        });
        
        setVisible(true);
    }

    private void setupLabel(JLabel label, int y) {
        label.setBounds(50, y, 200, 30);
        add(label);
    }

    // ---------------- DATABASE CONNECTION ----------------
    private void connectToDatabase() {
        try {
            // Load the MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL Driver not found! Add the connector jar to your library.", "Driver Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Connection Failed! Check your DB_USER and DB_PASSWORD in the code.", "DB Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ---------------- ACTION HANDLER ----------------
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addStudent) {
            handleAddStudent();
        } else if (e.getSource() == reset) {
            handleReset();
        } else if (e.getSource() == deleteRecord) {
            handleDelete();
        } else if (e.getSource() == searchButton) {
            handleSearch();
        }
    }

    // ---------------- LOGIC METHODS ----------------
    private void handleAddStudent() {
        String name = jstudentName.getText();
        String id = jstudentID.getText();
        String grade = jstudentGrade.getText();
        String dob = dobField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String gender = maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "";

        if (name.isEmpty() || id.isEmpty() || grade.isEmpty() || dob.isEmpty() || contact.isEmpty() || email.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidDate(dob)) {
            JOptionPane.showMessageDialog(this, "Invalid Date! Use format dd-MM-yyyy", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Insert into DB
        String sql = "INSERT INTO students (student_name, student_id, student_grade, dob, gender, contact, email) VALUES (?, ?, ?, STR_TO_DATE(?, '%d-%m-%Y'), ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setInt(2, Integer.parseInt(id));
            pst.setDouble(3, Double.parseDouble(grade));
            pst.setString(4, dob);
            pst.setString(5, gender);
            pst.setString(6, contact);
            pst.setString(7, email);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                tableModel.addRow(new Object[]{name, id, grade, dob, gender, contact, email});
                JOptionPane.showMessageDialog(this, "Student Added Successfully!");
                handleReset();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error Saving Data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Grade must be numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReset() {
        jstudentName.setText("");
        jstudentID.setText("");
        jstudentGrade.setText("");
        dobField.setText("");
        contactField.setText("");
        emailField.setText("");
        genderGroup.clearSelection();
    }

    private void handleDelete() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.");
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 1).toString();
        try (PreparedStatement pst = connection.prepareStatement("DELETE FROM students WHERE student_id = ?")) {
            pst.setInt(1, Integer.parseInt(id));
            int rows = pst.executeUpdate();
            if (rows > 0) {
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Record Deleted.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void handleSearch() {
        String searchId = searchField.getText();
        if (searchId.isEmpty()) return;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = tableModel.getValueAt(i, 1).toString();
            if (id.equals(searchId)) {
                studentTable.setRowSelectionInterval(i, i);
                studentTable.scrollRectToVisible(studentTable.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "ID Not Found");
    }

    private void loadStudentDataFromDatabase() {
        if (connection == null) return;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT student_name, student_id, student_grade, DATE_FORMAT(dob, '%d-%m-%Y'), gender, contact, email FROM students")) {
            
            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString(1), rs.getInt(2), rs.getDouble(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeDatabaseConnection() {
        try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) { return false; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }
}
