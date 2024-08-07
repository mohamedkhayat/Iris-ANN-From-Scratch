module com.mohamedkhayat.iris_ann_scratch {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    opens com.mohamedkhayat.iris_ann_scratch to com.google.gson,javafx.fxml;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.client5.httpclient5.fluent;
    exports com.mohamedkhayat.iris_ann_scratch;
}