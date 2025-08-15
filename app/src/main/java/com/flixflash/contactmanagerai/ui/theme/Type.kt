package com.flixflash.contactmanagerai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * FlixFlash Contact Manager AI
 * 
 * @module UI Typography
 * @description نظام الخطوط والنصوص مع دعم العربية
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * نظام خطوط متطور مع دعم:
 * - الخطوط العربية الجميلة
 * - خطوط متناسقة مع Material Design 3
 * - أحجام مختلفة للنصوص
 * - دعم RTL وLTR
 * - تحسين القراءة والوضوح
 */

// ══════════════════════════════════════════════════════════════════
// عائلات الخطوط
// ══════════════════════════════════════════════════════════════════

/**
 * الخط الأساسي للتطبيق - يفضل Noto Sans Arabic
 */
val FlixFlashFontFamily = FontFamily(
    // في حالة عدم توفر خطوط مخصصة، سيتم استخدام النظام الافتراضي
    Font(
        resId = android.R.font.sans_serif_medium,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resId = android.R.font.sans_serif_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    ),
    Font(
        resId = android.R.font.sans_serif_medium,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal
    ),
    Font(
        resId = android.R.font.sans_serif_medium,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )
)

/**
 * خط أحادي المسافة للأرقام والأكواد
 */
val FlixFlashMonospaceFontFamily = FontFamily(
    Font(
        resId = android.R.font.monospace,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
)

/**
 * خط للعناوين الرئيسية
 */
val FlixFlashDisplayFontFamily = FontFamily(
    Font(
        resId = android.R.font.sans_serif_medium,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )
)

// ══════════════════════════════════════════════════════════════════
// Typography الأساسية
// ══════════════════════════════════════════════════════════════════

val FlixFlashTypography = Typography(
    // العناوين الكبيرة (Display)
    displayLarge = TextStyle(
        fontFamily = FlixFlashDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FlixFlashDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FlixFlashDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    
    // العناوين (Headlines)
    headlineLarge = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    
    // العناوين الفرعية (Titles)
    titleLarge = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    // النصوص الأساسية (Body)
    bodyLarge = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    // التسميات والأزرار (Labels)
    labelLarge = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// ══════════════════════════════════════════════════════════════════
// أنماط نصوص مخصصة للتطبيق
// ══════════════════════════════════════════════════════════════════

object FlixFlashTextStyles {
    
    /**
     * نص أرقام الهواتف
     */
    val PhoneNumber = TextStyle(
        fontFamily = FlixFlashMonospaceFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )
    
    /**
     * نص أسماء الأشخاص
     */
    val ContactName = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )
    
    /**
     * نص الوقت والتاريخ
     */
    val DateTime = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp
    )
    
    /**
     * نص رسائل الذكاء الاصطناعي
     */
    val AIMessage = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    )
    
    /**
     * نص الإحصائيات والأرقام
     */
    val Statistics = TextStyle(
        fontFamily = FlixFlashMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )
    
    /**
     * نص التحذيرات
     */
    val Warning = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    
    /**
     * نص الأخطاء
     */
    val Error = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    
    /**
     * نص النجاح
     */
    val Success = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    
    /**
     * نص أوصاف المكالمات
     */
    val CallDescription = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.25.sp
    )
    
    /**
     * نص حالة الاتصال
     */
    val ConnectionStatus = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    
    /**
     * نص المساعد الذكي
     */
    val AIAssistant = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    )
    
    /**
     * نص الأزرار الكبيرة
     */
    val ButtonLarge = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    )
    
    /**
     * نص الأزرار الصغيرة
     */
    val ButtonSmall = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    
    /**
     * نص الشارات (Badges)
     */
    val Badge = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp
    )
    
    /**
     * نص التبويبات
     */
    val Tab = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    )
    
    /**
     * نص القوائم المنسدلة
     */
    val DropdownItem = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
    
    /**
     * نص النصائح والمساعدة
     */
    val Tooltip = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
    
    /**
     * نص العناوين الفرعية الصغيرة
     */
    val Subtitle = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.25.sp
    )
    
    /**
     * نص التسمياتالمصرية التوضيحية
     */
    val CaptionArabic = TextStyle(
        fontFamily = FlixFlashFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
}

// ══════════════════════════════════════════════════════════════════
// أنماط نصوص خاصة بالميزات
// ══════════════════════════════════════════════════════════════════

object FlixFlashFeatureTextStyles {
    
    /**
     * نصوص خاصة بالذكاء الاصطناعي المصري
     */
    object EgyptianAI {
        val ConversationText = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.25.sp
        )
        
        val VoiceTypeLabel = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
        
        val DialectRegion = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.25.sp
        )
    }
    
    /**
     * نصوص خاصة بكشف الإزعاج
     */
    object SpamDetection {
        val ConfidenceScore = TextStyle(
            fontFamily = FlixFlashMonospaceFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        )
        
        val SpamCategory = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
        
        val ThreatLevel = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.1.sp
        )
    }
    
    /**
     * نصوص خاصة بإدارة المكالمات
     */
    object CallManager {
        val CallDuration = TextStyle(
            fontFamily = FlixFlashMonospaceFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.sp
        )
        
        val CallType = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.5.sp
        )
        
        val CallStatus = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    }
    
    /**
     * نصوص خاصة بمعالجة الصوت
     */
    object VoiceProcessing {
        val RecordingStatus = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.25.sp
        )
        
        val AudioQuality = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.5.sp
        )
        
        val VoiceCommand = TextStyle(
            fontFamily = FlixFlashFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        )
    }
}

// ══════════════════════════════════════════════════════════════════
// مساعدات للنصوص
// ══════════════════════════════════════════════════════════════════

object TypographyUtils {
    
    /**
     * الحصول على نمط نص بناءً على الأولوية
     */
    fun getTextStyleByPriority(priority: Int): TextStyle {
        return when (priority) {
            0 -> FlixFlashTypography.bodyMedium    // منخفضة
            1 -> FlixFlashTypography.bodyLarge     // متوسطة
            2 -> FlixFlashTypography.titleMedium   // عالية
            else -> FlixFlashTypography.bodyMedium
        }
    }
    
    /**
     * الحصول على نمط نص بناءً على نوع المحتوى
     */
    fun getTextStyleByContentType(contentType: String): TextStyle {
        return when (contentType.lowercase()) {
            "phone" -> FlixFlashTextStyles.PhoneNumber
            "name" -> FlixFlashTextStyles.ContactName
            "datetime" -> FlixFlashTextStyles.DateTime
            "ai" -> FlixFlashTextStyles.AIMessage
            "error" -> FlixFlashTextStyles.Error
            "warning" -> FlixFlashTextStyles.Warning
            "success" -> FlixFlashTextStyles.Success
            else -> FlixFlashTypography.bodyMedium
        }
    }
    
    /**
     * تعديل حجم النص للإمكانية
     */
    fun TextStyle.withAccessibilityScale(scale: Float): TextStyle {
        return this.copy(
            fontSize = fontSize * scale,
            lineHeight = lineHeight * scale
        )
    }
    
    /**
     * تطبيق نمط عربي محسن
     */
    fun TextStyle.withArabicOptimization(): TextStyle {
        return this.copy(
            letterSpacing = letterSpacing * 0.8f, // تقليل المسافة للعربية
            lineHeight = lineHeight * 1.2f        // زيادة ارتفاع السطر
        )
    }
}