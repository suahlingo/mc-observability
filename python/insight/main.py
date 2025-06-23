from app.api.anomaly import anomaly
from app.api.prediction import prediction
from app.api.log_analysis import log_analysis
from app.core.otel.trace import init_otel_trace
from config.ConfigManager import ConfigManager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

import uvicorn


config = ConfigManager()

app = FastAPI(
    title='Insight Module DOCS',
    description='mc-observability insight module'
)

origins = ['*']

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*']
)
init_otel_trace(app)

api_prefix = config.get_prefix()
app.include_router(anomaly.router, prefix=api_prefix, tags=["[Insight] Anomaly Detection"])
app.include_router(prediction.router, prefix=api_prefix, tags=["[Insight] Prediction"])
app.include_router(log_analysis.router, prefix=api_prefix, tags=["[Insight] Log Analysis"])

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=9002, log_config="config/log.ini")
