from pydantic import BaseModel

class User(BaseModel):
    name: str
    gender: str
    age: str
    hobby: str
    trav_pref: str