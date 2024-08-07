from flask import Flask,jsonify,request
from train import forward_pass
import numpy as np
app = Flask(__name__)
CLASSES = ['setosa', 'versicolor', 'virginica']
def load_weights(model_name="model"):
    data = np.load("python/"+model_name+'.npz')
    W1 = data['W1']
    b1 = data['b1']
    W2 = data['W2']
    b2 = data['b2']
    W3 = data['W3']
    b3 = data['b3']
    return W1,b1,W2,b2,W3,b3

def make_prediction(X,W1,b1,W2,b2,W3,b3,CLASSES):
    predictions = forward_pass(X, W1, b1, W2, b2, W3, b3)[-1]
    predicted_label = np.argmax(predictions,axis=1)[0]
    print(predicted_label)
    return CLASSES[predicted_label]

X = [5.1,3.5,1.4,0.2]
    
@app.route('/predict',methods=['POST'])
def predict_species():
    data = request.get_json()
    if 'features' not in data:
        return jsonify({"error": "Missing 'features' in request data"}), 400
    
    X = data['features']
    try:
        X = np.array(X).reshape(1, -1)
    except Exception as e:
        return jsonify({"error": str(e)}), 400

    W1,b1,W2,b2,W3,b3 =load_weights()
    try:
        prediction = make_prediction(X,W1,b1,W2,b2,W3,b3,CLASSES)
    except Exception as e:
        return jsonify({"Error":str(e)}),500
    return jsonify(prediction), 200
    
if __name__=="__main__":
    app.run(debug=True)