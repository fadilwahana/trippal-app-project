from flask import Flask, request, jsonify
import pickle
from urllib.request import urlopen
import utils

app = Flask(__name__)
# f = open('model.pickle', 'rb')
# model = pickle.load(f)
model = pickle.load(urlopen("https://storage.googleapis.com/trippal-ml-model/model.pickle"))

# Still error
@app.route('/predict', methods=['POST'])
def predict():
    if request.method == 'POST':
        data = request.data['input']
   
        model_input = utils.preprocessing(data)
        results = model.predict(model_input)
    return jsonify({"prediction": results})

if __name__ == '__main__':
    app.run(port=9000, debug=True)