# finance-scraper

This project scrapes financial data from Yahoo and convert the data into a usable json format using spring boot and spring cloud function. So the code can be deployed to a FaaS (Function as a Service) platform.

## Setup

1. Clone this repo
2. Run `./gradlew clean build`

## Run

Run ``./gradlew bootRun`` to run this application

## Usage

Do a `POST` request with the following body on `http://localhost:8080/scrape`

```
{
    "ticker": "TSLA"
}
```

## Disclaimer

Please keep in mind that I've created this project for learning purposes only. During the creation of this afternoon project I've learned different new things. Scraping the finance data was just a good example for this. Please do not use this in production and check the terms of use of Yahoo's api.