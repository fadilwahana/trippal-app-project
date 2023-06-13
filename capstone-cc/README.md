# Trippal ML API
Welcome to our ml API

# How to setup with GCP
1. Open Cloud Shell
2. Clone this project using this command:
    - git clone https://github.com/fadilwahana/trippal-app-project.git
3. Change directory to capstone-cc in trippal-app-project folder
    - cd trippal-app-project/capstone-cc
4. Build a container
    - gcloud builds submit \ ``\``
      --tag gcr.io/$GOOGLE_CLOUD_PROJECT/ml-api:0.1
5. Deploy the built container
    - gcloud run deploy ml-api \ ``\``
      --image gcr.io/$GOOGLE_CLOUD_PROJECT/ml-api:0.1 \ ``\``
      --platform managed \ ``\``
      --region asia-southeast2 \ ``\``
      --allow-unauthenticated \ ``\``
      --max-instances=2 \ ``\``
      --cpu 2 \ ``\``
      --memory 8G
6. Once the server is running, check the API documentation using {service URL}/docs
