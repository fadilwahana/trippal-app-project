from pydantic import BaseModel

class User(BaseModel):
    name: str
    
    class Config:
        schema_extra = {
            'example': {
                'name': 'Nadia Sinaga'
            }
        }

class BagOfWords(BaseModel):
    text: str

    class Config:
        schema_extra = {
            'example': {
                'text': 'wanita 20 memasak wisataalam'
            }
        }