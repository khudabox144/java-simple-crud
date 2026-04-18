package controller;

import model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.*;
import java.util.stream.Collectors;

public class MainController {
    
    @FXML private TextField idField, nameField, deptField, emailField, cgpaField, searchField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colId, colName, colDept, colEmail;
    @FXML private TableColumn<Student, Double> colCgpa;
    @FXML private ComboBox<String> searchCombo;
    @FXML private Label statusLabel;
    
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private final String DATA_FILE = "data/students.csv";
    
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("department"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCgpa.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
        
        searchCombo.getItems().addAll("ID", "Name", "Department");
        searchCombo.setValue("Name");
        
        loadDataFromFile();
        studentTable.setItems(studentList);
        
        if (studentList.isEmpty()) {
            addSampleData();
        }
        
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f7fafc;");
                }
            });
            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("");
                }
            });
            return row;
        });
        
        updateStatus("Ready", "#48bb78");
    }
    
    private void addSampleData() {
        studentList.addAll(
            new Student("STU001", "John Doe", "Computer Science", "john.doe@university.com", 3.75),
            new Student("STU002", "Jane Smith", "Electrical Engineering", "jane.smith@university.com", 3.90),
            new Student("STU003", "Mike Johnson", "Mechanical Engineering", "mike.j@university.com", 3.45),
            new Student("STU004", "Sarah Williams", "Business Administration", "sarah.w@university.com", 3.85)
        );
        saveDataToFile();
    }
    
    @FXML
    public void addStudent() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            String email = emailField.getText().trim();
            double cgpa = Double.parseDouble(cgpaField.getText().trim());
            
            if (id.isEmpty() || name.isEmpty() || dept.isEmpty() || email.isEmpty()) {
                showAlert("Error", "All fields are required!", "warning");
                return;
            }
            
            if (cgpa < 0 || cgpa > 4.0) {
                showAlert("Error", "CGPA must be between 0 and 4.0", "error");
                return;
            }
            
            if (!email.contains("@") || !email.contains(".")) {
                showAlert("Error", "Please enter a valid email address!", "warning");
                return;
            }
            
            boolean exists = studentList.stream().anyMatch(s -> s.getId().equals(id));
            if (exists) {
                showAlert("Error", "Student ID already exists!", "warning");
                return;
            }
            
            Student student = new Student(id, name, dept, email, cgpa);
            studentList.add(student);
            saveDataToFile();
            clearFields();
            updateStatus("Student added successfully!", "#48bb78");
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid CGPA format! Please enter a number (0.0 - 4.0)", "error");
        }
    }
    
    @FXML
    public void updateStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to update!", "info");
            return;
        }
        
        try {
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            String email = emailField.getText().trim();
            double cgpa = Double.parseDouble(cgpaField.getText().trim());
            
            if (name.isEmpty() || dept.isEmpty() || email.isEmpty()) {
                showAlert("Error", "Please fill all fields!", "warning");
                return;
            }
            
            selected.setName(name);
            selected.setDepartment(dept);
            selected.setEmail(email);
            selected.setCgpa(cgpa);
            
            studentTable.refresh();
            saveDataToFile();
            clearFields();
            updateStatus("Student updated successfully!", "#48bb78");
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid CGPA format!", "error");
        }
    }
    
    @FXML
    public void deleteStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to delete!", "info");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Student Record");
        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            studentList.remove(selected);
            saveDataToFile();
            clearFields();
            updateStatus("Student deleted successfully!", "#f56565");
        }
    }
    
    @FXML
    public void searchStudent() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String searchType = searchCombo.getValue();
        
        if (searchTerm.isEmpty()) {
            studentTable.setItems(studentList);
            updateStatus("Showing all " + studentList.size() + " students", "#48bb78");
            return;
        }
        
        ObservableList<Student> filtered = FXCollections.observableArrayList(
            studentList.stream()
                .filter(s -> {
                    switch (searchType) {
                        case "ID": return s.getId().toLowerCase().contains(searchTerm);
                        case "Name": return s.getName().toLowerCase().contains(searchTerm);
                        case "Department": return s.getDepartment().toLowerCase().contains(searchTerm);
                        default: return false;
                    }
                })
                .collect(Collectors.toList())
        );
        
        studentTable.setItems(filtered);
        updateStatus("Found " + filtered.size() + " matching students", "#4299e1");
    }
    
    @FXML
    public void refreshSearch() {
        searchField.clear();
        studentTable.setItems(studentList);
        updateStatus("Showing all " + studentList.size() + " students", "#48bb78");
    }
    
    @FXML
    public void loadSelectedStudent(MouseEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            idField.setText(selected.getId());
            nameField.setText(selected.getName());
            deptField.setText(selected.getDepartment());
            emailField.setText(selected.getEmail());
            cgpaField.setText(String.valueOf(selected.getCgpa()));
        }
    }
    
    private void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Student s = new Student(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));
                    studentList.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
    private void saveDataToFile() {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Student s : studentList) {
                bw.write(s.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save data!", "error");
        }
    }
    
    private void clearFields() {
        idField.clear();
        nameField.clear();
        deptField.clear();
        emailField.clear();
        cgpaField.clear();
        idField.requestFocus();
    }
    
    private void updateStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 13px; -fx-font-weight: bold;");
        
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            if (statusLabel.getText().equals(message)) {
                statusLabel.setText("Ready");
                statusLabel.setStyle("-fx-text-fill: #a0aec0; -fx-font-size: 13px;");
            }
        });
        pause.play();
    }
    
    private void showAlert(String title, String content, String type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        DialogPane dialogPane = alert.getDialogPane();
        switch (type) {
            case "error":
                dialogPane.setStyle("-fx-background-color: #fed7d7;");
                break;
            case "warning":
                dialogPane.setStyle("-fx-background-color: #feebc8;");
                break;
            case "info":
                dialogPane.setStyle("-fx-background-color: #bee3f8;");
                break;
        }
        
        alert.showAndWait();
    }
}
