# README
# Hello everyone, in here I (Kaenova | Bangkit Mentor ML-20) 
# will give you some headstart on createing ML API. 
# Please read every lines and comments carefully. 
# 
# I give you a headstart on text based input and image based input API. 
# To run this server, don't forget to install all the libraries in the
# requirements.txt simply by "pip install -r requirements.txt" 
# and then use "python main.py" to run it
# 
# For ML:
# Please prepare your model either in .h5 or saved model format.
# Put your model in the same folder as this main.py file.
# You will load your model down the line into this code. 
# There are 2 option I give you, either your model image based input 
# or text based input. You need to finish functions "def predict_text" or "def predict_image"
# 
# For CC:
# You can check the endpoint that ML being used, eiter it's /predict_text or 
# /predict_image. For /predict_text you need a JSON {"text": "your text"},
# and for /predict_image you need to send an multipart-form with a "uploaded_file" 
# field. you can see this api documentation when running this server and go into /docs
# I also prepared the Dockerfile so you can easily modify and create a container iamge
# The default port is 8080, but you can inject PORT environement variable.
# 
# If you want to have consultation with me
# just chat me through Discord (kaenova#2859) and arrange the consultation time
#
# Share your capstone application with me! 🥳
# Instagram @kaenovama
# Twitter @kaenovama
# LinkedIn /in/kaenova

# Start your code here!

import os
import uvicorn
import traceback
import tensorflow as tf
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity

from sklearn.feature_extraction.text import CountVectorizer
from pydantic import BaseModel
from urllib.request import Request
from fastapi import FastAPI, Response, UploadFile
from utils import load_image_into_numpy_array
from joblib import dump, load

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

# Initialize Model
# If you already put your model in the same folder as this main.py
# You can load .h5 model or any model below this line

# If you use h5 type uncomment line below
# model = tf.keras.models.load_model('./TripPal.h5')
# If you use saved model type uncomment line below
# model = tf.saved_model.load("./my_model_folder")

app = FastAPI()


# This endpoint is for a test (or health check) to this server
@app.get("/")
def index():
    return "Hello world from ML endpoint!"


# If your model need text input use this endpoint!
class RequestText(BaseModel):
    text: str


@app.post("/predict_text")
def predict_text(req: RequestText, response: Response):
    try:
        # In here you will get text sent by the user
        text = req.text
        print("Uploaded text:", text)

        # Step 1: (Optional) Do your text preprocessing #

        data = pd.read_csv('dummy.csv')
        data.head()

        # Preprocess the data

        data['Preferensi Perjalanan'] = data['Preferensi Perjalanan'].apply(lambda x: ''.join(x.split()).lower())
        data['Jenis Kelamin'] = data['Jenis Kelamin'].apply(lambda x: x.lower())
        data['Hobi'] = data['Hobi'].apply(lambda x: x.lower())
        data.set_index('Nama', inplace=True)
        data.head()

        # Create bag of words for data

        data['bag_of_words'] = data['Jenis Kelamin'].astype(str) + ' ' + data['Umur'].astype(str) + ' ' + ' ' + data[
            'Hobi'].astype(str) + ' ' + data['Preferensi Perjalanan'].astype(str)
        data.drop(columns=['Jenis Kelamin', 'Umur', 'Hobi', 'Preferensi Perjalanan'], inplace=True)
        data.head()

        # Create count matrix for data
        count = CountVectorizer()
        count_matrix = count.fit_transform(data['bag_of_words'])

        indices = pd.Series(data.index)
        cosine_sim = cosine_similarity(count_matrix, count_matrix)

        def recommendations(nama, cosine_sim=cosine_sim):
            recommended_people = []
            idx = indices[indices == nama].index[0]
            score_series = pd.Series(cosine_sim[idx]).sort_values(ascending=False)
            top_10_indexes = list(score_series.iloc[1:11].index)
            for i in top_10_indexes:
                recommended_people.append(list(data.index)[i])
            return recommended_people

        # Step 2: Prepare your data to your model

        rec = recommendations(text, cosine_sim)

        # Step 3: Predict the data
        # result = model.predict(...)

        # Step 4: Change the result your determined API output

        return rec
    except Exception as e:
        traceback.print_exc()
        response.status_code = 500
        return "Internal Server Error"


@app.post("/predict_tf")
def predict_tf(req: RequestText, response: Response):
    try:
        # In here you will get text sent by the user
        text = req.text
        print("Uploaded text:", text)

        # Step 1: (Optional) Do your text preprocessing

        def matching(text_input):
            tf_model = tf.keras.models.load_model('TripPal.h5', compile=False)
            clf = load('Model CV.joblib')
            count_matrix = clf.transform([text])
            print(count_matrix)
            save = tf_model.predict(count_matrix)
            return save.tolist()

        # Step 2: Prepare your data to your model

        # Step 3: Predict the data
        # result = model.predict(...)

        # Step 4: Change the result your determined API output

        return matching(text)

    except Exception as e:
        traceback.print_exc()
        response.status_code = 500
        return "Internal Server Error"


# Starting the server
# Your can check the API documentation easily using /docs after the server is running
port = os.environ.get("PORT", 8080)
print(f"Listening to http://0.0.0.0:{port}")
uvicorn.run(app, host='0.0.0.0', port=port)
