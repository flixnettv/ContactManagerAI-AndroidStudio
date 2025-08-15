package com.flixflash.contactmanagerai.data.database.entities

import androidx.room.*
import java.util.*

/**
 * FlixFlash Contact Manager AI
 * 
 * @module Data Layer
 * @description Call entities لقاعدة بيانات Room
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * كيانات المكالمات مع دعم:
 * - تسجيل جميع المكالمات
 * - المكالمات بالذكاء الاصطناعي
 * - تحليل الإزعاج والحجب
 * - تسجيل المحادثات
 * - إحصائيات مفصلة
 */

@Entity(
    tableName = "call_records",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["contactId"], name = "idx_call_contact_id"),
        Index(value = ["phoneNumber"], name = "idx_call_phone_number"),
        Index(value = ["callType"], name = "idx_call_type"),
        Index(value = ["startTime"], name = "idx_call_start_time"),
        Index(value = ["endTime"], name = "idx_call_end_time"),
        Index(value = ["duration"], name = "idx_call_duration"),
        Index(value = ["isSpam"], name = "idx_call_spam"),
        Index(value = ["isBlocked"], name = "idx_call_blocked"),
        Index(value = ["aiHandled"], name = "idx_call_ai_handled"),
        Index(value = ["isRecorded"], name = "idx_call_recorded"),
        Index(value = ["callDirection"], name = "idx_call_direction")
    ]
)
data class CallRecordEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // معلومات أساسية
    val contactId: String? = null,
    val phoneNumber: String,
    val contactName: String? = null,
    val contactPhotoUri: String? = null,
    
    // نوع واتجاه المكالمة
    val callType: String, // incoming, outgoing, missed, rejected, blocked
    val callDirection: String, // inbound, outbound
    val callState: String = "completed", // ringing, active, completed, failed
    
    // أوقات المكالمة
    val startTime: Long,
    val endTime: Long? = null,
    val duration: Int = 0, // بالثواني
    val ringDuration: Int = 0, // مدة الرنين بالثواني
    
    // معلومات التسجيل
    val isRecorded: Boolean = false,
    val recordingPath: String? = null,
    val recordingSize: Long = 0, // بالبايت
    val recordingDuration: Int = 0, // بالثواني
    val recordingQuality: String? = null, // high, medium, low
    
    // معلومات الذكاء الاصطناعي
    val aiHandled: Boolean = false,
    val aiVoiceType: String? = null, // نوع الصوت المستخدم
    val aiDialect: String? = null, // اللهجة المستخدمة
    val aiTranscript: String? = null, // نسخة نصية من المحادثة
    val aiSummary: String? = null, // ملخص المحادثة
    val aiSentiment: String? = null, // positive, negative, neutral
    val aiConfidence: Float = 0.0f, // مستوى الثقة في التحليل
    
    // كشف الإزعاج والحجب
    val isSpam: Boolean = false,
    val isBlocked: Boolean = false,
    val spamReason: String? = null,
    val spamConfidence: Float = 0.0f,
    val spamCategory: String? = null, // telemarketing, scam, robocall, etc.
    val blockReason: String? = null,
    
    // معلومات الشبكة والجودة
    val networkType: String? = null, // wifi, mobile, roaming
    val signalStrength: Int = 0, // 0-100
    val audioQuality: String? = null, // excellent, good, fair, poor
    val hasEcho: Boolean = false,
    val hasNoise: Boolean = false,
    val codec: String? = null, // AMR, G.711, etc.
    
    // معلومات إضافية
    val callReason: String? = null, // سبب المكالمة
    val notes: String? = null, // ملاحظات المستخدم
    val tags: String? = null, // JSON array of tags
    val priority: Int = 0, // 0=عادية، 1=مهمة، 2=طوارئ
    val isEmergency: Boolean = false,
    val isPrivate: Boolean = false,
    
    // حالة المزامنة والنسخ الاحتياطي
    val isSynced: Boolean = false,
    val syncTime: Long? = null,
    val isBackedUp: Boolean = false,
    val backupTime: Long? = null,
    
    // معلومات النظام
    val deviceId: String? = null,
    val appVersion: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان محادثات الذكاء الاصطناعي
 */
@Entity(
    tableName = "ai_conversations",
    foreignKeys = [
        ForeignKey(
            entity = CallRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["callRecordId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["callRecordId"], name = "idx_conv_call_record_id"),
        Index(value = ["timestamp"], name = "idx_conv_timestamp"),
        Index(value = ["speaker"], name = "idx_conv_speaker"),
        Index(value = ["messageType"], name = "idx_conv_message_type")
    ]
)
data class AIConversationEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    val callRecordId: String,
    val sequenceNumber: Int, // ترتيب الرسالة في المحادثة
    val timestamp: Long,
    
    // معلومات المتحدث
    val speaker: String, // user, ai, system
    val speakerName: String? = null,
    
    // محتوى الرسالة
    val originalText: String? = null, // النص الأصلي
    val translatedText: String? = null, // النص المترجم
    val audioPath: String? = null, // مسار الملف الصوتي
    val audioDuration: Int = 0, // مدة الصوت بالثواني
    
    // نوع ومعلومات الرسالة
    val messageType: String = "text", // text, audio, system, action
    val language: String = "ar", // لغة الرسالة
    val dialect: String = "egyptian", // اللهجة
    val confidence: Float = 0.0f, // ثقة التعرف على الكلام
    
    // معلومات الذكاء الاصطناعي
    val aiModel: String? = null, // نموذج الذكاء الاصطناعي المستخدم
    val aiVoiceType: String? = null, // نوع الصوت
    val processingTime: Int = 0, // وقت المعالجة بالميلي ثانية
    val tokens: Int = 0, // عدد الرموز المستخدمة
    
    // التحليل والعواطف
    val sentiment: String? = null, // positive, negative, neutral
    val emotion: String? = null, // happy, sad, angry, surprised, etc.
    val intent: String? = null, // greeting, question, request, complaint, etc.
    val keywords: String? = null, // JSON array of keywords
    
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * كيان إحصائيات المكالمات
 */
@Entity(
    tableName = "call_statistics",
    indices = [
        Index(value = ["phoneNumber"], name = "idx_stats_phone_number"),
        Index(value = ["contactId"], name = "idx_stats_contact_id"),
        Index(value = ["date"], name = "idx_stats_date"),
        Index(value = ["period"], name = "idx_stats_period")
    ]
)
data class CallStatisticsEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // معرفات
    val contactId: String? = null,
    val phoneNumber: String? = null,
    
    // الفترة الزمنية
    val date: String, // YYYY-MM-DD
    val period: String, // daily, weekly, monthly, yearly
    val startTime: Long,
    val endTime: Long,
    
    // إحصائيات المكالمات
    val totalCalls: Int = 0,
    val incomingCalls: Int = 0,
    val outgoingCalls: Int = 0,
    val missedCalls: Int = 0,
    val rejectedCalls: Int = 0,
    val blockedCalls: Int = 0,
    
    // إحصائيات المدة
    val totalDuration: Int = 0, // بالثواني
    val averageDuration: Int = 0,
    val longestCall: Int = 0,
    val shortestCall: Int = 0,
    
    // إحصائيات الذكاء الاصطناعي
    val aiHandledCalls: Int = 0,
    val aiSuccessRate: Float = 0.0f,
    val aiAverageDuration: Int = 0,
    val aiTotalSavings: Int = 0, // وقت موفر بالثواني
    
    // إحصائيات الإزعاج
    val spamCalls: Int = 0,
    val spamBlocked: Int = 0,
    val spamRate: Float = 0.0f,
    val falsePositives: Int = 0,
    val falseNegatives: Int = 0,
    
    // إحصائيات الجودة
    val recordedCalls: Int = 0,
    val highQualityCalls: Int = 0,
    val lowQualityCalls: Int = 0,
    val droppedCalls: Int = 0,
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان قوائم الحجب
 */
@Entity(
    tableName = "blocked_numbers",
    indices = [
        Index(value = ["phoneNumber"], name = "idx_blocked_phone_number"),
        Index(value = ["pattern"], name = "idx_blocked_pattern"),
        Index(value = ["blockType"], name = "idx_blocked_type"),
        Index(value = ["isActive"], name = "idx_blocked_active"),
        Index(value = ["createdAt"], name = "idx_blocked_created_at")
    ]
)
data class BlockedNumberEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // معلومات الرقم المحجوب
    val phoneNumber: String? = null, // رقم محدد
    val pattern: String? = null, // نمط أرقام (regex)
    val countryCode: String? = null,
    val areaCode: String? = null,
    
    // نوع ومستوى الحجب
    val blockType: String, // number, pattern, area_code, country_code
    val blockLevel: String = "calls", // calls, sms, all
    val blockReason: String, // spam, harassment, telemarketing, user_choice
    val severity: Int = 1, // 1=عادي، 2=مهم، 3=شديد
    
    // إعدادات الحجب
    val isActive: Boolean = true,
    val allowEmergency: Boolean = true, // السماح في الطوارئ
    val allowContacts: Boolean = false, // السماح لجهات الاتصال
    val autoDelete: Boolean = false, // حذف تلقائي بعد فترة
    val expiryTime: Long? = null, // وقت انتهاء الحجب
    
    // إحصائيات
    val blockedCount: Int = 0, // عدد المكالمات المحجوبة
    val lastBlockedTime: Long? = null,
    val reportedBy: String = "user", // user, system, community
    val confidence: Float = 1.0f, // مستوى الثقة في الحجب
    
    // مصدر البيانات
    val source: String = "user", // user, spam_db, ml_model, community
    val sourceId: String? = null,
    val isShared: Boolean = false, // مشاركة مع المجتمع
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان جدولة المكالمات
 */
@Entity(
    tableName = "scheduled_calls",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["contactId"], name = "idx_scheduled_contact_id"),
        Index(value = ["phoneNumber"], name = "idx_scheduled_phone_number"),
        Index(value = ["scheduledTime"], name = "idx_scheduled_time"),
        Index(value = ["status"], name = "idx_scheduled_status"),
        Index(value = ["isAICall"], name = "idx_scheduled_ai_call")
    ]
)
data class ScheduledCallEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // معلومات الاتصال
    val contactId: String? = null,
    val phoneNumber: String,
    val contactName: String? = null,
    
    // معلومات الجدولة
    val scheduledTime: Long, // وقت المكالمة المجدولة
    val timeZone: String = "Africa/Cairo",
    val repeatType: String = "none", // none, daily, weekly, monthly, yearly
    val repeatInterval: Int = 1, // كل كم مرة
    val repeatUntil: Long? = null, // تاريخ انتهاء التكرار
    
    // إعدادات المكالمة
    val isAICall: Boolean = false,
    val aiVoiceType: String? = null,
    val aiDialect: String = "egyptian",
    val callReason: String? = null,
    val callScript: String? = null, // نص المكالمة للذكاء الاصطناعي
    val maxDuration: Int = 300, // الحد الأقصى للمدة بالثواني
    
    // الحالة والتنفيذ
    val status: String = "scheduled", // scheduled, in_progress, completed, failed, cancelled
    val attempts: Int = 0, // عدد المحاولات
    val maxAttempts: Int = 3,
    val lastAttemptTime: Long? = null,
    val completedTime: Long? = null,
    val failureReason: String? = null,
    
    // النتائج
    val callRecordId: String? = null, // ربط مع سجل المكالمة
    val wasSuccessful: Boolean = false,
    val actualDuration: Int = 0,
    val aiSummary: String? = null,
    
    // إعدادات الإشعار
    val notifyBefore: Int = 5, // تنبيه قبل المكالمة بالدقائق
    val isNotificationSent: Boolean = false,
    val priority: Int = 0, // 0=عادية، 1=مهمة، 2=طوارئ
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val createdBy: String = "user" // user, system, auto
)

/**
 * كيان تحليل جودة المكالمات
 */
@Entity(
    tableName = "call_quality_analysis",
    foreignKeys = [
        ForeignKey(
            entity = CallRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["callRecordId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["callRecordId"], name = "idx_quality_call_record_id"),
        Index(value = ["overallScore"], name = "idx_quality_overall_score"),
        Index(value = ["audioQuality"], name = "idx_quality_audio"),
        Index(value = ["networkQuality"], name = "idx_quality_network")
    ]
)
data class CallQualityAnalysisEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    val callRecordId: String,
    
    // التقييم العام
    val overallScore: Float = 0.0f, // 0.0 - 10.0
    val qualityRating: String = "unknown", // excellent, good, fair, poor, terrible
    
    // جودة الصوت
    val audioQuality: Float = 0.0f,
    val audioClarity: Float = 0.0f,
    val volumeLevel: Float = 0.0f,
    val noiseLevel: Float = 0.0f,
    val echoLevel: Float = 0.0f,
    val distortionLevel: Float = 0.0f,
    
    // جودة الشبكة
    val networkQuality: Float = 0.0f,
    val latency: Int = 0, // بالميلي ثانية
    val jitter: Int = 0, // بالميلي ثانية
    val packetLoss: Float = 0.0f, // نسبة فقدان الحزم
    val bandwidth: Int = 0, // عرض النطاق المستخدم
    
    // مشاكل تم اكتشافها
    val detectedIssues: String? = null, // JSON array of issues
    val dropoutDuration: Int = 0, // مدة انقطاع الصوت بالثواني
    val silenceDuration: Int = 0, // مدة الصمت بالثواني
    
    // معلومات تقنية
    val codec: String? = null,
    val bitrate: Int = 0,
    val sampleRate: Int = 0,
    val channels: Int = 1,
    
    // تحليل المحتوى (للمكالمات المسجلة)
    val speechQuality: Float = 0.0f,
    val speechClarity: Float = 0.0f,
    val backgroundNoise: Float = 0.0f,
    val voiceActivity: Float = 0.0f, // نسبة وقت الكلام
    
    val analyzedAt: Long = System.currentTimeMillis(),
    val analysisVersion: String = "1.0.0"
)