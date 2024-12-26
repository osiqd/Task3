module com.example.observer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop; // Добавлено для доступа к javax.print.attribute.standard

    opens com.example.observer to javafx.fxml;
    exports com.example.observer;
}