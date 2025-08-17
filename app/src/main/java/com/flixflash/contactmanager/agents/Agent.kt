package com.flixflash.contactmanager.agents

import android.util.Log
import kotlinx.coroutines.delay

/**
 * الكلاس الأساسي لجميع الوكلاء الذكيين
 * يحتوي على الوظائف الأساسية وحلول مشاكل القوائم
 */
abstract class Agent(
    val id: String,
    val name: String,
    val description: String,
    val intelligenceLevel: Int,
    val voiceType: String,
    val activeHours: String,
    val capabilities: List<String>,
    val category: AgentCategory
) {
    
    private val TAG = "Agent_$id"
    
    // حالة الوكيل
    var isActive: Boolean = false
        private set
    
    var isProcessing: Boolean = false
        private set
    
    // إحصائيات الوكيل
    var totalRequests: Int = 0
        private set
    
    var successfulRequests: Int = 0
        private set
    
    var averageResponseTime: Long = 0L
        private set
    
    /**
     * تفعيل الوكيل
     */
    open fun activate() {
        isActive = true
        Log.d(TAG, "🚀 تم تفعيل الوكيل: $name")
        onActivated()
    }
    
    /**
     * إلغاء تفعيل الوكيل
     */
    open fun deactivate() {
        isActive = false
        isProcessing = false
        Log.d(TAG, "🔄 تم إلغاء تفعيل الوكيل: $name")
        onDeactivated()
    }
    
    /**
     * معالجة طلب من المستخدم
     */
    suspend fun processRequest(request: String, context: CallContext): AgentResponse {
        if (!isActive) {
            return AgentResponse(
                success = false,
                message = "الوكيل غير نشط حالياً",
                agentId = id,
                confidence = 0.0f
            )
        }
        
        if (isProcessing) {
            return AgentResponse(
                success = false,
                message = "الوكيل مشغول بطلب آخر، من فضلك انتظر قليلاً",
                agentId = id,
                confidence = 0.0f
            )
        }
        
        isProcessing = true
        totalRequests++
        
        val startTime = System.currentTimeMillis()
        
        try {
            Log.d(TAG, "📝 معالجة طلب: $request")
            
            // محاكاة وقت المعالجة
            delay(500L + (intelligenceLevel * 10L))
            
            val response = handleSpecificRequest(request, context)
            
            if (response.success) {
                successfulRequests++
            }
            
            val responseTime = System.currentTimeMillis() - startTime
            updateAverageResponseTime(responseTime)
            
            Log.d(TAG, "✅ تم معالجة الطلب في ${responseTime}ms")
            
            return response
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ خطأ في معالجة الطلب: ${e.message}")
            
            return AgentResponse(
                success = false,
                message = "حدث خطأ أثناء معالجة طلبك، من فضلك حاول مرة أخرى",
                agentId = id,
                confidence = 0.0f,
                error = e.message
            )
            
        } finally {
            isProcessing = false
        }
    }
    
    /**
     * تحديد ما إذا كان الوكيل يستطيع التعامل مع طلب معين
     */
    abstract fun canHandle(request: String): Boolean
    
    /**
     * معالجة الطلب المحدد (يجب تطبيقه في كل وكيل)
     */
    protected abstract suspend fun handleSpecificRequest(request: String, context: CallContext): AgentResponse
    
    /**
     * يتم استدعاؤها عند تفعيل الوكيل
     */
    protected open fun onActivated() {
        // يمكن للوكلاء المحددة تخصيص هذه الدالة
    }
    
    /**
     * يتم استدعاؤها عند إلغاء تفعيل الوكيل
     */
    protected open fun onDeactivated() {
        // يمكن للوكلاء المحددة تخصيص هذه الدالة
    }
    
    /**
     * تحديث متوسط وقت الاستجابة
     */
    private fun updateAverageResponseTime(responseTime: Long) {
        averageResponseTime = if (totalRequests == 1) {
            responseTime
        } else {
            (averageResponseTime + responseTime) / 2
        }
    }
    
    /**
     * الحصول على معدل نجاح الوكيل
     */
    fun getSuccessRate(): Float {
        return if (totalRequests > 0) {
            (successfulRequests.toFloat() / totalRequests.toFloat()) * 100f
        } else {
            0f
        }
    }
    
    /**
     * إعادة تعيين إحصائيات الوكيل
     */
    fun resetStats() {
        totalRequests = 0
        successfulRequests = 0
        averageResponseTime = 0L
    }
    
    /**
     * الحصول على معلومات الوكيل
     */
    fun getInfo(): AgentInfo {
        return AgentInfo(
            id = id,
            name = name,
            description = description,
            intelligenceLevel = intelligenceLevel,
            voiceType = voiceType,
            activeHours = activeHours,
            capabilities = capabilities,
            category = category,
            isActive = isActive,
            isProcessing = isProcessing,
            totalRequests = totalRequests,
            successfulRequests = successfulRequests,
            successRate = getSuccessRate(),
            averageResponseTime = averageResponseTime
        )
    }
}

/**
 * فئات الوكلاء
 */
enum class AgentCategory {
    BUSINESS,       // الأعمال
    HOME,          // المنزل
    EDUCATION,     // التعليم
    ENTERTAINMENT, // الترفيه
    HEALTH,        // الصحة
    FINANCE,       // المال
    TRAVEL,        // السفر
    SPECIALIZED    // المتخصصون
}

/**
 * استجابة الوكيل
 */
data class AgentResponse(
    val success: Boolean,
    val message: String,
    val agentId: String,
    val confidence: Float,
    val data: Map<String, Any>? = null,
    val error: String? = null,
    val suggestions: List<String>? = null,
    val requiresFollowUp: Boolean = false
)

/**
 * سياق المكالمة
 */
data class CallContext(
    val callerId: String? = null,
    val callerName: String? = null,
    val callType: String = "incoming",
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, Any>? = null
)

/**
 * معلومات الوكيل
 */
data class AgentInfo(
    val id: String,
    val name: String,
    val description: String,
    val intelligenceLevel: Int,
    val voiceType: String,
    val activeHours: String,
    val capabilities: List<String>,
    val category: AgentCategory,
    val isActive: Boolean,
    val isProcessing: Boolean,
    val totalRequests: Int,
    val successfulRequests: Int,
    val successRate: Float,
    val averageResponseTime: Long
)

/**
 * إحصائيات الوكلاء
 */
data class AgentStats(
    val totalAgents: Int,
    val activeAgents: Int,
    val businessAgents: Int,
    val homeAgents: Int,
    val educationAgents: Int,
    val entertainmentAgents: Int,
    val healthAgents: Int,
    val financeAgents: Int,
    val travelAgents: Int,
    val specializedAgents: Int
)