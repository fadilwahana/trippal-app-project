from fastapi import FastAPI
from pydantic import BaseModel
from urllib.request import urlopen
import joblib

app = FastAPI()
# f = open('model.pickle', 'rb')
# model = pickle.load(f)
model = joblib.load(open('model-2.h5', 'rb'))

class User(BaseModel):
    name: str
    bag_of_words: str 

@app.post('/predict', tags=['PREDICT'])
def predict(user: User):
    return {
        'data': user
    }

if __name__ == '__main__':
    app.run(port=9000, debug=True)
    print(model)
