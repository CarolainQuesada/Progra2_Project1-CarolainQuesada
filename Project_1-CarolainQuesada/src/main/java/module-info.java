module com.mycompany.project_1.carolainquesada {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.project_1.carolainquesada to javafx.fxml;
    exports com.mycompany.project_1.carolainquesada;
}
