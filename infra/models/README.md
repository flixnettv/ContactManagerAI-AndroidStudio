# Models (Open-Source)

Vosk (Arabic):
- Download `vosk-model-small-ar-0.22` from:
  - https://alphacephei.com/vosk/models
  - or GitHub mirror
- Unzip into `infra/models/vosk/vosk-model-small-ar-0.22`

Piper (Arabic voice example):
- Download `ar_JO-kareem-low.onnx` and `.onnx.json` from:
  - https://huggingface.co/rhasspy/piper-voices/tree/main/ar
- Place as `infra/models/piper/ar_voice.onnx` and `infra/models/piper/ar_voice.json`

Then run docker-compose in `infra/`:
```bash
docker compose up -d
```