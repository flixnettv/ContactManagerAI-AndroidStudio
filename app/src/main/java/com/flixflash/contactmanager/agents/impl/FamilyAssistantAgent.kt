package com.flixflash.contactmanager.agents.impl

import android.util.Log
import com.flixflash.contactmanager.agents.*

/**
 * وكيل مساعد الأسرة - يحل مشاكل القوائم والمهام المنزلية
 * متخصص في إدارة المهام العائلية والتنظيم المنزلي
 */
class FamilyAssistantAgent(
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
    category = AgentCategory.HOME
) {
    
    private val TAG = "FamilyAssistantAgent"
    
    // قوائم المهام المنزلية
    private val shoppingLists = mutableMapOf<String, MutableList<String>>()
    private val todoLists = mutableMapOf<String, MutableList<TaskItem>>()
    private val familySchedule = mutableMapOf<String, MutableList<ScheduleItem>>()
    
    // كلمات مفتاحية للتعرف على الطلبات
    private val shoppingKeywords = listOf(
        "قائمة", "تسوق", "شراء", "محتاج", "عايز اشتري", "فاكر", "اشتري",
        "قولي المطلوب", "ايه المحتاج", "shopping", "list", "buy"
    )
    
    private val todoKeywords = listOf(
        "مهمة", "مهام", "لازم اعمل", "فكرني", "تذكير", "موعد", "اعمل",
        "todo", "task", "remind", "appointment"
    )
    
    private val scheduleKeywords = listOf(
        "جدول", "برنامج", "موعد", "وقت", "امتى", "schedule", "time", "when"
    )
    
    override fun canHandle(request: String): Boolean {
        val requestLower = request.lowercase()
        
        return shoppingKeywords.any { requestLower.contains(it) } ||
                todoKeywords.any { requestLower.contains(it) } ||
                scheduleKeywords.any { requestLower.contains(it) } ||
                requestLower.contains("أسرة") ||
                requestLower.contains("عيلة") ||
                requestLower.contains("بيت") ||
                requestLower.contains("منزل") ||
                requestLower.contains("family") ||
                requestLower.contains("home")
    }
    
    override suspend fun handleSpecificRequest(request: String, context: CallContext): AgentResponse {
        val requestLower = request.lowercase()
        
        Log.d(TAG, "🏠 معالجة طلب مساعد الأسرة: $request")
        
        return when {
            // إدارة قوائم التسوق
            shoppingKeywords.any { requestLower.contains(it) } -> {
                handleShoppingListRequest(request, context)
            }
            
            // إدارة المهام
            todoKeywords.any { requestLower.contains(it) } -> {
                handleTodoListRequest(request, context)
            }
            
            // إدارة الجدول الزمني
            scheduleKeywords.any { requestLower.contains(it) } -> {
                handleScheduleRequest(request, context)
            }
            
            // طلبات عامة
            else -> {
                handleGeneralFamilyRequest(request, context)
            }
        }
    }
    
    /**
     * معالجة طلبات قوائم التسوق
     */
    private suspend fun handleShoppingListRequest(request: String, context: CallContext): AgentResponse {
        val requestLower = request.lowercase()
        val familyId = context.callerId ?: "default_family"
        
        return when {
            // إضافة عنصر للقائمة
            requestLower.contains("ضيف") || requestLower.contains("اضافة") || requestLower.contains("add") -> {
                val item = extractItemFromRequest(request)
                if (item.isNotEmpty()) {
                    addToShoppingList(familyId, item)
                    AgentResponse(
                        success = true,
                        message = "تمام! ضفت '$item' لقائمة التسوق. دلوقتي عندك ${getShoppingListSize(familyId)} حاجة في القائمة.",
                        agentId = id,
                        confidence = 0.9f,
                        data = mapOf("item" to item, "listSize" to getShoppingListSize(familyId)),
                        suggestions = listOf("عايز تضيف حاجة تانية؟", "عايز تشوف القائمة كاملة؟")
                    )
                } else {
                    AgentResponse(
                        success = false,
                        message = "مفهمتش إيه اللي عايز تضيفه. ممكن تقولي بوضوح إيه اللي محتاجه؟",
                        agentId = id,
                        confidence = 0.3f,
                        suggestions = listOf("مثال: ضيف أرز لقائمة التسوق", "مثال: محتاج اشتري لبن")
                    )
                }
            }
            
            // حذف عنصر من القائمة
            requestLower.contains("امسح") || requestLower.contains("شيل") || requestLower.contains("remove") -> {
                val item = extractItemFromRequest(request)
                if (item.isNotEmpty()) {
                    val removed = removeFromShoppingList(familyId, item)
                    if (removed) {
                        AgentResponse(
                            success = true,
                            message = "تم! شيلت '$item' من قائمة التسوق. باقي ${getShoppingListSize(familyId)} حاجة.",
                            agentId = id,
                            confidence = 0.9f,
                            data = mapOf("removedItem" to item, "listSize" to getShoppingListSize(familyId))
                        )
                    } else {
                        AgentResponse(
                            success = false,
                            message = "'$item' مش موجود في القائمة أصلاً. عايز تشوف القائمة كاملة؟",
                            agentId = id,
                            confidence = 0.7f,
                            suggestions = listOf("اعرض قائمة التسوق", "ايه اللي في القائمة؟")
                        )
                    }
                } else {
                    AgentResponse(
                        success = false,
                        message = "مفهمتش إيه اللي عايز تشيله. قولي بوضوح إيه اللي تم شراؤه؟",
                        agentId = id,
                        confidence = 0.3f
                    )
                }
            }
            
            // عرض القائمة
            requestLower.contains("اعرض") || requestLower.contains("شوف") || requestLower.contains("ايه") || requestLower.contains("show") -> {
                val list = getShoppingList(familyId)
                if (list.isNotEmpty()) {
                    val listText = list.mapIndexed { index, item -> "${index + 1}. $item" }.joinToString("\n")
                    AgentResponse(
                        success = true,
                        message = "دي قائمة التسوق بتاعتكم:\n$listText\n\nمجموع ${list.size} حاجة محتاجينها.",
                        agentId = id,
                        confidence = 0.95f,
                        data = mapOf("shoppingList" to list),
                        suggestions = listOf("عايز تضيف حاجة؟", "عايز تشيل حاجة اشتريتها؟", "امسح القائمة كلها")
                    )
                } else {
                    AgentResponse(
                        success = true,
                        message = "قائمة التسوق فاضية دلوقتي. عايز تضيف حاجات محتاجينها؟",
                        agentId = id,
                        confidence = 0.9f,
                        suggestions = listOf("ضيف أرز", "ضيف لبن", "ضيف خضار")
                    )
                }
            }
            
            // مسح القائمة
            requestLower.contains("امسح القائمة") || requestLower.contains("clear") -> {
                clearShoppingList(familyId)
                AgentResponse(
                    success = true,
                    message = "تمام! مسحت قائمة التسوق كلها. دلوقتي القائمة فاضية.",
                    agentId = id,
                    confidence = 0.95f,
                    suggestions = listOf("عايز تبدأ قائمة جديدة؟")
                )
            }
            
            else -> {
                AgentResponse(
                    success = true,
                    message = "أنا هنا عشان أساعدك في قائمة التسوق! ممكن تقولي:\n• 'ضيف أرز لقائمة التسوق'\n• 'اعرض قائمة التسوق'\n• 'شيل اللبن من القائمة'",
                    agentId = id,
                    confidence = 0.8f,
                    suggestions = listOf("اعرض قائمة التسوق", "ضيف حاجة للقائمة", "امسح القائمة")
                )
            }
        }
    }
    
    /**
     * معالجة طلبات قوائم المهام
     */
    private suspend fun handleTodoListRequest(request: String, context: CallContext): AgentResponse {
        val requestLower = request.lowercase()
        val familyId = context.callerId ?: "default_family"
        
        return when {
            // إضافة مهمة
            requestLower.contains("ضيف") || requestLower.contains("لازم") || requestLower.contains("فكرني") -> {
                val task = extractTaskFromRequest(request)
                if (task.isNotEmpty()) {
                    addTodoTask(familyId, task)
                    AgentResponse(
                        success = true,
                        message = "حاضر! ضفت المهمة '$task' لقائمة المهام. دلوقتي عندك ${getTodoListSize(familyId)} مهمة.",
                        agentId = id,
                        confidence = 0.9f,
                        data = mapOf("task" to task, "todoCount" to getTodoListSize(familyId)),
                        suggestions = listOf("عايز تضيف مهمة تانية؟", "اعرض كل المهام")
                    )
                } else {
                    AgentResponse(
                        success = false,
                        message = "مفهمتش إيه المهمة اللي عايزني أفكرك بيها. ممكن توضحلي أكتر؟",
                        agentId = id,
                        confidence = 0.3f,
                        suggestions = listOf("مثال: فكرني أدفع الفواتير", "مثال: لازم أنظف البيت")
                    )
                }
            }
            
            // عرض المهام
            requestLower.contains("اعرض") || requestLower.contains("ايه المهام") || requestLower.contains("show") -> {
                val tasks = getTodoList(familyId)
                if (tasks.isNotEmpty()) {
                    val tasksText = tasks.mapIndexed { index, task -> 
                        val status = if (task.completed) "✅" else "⏳"
                        "${index + 1}. $status ${task.description}"
                    }.joinToString("\n")
                    
                    val completedCount = tasks.count { it.completed }
                    val pendingCount = tasks.size - completedCount
                    
                    AgentResponse(
                        success = true,
                        message = "دي قائمة المهام:\n$tasksText\n\n✅ مكتمل: $completedCount مهمة\n⏳ باقي: $pendingCount مهمة",
                        agentId = id,
                        confidence = 0.95f,
                        data = mapOf(
                            "tasks" to tasks,
                            "completed" to completedCount,
                            "pending" to pendingCount
                        ),
                        suggestions = listOf("خلصت مهمة؟", "ضيف مهمة جديدة", "امسح المهام المكتملة")
                    )
                } else {
                    AgentResponse(
                        success = true,
                        message = "مفيش مهام دلوقتي! عايز تضيف مهام جديدة؟",
                        agentId = id,
                        confidence = 0.9f,
                        suggestions = listOf("فكرني أنظف البيت", "لازم أدفع الفواتير", "ضيف مهمة")
                    )
                }
            }
            
            // إنجاز مهمة
            requestLower.contains("خلصت") || requestLower.contains("انجزت") || requestLower.contains("complete") -> {
                val taskDescription = extractTaskFromRequest(request)
                val completed = completeTask(familyId, taskDescription)
                
                if (completed) {
                    AgentResponse(
                        success = true,
                        message = "مبروك! ✅ خلصت المهمة بنجاح. باقي ${getPendingTasksCount(familyId)} مهمة.",
                        agentId = id,
                        confidence = 0.9f,
                        data = mapOf("completedTask" to taskDescription),
                        suggestions = listOf("اعرض المهام الباقية", "ضيف مهمة جديدة")
                    )
                } else {
                    AgentResponse(
                        success = false,
                        message = "مش لاقي المهمة دي في القائمة. ممكن تشوف قائمة المهام الأول؟",
                        agentId = id,
                        confidence = 0.6f,
                        suggestions = listOf("اعرض قائمة المهام", "ايه المهام الباقية؟")
                    )
                }
            }
            
            else -> {
                AgentResponse(
                    success = true,
                    message = "أنا هنا عشان أساعدك في تنظيم المهام! ممكن تقولي:\n• 'فكرني أنظف البيت'\n• 'اعرض قائمة المهام'\n• 'خلصت مهمة التنظيف'",
                    agentId = id,
                    confidence = 0.8f,
                    suggestions = listOf("اعرض المهام", "ضيف مهمة جديدة", "خلصت مهمة")
                )
            }
        }
    }
    
    /**
     * معالجة طلبات الجدول الزمني
     */
    private suspend fun handleScheduleRequest(request: String, context: CallContext): AgentResponse {
        val familyId = context.callerId ?: "default_family"
        
        return AgentResponse(
            success = true,
            message = "الجدول الزمني للأسرة قيد التطوير! هيكون متاح قريباً إن شاء الله. دلوقتي ممكن أساعدك في قوائم التسوق والمهام.",
            agentId = id,
            confidence = 0.7f,
            suggestions = listOf("اعرض قائمة التسوق", "اعرض المهام", "ضيف مهمة جديدة")
        )
    }
    
    /**
     * معالجة الطلبات العامة للأسرة
     */
    private suspend fun handleGeneralFamilyRequest(request: String, context: CallContext): AgentResponse {
        return AgentResponse(
            success = true,
            message = "مرحباً! أنا مساعد الأسرة الذكي 👨‍👩‍👧‍👦\n\nممكن أساعدك في:\n• إدارة قوائم التسوق 🛒\n• تنظيم المهام المنزلية ✅\n• تذكيرات مهمة ⏰\n\nقولي محتاج إيه وأنا هساعدك!",
            agentId = id,
            confidence = 0.8f,
            suggestions = listOf(
                "اعرض قائمة التسوق",
                "ضيف حاجة للتسوق", 
                "اعرض المهام",
                "فكرني بمهمة"
            )
        )
    }
    
    // =============== دوال مساعدة لقوائم التسوق ===============
    
    private fun addToShoppingList(familyId: String, item: String) {
        if (!shoppingLists.containsKey(familyId)) {
            shoppingLists[familyId] = mutableListOf()
        }
        shoppingLists[familyId]?.add(item)
    }
    
    private fun removeFromShoppingList(familyId: String, item: String): Boolean {
        return shoppingLists[familyId]?.removeIf { it.equals(item, ignoreCase = true) } ?: false
    }
    
    private fun getShoppingList(familyId: String): List<String> {
        return shoppingLists[familyId] ?: emptyList()
    }
    
    private fun getShoppingListSize(familyId: String): Int {
        return shoppingLists[familyId]?.size ?: 0
    }
    
    private fun clearShoppingList(familyId: String) {
        shoppingLists[familyId]?.clear()
    }
    
    // =============== دوال مساعدة لقوائم المهام ===============
    
    private fun addTodoTask(familyId: String, taskDescription: String) {
        if (!todoLists.containsKey(familyId)) {
            todoLists[familyId] = mutableListOf()
        }
        val task = TaskItem(
            id = System.currentTimeMillis().toString(),
            description = taskDescription,
            completed = false,
            createdAt = System.currentTimeMillis()
        )
        todoLists[familyId]?.add(task)
    }
    
    private fun getTodoList(familyId: String): List<TaskItem> {
        return todoLists[familyId] ?: emptyList()
    }
    
    private fun getTodoListSize(familyId: String): Int {
        return todoLists[familyId]?.size ?: 0
    }
    
    private fun completeTask(familyId: String, taskDescription: String): Boolean {
        val tasks = todoLists[familyId] ?: return false
        val task = tasks.find { it.description.equals(taskDescription, ignoreCase = true) && !it.completed }
        return if (task != null) {
            task.completed = true
            task.completedAt = System.currentTimeMillis()
            true
        } else {
            false
        }
    }
    
    private fun getPendingTasksCount(familyId: String): Int {
        return todoLists[familyId]?.count { !it.completed } ?: 0
    }
    
    // =============== دوال استخراج النص ===============
    
    private fun extractItemFromRequest(request: String): String {
        val requestLower = request.lowercase()
        
        // البحث عن الكلمات بعد "ضيف" أو "اشتري" أو "محتاج"
        val patterns = listOf(
            "ضيف (.+?)(?:\\s|$)",
            "اشتري (.+?)(?:\\s|$)",
            "محتاج (.+?)(?:\\s|$)",
            "عايز (.+?)(?:\\s|$)",
            "add (.+?)(?:\\s|$)",
            "buy (.+?)(?:\\s|$)"
        )
        
        for (pattern in patterns) {
            val regex = Regex(pattern)
            val match = regex.find(requestLower)
            if (match != null) {
                return match.groupValues[1].trim()
            }
        }
        
        // إذا لم تنجح الطرق السابقة، حاول استخراج الكلمات الأخيرة
        val words = requestLower.split(" ")
        if (words.size > 2) {
            return words.takeLast(2).joinToString(" ")
        }
        
        return ""
    }
    
    private fun extractTaskFromRequest(request: String): String {
        val requestLower = request.lowercase()
        
        val patterns = listOf(
            "فكرني (.+?)(?:\\s|$)",
            "لازم (.+?)(?:\\s|$)",
            "مهمة (.+?)(?:\\s|$)",
            "ضيف مهمة (.+?)(?:\\s|$)",
            "remind (.+?)(?:\\s|$)",
            "task (.+?)(?:\\s|$)"
        )
        
        for (pattern in patterns) {
            val regex = Regex(pattern)
            val match = regex.find(requestLower)
            if (match != null) {
                return match.groupValues[1].trim()
            }
        }
        
        // إذا لم تنجح الطرق السابقة
        val words = requestLower.split(" ")
        if (words.size > 2) {
            return words.drop(2).joinToString(" ")
        }
        
        return ""
    }
}

/**
 * عنصر مهمة في قائمة المهام
 */
data class TaskItem(
    val id: String,
    val description: String,
    var completed: Boolean = false,
    val createdAt: Long,
    var completedAt: Long? = null
)

/**
 * عنصر في الجدول الزمني
 */
data class ScheduleItem(
    val id: String,
    val title: String,
    val description: String?,
    val startTime: Long,
    val endTime: Long?,
    val type: String = "event"
)