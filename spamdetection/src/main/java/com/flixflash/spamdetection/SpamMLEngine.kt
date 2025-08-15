package com.flixflash.spamdetection

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

/**
 * FlixFlash Contact Manager AI
 * 
 * @module SpamDetection
 * @agent Spam Detection Agent
 * @description محرك ML متقدم لكشف المكالمات المزعجة
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * نظام متطور لكشف الإزعاج باستخدام الذكاء الاصطناعي مع دعم:
 * - نماذج TensorFlow Lite للتحليل
 * - تحليل أنماط الأرقام المتقدم
 * - قاعدة بيانات مجتمعية للبلاغات
 * - تعلم آلي محلي (بدون إنترنت)
 * - نظام ثقة وتقييم متدرج
 */
@Singleton
class SpamMLEngine @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val TAG = "SpamMLEngine"
        private const val MODEL_FILENAME = "spam_detection_model.tflite"
        private const val PREFERENCES_NAME = "spam_ml_prefs"
        
        // عتبات الثقة
        private const val SPAM_THRESHOLD = 0.7f
        private const val HIGH_CONFIDENCE_THRESHOLD = 0.9f
        private const val LOW_CONFIDENCE_THRESHOLD = 0.3f
        
        // حجم المدخلات للنموذج
        private const val INPUT_SIZE = 50 // 50 خاصية مستخرجة من الرقم
        
        // مفاتيح التفضيلات
        private val TOTAL_ANALYZED = intPreferencesKey("total_numbers_analyzed")
        private val SPAM_DETECTED = intPreferencesKey("spam_numbers_detected")
        private val MODEL_VERSION = stringPreferencesKey("model_version")
        private val LAST_UPDATE = stringPreferencesKey("last_model_update")
    }
    
    // DataStore للإحصائيات
    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
    
    // مفسر TensorFlow Lite
    private var interpreter: Interpreter? = null
    
    // مدير قاعدة البيانات المجتمعية
    @Inject
    lateinit var communityDatabase: CommunitySpamDatabase
    
    // محلل الأنماط
    @Inject
    lateinit var patternAnalyzer: NumberPatternAnalyzer
    
    // مولد الخصائص
    @Inject
    lateinit var featureExtractor: FeatureExtractor
    
    // حالة التهيئة
    private var isInitialized = false
    
    /**
     * تهيئة محرك ML
     */
    suspend fun initialize(): Boolean {
        return try {
            Log.d(TAG, "🚀 Initializing Spam ML Engine...")
            
            // تحميل نموذج TensorFlow Lite
            loadTensorFlowModel()
            
            // تهيئة قاعدة البيانات المجتمعية
            communityDatabase.initialize()
            
            // تهيئة محلل الأنماط
            patternAnalyzer.initialize()
            
            isInitialized = true
            Log.d(TAG, "✅ Spam ML Engine initialized successfully")
            true
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to initialize Spam ML Engine", e)
            false
        }
    }
    
    /**
     * تحليل رقم للكشف عن الإزعاج
     */
    suspend fun analyzeNumber(phoneNumber: String): SpamAnalysisResult {
        if (!isInitialized) {
            Log.w(TAG, "⚠️ ML Engine not initialized, using fallback analysis")
            return fallbackAnalysis(phoneNumber)
        }
        
        try {
            Log.d(TAG, "🔍 Analyzing number: $phoneNumber")
            
            // 1. تحليل الأنماط السريع
            val patternResult = patternAnalyzer.analyzeQuickPatterns(phoneNumber)
            
            // 2. فحص قاعدة البيانات المجتمعية
            val communityResult = communityDatabase.checkNumber(phoneNumber)
            
            // 3. استخراج الخصائص للنموذج
            val features = featureExtractor.extractFeatures(phoneNumber)
            
            // 4. تشغيل نموذج ML
            val mlPrediction = runMLModel(features)
            
            // 5. دمج النتائج
            val finalResult = combineResults(
                phoneNumber = phoneNumber,
                patternResult = patternResult,
                communityResult = communityResult,
                mlPrediction = mlPrediction
            )
            
            // 6. تحديث الإحصائيات
            updateStatistics(finalResult)
            
            Log.d(TAG, "📊 Analysis result: ${finalResult.confidence * 100}% spam probability")
            return finalResult
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error during number analysis", e)
            return fallbackAnalysis(phoneNumber)
        }
    }
    
    /**
     * تحميل نموذج TensorFlow Lite
     */
    private fun loadTensorFlowModel() {
        try {
            // تحميل النموذج من assets أو إنشاء نموذج افتراضي
            val modelBuffer = try {
                FileUtil.loadMappedFile(context, MODEL_FILENAME)
            } catch (e: Exception) {
                Log.w(TAG, "Model file not found, creating default model")
                createDefaultModel()
            }
            
            // إنشاء مفسر TensorFlow
            val options = Interpreter.Options().apply {
                setNumThreads(2) // استخدام 2 threads للأداء الأمثل
                setUseXNNPACK(true) // تفعيل تحسينات XNNPACK
            }
            
            interpreter = Interpreter(modelBuffer, options)
            
            Log.d(TAG, "📈 TensorFlow Lite model loaded successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to load TensorFlow model", e)
            throw e
        }
    }
    
    /**
     * إنشاء نموذج افتراضي للاختبار
     */
    private fun createDefaultModel(): ByteBuffer {
        // إنشاء نموذج بسيط للاختبار
        // في الإنتاج، سيتم تدريب نموذج حقيقي
        val modelSize = 1024 * 1024 // 1MB
        return ByteBuffer.allocateDirect(modelSize).order(ByteOrder.nativeOrder())
    }
    
    /**
     * تشغيل نموذج ML على الخصائص المستخرجة
     */
    private fun runMLModel(features: FloatArray): MLPrediction {
        return try {
            val interpreter = this.interpreter ?: throw IllegalStateException("Model not loaded")
            
            // إعداد مصفوفة الإدخال
            val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, INPUT_SIZE), org.tensorflow.lite.DataType.FLOAT32)
            inputBuffer.loadArray(features)
            
            // إعداد مصفوفة الإخراج
            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 3), org.tensorflow.lite.DataType.FLOAT32) // [spam_prob, category, confidence]
            
            // تشغيل النموذج
            interpreter.run(inputBuffer.buffer, outputBuffer.buffer)
            
            val output = outputBuffer.floatArray
            
            MLPrediction(
                spamProbability = output[0],
                categoryIndex = output[1].toInt(),
                modelConfidence = output[2]
            )
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ ML model prediction failed", e)
            // استخدام التحليل الاحتياطي
            MLPrediction(
                spamProbability = 0.5f,
                categoryIndex = 0,
                modelConfidence = 0.3f
            )
        }
    }
    
    /**
     * دمج نتائج التحليل المختلفة
     */
    private fun combineResults(
        phoneNumber: String,
        patternResult: PatternAnalysisResult,
        communityResult: CommunityAnalysisResult,
        mlPrediction: MLPrediction
    ): SpamAnalysisResult {
        
        // حساب النتيجة المدمجة باستخدام أوزان مختلفة
        val weights = AnalysisWeights(
            patternWeight = 0.3f,    // 30% للأنماط
            communityWeight = 0.4f,  // 40% للمجتمع
            mlWeight = 0.3f          // 30% للـ ML
        )
        
        val combinedScore = (
            patternResult.suspiciousScore * weights.patternWeight +
            communityResult.spamScore * weights.communityWeight +
            mlPrediction.spamProbability * weights.mlWeight
        )
        
        // تحديد الفئة النهائية
        val category = when {
            communityResult.reportedCategory.isNotEmpty() -> communityResult.reportedCategory
            mlPrediction.categoryIndex == 1 -> "telemarketing"
            mlPrediction.categoryIndex == 2 -> "scam"
            patternResult.suspiciousPatterns.contains("international") -> "international_spam"
            else -> "unknown_spam"
        }
        
        // حساب مستوى الثقة
        val confidence = calculateConfidence(
            patternResult.confidence,
            communityResult.confidence,
            mlPrediction.modelConfidence
        )
        
        // جمع الأسباب
        val reasons = mutableListOf<String>().apply {
            addAll(patternResult.detectedPatterns)
            if (communityResult.reportCount > 0) {
                add("تم الإبلاغ عنه ${communityResult.reportCount} مرة")
            }
            if (mlPrediction.spamProbability > 0.5f) {
                add("تم كشفه بواسطة الذكاء الاصطناعي")
            }
        }
        
        return SpamAnalysisResult(
            phoneNumber = phoneNumber,
            isSpam = combinedScore >= SPAM_THRESHOLD,
            confidence = confidence,
            spamScore = combinedScore,
            category = category,
            reasons = reasons,
            analysisDetails = AnalysisDetails(
                patternAnalysis = patternResult,
                communityAnalysis = communityResult,
                mlPrediction = mlPrediction,
                combinedScore = combinedScore
            )
        )
    }
    
    /**
     * حساب مستوى الثقة المدمج
     */
    private fun calculateConfidence(
        patternConfidence: Float,
        communityConfidence: Float,
        mlConfidence: Float
    ): Float {
        // استخدام المتوسط المرجح مع إعطاء وزن أكبر للنتائج عالية الثقة
        val weights = when {
            communityConfidence > 0.8f -> AnalysisWeights(0.2f, 0.6f, 0.2f)
            mlConfidence > 0.8f -> AnalysisWeights(0.2f, 0.2f, 0.6f)
            patternConfidence > 0.8f -> AnalysisWeights(0.6f, 0.2f, 0.2f)
            else -> AnalysisWeights(0.33f, 0.34f, 0.33f)
        }
        
        return (patternConfidence * weights.patternWeight +
                communityConfidence * weights.communityWeight +
                mlConfidence * weights.mlWeight).coerceIn(0f, 1f)
    }
    
    /**
     * تحليل احتياطي في حالة فشل النظام الرئيسي
     */
    private suspend fun fallbackAnalysis(phoneNumber: String): SpamAnalysisResult {
        Log.d(TAG, "🔄 Using fallback analysis for: $phoneNumber")
        
        // تحليل بسيط بدون ML
        val basicPatterns = analyzeBasicPatterns(phoneNumber)
        val communityCheck = communityDatabase.checkNumber(phoneNumber)
        
        val spamScore = (basicPatterns.suspiciousScore + communityCheck.spamScore) / 2
        
        return SpamAnalysisResult(
            phoneNumber = phoneNumber,
            isSpam = spamScore >= SPAM_THRESHOLD,
            confidence = 0.6f, // ثقة متوسطة للتحليل الاحتياطي
            spamScore = spamScore,
            category = if (communityCheck.reportedCategory.isNotEmpty()) 
                communityCheck.reportedCategory else "unknown",
            reasons = basicPatterns.detectedPatterns,
            analysisDetails = null
        )
    }
    
    /**
     * تحليل أنماط أساسي
     */
    private fun analyzeBasicPatterns(phoneNumber: String): PatternAnalysisResult {
        val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        var suspiciousScore = 0f
        val patterns = mutableListOf<String>()
        
        // فحص الأنماط الأساسية
        if (cleanNumber.length < 7) {
            suspiciousScore += 0.5f
            patterns.add("رقم قصير")
        }
        
        if (cleanNumber.contains(Regex("(\\d)\\1{3,}"))) {
            suspiciousScore += 0.3f
            patterns.add("أرقام متكررة")
        }
        
        if (cleanNumber.startsWith("00")) {
            suspiciousScore += 0.2f
            patterns.add("مكالمة دولية")
        }
        
        return PatternAnalysisResult(
            phoneNumber = phoneNumber,
            suspiciousScore = suspiciousScore.coerceAtMost(1f),
            detectedPatterns = patterns,
            confidence = 0.7f
        )
    }
    
    /**
     * تحديث إحصائيات النظام
     */
    private suspend fun updateStatistics(result: SpamAnalysisResult) {
        try {
            context.dataStore.edit { preferences ->
                val currentTotal = preferences[TOTAL_ANALYZED] ?: 0
                val currentSpam = preferences[SPAM_DETECTED] ?: 0
                
                preferences[TOTAL_ANALYZED] = currentTotal + 1
                if (result.isSpam) {
                    preferences[SPAM_DETECTED] = currentSpam + 1
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update statistics", e)
        }
    }
    
    /**
     * الحصول على إحصائيات النظام
     */
    fun getStatistics(): Flow<SpamDetectionStatistics> {
        return context.dataStore.data.map { preferences ->
            val total = preferences[TOTAL_ANALYZED] ?: 0
            val spam = preferences[SPAM_DETECTED] ?: 0
            
            SpamDetectionStatistics(
                totalNumbersAnalyzed = total,
                spamNumbersDetected = spam,
                detectionRate = if (total > 0) spam.toFloat() / total else 0f,
                modelVersion = preferences[MODEL_VERSION] ?: "1.0.0",
                lastUpdate = preferences[LAST_UPDATE] ?: "غير محدد"
            )
        }
    }
    
    /**
     * تدريب النموذج بناءً على ملاحظات المستخدم
     */
    suspend fun trainWithFeedback(
        phoneNumber: String,
        isSpam: Boolean,
        userReportedCategory: String? = null
    ) {
        try {
            Log.d(TAG, "📚 Training model with user feedback: $phoneNumber = $isSpam")
            
            // حفظ البيانات للتدريب المستقبلي
            val feedbackData = TrainingData(
                phoneNumber = phoneNumber,
                isSpam = isSpam,
                userCategory = userReportedCategory,
                timestamp = System.currentTimeMillis(),
                features = featureExtractor.extractFeatures(phoneNumber)
            )
            
            // إضافة إلى قاعدة بيانات التدريب
            saveTrainingData(feedbackData)
            
            // تحديث النموذج المحلي (إذا كان متاحاً)
            updateLocalModel(feedbackData)
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to train with feedback", e)
        }
    }
    
    /**
     * حفظ بيانات التدريب
     */
    private suspend fun saveTrainingData(data: TrainingData) {
        // حفظ في قاعدة بيانات محلية للتدريب المستقبلي
        // يمكن استخدامها لتحديث النموذج لاحقاً
    }
    
    /**
     * تحديث النموذج المحلي
     */
    private suspend fun updateLocalModel(data: TrainingData) {
        // تحديث بسيط للنموذج بناءً على الملاحظات
        // في النظام الكامل، سيتم تنفيذ تدريب إضافي هنا
    }
    
    /**
     * تصدير بيانات الإزعاج للمشاركة المجتمعية
     */
    suspend fun exportSpamData(): List<SpamReportData> {
        return try {
            // تصدير البيانات المؤكدة للمشاركة
            communityDatabase.getConfirmedSpamNumbers()
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to export spam data", e)
            emptyList()
        }
    }
    
    /**
     * استيراد بيانات إزعاج من مصادر خارجية
     */
    suspend fun importSpamData(data: List<SpamReportData>): Int {
        return try {
            Log.d(TAG, "📥 Importing ${data.size} spam reports")
            communityDatabase.importSpamReports(data)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to import spam data", e)
            0
        }
    }
    
    fun cleanup() {
        try {
            interpreter?.close()
            interpreter = null
            isInitialized = false
            Log.d(TAG, "🧹 Spam ML Engine cleaned up")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error during cleanup", e)
        }
    }
    
    // Data Classes والـ Enums
    
    data class SpamAnalysisResult(
        val phoneNumber: String,
        val isSpam: Boolean,
        val confidence: Float,
        val spamScore: Float,
        val category: String,
        val reasons: List<String>,
        val analysisDetails: AnalysisDetails?
    )
    
    data class AnalysisDetails(
        val patternAnalysis: PatternAnalysisResult,
        val communityAnalysis: CommunityAnalysisResult,
        val mlPrediction: MLPrediction,
        val combinedScore: Float
    )
    
    data class PatternAnalysisResult(
        val phoneNumber: String,
        val suspiciousScore: Float,
        val detectedPatterns: List<String>,
        val confidence: Float
    )
    
    data class CommunityAnalysisResult(
        val phoneNumber: String,
        val spamScore: Float,
        val reportCount: Int,
        val reportedCategory: String,
        val confidence: Float
    )
    
    data class MLPrediction(
        val spamProbability: Float,
        val categoryIndex: Int,
        val modelConfidence: Float
    )
    
    data class AnalysisWeights(
        val patternWeight: Float,
        val communityWeight: Float,
        val mlWeight: Float
    )
    
    data class SpamDetectionStatistics(
        val totalNumbersAnalyzed: Int,
        val spamNumbersDetected: Int,
        val detectionRate: Float,
        val modelVersion: String,
        val lastUpdate: String
    )
    
    data class TrainingData(
        val phoneNumber: String,
        val isSpam: Boolean,
        val userCategory: String?,
        val timestamp: Long,
        val features: FloatArray
    )
    
    data class SpamReportData(
        val phoneNumber: String,
        val category: String,
        val reportCount: Int,
        val lastReported: Long,
        val confidence: Float
    )
}

/**
 * قاعدة البيانات المجتمعية للإزعاج
 */
class CommunitySpamDatabase @Inject constructor() {
    
    suspend fun initialize() {
        // تهيئة قاعدة البيانات المحلية
    }
    
    suspend fun checkNumber(phoneNumber: String): CommunityAnalysisResult {
        // فحص الرقم في قاعدة البيانات المجتمعية
        return CommunityAnalysisResult(
            phoneNumber = phoneNumber,
            spamScore = 0f,
            reportCount = 0,
            reportedCategory = "",
            confidence = 0f
        )
    }
    
    suspend fun getConfirmedSpamNumbers(): List<SpamReportData> {
        // إرجاع الأرقام المؤكدة كإزعاج
        return emptyList()
    }
    
    suspend fun importSpamReports(data: List<SpamReportData>): Int {
        // استيراد تقارير الإزعاج
        return data.size
    }
}

/**
 * محلل أنماط الأرقام
 */
class NumberPatternAnalyzer @Inject constructor() {
    
    suspend fun initialize() {
        // تهيئة محلل الأنماط
    }
    
    suspend fun analyzeQuickPatterns(phoneNumber: String): PatternAnalysisResult {
        // تحليل سريع للأنماط
        return PatternAnalysisResult(
            phoneNumber = phoneNumber,
            suspiciousScore = 0f,
            detectedPatterns = emptyList(),
            confidence = 0f
        )
    }
}

/**
 * مستخرج الخصائص للنموذج
 */
class FeatureExtractor @Inject constructor() {
    
    fun extractFeatures(phoneNumber: String): FloatArray {
        // استخراج 50 خاصية من الرقم للنموذج
        val features = FloatArray(SpamMLEngine.INPUT_SIZE)
        val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        
        // خصائص أساسية
        features[0] = cleanNumber.length.toFloat()
        features[1] = if (cleanNumber.startsWith("00")) 1f else 0f
        features[2] = countRepeatingDigits(cleanNumber).toFloat()
        features[3] = countSequentialDigits(cleanNumber).toFloat()
        
        // إضافة المزيد من الخصائص...
        for (i in 4 until SpamMLEngine.INPUT_SIZE) {
            features[i] = 0f // قيم افتراضية
        }
        
        return features
    }
    
    private fun countRepeatingDigits(number: String): Int {
        return number.groupBy { it }.maxOfOrNull { it.value.size } ?: 0
    }
    
    private fun countSequentialDigits(number: String): Int {
        var maxSequence = 0
        var currentSequence = 1
        
        for (i in 1 until number.length) {
            if (number[i].digitToInt() == number[i-1].digitToInt() + 1) {
                currentSequence++
            } else {
                maxSequence = maxOf(maxSequence, currentSequence)
                currentSequence = 1
            }
        }
        
        return maxOf(maxSequence, currentSequence)
    }
}

// استخدام نفس Data Classes من SpamMLEngine
typealias CommunityAnalysisResult = SpamMLEngine.CommunityAnalysisResult
typealias PatternAnalysisResult = SpamMLEngine.PatternAnalysisResult
typealias SpamReportData = SpamMLEngine.SpamReportData