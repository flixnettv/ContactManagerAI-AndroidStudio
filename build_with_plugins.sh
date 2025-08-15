#!/bin/bash
echo "🔨 بناء FlixFlash Contact Manager AI مع جميع الإضافات والوكلاء..."

# تنظيف البناء السابق
./gradlew clean

# بناء APK للتطوير
echo "📱 بناء APK للتطوير..."
./gradlew assembleDebug

# بناء APK للإنتاج
echo "🚀 بناء APK للإنتاج..."
./gradlew assembleRelease

echo "✅ تم الانتهاء من البناء!"
echo "📱 APK التطوير: app/build/outputs/apk/debug/"
echo "🚀 APK الإنتاج: app/build/outputs/apk/release/"
