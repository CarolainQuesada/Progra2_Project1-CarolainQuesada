module com.mycompany.project_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens controller to javafx.fxml;
  
    opens com.mycompany.project_1 to javafx.fxml;
    exports com.mycompany.project_1;
    
    
}
