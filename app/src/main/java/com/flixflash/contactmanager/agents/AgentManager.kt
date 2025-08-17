package com.flixflash.contactmanager.agents

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * مدير الوكلاء الذكيين - FlixFlash Contact Manager AI
 * يدير جميع الوكلاء الـ 25 ويحل مشاكل القوائم والمهام
 */
@Singleton
class AgentManager @Inject constructor(
    private val context: Context
) {
    
    private val TAG = "AgentManager"
    
    // حالة الوكلاء النشطين
    private val _activeAgents = MutableStateFlow<List<Agent>>(emptyList())
    val activeAgents: StateFlow<List<Agent>> = _activeAgents.asStateFlow()
    
    // قائمة جميع الوكلاء المتاحين
    private val availableAgents = mutableListOf<Agent>()
    
    init {
        initializeAllAgents()
    }
    
    /**
     * تهيئة جميع الوكلاء الـ 25
     */
    private fun initializeAllAgents() {
        Log.d(TAG, "🚀 تهيئة جميع الوكلاء الذكيين...")
        
        // 👨‍💼 وكلاء الأعمال
        availableAgents.addAll(createBusinessAgents())
        
        // 🏠 وكلاء المنزل والأسرة
        availableAgents.addAll(createHomeAgents())
        
        // 🎓 وكلاء التعليم والثقافة
        availableAgents.addAll(createEducationAgents())
        
        // 🎉 وكلاء الترفيه
        availableAgents.addAll(createEntertainmentAgents())
        
        // 🏥 وكلاء الصحة
        availableAgents.addAll(createHealthAgents())
        
        // 💰 وكلاء المال والأعمال
        availableAgents.addAll(createFinanceAgents())
        
        // 🌍 وكلاء السفر والسياحة
        availableAgents.addAll(createTravelAgents())
        
        // 💼 وكلاء متخصصون
        availableAgents.addAll(createSpecializedAgents())
        
        Log.d(TAG, "✅ تم تهيئة ${availableAgents.size} وكيل ذكي")
    }
    
    /**
     * إنشاء وكلاء الأعمال
     */
    private fun createBusinessAgents(): List<Agent> {
        return listOf(
            BusinessCallAgent(
                id = "business_call",
                name = "وكيل المكالمات التجارية",
                description = "يدير المكالمات التجارية والمهنية بكفاءة عالية",
                intelligenceLevel = 95,
                voiceType = "professional_male",
                activeHours = "09:00-17:00",
                capabilities = listOf(
                    "ترحيب احترافي بالعملاء",
                    "تحويل المكالمات للأقسام المناسبة", 
                    "تسجيل الطلبات والاستفسارات",
                    "جدولة الاجتماعات تلقائياً",
                    "إرسال تقارير يومية للإدارة"
                )
            ),
            
            CustomerServiceAgent(
                id = "customer_service",
                name = "وكيل خدمة العملاء",
                description = "يقدم خدمة عملاء متقدمة باللهجة المصرية",
                intelligenceLevel = 92,
                voiceType = "friendly_female",
                activeHours = "24/7",
                capabilities = listOf(
                    "الرد على الاستفسارات الشائعة",
                    "حل المشاكل التقنية البسيطة",
                    "توجيه العملاء للخدمات المناسبة",
                    "تسجيل الشكاوى ومتابعتها",
                    "تقديم المساعدة باللهجة المحلية"
                )
            ),
            
            SalesAssistantAgent(
                id = "sales_assistant", 
                name = "مساعد المبيعات",
                description = "يساعد في عمليات البيع وإقناع العملاء",
                intelligenceLevel = 94,
                voiceType = "persuasive_male",
                activeHours = "09:00-21:00",
                capabilities = listOf(
                    "تقديم المنتجات والخدمات",
                    "حساب الأسعار والخصومات",
                    "متابعة العملاء المحتملين",
                    "إرسال عروض مخصصة",
                    "تحليل اهتمامات العملاء"
                )
            )
        )
    }
    
    /**
     * إنشاء وكلاء المنزل والأسرة
     */
    private fun createHomeAgents(): List<Agent> {
        return listOf(
            FamilyAssistantAgent(
                id = "family_assistant",
                name = "مساعد الأسرة",
                description = "يساعد في تنظيم الحياة العائلية والمهام المنزلية",
                intelligenceLevel = 90,
                voiceType = "warm_female",
                activeHours = "06:00-22:00",
                capabilities = listOf(
                    "تذكير أفراد الأسرة بالمواعيد",
                    "إدارة قوائم التسوق",
                    "تنسيق الأنشطة العائلية",
                    "مراقبة الأطفال (آمنة)",
                    "تقديم نصائح تربوية"
                )
            ),
            
            KitchenHelperAgent(
                id = "kitchen_helper",
                name = "مساعد المطبخ",
                description = "يساعد في الطبخ والوصفات المصرية التقليدية",
                intelligenceLevel = 88,
                voiceType = "motherly_female",
                activeHours = "06:00-23:00",
                capabilities = listOf(
                    "اقتراح وصفات مصرية تقليدية",
                    "حساب المقادير تلقائياً",
                    "تذكير بمواعيد الطبخ",
                    "نصائح للطبخ الصحي",
                    "قوائم التسوق الذكية"
                )
            ),
            
            HomeSecurityAgent(
                id = "home_security",
                name = "وكيل أمان المنزل",
                description = "يراقب أمان المنزل ويكشف المكالمات المشبوهة",
                intelligenceLevel = 96,
                voiceType = "authoritative_male",
                activeHours = "24/7",
                capabilities = listOf(
                    "مراقبة المكالمات المشبوهة",
                    "تنبيهات أمنية فورية",
                    "التحقق من هوية المتصلين",
                    "إدارة قوائم الأمان",
                    "ربط مع أنظمة الأمان المنزلي"
                )
            )
        )
    }
    
    /**
     * إنشاء وكلاء التعليم والثقافة
     */
    private fun createEducationAgents(): List<Agent> {
        return listOf(
            EducationalAgent(
                id = "educational",
                name = "الوكيل التعليمي",
                description = "يساعد في التعليم والمساعدة الأكاديمية",
                intelligenceLevel = 93,
                voiceType = "teacher_female",
                activeHours = "07:00-22:00",
                capabilities = listOf(
                    "مساعدة الطلاب في الواجبات",
                    "شرح المفاهيم التعليمية",
                    "تنظيم جداول الدراسة",
                    "تذكير بالامتحانات",
                    "توفير مصادر تعليمية"
                )
            ),
            
            ArabicLanguageAgent(
                id = "arabic_language",
                name = "وكيل اللغة العربية",
                description = "متخصص في اللغة العربية والأدب",
                intelligenceLevel = 95,
                voiceType = "classical_male",
                activeHours = "08:00-20:00",
                capabilities = listOf(
                    "تصحيح الأخطاء اللغوية",
                    "تعليم قواعد النحو",
                    "شرح المفردات الصعبة",
                    "مساعدة في الكتابة",
                    "نصائح للخطابة"
                )
            ),
            
            IslamicKnowledgeAgent(
                id = "islamic_knowledge",
                name = "وكيل المعرفة الإسلامية",
                description = "يقدم المعرفة الإسلامية والأحكام الشرعية",
                intelligenceLevel = 91,
                voiceType = "religious_male",
                activeHours = "05:00-23:00",
                capabilities = listOf(
                    "الإجابة على الأسئلة الشرعية",
                    "تذكير بأوقات الصلاة",
                    "تقديم الأدعية المناسبة",
                    "معلومات عن المناسبات الدينية",
                    "نصائح إسلامية يومية"
                )
            )
        )
    }
    
    /**
     * إنشاء وكلاء الترفيه
     */
    private fun createEntertainmentAgents(): List<Agent> {
        return listOf(
            ComedyAgent(
                id = "comedy",
                name = "وكيل الكوميديا",
                description = "يقدم الترفيه والضحك باللهجة المصرية",
                intelligenceLevel = 85,
                voiceType = "funny_male",
                activeHours = "10:00-23:00",
                capabilities = listOf(
                    "إلقاء النكت المصرية",
                    "تقليد الشخصيات المشهورة",
                    "ألعاب صوتية تفاعلية",
                    "قصص مضحكة",
                    "تحسين المزاج"
                )
            ),
            
            MusicAgent(
                id = "music",
                name = "وكيل الموسيقى",
                description = "متخصص في الموسيقى والأغاني العربية",
                intelligenceLevel = 87,
                voiceType = "artistic_female",
                activeHours = "08:00-24:00",
                capabilities = listOf(
                    "اقتراح الأغاني المناسبة",
                    "معلومات عن الفنانين",
                    "كلمات الأغاني",
                    "تنظيم قوائم التشغيل",
                    "توصيات موسيقية مخصصة"
                )
            ),
            
            SportsAgent(
                id = "sports",
                name = "وكيل الرياضة",
                description = "متخصص في الرياضة والألعاب المصرية والعالمية",
                intelligenceLevel = 89,
                voiceType = "energetic_male",
                activeHours = "06:00-24:00",
                capabilities = listOf(
                    "أخبار الرياضة المصرية",
                    "نتائج المباريات",
                    "إحصائيات اللاعبين",
                    "توقعات المباريات",
                    "معلومات عن الأندية"
                )
            )
        )
    }
    
    /**
     * إنشاء وكلاء الصحة
     */
    private fun createHealthAgents(): List<Agent> {
        return listOf(
            HealthAssistantAgent(
                id = "health_assistant",
                name = "مساعد الصحة",
                description = "يقدم النصائح الصحية والتذكيرات الطبية",
                intelligenceLevel = 94,
                voiceType = "caring_female",
                activeHours = "24/7",
                capabilities = listOf(
                    "تذكير بمواعيد الأدوية",
                    "نصائح صحية يومية",
                    "معلومات عن الأمراض الشائعة",
                    "توجيه للطوارئ الطبية",
                    "ربط مع التطبيقات الصحية"
                )
            ),
            
            MentalHealthAgent(
                id = "mental_health",
                name = "وكيل الصحة النفسية",
                description = "يقدم الدعم النفسي والعاطفي",
                intelligenceLevel = 92,
                voiceType = "supportive_female",
                activeHours = "24/7",
                capabilities = listOf(
                    "تقديم الدعم النفسي",
                    "تمارين الاسترخاء",
                    "نصائح للتعامل مع الضغوط",
                    "توجيه للمختصين عند الحاجة",
                    "مراقبة الحالة النفسية"
                )
            )
        )
    }
    
    /**
     * إنشاء وكلاء المال والأعمال
     */
    private fun createFinanceAgents(): List<Agent> {
        return listOf(
            FinancialAdvisorAgent(
                id = "financial_advisor",
                name = "المستشار المالي",
                description = "يقدم الاستشارات المالية والاستثمارية",
                intelligenceLevel = 93,
                voiceType = "professional_male",
                activeHours = "09:00-18:00",
                capabilities = listOf(
                    "نصائح للادخار والاستثمار",
                    "حساب الزكاة تلقائياً",
                    "تتبع المصروفات",
                    "تقارير مالية شخصية",
                    "توجيه للخدمات المصرفية"
                )
            ),
            
            ShoppingAssistantAgent(
                id = "shopping_assistant",
                name = "مساعد التسوق",
                description = "يساعد في التسوق الذكي ومقارنة الأسعار",
                intelligenceLevel = 88,
                voiceType = "helpful_female",
                activeHours = "08:00-22:00",
                capabilities = listOf(
                    "مقارنة الأسعار",
                    "البحث عن أفضل العروض",
                    "تذكير بقوائم التسوق",
                    "توصيات المنتجات",
                    "مراجعات المتاجر"
                )
            )
        )
    }
    
    /**
     * إنشاء وكلاء السفر والسياحة
     */
    private fun createTravelAgents(): List<Agent> {
        return listOf(
            TravelGuideAgent(
                id = "travel_guide",
                name = "دليل السفر",
                description = "يساعد في التخطيط للرحلات والسفر",
                intelligenceLevel = 90,
                voiceType = "adventurous_male",
                activeHours = "24/7",
                capabilities = listOf(
                    "التخطيط للرحلات",
                    "معلومات عن الوجهات السياحية",
                    "حجز الفنادق والطيران",
                    "نصائح السفر",
                    "تحديثات الطقس"
                )
            ),
            
            EgyptianHeritageAgent(
                id = "egyptian_heritage",
                name = "وكيل التراث المصري",
                description = "متخصص في التراث والحضارة المصرية",
                intelligenceLevel = 91,
                voiceType = "cultural_male",
                activeHours = "09:00-21:00",
                capabilities = listOf(
                    "معلومات عن الآثار المصرية",
                    "قصص من التاريخ المصري",
                    "جولات افتراضية",
                    "معلومات ثقافية",
                    "حفظ التراث الشعبي"
                )
            )
        )
    }
    
    /**
     * إنشاء الوكلاء المتخصصون
     */
    private fun createSpecializedAgents(): List<Agent> {
        return listOf(
            TransportationAgent(
                id = "transportation",
                name = "وكيل النقل والمواصلات",
                description = "يساعد في النقل والمواصلات في مصر",
                intelligenceLevel = 87,
                voiceType = "street_smart_male",
                activeHours = "05:00-24:00",
                capabilities = listOf(
                    "معلومات المرور في الوقت الفعلي",
                    "أفضل الطرق للوصول",
                    "مواعيد المواصلات العامة",
                    "حجز سيارات الأجرة",
                    "نصائح القيادة الآمنة"
                )
            ),
            
            LegalAdvisorAgent(
                id = "legal_advisor",
                name = "المستشار القانوني",
                description = "يقدم الاستشارات القانونية الأساسية",
                intelligenceLevel = 95,
                voiceType = "authoritative_male",
                activeHours = "09:00-17:00",
                capabilities = listOf(
                    "نصائح قانونية أساسية",
                    "توضيح القوانين المصرية",
                    "توجيه للمحامين المختصين",
                    "معلومات عن الحقوق والواجبات",
                    "مساعدة في الإجراءات الحكومية"
                )
            ),
            
            CreativeAgent(
                id = "creative",
                name = "الوكيل الإبداعي",
                description = "يساعد في الأعمال الفنية والإبداعية",
                intelligenceLevel = 86,
                voiceType = "artistic_female",
                activeHours = "10:00-22:00",
                capabilities = listOf(
                    "اقتراحات إبداعية للمشاريع",
                    "نصائح في التصميم",
                    "معلومات عن الفنون",
                    "تحفيز الإبداع",
                    "ربط مع المجتمع الفني"
                )
            ),
            
            TechnicalSupportAgent(
                id = "technical_support",
                name = "الدعم التقني",
                description = "يقدم المساعدة التقنية والحلول",
                intelligenceLevel = 94,
                voiceType = "tech_savvy_male",
                activeHours = "24/7",
                capabilities = listOf(
                    "حل المشاكل التقنية",
                    "تعليمات الصيانة",
                    "نصائح الأمان الرقمي",
                    "تحديثات البرامج",
                    "استرداد البيانات"
                )
            ),
            
            NewsAgent(
                id = "news",
                name = "وكيل الأخبار",
                description = "يقدم آخر الأخبار والمعلومات",
                intelligenceLevel = 93,
                voiceType = "news_anchor_male",
                activeHours = "24/7",
                capabilities = listOf(
                    "آخر الأخبار المحلية والعالمية",
                    "تحليل الأحداث",
                    "تلخيص الأخبار",
                    "تنبيهات الأخبار المهمة",
                    "أرشيف الأحداث"
                )
            ),
            
            WeatherAgent(
                id = "weather",
                name = "وكيل الطقس",
                description = "يقدم معلومات الطقس والمناخ",
                intelligenceLevel = 89,
                voiceType = "weather_reporter_female",
                activeHours = "24/7",
                capabilities = listOf(
                    "توقعات الطقس الدقيقة",
                    "تنبيهات الطقس السيء",
                    "نصائح حسب حالة الطقس",
                    "معلومات زراعية",
                    "تخطيط الأنشطة حسب الطقس"
                )
            ),
            
            GamingAgent(
                id = "gaming",
                name = "وكيل الألعاب",
                description = "متخصص في الألعاب والترفيه الرقمي",
                intelligenceLevel = 85,
                voiceType = "gamer_male",
                activeHours = "12:00-02:00",
                capabilities = listOf(
                    "اقتراحات الألعاب",
                    "نصائح للألعاب",
                    "أخبار صناعة الألعاب",
                    "تحديات تفاعلية",
                    "ربط مع مجتمع اللاعبين"
                )
            )
        )
    }
    
    /**
     * تفعيل وكيل معين
     */
    fun activateAgent(agentId: String): Boolean {
        val agent = availableAgents.find { it.id == agentId }
        return if (agent != null && !_activeAgents.value.contains(agent)) {
            val currentActive = _activeAgents.value.toMutableList()
            
            // حد أقصى 3 وكلاء نشطين في نفس الوقت
            if (currentActive.size >= 3) {
                Log.w(TAG, "⚠️ الحد الأقصى للوكلاء النشطين هو 3")
                return false
            }
            
            currentActive.add(agent)
            _activeAgents.value = currentActive
            agent.activate()
            
            Log.d(TAG, "✅ تم تفعيل الوكيل: ${agent.name}")
            true
        } else {
            Log.w(TAG, "❌ فشل في تفعيل الوكيل: $agentId")
            false
        }
    }
    
    /**
     * إلغاء تفعيل وكيل
     */
    fun deactivateAgent(agentId: String): Boolean {
        val agent = _activeAgents.value.find { it.id == agentId }
        return if (agent != null) {
            val currentActive = _activeAgents.value.toMutableList()
            currentActive.remove(agent)
            _activeAgents.value = currentActive
            agent.deactivate()
            
            Log.d(TAG, "🔄 تم إلغاء تفعيل الوكيل: ${agent.name}")
            true
        } else {
            Log.w(TAG, "❌ الوكيل غير موجود في القائمة النشطة: $agentId")
            false
        }
    }
    
    /**
     * معالجة طلب من المستخدم
     */
    suspend fun handleUserRequest(request: String, context: CallContext): AgentResponse {
        Log.d(TAG, "📞 معالجة طلب: $request")
        
        // تحديد أفضل وكيل للطلب
        val bestAgent = findBestAgentForRequest(request)
        
        return if (bestAgent != null) {
            Log.d(TAG, "🎯 اختيار الوكيل: ${bestAgent.name}")
            bestAgent.processRequest(request, context)
        } else {
            Log.w(TAG, "❌ لم يتم العثور على وكيل مناسب")
            AgentResponse(
                success = false,
                message = "عذراً، لم أتمكن من فهم طلبك. يمكنك تجربة إعادة صياغة السؤال؟",
                agentId = "none",
                confidence = 0.0f
            )
        }
    }
    
    /**
     * العثور على أفضل وكيل لطلب معين
     */
    private fun findBestAgentForRequest(request: String): Agent? {
        val requestLower = request.lowercase()
        
        // البحث في الوكلاء النشطين أولاً
        for (agent in _activeAgents.value) {
            if (agent.canHandle(requestLower)) {
                return agent
            }
        }
        
        // إذا لم يوجد في النشطين، ابحث في جميع الوكلاء
        for (agent in availableAgents) {
            if (agent.canHandle(requestLower)) {
                // تفعيل الوكيل تلقائياً إذا لم يكن نشطاً
                activateAgent(agent.id)
                return agent
            }
        }
        
        return null
    }
    
    /**
     * الحصول على جميع الوكلاء المتاحين
     */
    fun getAllAvailableAgents(): List<Agent> = availableAgents.toList()
    
    /**
     * الحصول على وكيل بالمعرف
     */
    fun getAgentById(agentId: String): Agent? = availableAgents.find { it.id == agentId }
    
    /**
     * إحصائيات الوكلاء
     */
    fun getAgentsStats(): AgentStats {
        return AgentStats(
            totalAgents = availableAgents.size,
            activeAgents = _activeAgents.value.size,
            businessAgents = availableAgents.count { it.category == AgentCategory.BUSINESS },
            homeAgents = availableAgents.count { it.category == AgentCategory.HOME },
            educationAgents = availableAgents.count { it.category == AgentCategory.EDUCATION },
            entertainmentAgents = availableAgents.count { it.category == AgentCategory.ENTERTAINMENT },
            healthAgents = availableAgents.count { it.category == AgentCategory.HEALTH },
            financeAgents = availableAgents.count { it.category == AgentCategory.FINANCE },
            travelAgents = availableAgents.count { it.category == AgentCategory.TRAVEL },
            specializedAgents = availableAgents.count { it.category == AgentCategory.SPECIALIZED }
        )
    }
}