module com.example.canvas {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens com.example.canvas to javafx.fxml;
    exports com.example.canvas;
}
