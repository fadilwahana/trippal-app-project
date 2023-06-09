from fastapi import FastAPI
from .models import User

app = FastAPI()

@app.get('/', tags=['ROOT'])
def root():
    return {
        'message': 'Successfully connected'
    }

@app.post('/predict', tags=['PREDICT'])
def predict(user: User):
    name = user.name
    gender = user.gender
    age = user.age
    hobby = user.hobby
    travel = user.trav_pref

    list_of_words = [gender, age, hobby, travel]
    bag_of_words = ' '.join(list_of_words)
    
    return {
        'message': 'Success',
        'data': {
            'name': name,
            'bag_of_words': bag_of_words   
        } 
    }