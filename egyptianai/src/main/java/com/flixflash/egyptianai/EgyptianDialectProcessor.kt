package com.flixflash.egyptianai

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FlixFlash Contact Manager AI
 * 
 * @module EgyptianAI
 * @agent AI Voice Agent
 * @description معالج اللهجة المصرية للذكاء الاصطناعي
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * نظام متطور لمعالجة اللهجة المصرية العامية مع دعم:
 * - فهم المصطلحات والتعبيرات المصرية
 * - تحويل الفصحى إلى عامية مصرية
 * - تحليل السياق العاطفي والاجتماعي
 * - توليد ردود طبيعية بالعامية المصرية
 */
@Singleton
class EgyptianDialectProcessor @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val TAG = "EgyptianDialectProcessor"
        private const val PREFERENCES_NAME = "egyptian_dialect_prefs"
        
        // مفاتيح التفضيلات
        private val SELECTED_REGION = stringPreferencesKey("selected_egyptian_region")
        private val FORMALITY_LEVEL = stringPreferencesKey("formality_level")
        private val PREFERRED_EXPRESSIONS = stringPreferencesKey("preferred_expressions")
    }
    
    // DataStore للتفضيلات
    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
    
    /**
     * المناطق المصرية المدعومة مع خصائصها اللهجية
     */
    enum class EgyptianRegion(val displayName: String, val characteristics: String) {
        CAIRO("القاهرة", "لهجة قاهرية كلاسيكية"),
        ALEXANDRIA("الإسكندرية", "لهجة سكندرية مميزة"),
        UPPER_EGYPT("الصعيد", "لهجة صعيدية أصيلة"),
        DELTA("الدلتا", "لهجة دلتاوية"),
        CANAL("المحافظات الحدودية", "لهجة قناة السويس"),
        GENERAL("عامة مصرية", "لهجة مصرية شائعة")
    }
    
    /**
     * مستويات الرسمية في الحديث
     */
    enum class FormalityLevel(val displayName: String, val description: String) {
        VERY_INFORMAL("عامية جداً", "كلام الشارع والبيت"),
        INFORMAL("عامية", "كلام اجتماعي عادي"),
        SEMI_FORMAL("نصف رسمي", "مزج بين العامية والفصحى"),
        FORMAL("رسمي", "قريب من الفصحى"),
        VERY_FORMAL("رسمي جداً", "فصحى العصر")
    }
    
    /**
     * قاموس المصطلحات المصرية الشائعة
     */
    private val egyptianDictionary = mapOf(
        // تحيات ومجاملات
        "مرحبا" to listOf("أهلاً", "أهلين", "مرحبتين", "أهلاً وسهلاً"),
        "شكرا" to listOf("مرسي", "تسلم", "ربنا يخليك", "مشكور"),
        "عفوا" to listOf("العفو", "ولا يهمك", "عادي", "مش مشكلة"),
        "وداع" to listOf("سلام", "مع السلامة", "ربنا معاك", "باي"),
        
        // تعبيرات الموافقة والرفض
        "نعم" to listOf("آه", "أيوة", "صح", "تمام", "ماشي"),
        "لا" to listOf("لأ", "مش كده", "لا خالص", "مستحيل"),
        "موافق" to listOf("تمام", "ماشي", "أوكيه", "كده كويس"),
        
        // المشاعر والعواطف
        "سعيد" to listOf("فرحان", "مبسوط", "منشرح", "فى أحسن حال"),
        "حزين" to listOf("زعلان", "مكتئب", "مش فى مودي", "نفسيتي مش كويسة"),
        "غاضب" to listOf("زعلان", "مضايق", "متنرفز", "دمي حامي"),
        "متعب" to listOf("تعبان", "مجهد", "مرهق", "مش قادر"),
        
        // وصف الأشياء
        "جميل" to listOf("حلو", "جميل", "عسل", "كويس", "لذيذ"),
        "سيء" to listOf("وحش", "مش كويس", "بايخ", "فاضي"),
        "كبير" to listOf("كبير", "كتير", "واجد", "جامد"),
        "صغير" to listOf("صغير", "شوية", "كده"),
        
        // الطعام والشراب
        "طعام" to listOf("أكل", "طعمية", "فطار", "غدا", "عشا"),
        "ماء" to listOf("مية", "مياه"),
        "شاي" to listOf("شاي", "كشري", "شاي بالنعناع"),
        "قهوة" to listOf("قهوة", "أهوة"),
        
        // العائلة والأقارب
        "أب" to listOf("بابا", "والد", "الوالد"),
        "أم" to listOf("ماما", "والدة", "الوالدة"),
        "أخ" to listOf("أخويا", "أخ"),
        "أخت" to listOf("أختي", "أخت"),
        "جد" to listOf("جدو", "سيدو"),
        "جدة" to listOf("تيتا", "نونة", "ستّي"),
        
        // الوقت والزمن
        "اليوم" to listOf("النهارده", "اليوم"),
        "أمس" to listOf("إمبارح", "أمس"),
        "غدا" to listOf("بكرة", "غداً"),
        "الآن" to listOf("دلوقتي", "دلوقت", "الوقتي"),
        "بعدين" to listOf("بعدين", "كمان شوية", "لاحقاً"),
        
        // المكان والاتجاه
        "هنا" to listOf("هنا", "المكان ده"),
        "هناك" to listOf("هناك", "ناحية دي"),
        "فوق" to listOf("فوق", "فوقاني"),
        "تحت" to listOf("تحت", "تحتاني"),
        "يمين" to listOf("يمين", "الناحية اليمين"),
        "شمال" to listOf("شمال", "الناحية الشمال"),
        
        // أرقام بالعامية
        "واحد" to listOf("واحد", "وحدة"),
        "اثنان" to listOf("اتنين", "اتنين"),
        "ثلاثة" to listOf("تلاتة", "تلاته"),
        "أربعة" to listOf("أربعة", "أربعه"),
        "خمسة" to listOf("خمسة", "خمسه"),
        "عشرة" to listOf("عشرة", "عشره"),
        "مئة" to listOf("مية", "مئة"),
        "ألف" to listOf("ألف", "ألف")
    )
    
    /**
     * التعبيرات المصرية الشائعة حسب السياق
     */
    private val contextualExpressions = mapOf(
        "greeting" to listOf(
            "أهلاً أهلاً، إزيك؟",
            "مرحبتين، عامل إيه؟",
            "أهلين، كل سنة وإنت طيب",
            "أهلاً وسهلاً، نورت"
        ),
        "farewell" to listOf(
            "سلام عليكم، ربنا معاك",
            "مع السلامة، وصلت بالسلامة",
            "باي باي، ربنا يحفظك",
            "سلامات، إن شاء الله نشوفك قريب"
        ),
        "agreement" to listOf(
            "تمام كده، ماشي الحال",
            "أيوة صح، كده كويس",
            "بالظبط كده، إنت فاهم",
            "آه صحيح، كلامك سليم"
        ),
        "disagreement" to listOf(
            "لأ مش كده، ده مش صح",
            "معلش، أنا مش موافق",
            "مستحيل، ده صعب أوي",
            "لأ خالص، ده مش منطقي"
        ),
        "surprise" to listOf(
            "إيه ده! مش معقول!",
            "لا حول ولا قوة! إيه الحكاية دي!",
            "يا سلام! ده جامد!",
            "ربنا يستر! إيه اللي حصل ده!"
        ),
        "complaint" to listOf(
            "والله العظيم زهقت",
            "خلاص مش قادر أستحمل",
            "ده إحباط، مش كده الموضوع",
            "ربنا يصبرني، ده تعب"
        ),
        "happiness" to listOf(
            "والله أنا فرحان جداً!",
            "ده أحلى خبر سمعته!",
            "ربنا يديم الفرحة!",
            "أخيراً! ده اللي كنت مستنيه!"
        ),
        "encouragement" to listOf(
            "ربنا معاك، إنت تقدر",
            "خليك قوي، ده هيعدي",
            "إنت بطل، مش هتقف قدامك حاجة",
            "ثقة فيك، إنت هتعملها"
        )
    )
    
    /**
     * قواعد التحويل من الفصحى للعامية المصرية
     */
    private val conversionRules = mapOf(
        // تحويل الضمائر
        "أنت" to "إنت",
        "أنتم" to "إنتوا",
        "هم" to "هما",
        "هن" to "هما",
        "نحن" to "إحنا",
        
        // تحويل أدوات الاستفهام
        "ماذا" to "إيه",
        "كيف" to "إزاي",
        "متى" to "إمتى",
        "أين" to "فين",
        "لماذا" to "ليه",
        "كم" to "قد إيه",
        
        // تحويل حروف الجر والظروف
        "معي" to "معايا",
        "معك" to "معاك",
        "معه" to "معاه",
        "معها" to "معاها",
        "معنا" to "معانا",
        "معكم" to "معاكوا",
        "معهم" to "معاهم",
        
        // أفعال شائعة
        "أريد" to "عايز",
        "تريد" to "عايز",
        "يريد" to "عايز",
        "نريد" to "عايزين",
        "تريدون" to "عايزين",
        "يريدون" to "عايزين",
        
        "أعرف" to "أعرف",
        "تعرف" to "تعرف",
        "يعرف" to "يعرف",
        "نعرف" to "نعرف",
        
        "أقول" to "أقول",
        "تقول" to "تقول",
        "يقول" to "يقول",
        "تقولين" to "تقولي",
        
        // نفي
        "لا أعرف" to "مش عارف",
        "لا أريد" to "مش عايز",
        "لا أستطيع" to "مش قادر",
        "لا يمكن" to "مش ممكن"
    )
    
    /**
     * تحليل النص وتحديد نوع السياق
     */
    fun analyzeContext(text: String): ContextAnalysis {
        val lowerText = text.lowercase()
        
        // تحليل النغمة العاطفية
        val sentiment = when {
            containsWords(lowerText, listOf("فرحان", "مبسوط", "سعيد", "جميل", "رائع", "ممتاز")) -> Sentiment.POSITIVE
            containsWords(lowerText, listOf("زعلان", "حزين", "مضايق", "تعبان", "صعب", "مش كويس")) -> Sentiment.NEGATIVE
            containsWords(lowerText, listOf("مفيش", "عادي", "تمام", "ماشي", "أوكيه")) -> Sentiment.NEUTRAL
            else -> Sentiment.NEUTRAL
        }
        
        // تحديد نوع المحادثة
        val conversationType = when {
            containsWords(lowerText, listOf("أهلاً", "مرحبا", "إزيك", "عامل إيه")) -> ConversationType.GREETING
            containsWords(lowerText, listOf("سلام", "مع السلامة", "باي", "ربنا معاك")) -> ConversationType.FAREWELL
            containsWords(lowerText, listOf("عايز", "محتاج", "ممكن", "ياريت")) -> ConversationType.REQUEST
            containsWords(lowerText, listOf("إيه رأيك", "إيه اللي تقترحه", "نعمل إيه")) -> ConversationType.QUESTION
            containsWords(lowerText, listOf("مشكلة", "عندي شكوى", "مش راضي", "زهقت")) -> ConversationType.COMPLAINT
            containsWords(lowerText, listOf("شكراً", "مرسي", "ربنا يخليك", "تسلم")) -> ConversationType.GRATITUDE
            else -> ConversationType.GENERAL
        }
        
        // تحديد مستوى الرسمية
        val formalityDetected = when {
            containsWords(lowerText, listOf("حضرتك", "سيادتك", "المحترم", "يشرفني")) -> FormalityLevel.FORMAL
            containsWords(lowerText, listOf("إنت", "إزيك", "عامل إيه", "يلا")) -> FormalityLevel.INFORMAL
            else -> FormalityLevel.SEMI_FORMAL
        }
        
        return ContextAnalysis(
            sentiment = sentiment,
            conversationType = conversationType,
            formalityLevel = formalityDetected,
            keyPhrases = extractKeyPhrases(text),
            confidence = calculateConfidence(text)
        )
    }
    
    /**
     * تحويل النص من الفصحى إلى العامية المصرية
     */
    fun convertToEgyptianDialect(
        formalText: String, 
        targetRegion: EgyptianRegion = EgyptianRegion.GENERAL,
        formalityLevel: FormalityLevel = FormalityLevel.INFORMAL
    ): String {
        Log.d(TAG, "Converting text to Egyptian dialect: $formalText")
        
        var convertedText = formalText
        
        // تطبيق قواعد التحويل الأساسية
        conversionRules.forEach { (formal, dialect) ->
            convertedText = convertedText.replace(formal, dialect, ignoreCase = true)
        }
        
        // إضافة التعبيرات المناسبة حسب المنطقة
        convertedText = addRegionalFlavor(convertedText, targetRegion)
        
        // تعديل مستوى الرسمية
        convertedText = adjustFormality(convertedText, formalityLevel)
        
        Log.d(TAG, "Converted text: $convertedText")
        return convertedText
    }
    
    /**
     * توليد رد طبيعي بالعامية المصرية
     */
    fun generateEgyptianResponse(
        context: ContextAnalysis,
        userInput: String,
        personalityTrait: PersonalityTrait = PersonalityTrait.FRIENDLY
    ): EgyptianResponse {
        
        val baseResponse = when (context.conversationType) {
            ConversationType.GREETING -> generateGreeting(context.sentiment, personalityTrait)
            ConversationType.FAREWELL -> generateFarewell(context.sentiment, personalityTrait)
            ConversationType.REQUEST -> generateRequestResponse(userInput, personalityTrait)
            ConversationType.QUESTION -> generateQuestionResponse(userInput, personalityTrait)
            ConversationType.COMPLAINT -> generateComplaintResponse(context.sentiment, personalityTrait)
            ConversationType.GRATITUDE -> generateGratitudeResponse(personalityTrait)
            ConversationType.GENERAL -> generateGeneralResponse(userInput, context.sentiment, personalityTrait)
        }
        
        return EgyptianResponse(
            text = baseResponse,
            confidence = context.confidence,
            emotion = mapSentimentToEmotion(context.sentiment),
            suggestedVoiceType = suggestVoiceType(personalityTrait, context),
            culturalContext = "مصري أصيل"
        )
    }
    
    /**
     * اقتراح نوع الصوت المناسب حسب السياق
     */
    private fun suggestVoiceType(personality: PersonalityTrait, context: ContextAnalysis): String {
        return when {
            personality == PersonalityTrait.COMEDIAN -> "funny_comedian"
            context.conversationType == ConversationType.COMPLAINT -> "elderly_male"
            context.sentiment == Sentiment.POSITIVE -> "young_female"
            context.formalityLevel == FormalityLevel.FORMAL -> "elderly_male"
            else -> "young_male"
        }
    }
    
    // توليد التحيات
    private fun generateGreeting(sentiment: Sentiment, personality: PersonalityTrait): String {
        val greetings = when (personality) {
            PersonalityTrait.FORMAL -> listOf(
                "أهلاً وسهلاً، أزيك النهارده؟",
                "مرحباً، إزيك وإزي الأحوال؟",
                "أهلين، عامل إيه وإيه أخبارك؟"
            )
            PersonalityTrait.FRIENDLY -> listOf(
                "أهلاً يا حبيبي، إزيك؟",
                "مرحبتين، عامل إيه؟",
                "أهلين يا عسل، كيفك؟"
            )
            PersonalityTrait.COMEDIAN -> listOf(
                "أهلاً يا باشا، إزيك ولا عايز تتغدى؟ 😄",
                "مرحبتين، عامل إيه ولا الحر خلاك تذوب؟ 😂",
                "أهلين، كيفك ولا ناسي إزاي تتكلم؟ 😜"
            )
            PersonalityTrait.PROFESSIONAL -> listOf(
                "أهلاً وسهلاً، كيف يمكنني مساعدتك؟",
                "مرحباً، إزاي ممكن أساعدك النهارده؟",
                "أهلين، إيه اللي تحتاجه؟"
            )
        }
        return greetings.random()
    }
    
    // توليد الوداع
    private fun generateFarewell(sentiment: Sentiment, personality: PersonalityTrait): String {
        val farewells = when (personality) {
            PersonalityTrait.FORMAL -> listOf(
                "مع السلامة، ربنا يحفظك",
                "سلامات، ربنا معاك",
                "باي، وصلت بالسلامة"
            )
            PersonalityTrait.FRIENDLY -> listOf(
                "سلام يا حبيبي، ربنا معاك",
                "باي باي، إن شاء الله نشوفك قريب",
                "مع السلامة يا عسل"
            )
            PersonalityTrait.COMEDIAN -> listOf(
                "باي باي، ومتنساش تاكل عشان مترفعش! 😄",
                "سلامات، وبلاش تخليني أشتاقلك أوي! 😂",
                "مع السلامة، ومتنساش تبقى إنسان! 😜"
            )
            PersonalityTrait.PROFESSIONAL -> listOf(
                "شكراً لتواصلك معانا، مع السلامة",
                "باي، نتشرف بخدمتك مرة تانية",
                "سلامات، أي حاجة تانية أنا تحت أمرك"
            )
        }
        return farewells.random()
    }
    
    // توليد الرد على الطلبات
    private fun generateRequestResponse(userInput: String, personality: PersonalityTrait): String {
        val responses = when (personality) {
            PersonalityTrait.FORMAL -> listOf(
                "تمام، هحاول أساعدك في ده",
                "ماشي، خليني أشوف إيه اللي أقدر أعمله",
                "أوكيه، هوضحلك الموضوع ده"
            )
            PersonalityTrait.FRIENDLY -> listOf(
                "أكيد يا حبيبي، هساعدك فيها",
                "ماشي يا عسل، مين قالك لأ؟",
                "طبعاً، عينيا عينك"
            )
            PersonalityTrait.COMEDIAN -> listOf(
                "ماشي بس مش هاخد فلوس! 😄",
                "تمام، بس إنت متأكد إني اللي هقدر أساعدك؟ 😂",
                "أوكيه، بس لو مش عرفت تقولي عليّا كده! 😜"
            )
            PersonalityTrait.PROFESSIONAL -> listOf(
                "بالطبع، سأقوم بمساعدتك في هذا الأمر",
                "تمام، خليني أوضحلك الخطوات",
                "ماشي، هنشوف أحسن حل للموضوع ده"
            )
        }
        return responses.random()
    }
    
    // بقية المساعد الوظائف...
    private fun generateQuestionResponse(userInput: String, personality: PersonalityTrait): String {
        return when (personality) {
            PersonalityTrait.COMEDIAN -> "دي سؤال صعب، بس خليني أفكر وأرد عليك! 😄"
            else -> "سؤال حلو، خليني أفكر وأرد عليك"
        }
    }
    
    private fun generateComplaintResponse(sentiment: Sentiment, personality: PersonalityTrait): String {
        return when (personality) {
            PersonalityTrait.COMEDIAN -> "معلش، الدنيا كده، بس إن شاء الله هتبقى أحسن! 😊"
            else -> "متقلقش، إن شاء الله هنلاقي حل للموضوع ده"
        }
    }
    
    private fun generateGratitudeResponse(personality: PersonalityTrait): String {
        return when (personality) {
            PersonalityTrait.COMEDIAN -> "العفو، وده واجبي! بس المرة الجاية جيبلي حاجة! 😄"
            else -> "العفو، ده واجبي"
        }
    }
    
    private fun generateGeneralResponse(userInput: String, sentiment: Sentiment, personality: PersonalityTrait): String {
        return when (personality) {
            PersonalityTrait.COMEDIAN -> "إيه رأيك نغير الموضوع ونتكلم في حاجة مفرحة؟ 😄"
            else -> "آه، فهمت قصدك، إيه رأيك نتكلم أكتر في الموضوع ده؟"
        }
    }
    
    // مساعد الوظائف الأخرى
    private fun containsWords(text: String, words: List<String>): Boolean {
        return words.any { word -> text.contains(word, ignoreCase = true) }
    }
    
    private fun extractKeyPhrases(text: String): List<String> {
        // استخراج العبارات المفتاحية من النص
        return text.split(" ").filter { it.length > 3 }.take(5)
    }
    
    private fun calculateConfidence(text: String): Float {
        // حساب مستوى الثقة في التحليل
        return when {
            text.length > 50 -> 0.9f
            text.length > 20 -> 0.7f
            else -> 0.5f
        }
    }
    
    private fun addRegionalFlavor(text: String, region: EgyptianRegion): String {
        // إضافة نكهة محلية حسب المنطقة
        return when (region) {
            EgyptianRegion.ALEXANDRIA -> text.replace("كده", "كدا")
            EgyptianRegion.UPPER_EGYPT -> text.replace("إزيك", "كيفك")
            else -> text
        }
    }
    
    private fun adjustFormality(text: String, formality: FormalityLevel): String {
        // تعديل مستوى الرسمية
        return when (formality) {
            FormalityLevel.VERY_FORMAL -> text.replace("إنت", "حضرتك")
            FormalityLevel.VERY_INFORMAL -> text.replace("حضرتك", "إنت")
            else -> text
        }
    }
    
    private fun mapSentimentToEmotion(sentiment: Sentiment): String {
        return when (sentiment) {
            Sentiment.POSITIVE -> "سعيد"
            Sentiment.NEGATIVE -> "متعاطف"
            Sentiment.NEUTRAL -> "طبيعي"
        }
    }
    
    // Data Classes والـ Enums
    
    enum class Sentiment { POSITIVE, NEGATIVE, NEUTRAL }
    
    enum class ConversationType {
        GREETING, FAREWELL, REQUEST, QUESTION, COMPLAINT, GRATITUDE, GENERAL
    }
    
    enum class PersonalityTrait {
        FORMAL, FRIENDLY, COMEDIAN, PROFESSIONAL
    }
    
    data class ContextAnalysis(
        val sentiment: Sentiment,
        val conversationType: ConversationType,
        val formalityLevel: FormalityLevel,
        val keyPhrases: List<String>,
        val confidence: Float
    )
    
    data class EgyptianResponse(
        val text: String,
        val confidence: Float,
        val emotion: String,
        val suggestedVoiceType: String,
        val culturalContext: String
    )
    
    /**
     * حفظ التفضيلات
     */
    suspend fun savePreferences(
        region: EgyptianRegion,
        formality: FormalityLevel,
        preferredExpressions: List<String>
    ) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_REGION] = region.name
            preferences[FORMALITY_LEVEL] = formality.name
            preferences[PREFERRED_EXPRESSIONS] = preferredExpressions.joinToString(",")
        }
        Log.d(TAG, "Preferences saved: region=$region, formality=$formality")
    }
    
    /**
     * قراءة التفضيلات
     */
    fun getPreferences(): Flow<EgyptianDialectPreferences> {
        return context.dataStore.data.map { preferences ->
            EgyptianDialectPreferences(
                selectedRegion = EgyptianRegion.valueOf(
                    preferences[SELECTED_REGION] ?: EgyptianRegion.GENERAL.name
                ),
                formalityLevel = FormalityLevel.valueOf(
                    preferences[FORMALITY_LEVEL] ?: FormalityLevel.INFORMAL.name
                ),
                preferredExpressions = preferences[PREFERRED_EXPRESSIONS]?.split(",") ?: emptyList()
            )
        }
    }
    
    data class EgyptianDialectPreferences(
        val selectedRegion: EgyptianRegion,
        val formalityLevel: FormalityLevel,
        val preferredExpressions: List<String>
    )
}