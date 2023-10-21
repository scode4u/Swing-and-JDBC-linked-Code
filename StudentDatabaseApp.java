import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StudentDatabaseApp {
    private Connection connection;
    private JFrame frame;
    private JTextField idField, nameField, courseField, emailField, mobileField;
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDatabaseApp());
    }

    public StudentDatabaseApp() {
        initializeDatabaseConnection();
        createAndShowGUI();
    
    }

    private void initializeDatabaseConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "your_username", "your_password");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to connect to the database.");
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Student Database Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel idLabel = new JLabel("Student ID:");
        JLabel nameLabel = new JLabel("Student Name:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel mobileLabel = new JLabel("Mobile:");

        idField = new JTextField(10);
        nameField = new JTextField(20);
        courseField = new JTextField(10);
        emailField = new JTextField(20);
        mobileField = new JTextField(12);

        JButton insertButton = new JButton("Insert");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton displayButton = new JButton("Display");
        JButton resetButton = new JButton("Reset");

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(courseLabel);
        inputPanel.add(courseField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(mobileLabel);
        inputPanel.add(mobileField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(insertButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(resetButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        insertButton.addActionListener(e -> insertStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        updateButton.addActionListener(e -> updateStudent());
        displayButton.addActionListener(e -> displayStudents());
        resetButton.addActionListener(e -> resetFields());

        frame.pack();
        frame.setVisible(true);
    }

    private void insertStudent() {
        try {
            String sql = "INSERT INTO Students (ID, Name, Course, Email, Mobile) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
            preparedStatement.setString(2, nameField.getText());
            preparedStatement.setString(3, courseField.getText());
            preparedStatement.setString(4, emailField.getText());
            preparedStatement.setString(5, mobileField.getText());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Student information inserted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to insert student information.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to insert student information.");
        }
    }

    private void deleteStudent() {
        try {
            String sql = "DELETE FROM Students WHERE Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, nameField.getText());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(frame, "Student deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "No student found with that name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to delete student.");
        }
    }

    private void updateStudent() {
        try {
            String sql = "UPDATE Students SET Name = ? WHERE Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, nameField.getText()); // Assuming you want to update by name

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(frame, "Student information updated successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "No student found with that name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to update student information.");
        }
    }

    private void displayStudents() {
        try {
            String sql = "SELECT * FROM Students";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder result = new StringBuilder("Student Information:\n\n");
            while (resultSet.next()) {
                result.append("ID: ").append(resultSet.getInt("ID")).append("\n");
                result.append("Name: ").append(resultSet.getString("Name")).append("\n");
                result.append("Course: ").append(resultSet.getString("Course")).append("\n");
                result.append("Email: ").append(resultSet.getString("Email")).append("\n");
                result.append("Mobile: ").append(resultSet.getString("Mobile")).append("\n");
                result.append("\n");
            }

            JOptionPane.showMessageDialog(frame, result.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to retrieve student information.");
        }
    }

    private void resetFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        emailField.setText("");
        mobileField.setText("");
    }
}
