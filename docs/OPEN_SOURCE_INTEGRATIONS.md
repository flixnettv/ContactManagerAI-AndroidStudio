# Open-Source Integrations

- STT: Vosk Server
  - Image: `alphacep/vosk-server`
  - Source: https://github.com/alphacep/vosk-server
  - Models: https://alphacephei.com/vosk/models (Arabic `vosk-model-small-ar-0.22`)

- TTS: Piper (Rhasspy)
  - Image: `rhasspy/tts-piper`
  - Source: https://github.com/rhasspy/piper
  - Voices: https://huggingface.co/rhasspy/piper-voices

- AI Agent (Dialogue): Rasa OSS
  - Image: `rasa/rasa:3.6`
  - Source: https://github.com/RasaHQ/rasa

- Backend (CallerID/Spam): FastAPI (Python)
  - Source: https://github.com/tiangolo/fastapi (we provide our own microservice implementation)

- Kotlin/Compose App
  - Jetpack Compose (Apache 2.0): https://github.com/androidx/androidx
  - Hilt, Room, Retrofit, OkHttp (Apache 2.0)

Usage
- Compose up services under `infra/`
- Configure app endpoints in Advanced Settings or via BuildConfig defaults