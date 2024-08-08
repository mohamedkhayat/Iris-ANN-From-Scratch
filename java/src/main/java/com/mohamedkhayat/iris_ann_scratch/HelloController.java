package com.mohamedkhayat.iris_ann_scratch;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.client5.http.fluent.Request;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class HelloController {
    @FXML
    private ImageView imview;
    @FXML
    private TextArea textArea;
    @FXML
    private Button button;
    @FXML
    private TextField sepal_length_input;
    @FXML
    private TextField sepal_width_input;
    @FXML
    private TextField petal_length_input;
    @FXML
    private TextField petal_width_input;
    private String Species = "";
    @FXML
    private Label hint;
    @FXML
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void initialize() {
        imview.setVisible(true);
        textArea.setVisible(false);
        textArea.setText("");
        hint.setVisible(false);

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> sepalLengthFormatter = new TextFormatter<>(filter);
        TextFormatter<String> sepalWidthFormatter = new TextFormatter<>(filter);
        TextFormatter<String> petalLengthFormatter = new TextFormatter<>(filter);
        TextFormatter<String> petalWidthFormatter = new TextFormatter<>(filter);

        sepal_length_input.setTextFormatter(sepalLengthFormatter);
        sepal_width_input.setTextFormatter(sepalWidthFormatter);
        petal_length_input.setTextFormatter(petalLengthFormatter);
        petal_width_input.setTextFormatter(petalWidthFormatter);
    }
    @FXML
    void predict(){
        String sepalLength = sepal_length_input.getText();
        String sepalWidth = sepal_width_input.getText();
        String petalLength = petal_length_input.getText();
        String petalWidth = petal_width_input.getText();

        try{
            double sepalLengthVal = Double.parseDouble(sepalLength);
            double sepalWidthVal = Double.parseDouble(sepalWidth);
            double petalLengthVal = Double.parseDouble(petalLength);
            double petalWidthVal = Double.parseDouble(petalWidth);
            IrisInput input = new IrisInput(sepalLengthVal, sepalWidthVal, petalLengthVal, petalWidthVal);
            Gson gson = new Gson();
            String json = gson.toJson(input);
            System.out.println(json);
            try {
                String apiUrl = "http://localhost:5000/predict";

                String response = Request.post(apiUrl)
                        .bodyString(json, ContentType.APPLICATION_JSON)
                        .execute()
                        .returnContent()
                        .asString();
                response = response.trim().replace("\"", "");
                this.Species = response;
                switch (response) {
                    case "setosa" -> {
                        initialize();
                        hint.setVisible(true);
                        Image setosa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mohamedkhayat/iris_ann_scratch/images/setosa.jpg")));
                        imview.setImage(setosa);
                    }
                    case "versicolor" -> {
                        initialize();
                        hint.setVisible(true);
                        Image versicolor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mohamedkhayat/iris_ann_scratch/images/versicolor.jpg")));
                        imview.setImage(versicolor);
                    }
                    case "virginica" -> {
                        initialize();
                        hint.setVisible(true);
                        Image virginica = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mohamedkhayat/iris_ann_scratch/images/virginica.jpg")));
                        imview.setImage(virginica);
                    }
                    case null, default -> showError("something went wrong, try again");
                }
                sepal_width_input.clear();
                sepal_length_input.clear();
                petal_length_input.clear();
                petal_width_input.clear();
            }
            catch(Exception e){
               showError("Connection to the api failed, make sure the app.py is running and on port 5000,else change the url variable");
            }


        }
        catch(NumberFormatException e){
            showError("Invalid input, please enter valid numbers.");

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void display(MouseEvent mouseEvent) {
        if(!Objects.equals(this.Species, "")){
            imview.setVisible(false);
            textArea.setVisible(true);
            hint.setVisible(false);
            switch(this.Species){
                case "setosa" -> {
                    textArea.appendText("Iris Setosa\n\n" +
                            "Physical Characteristics:\n\n" +
                            "Iris setosa is often shorter than the other two species,\n" +
                            "with smaller petals.\n\n" +
                            "The flowers are typically deep blue to violet,\n" +
                            "although they can sometimes be white.\n\n" +
                            "The plant usually grows to about 30-50 cm in height.\n\n" +
                            "Habitat:\n\n" +
                            "Setosa is native to the temperate regions of North\n" +
                            "America and eastern Asia.\n\n" +
                            "It is commonly found in moist meadows, marshes,\n" +
                            "and along riverbanks.\n\n" +
                            "Cultural Significance:\n\n" +
                            "In Japan, Iris setosa is known as \"Ayame\" and has\n" +
                            "been a part of traditional Japanese gardens for\n" +
                            "centuries.\n\n" +
                            "Adaptability:\n\n" +
                            "This species is highly adaptable and can thrive\n" +
                            "in a variety of soil types, particularly in wet, boggy\n" +
                            "conditions.\n\n");
                }
                case "versicolor" -> {
                    textArea.appendText("Iris Versicolor\n\n" +
                            "Physical Characteristics:\n\n" +
                            "Iris versicolor, also known as the \"Blue Flag Iris,\"\n" +
                            "typically has larger petals than Setosa.\n\n" +
                            "The flowers are usually a mix of blue and purple,\n" +
                            "with yellow and white markings.\n\n" +
                            "Habitat:\n\n" +
                            "Native to North America, Versicolor is often found\n" +
                            "in wetlands, marshes, and along the edges of lakes\n" +
                            "and rivers.\n\n" +
                            "Toxicity:\n\n" +
                            "Iris versicolor is known to be toxic if ingested,\n" +
                            "particularly the rhizomes, which contain iridin, a\n" +
                            "toxic compound.\n\n" +
                            "Symbolism:\n\n" +
                            "It is the provincial flower of Quebec, Canada, and is\n" +
                            "often associated with the natural beauty of wetlands.\n\n");
                }
                case "virginica" -> {
                    textArea.appendText("Iris Virginica\n\n" +
                            "Physical Characteristics:\n\n" +
                            "Iris virginica, known as the \"Virginia Iris,\" tends\n" +
                            "to have tall, slender stems and large, showy flowers.\n\n" +
                            "The flowers are usually deep blue to violet, similar\n" +
                            "to Versicolor, but with a slightly different petal shape\n" +
                            "and structure.\n\n" +
                            "Habitat:\n\n" +
                            "Native to the southeastern United States, Virginica\n" +
                            "is commonly found in wet, swampy areas, including\n" +
                            "coastal plains and marshes.\n\n" +
                            "Growth:\n\n" +
                            "Iris virginica is known for its robust growth and can\n" +
                            "reach up to 90 cm in height, making it the tallest\n" +
                            "among the three species.\n\n" +
                            "Pollination:\n\n" +
                            "This species is pollinated by a variety of insects,\n" +
                            "including bees and butterflies, which are attracted\n" +
                            "to its vibrant flowers.\n\n");
                }

            }
        }
    }
}