package com.mohamedkhayat.iris_ann_scratch;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.client5.http.fluent.Request;

import java.util.Objects;

public class HelloController {
    String sepal_length;
    String sepal_width;
    String pet_length;
    String pet_width;
    @FXML
    private ImageView imview;
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

    @FXML
    void predict(){
        String sepalLength = sepal_length_input.getText();
        String sepalWidth = sepal_width_input.getText();
        String petalLength = petal_length_input.getText();
        String petalWidth = petal_width_input.getText();

        try{
            //String sepalLength = "5.7";
            //String sepalWidth = "2.6";
            //String petalLength = "3.5";
            //String petalWidth = "1.0";
            double sepalLengthVal = Double.parseDouble(sepalLength);
            double sepalWidthVal = Double.parseDouble(sepalWidth);
            double petalLengthVal = Double.parseDouble(petalLength);
            double petalWidthVal = Double.parseDouble(petalWidth);
            IrisInput input = new IrisInput(sepalLengthVal, sepalWidthVal, petalLengthVal, petalWidthVal);
            Gson gson = new Gson();
            String json = gson.toJson(input);
            System.out.println(json); // Pretty print with indentation of 4 spaces

            String apiUrl ="http://localhost:5000/predict";

            String response = Request.post(apiUrl)
                    .bodyString(json, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();
            response = response.trim().replace("\"", "");
            //System.out.println("Response from API: " + response);
            System.out.println("Raw response from API: '" + response + "'");

            switch (response) {
                case "setosa" -> {
                    Image setosa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mohamedkhayat/iris_ann_scratch/images/setosa.jpg")));
                    imview.setImage(setosa);
                }
                case "versicolor" -> {
                    Image versicolor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mohamedkhayat/iris_ann_scratch/images/versicolor.jpg")));
                    imview.setImage(versicolor);
                }
                case "virginica" -> {
                    Image virginica = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mohamedkhayat/iris_ann_scratch/images/virginica.jpg")));
                    imview.setImage(virginica);
                }
                case null, default -> System.out.println("ERROR");
            }

        }
        catch(NumberFormatException e){
            System.out.println("Invalid input, please enter valid numbers.");

        }
        catch(Exception e){
            e.printStackTrace();


        }

    }
}