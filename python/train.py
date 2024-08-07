import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

def train_test_split(X,y,test_size=0.2,random_state=None):
    if random_state==None:
        np.random.seed(random_state)
    
    n = X.shape[0]
    
    indices = np.arange(n)
    np.random.shuffle(indices)
    
    split_index = int(n * (1 - test_size))
    train_indices = indices[:split_index]
    test_indices = indices[split_index:]
    
    X_train,X_test = X[train_indices],X[test_indices]
    y_train,y_test = y[train_indices],y[test_indices]
    
    return X_train,X_test,y_train,y_test

def softmax(z):
    shift_z = z - np.max(z, axis=1, keepdims=True)
    ez = np.exp(shift_z)
    return ez / np.sum(ez, axis=1, keepdims=True)

def ReLU(z):
    return np.maximum(0, z)

def forward_pass(X, W1, b1, W2, b2, W3, b3):
    Z1 = np.matmul(X, W1) + b1
    a1 = ReLU(Z1)
    Z2 = np.matmul(a1, W2) + b2
    a2 = ReLU(Z2)
    Z3 = np.matmul(a2, W3) + b3
    y_pred = softmax(Z3)
    return Z1, a1, Z2, a2, Z3, y_pred

def categorical_crossentropy(y_true,y_pred):
    y_pred = np.clip(y_pred, 1e-15, 1 - 1e-15)
    loss = -np.sum(y_true * np.log(y_pred)) / y_true.shape[0]

    return loss

def backprop(X, y_true, Z1, a1, Z2, a2, Z3, y_pred, W2, W3):
    m = X.shape[0]

    # Output layer gradients
    dZ3 = y_pred - y_true
    dW3 = np.matmul(a2.T, dZ3) / m
    db3 = np.sum(dZ3, axis=0, keepdims=True) / m

    # Hidden layer 2 gradients
    dA2 = np.matmul(dZ3, W3.T)
    dZ2 = dA2 * (Z2 > 0).astype(float)  # ReLU derivative
    dW2 = np.matmul(a1.T, dZ2) / m
    db2 = np.sum(dZ2, axis=0, keepdims=True) / m

    # Hidden layer 1 gradients
    dA1 = np.matmul(dZ2, W2.T)
    dZ1 = dA1 * (Z1 > 0).astype(float)  # ReLU derivative
    dW1 = np.matmul(X.T, dZ1) / m
    db1 = np.sum(dZ1, axis=0, keepdims=True) / m

    return dW1, db1, dW2, db2, dW3, db3
def update_parameters(W1, b1, W2, b2, W3, b3, dW1, db1, dW2, db2, dW3, db3, learning_rate):
    W1 -= learning_rate * dW1
    b1 -= learning_rate * db1
    W2 -= learning_rate * dW2
    b2 -= learning_rate * db2
    W3 -= learning_rate * dW3
    b3 -= learning_rate * db3
    return W1, b1, W2, b2, W3, b3

def initialize_parameters(input_size, hidden_size, output_size):
    W1 = np.random.randn(input_size, hidden_size) * np.sqrt(1. / input_size)
    b1 = np.zeros((1, hidden_size))
    W2 = np.random.randn(hidden_size, hidden_size) * np.sqrt(1. / hidden_size)
    b2 = np.zeros((1, hidden_size))
    W3 = np.random.randn(hidden_size, output_size) * np.sqrt(1. / hidden_size)
    b3 = np.zeros((1, output_size))
    return W1, b1, W2, b2, W3, b3

def precision(y_true,y_pred):
    y_pred_labels = np.argmax(y_pred,axis=1)
    y_true_labels = np.argmax(y_true,axis=1)
    
    classes = np.unique(y_true_labels)
    precisions = []

    for clss in classes:
        true_positive = np.sum((y_true_labels == clss) & (y_pred_labels == clss))
        false_positive = np.sum((y_true_labels !=clss) & (y_pred_labels == clss))
        precision = true_positive / (true_positive + false_positive) if (true_positive + false_positive) > 0 else 0
        precisions.append(precision)
    return np.mean(precisions)

def recall(y_true,y_pred):
    y_pred_labels = np.argmax(y_pred,axis=1)
    y_true_labels = np.argmax(y_true,axis=1)
    
    classes = np.unique(y_true_labels)
    recalls = []

    for clss in classes:
        true_positive = np.sum((y_true_labels == clss) & (y_pred_labels == clss))
        false_negative = np.sum((y_true_labels == clss) & (y_pred_labels != clss))
        recall = true_positive / (true_positive + false_negative) if (true_positive + false_negative) > 0 else 0
        recalls.append(recall)
    return np.mean(recalls)

def f1_score(y_true, y_pred):
    prec = precision(y_true, y_pred)
    rec = recall(y_true, y_pred)
    return 2 * (prec * rec) / (prec + rec) if (prec + rec) > 0 else 0

def compute_metrics(y_true, y_pred):
    precision_val = precision(y_true, y_pred)
    recall_val = recall(y_true, y_pred)
    f1_val = f1_score(y_true, y_pred)
    return precision_val, recall_val, f1_val

def compute_confusion_matrix(y_true, y_pred):
    y_pred_labels = np.argmax(y_pred,axis=1)
    y_true_labels = np.argmax(y_true,axis=1)
    
    classes = np.unique(y_true_labels)
    num_classes = len(classes)
    
    cm = np.zeros((num_classes, num_classes), dtype=int)
    
    for true_label, pred_label in zip(y_true_labels, y_pred_labels):
        cm[true_label, pred_label] += 1
    
    return cm
if __name__=="__main__":
    url = "https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data"
    columns = ['sepal_length', 'sepal_width', 'petal_length', 'petal_width', 'species']
    iris = pd.read_csv(url, header=None, names=columns)

    iris = pd.get_dummies(iris,columns=['species']).astype(int)

    y = iris[['species_Iris-setosa', 'species_Iris-versicolor', 'species_Iris-virginica']].values
    X = iris[['sepal_length', 'sepal_width', 'petal_length', 'petal_width']].values

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    input_size = X.shape[1]  
    hidden_size = 32
    output_size = y.shape[1]  

    W1, b1, W2, b2, W3, b3 = initialize_parameters(input_size, hidden_size, output_size)
    epochs = 200
    learning_rate = 0.01
    losses = []
    for epoch in range(epochs):
        
        Z1, a1, Z2, a2, Z3, y_pred = forward_pass(X_train, W1, b1, W2, b2, W3, b3)
        
        loss = categorical_crossentropy(y_train, y_pred)
        losses.append(loss)
        print(f'Epoch {epoch+1}, Loss: {loss:.4f}')
        
        dW1, db1, dW2, db2, dW3, db3 = backprop(X_train, y_train, Z1, a1, Z2, a2, Z3, y_pred, W2, W3)
        
        W1, b1, W2, b2, W3, b3 = update_parameters(W1, b1, W2, b2, W3, b3, dW1, db1, dW2, db2, dW3, db3, learning_rate)

    y_pred_test = forward_pass(X_test, W1, b1, W2, b2, W3, b3)[-1]
    precision_val, recall_val, f1_val = compute_metrics(y_test, y_pred_test)

    print(f'Test Precision: {precision_val:.4f}, Recall: {recall_val:.4f}, F1 Score: {f1_val:.4f}')
    fig, ax = plt.subplots(1, 2, figsize=(15, 6))
    ax[0].plot(losses)
    ax[0].set_xlabel("Epoch")
    ax[0].set_ylabel("Loss")
    ax[0].set_title("Loss per Epoch")

    class_names = ['setosa', 'versicolor', 'virginica']
    cm = compute_confusion_matrix(y_true=y_test,y_pred=y_pred_test)
    sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', xticklabels=class_names, yticklabels=class_names)
    ax[1].set_xlabel('Predicted Labels')
    ax[1].set_ylabel('True Labels')
    ax[1].set_title('Confusion Matrix')
    plt.tight_layout()
    plt.show()

    save =input("save the model ? ")
    if save == "y":
        np.savez('model.npz', W1=W1, b1=b1, W2=W2, b2=b2, W3=W3, b3=b3)
        