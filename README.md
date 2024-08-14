## Overview

This repository features an Artificial Neural Network (ANN) built from scratch using NumPy and Python to classify the Iris dataset. The project provides an understanding of constructing and training an ANN without relying on high-level libraries like TensorFlow or PyTorch.

## Components

- **train.py:** Contains the code to train the ANN model. This script uses NumPy to build and train the neural network on the Iris dataset.

- **app.py:** Provides a Flask API for interacting with the trained model. This API allows users to send data to the model and receive predictions.

- **Java Frontend GUI:** 
  - Located in the `java` folder.
  - Includes code for a graphical user interface application built with JavaFX.
  - Provides an interface for users to input data and visualize predictions from the ANN model.

- **Executable:**
  - Located in the root of the repository.
  - Contains a compiled version of the Java GUI application.
  - **To run the executable:**
    1. Start the Flask API by running `python app.py`.
    2. Open the executable file to launch the JavaFX GUI application.
    3. Use the GUI to interact with the model and view predictions.

## Dependencies

**Python:**

- `numpy`: For numerical operations and matrix manipulations.
- `pandas`: For handling the Iris dataset.
- `flask`: For creating the API to interact with the model.

**Java:**

- `JavaFX`: For building the graphical user interface.

## Dataset

- **Iris Dataset:** Selected for its simplicity and educational value. The dataset’s limited number of instances may impact the model’s robustness, particularly in classifying the versicolor class.

## Results

- **Validation Loss Curve and Confusion Matrix:** Visual representations of model performance. See the included image for details.

  ![Screenshot 2024-08-07 205859](https://github.com/user-attachments/assets/1ce5919d-c831-49b2-9cc3-76d12d49ac42)

- **Final Result:** The video demonstrates the final outcome of the project.

  [View Final Result Video](https://github.com/user-attachments/assets/e9ea40cb-1dbb-4d68-b70d-8fb2377d2d45)

## Future Work

Future improvements will include:

- Revisiting the project with a more suitable dataset to enhance model performance and generalizability.
- Further refining the ANN architecture and training process to address current limitations.
