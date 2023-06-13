import os
import uvicorn
import traceback
import tensorflow as tf
import pandas as pd

from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import CountVectorizer
from fastapi import FastAPI, Response
from joblib import load
from models import User, BagOfWords

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

app = FastAPI()

# This endpoint is for a test (or health check) to this server
@app.get("/", tags=['ROOT'])
def root():
    return "Hello world from ML endpoint!"

@app.post("/recommendations", tags=['ML predictions'])
def recommendations(req: User, response: Response):
    try:
        # In here you will get text sent by the user
        name = req.name
        print("Uploaded text:", name)

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

        # Predict the data
        rec = recommendations(name, cosine_sim)
        return {
            'data': rec
        }
    except Exception as e:
        traceback.print_exc()
        response.status_code = 500
        return "Internal Server Error"


@app.post("/score", tags=['ML predictions'])
def tf_score(req: BagOfWords, response: Response):
    try:
        # In here you will get text sent by the user
        text = req.text
        print("Uploaded text:", text)

        def matching(text_input):
            tf_model = tf.keras.models.load_model('TripPal.h5', compile=False)
            clf = load('Model CV.joblib')
            count_matrix = clf.transform([text])
            print(count_matrix)
            save = tf_model.predict(count_matrix)
            return save.tolist()

        return {
            'data': matching(text)
        }

    except Exception as e:
        traceback.print_exc()
        response.status_code = 500
        return "Internal Server Error"


# Starting the server
port = os.environ.get("PORT", 8080)
print(f"Listening to http://0.0.0.0:{port}")
uvicorn.run(app, host='0.0.0.0', port=port)
