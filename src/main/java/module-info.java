module org.snaccooperative.ingesttool {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires datamodel;

    opens org.snaccooperative.ingesttool to javafx.fxml;
    exports org.snaccooperative.ingesttool;
}