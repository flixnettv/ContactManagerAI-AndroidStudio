# Infra (Open-Source Services)

Services:
- Vosk (STT): alphacep/vosk-server
- Piper (TTS): rhasspy/tts-piper
- Rasa (dialogue): rasa/rasa

Quick start:
```bash
cd infra
# Prepare models (see models/README.md)
docker compose up -d
```

Rasa project:
```bash
cd infra/rasa_project
# Train NLU model
docker run --rm -v $(pwd):/app rasa/rasa:3.6.20 train nlu --fixed-model-name nlu
# Run server (already in compose), else:
docker run --rm -p 5005:5005 -v $(pwd):/app rasa/rasa:3.6.20 run --enable-api --cors "*"
```