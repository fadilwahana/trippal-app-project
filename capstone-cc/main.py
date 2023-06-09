import uvicorn
    

if __name__ == '__main__':
    # Run apps on GCP
    # uvicorn.run('app.app:app', host='0.0.0.0', port=8000, reload=True)

    # Run apps locally
    uvicorn.run('app.app:app', port=8000, reload=True)