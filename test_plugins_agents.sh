#!/bin/bash
echo "🧪 اختبار الإضافات والوكلاء..."

echo "🔌 فحص الإضافات:"
find app/libs -name "*plugin*" -o -name "*advanced*" -o -name "*voice*" -o -name "*smart*" | head -10

echo ""
echo "🤖 فحص الوكلاء:"
find app/libs -name "*agent*" | head -10

echo ""
echo "📊 إحصائيات المكتبات:"
echo "عدد ملفات JAR: $(find app/libs -name "*.jar" | wc -l)"
echo "حجم المجلد: $(du -sh app/libs)"

echo "✅ انتهى الاختبار"
