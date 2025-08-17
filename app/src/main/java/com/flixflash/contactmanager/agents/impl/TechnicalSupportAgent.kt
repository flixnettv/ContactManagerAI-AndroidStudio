package com.flixflash.contactmanager.agents.impl

import android.util.Log
import com.flixflash.contactmanager.agents.*

/**
 * وكيل الدعم التقني - يحل المشاكل التقنية والتطبيقات
 * متخصص في حل المشاكل التقنية وتقديم الحلول العملية
 */
class TechnicalSupportAgent(
    id: String,
    name: String,
    description: String,
    intelligenceLevel: Int,
    voiceType: String,
    activeHours: String,
    capabilities: List<String>
) : Agent(
    id = id,
    name = name,
    description = description,
    intelligenceLevel = intelligenceLevel,
    voiceType = voiceType,
    activeHours = activeHours,
    capabilities = capabilities,
    category = AgentCategory.SPECIALIZED
) {
    
    private val TAG = "TechnicalSupportAgent"
    
    // قاعدة بيانات المشاكل والحلول الشائعة
    private val commonProblems = mapOf(
        // مشاكل التطبيق
        "التطبيق لا يعمل" to "جرب إعادة تشغيل التطبيق أو إعادة تشغيل الهاتف",
        "التطبيق بطيء" to "أغلق التطبيقات الأخرى وتأكد من وجود ذاكرة كافية",
        "الصوت لا يعمل" to "تحقق من أذونات الميكروفون وتأكد من عدم كتم الصوت",
        "لا يتعرف على صوتي" to "جرب التحدث بوضوح أكبر أو إعادة تدريب النموذج الصوتي",
        
        // مشاكل الهاتف
        "الهاتف بطيء" to "امسح الملفات المؤقتة وأعد تشغيل الهاتف",
        "البطارية تنفد بسرعة" to "أغلق التطبيقات غير المستخدمة وقلل سطوع الشاشة",
        "مفيش مساحة" to "امسح الملفات غير المهمة أو انقل الصور للسحابة",
        "الإنترنت بطيء" to "تحقق من قوة الإشارة أو جرب إعادة تشغيل الراوتر",
        
        // مشاكل المكالمات
        "مش بسمع المتصل" to "ارفع مستوى الصوت أو جرب سماعات الأذن",
        "المتصل مش بيسمعني" to "تحقق من الميكروفون وتأكد من عدم تغطيته",
        "المكالمة تقطع" to "تحقق من قوة الشبكة أو جرب مكان آخر",
        
        // مشاكل جهات الاتصال
        "مش لاقي جهة الاتصال" to "جرب البحث باسم مختلف أو تحقق من المزامنة",
        "الأرقام مش ظاهرة" to "تحقق من أذونات جهات الاتصال",
        
        // مشاكل عامة
        "التطبيق يقفل" to "تحديث التطبيق أو مسح الكاش",
        "رسالة خطأ" to "اعمل لقطة شاشة للخطأ وأرسلها للدعم التقني"
    )
    
    // كلمات مفتاحية للتعرف على المشاكل التقنية
    private val problemKeywords = listOf(
        "مشكلة", "خطأ", "عطل", "مش شغال", "لا يعمل", "بطيء", "مش بيشتغل",
        "problem", "error", "issue", "bug", "crash", "slow", "not working"
    )
    
    private val solutionKeywords = listOf(
        "حل", "إصلاح", "ساعدني", "إزاي", "كيف", "solution", "fix", "help", "how"
    )
    
    override fun canHandle(request: String): Boolean {
        val requestLower = request.lowercase()
        
        return problemKeywords.any { requestLower.contains(it) } ||
                solutionKeywords.any { requestLower.contains(it) } ||
                requestLower.contains("تقني") ||
                requestLower.contains("technical") ||
                requestLower.contains("support") ||
                requestLower.contains("دعم") ||
                commonProblems.keys.any { requestLower.contains(it.lowercase()) }
    }
    
    override suspend fun handleSpecificRequest(request: String, context: CallContext): AgentResponse {
        val requestLower = request.lowercase()
        
        Log.d(TAG, "🔧 معالجة مشكلة تقنية: $request")
        
        return when {
            // البحث عن حل للمشكلة
            problemKeywords.any { requestLower.contains(it) } -> {
                findSolutionForProblem(request, context)
            }
            
            // طلب مساعدة عامة
            solutionKeywords.any { requestLower.contains(it) } -> {
                provideGeneralTechnicalHelp(request, context)
            }
            
            // استفسارات تقنية عامة
            else -> {
                handleGeneralTechnicalQuery(request, context)
            }
        }
    }
    
    /**
     * البحث عن حل لمشكلة محددة
     */
    private suspend fun findSolutionForProblem(request: String, context: CallContext): AgentResponse {
        val requestLower = request.lowercase()
        
        // البحث في قاعدة بيانات المشاكل الشائعة
        for ((problem, solution) in commonProblems) {
            if (requestLower.contains(problem.lowercase())) {
                return AgentResponse(
                    success = true,
                    message = "🔧 لقيت الحل!\n\n❓ المشكلة: $problem\n✅ الحل: $solution\n\nجرب الحل ده وقولي لو اشتغل معاك.",
                    agentId = id,
                    confidence = 0.9f,
                    data = mapOf(
                        "problem" to problem,
                        "solution" to solution
                    ),
                    suggestions = listOf(
                        "الحل اشتغل معايا",
                        "لسه مفيش فايدة",
                        "عايز حل تاني"
                    )
                )
            }
        }
        
        // إذا لم توجد مشكلة مطابقة، قدم حلول عامة
        return when {
            requestLower.contains("بطيء") || requestLower.contains("slow") -> {
                AgentResponse(
                    success = true,
                    message = "🐌 مشكلة البطء ممكن تحل بالطرق دي:\n\n1️⃣ أعد تشغيل الهاتف\n2️⃣ أغلق التطبيقات اللي مش بتستخدمها\n3️⃣ امسح الكاش من الإعدادات\n4️⃣ تأكد من وجود مساحة فارغة كافية\n\nأي طريقة جربتها؟",
                    agentId = id,
                    confidence = 0.85f,
                    suggestions = listOf("جربت إعادة التشغيل", "مسحت الكاش", "عايز مساعدة تانية")
                )
            }
            
            requestLower.contains("يقفل") || requestLower.contains("crash") -> {
                AgentResponse(
                    success = true,
                    message = "💥 مشكلة إقفال التطبيق:\n\n1️⃣ حدث التطبيق لآخر إصدار\n2️⃣ امسح بيانات التطبيق\n3️⃣ أعد تثبيت التطبيق\n4️⃣ تأكد من توافق الهاتف\n\nجرب الخطوات دي بالترتيب.",
                    agentId = id,
                    confidence = 0.88f,
                    suggestions = listOf("حدثت التطبيق", "مسحت البيانات", "لسه بيقفل")
                )
            }
            
            requestLower.contains("صوت") || requestLower.contains("voice") || requestLower.contains("audio") -> {
                AgentResponse(
                    success = true,
                    message = "🔊 مشاكل الصوت:\n\n1️⃣ تحقق من أذونات الميكروفون\n2️⃣ تأكد من عدم كتم الصوت\n3️⃣ جرب سماعات الأذن\n4️⃣ أعد تشغيل التطبيق\n5️⃣ تأكد من عدم تغطية الميكروفون\n\nأي خطوة محتاج تفاصيل أكتر عنها؟",
                    agentId = id,
                    confidence = 0.92f,
                    suggestions = listOf("إزاي أتحقق من الأذونات؟", "الميكروفون فين؟", "جربت كل حاجة")
                )
            }
            
            else -> {
                AgentResponse(
                    success = true,
                    message = "🤔 مش قادر أحدد المشكلة بالضبط. ممكن توصفلي المشكلة أكتر؟\n\nمثلاً:\n• 'التطبيق بطيء'\n• 'الصوت مش شغال'\n• 'التطبيق بيقفل'\n• 'مش بسمع المتصل'",
                    agentId = id,
                    confidence = 0.6f,
                    suggestions = listOf(
                        "التطبيق مش شغال",
                        "مشكلة في الصوت",
                        "الهاتف بطيء",
                        "مشكلة في المكالمات"
                    )
                )
            }
        }
    }
    
    /**
     * تقديم مساعدة تقنية عامة
     */
    private suspend fun provideGeneralTechnicalHelp(request: String, context: CallContext): AgentResponse {
        return AgentResponse(
            success = true,
            message = "🔧 مرحباً! أنا الدعم التقني لـ FlixFlash Contact Manager AI\n\nممكن أساعدك في:\n\n🔧 حل المشاكل التقنية\n📱 مشاكل التطبيق\n🔊 مشاكل الصوت والمكالمات\n⚙️ إعدادات النظام\n🛡️ الأمان والخصوصية\n\nإيه المشكلة اللي واجهتك؟",
            agentId = id,
            confidence = 0.9f,
            suggestions = listOf(
                "التطبيق مش شغال",
                "مشكلة في الصوت", 
                "الهاتف بطيء",
                "مشكلة في الأمان"
            )
        )
    }
    
    /**
     * التعامل مع الاستفسارات التقنية العامة
     */
    private suspend fun handleGeneralTechnicalQuery(request: String, context: CallContext): AgentResponse {
        val requestLower = request.lowercase()
        
        return when {
            requestLower.contains("إعدادات") || requestLower.contains("settings") -> {
                AgentResponse(
                    success = true,
                    message = "⚙️ للوصول للإعدادات:\n\n1️⃣ افتح التطبيق\n2️⃣ اضغط على أيقونة الإعدادات ⚙️\n3️⃣ اختر القسم المطلوب:\n   • إعدادات الصوت 🔊\n   • إعدادات الوكلاء 🤖\n   • إعدادات الأمان 🛡️\n   • إعدادات الواجهة 🎨",
                    agentId = id,
                    confidence = 0.9f,
                    suggestions = listOf("إعدادات الصوت", "إعدادات الوكلاء", "إعدادات الأمان")
                )
            }
            
            requestLower.contains("تحديث") || requestLower.contains("update") -> {
                AgentResponse(
                    success = true,
                    message = "🔄 للتحديث:\n\n1️⃣ افتح Google Play Store\n2️⃣ ابحث عن 'FlixFlash Contact Manager AI'\n3️⃣ اضغط 'تحديث' إذا كان متاح\n\nأو:\n• التحديثات التلقائية مفعلة في التطبيق\n• ستحصل على إشعار عند توفر تحديث جديد",
                    agentId = id,
                    confidence = 0.9f,
                    suggestions = listOf("فين إعدادات التحديث؟", "إزاي أفعل التحديث التلقائي؟")
                )
            }
            
            requestLower.contains("أذونات") || requestLower.contains("permissions") -> {
                AgentResponse(
                    success = true,
                    message = "🔐 إدارة الأذونات:\n\n1️⃣ اذهب لإعدادات الهاتف\n2️⃣ اختر 'التطبيقات'\n3️⃣ ابحث عن 'FlixFlash Contact Manager AI'\n4️⃣ اضغط 'الأذونات'\n5️⃣ فعّل الأذونات المطلوبة:\n   📞 المكالمات\n   👥 جهات الاتصال\n   🎤 الميكروفون\n   📁 التخزين",
                    agentId = id,
                    confidence = 0.95f,
                    suggestions = listOf("أي أذونات مطلوبة؟", "إزاي أفعل أذونات المكالمات؟")
                )
            }
            
            requestLower.contains("أمان") || requestLower.contains("security") || requestLower.contains("حماية") -> {
                AgentResponse(
                    success = true,
                    message = "🛡️ ميزات الأمان في التطبيق:\n\n✅ تشفير AES-256 محلي\n✅ البيانات لا تغادر جهازك\n✅ كشف المكالمات المشبوهة\n✅ حماية من النصب الهاتفي\n✅ تحكم كامل في الخصوصية\n\nكل بياناتك آمنة ومحمية!",
                    agentId = id,
                    confidence = 0.95f,
                    suggestions = listOf("إزاي أشوف إعدادات الأمان؟", "إزاي أبلغ عن مكالمة مشبوهة؟")
                )
            }
            
            requestLower.contains("نسخ احتياطي") || requestLower.contains("backup") -> {
                AgentResponse(
                    success = true,
                    message = "💾 النسخ الاحتياطي:\n\n1️⃣ اذهب للإعدادات\n2️⃣ اختر 'النسخ الاحتياطي'\n3️⃣ فعّل 'النسخ التلقائي'\n4️⃣ اختر مكان الحفظ:\n   ☁️ Google Drive\n   📱 التخزين المحلي\n   💾 بطاقة SD\n\nبياناتك محفوظة ومشفرة!",
                    agentId = id,
                    confidence = 0.9f,
                    suggestions = listOf("إزاي أعمل نسخ احتياطي يدوي؟", "إزاي أسترجع البيانات؟")
                )
            }
            
            else -> {
                AgentResponse(
                    success = true,
                    message = "🔧 مرحباً! أنا الدعم التقني لـ FlixFlash\n\nممكن أساعدك في:\n• حل مشاكل التطبيق 📱\n• مشاكل الصوت والمكالمات 🔊\n• إعدادات النظام ⚙️\n• الأمان والخصوصية 🛡️\n• النسخ الاحتياطي 💾\n\nإيه المشكلة اللي محتاج حل لها؟",
                    agentId = id,
                    confidence = 0.8f,
                    suggestions = listOf(
                        "التطبيق مش شغال",
                        "مشكلة في الصوت",
                        "إعدادات الأمان",
                        "النسخ الاحتياطي"
                    )
                )
            }
        }
    }
    
    /**
     * تقديم تشخيص متقدم للمشكلة
     */
    fun diagnoseProblem(problemDescription: String): TechnicalDiagnosis {
        val description = problemDescription.lowercase()
        
        val severity = when {
            description.contains("يقفل") || description.contains("crash") -> ProblemSeverity.HIGH
            description.contains("بطيء") || description.contains("slow") -> ProblemSeverity.MEDIUM
            description.contains("صوت") || description.contains("voice") -> ProblemSeverity.MEDIUM
            else -> ProblemSeverity.LOW
        }
        
        val category = when {
            description.contains("صوت") || description.contains("مكالمة") -> ProblemCategory.AUDIO
            description.contains("بطيء") || description.contains("ذاكرة") -> ProblemCategory.PERFORMANCE
            description.contains("أمان") || description.contains("حماية") -> ProblemCategory.SECURITY
            description.contains("واجهة") || description.contains("شاشة") -> ProblemCategory.UI
            else -> ProblemCategory.GENERAL
        }
        
        val estimatedFixTime = when (severity) {
            ProblemSeverity.HIGH -> "5-10 دقائق"
            ProblemSeverity.MEDIUM -> "2-5 دقائق"
            ProblemSeverity.LOW -> "1-2 دقيقة"
        }
        
        return TechnicalDiagnosis(
            problemDescription = problemDescription,
            severity = severity,
            category = category,
            estimatedFixTime = estimatedFixTime,
            recommendedSteps = getRecommendedSteps(category, severity)
        )
    }
    
    /**
     * الحصول على خطوات الحل المُوصى بها
     */
    private fun getRecommendedSteps(category: ProblemCategory, severity: ProblemSeverity): List<String> {
        return when (category) {
            ProblemCategory.AUDIO -> listOf(
                "تحقق من أذونات الميكروفون",
                "تأكد من عدم كتم الصوت",
                "جرب سماعات الأذن",
                "أعد تشغيل التطبيق"
            )
            
            ProblemCategory.PERFORMANCE -> listOf(
                "أغلق التطبيقات الأخرى",
                "أعد تشغيل الهاتف",
                "امسح الكاش",
                "تحقق من المساحة المتاحة"
            )
            
            ProblemCategory.SECURITY -> listOf(
                "تحقق من إعدادات الأمان",
                "فعّل التشفير",
                "راجع قوائم الحظر",
                "حدث التطبيق"
            )
            
            ProblemCategory.UI -> listOf(
                "أعد تشغيل التطبيق",
                "تحقق من إعدادات العرض",
                "جرب ثيم مختلف",
                "أعد تعيين الإعدادات"
            )
            
            ProblemCategory.GENERAL -> listOf(
                "أعد تشغيل التطبيق",
                "تحقق من التحديثات",
                "أعد تشغيل الهاتف",
                "تواصل مع الدعم التقني"
            )
        }
    }
}

/**
 * تشخيص تقني للمشكلة
 */
data class TechnicalDiagnosis(
    val problemDescription: String,
    val severity: ProblemSeverity,
    val category: ProblemCategory,
    val estimatedFixTime: String,
    val recommendedSteps: List<String>
)

/**
 * مستوى خطورة المشكلة
 */
enum class ProblemSeverity {
    LOW,    // منخفض
    MEDIUM, // متوسط
    HIGH    // عالي
}

/**
 * فئة المشكلة التقنية
 */
enum class ProblemCategory {
    AUDIO,       // صوت
    PERFORMANCE, // أداء
    SECURITY,    // أمان
    UI,          // واجهة
    GENERAL      // عام
}