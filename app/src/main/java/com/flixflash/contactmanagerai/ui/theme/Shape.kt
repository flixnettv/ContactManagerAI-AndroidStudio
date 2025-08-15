package com.flixflash.contactmanagerai.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * FlixFlash Contact Manager AI
 * 
 * @module UI Shapes
 * @description نظام الأشكال والحدود للتطبيق
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * نظام أشكال متطور مع دعم:
 * - Material Design 3 shapes
 * - أشكال مخصصة للميزات
 * - حدود متناسقة ومتجاوبة
 * - تجربة مستخدم سلسة
 */

// ══════════════════════════════════════════════════════════════════
// الأشكال الأساسية لـ Material Design 3
// ══════════════════════════════════════════════════════════════════

val FlixFlashShapes = Shapes(
    // أشكال صغيرة (للأزرار الصغيرة، الشارات، إلخ)
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    
    // أشكال متوسطة (للبطاقات، المربعات الحوارية)
    medium = RoundedCornerShape(12.dp),
    
    // أشكال كبيرة (للأوراق السفلية، الأوراق الجانبية)
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp)
)

// ══════════════════════════════════════════════════════════════════
// أشكال مخصصة للتطبيق
// ══════════════════════════════════════════════════════════════════

object FlixFlashCustomShapes {
    
    // أشكال خاصة بالبطاقات
    val CardSmall = RoundedCornerShape(8.dp)
    val CardMedium = RoundedCornerShape(12.dp)
    val CardLarge = RoundedCornerShape(16.dp)
    val CardXLarge = RoundedCornerShape(20.dp)
    
    // أشكال خاصة بالأزرار
    val ButtonSmall = RoundedCornerShape(6.dp)
    val ButtonMedium = RoundedCornerShape(8.dp)
    val ButtonLarge = RoundedCornerShape(12.dp)
    val ButtonRound = RoundedCornerShape(50.dp)
    
    // أشكال خاصة بحقول الإدخال
    val InputField = RoundedCornerShape(8.dp)
    val InputFieldLarge = RoundedCornerShape(12.dp)
    val SearchField = RoundedCornerShape(24.dp)
    
    // أشكال خاصة بالحوارات والأوراق
    val DialogBox = RoundedCornerShape(16.dp)
    val BottomSheet = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    val TopSheet = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    
    // أشكال خاصة بالقوائم والتنقل
    val NavigationDrawer = RoundedCornerShape(
        topEnd = 16.dp,
        bottomEnd = 16.dp,
        topStart = 0.dp,
        bottomStart = 0.dp
    )
    val TabIndicator = RoundedCornerShape(3.dp)
    val MenuItem = RoundedCornerShape(8.dp)
    
    // أشكال خاصة بالمكونات الصغيرة
    val Chip = RoundedCornerShape(16.dp)
    val Badge = RoundedCornerShape(50.dp)
    val Avatar = RoundedCornerShape(50.dp)
    val Thumbnail = RoundedCornerShape(8.dp)
    
    // أشكال خاصة بالتنبيهات والإشعارات
    val NotificationCard = RoundedCornerShape(12.dp)
    val AlertDialog = RoundedCornerShape(16.dp)
    val Snackbar = RoundedCornerShape(8.dp)
    val Banner = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )
    
    // أشكال مع زوايا مختلطة
    val CallBubbleOutgoing = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 4.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    val CallBubbleIncoming = RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    
    // أشكال خاصة بالصور والوسائط
    val ImagePreview = RoundedCornerShape(8.dp)
    val VideoPlayer = RoundedCornerShape(12.dp)
    val AudioWaveform = RoundedCornerShape(4.dp)
}

// ══════════════════════════════════════════════════════════════════
// أشكال خاصة بالميزات
// ══════════════════════════════════════════════════════════════════

object FlixFlashFeatureShapes {
    
    /**
     * أشكال خاصة بالذكاء الاصطناعي
     */
    object AI {
        val MessageBubble = RoundedCornerShape(16.dp)
        val VoiceIndicator = RoundedCornerShape(50.dp)
        val AIAvatar = RoundedCornerShape(50.dp)
        val ConversationCard = RoundedCornerShape(12.dp)
        val VoiceSelector = RoundedCornerShape(8.dp)
    }
    
    /**
     * أشكال خاصة بإدارة المكالمات
     */
    object CallManager {
        val CallCard = RoundedCornerShape(12.dp)
        val CallButton = RoundedCornerShape(50.dp)
        val CallHistoryItem = RoundedCornerShape(8.dp)
        val RecordingIndicator = RoundedCornerShape(4.dp)
        val CallControls = RoundedCornerShape(16.dp)
    }
    
    /**
     * أشكال خاصة بكشف الإزعاج
     */
    object SpamDetection {
        val ThreatIndicator = RoundedCornerShape(6.dp)
        val ConfidenceBar = RoundedCornerShape(8.dp)
        val SpamBadge = RoundedCornerShape(50.dp)
        val AnalysisCard = RoundedCornerShape(12.dp)
        val FilterChip = RoundedCornerShape(16.dp)
    }
    
    /**
     * أشكال خاصة بجهات الاتصال
     */
    object Contacts {
        val ContactCard = RoundedCornerShape(12.dp)
        val ContactAvatar = RoundedCornerShape(50.dp)
        val ContactGroup = RoundedCornerShape(8.dp)
        val PhoneNumberChip = RoundedCornerShape(16.dp)
        val ContactActions = RoundedCornerShape(8.dp)
    }
    
    /**
     * أشكال خاصة بالإعدادات
     */
    object Settings {
        val SettingsGroup = RoundedCornerShape(12.dp)
        val SettingsItem = RoundedCornerShape(8.dp)
        val Switch = RoundedCornerShape(50.dp)
        val Slider = RoundedCornerShape(4.dp)
        val PreferenceCard = RoundedCornerShape(12.dp)
    }
}

// ══════════════════════════════════════════════════════════════════
// أشكال متحركة ومتفاعلة
// ══════════════════════════════════════════════════════════════════

object FlixFlashAnimatedShapes {
    
    /**
     * أشكال للحالات المختلفة
     */
    object States {
        val Default = RoundedCornerShape(8.dp)
        val Pressed = RoundedCornerShape(6.dp)
        val Focused = RoundedCornerShape(10.dp)
        val Disabled = RoundedCornerShape(4.dp)
    }
    
    /**
     * أشكال للتحولات
     */
    object Transitions {
        val FromCard = RoundedCornerShape(12.dp)
        val ToDialog = RoundedCornerShape(16.dp)
        val Expanding = RoundedCornerShape(20.dp)
        val Collapsing = RoundedCornerShape(4.dp)
    }
}

// ══════════════════════════════════════════════════════════════════
// أشكال للحالات الخاصة
// ══════════════════════════════════════════════════════════════════

object FlixFlashSpecialShapes {
    
    /**
     * أشكال للطوارئ والتنبيهات
     */
    object Emergency {
        val AlertCard = RoundedCornerShape(8.dp)
        val EmergencyButton = RoundedCornerShape(12.dp)
        val WarningBanner = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 8.dp,
            bottomEnd = 8.dp
        )
        val CriticalDialog = RoundedCornerShape(16.dp)
    }
    
    /**
     * أشكال للحالة المظلمة
     */
    object DarkMode {
        val CardElevated = RoundedCornerShape(12.dp)
        val SurfaceElevated = RoundedCornerShape(8.dp)
        val DialogElevated = RoundedCornerShape(16.dp)
        val MenuElevated = RoundedCornerShape(8.dp)
    }
    
    /**
     * أشكال لإمكانية الوصول
     */
    object Accessibility {
        val LargeTouch = RoundedCornerShape(12.dp)
        val HighContrast = RoundedCornerShape(4.dp)
        val ScreenReader = RoundedCornerShape(8.dp)
        val ReducedMotion = RoundedCornerShape(6.dp)
    }
}

// ══════════════════════════════════════════════════════════════════
// مساعدات للأشكال
// ══════════════════════════════════════════════════════════════════

object ShapeUtils {
    
    /**
     * الحصول على شكل بناءً على الحجم
     */
    fun getShapeBySize(size: ShapeSize): Shape {
        return when (size) {
            ShapeSize.EXTRA_SMALL -> FlixFlashShapes.extraSmall
            ShapeSize.SMALL -> FlixFlashShapes.small
            ShapeSize.MEDIUM -> FlixFlashShapes.medium
            ShapeSize.LARGE -> FlixFlashShapes.large
            ShapeSize.EXTRA_LARGE -> FlixFlashShapes.extraLarge
        }
    }
    
    /**
     * الحصول على شكل بناءً على النوع
     */
    fun getShapeByType(type: ComponentType): Shape {
        return when (type) {
            ComponentType.BUTTON -> FlixFlashCustomShapes.ButtonMedium
            ComponentType.CARD -> FlixFlashCustomShapes.CardMedium
            ComponentType.INPUT -> FlixFlashCustomShapes.InputField
            ComponentType.DIALOG -> FlixFlashCustomShapes.DialogBox
            ComponentType.CHIP -> FlixFlashCustomShapes.Chip
            ComponentType.BADGE -> FlixFlashCustomShapes.Badge
        }
    }
    
    /**
     * تخصيص شكل للحالة المختارة
     */
    fun Shape.forState(isSelected: Boolean): Shape {
        return if (isSelected) {
            RoundedCornerShape(12.dp)
        } else {
            this
        }
    }
    
    /**
     * تخصيص شكل للحالة المضغوطة
     */
    fun Shape.forPressed(isPressed: Boolean): Shape {
        return if (isPressed) {
            RoundedCornerShape(6.dp)
        } else {
            this
        }
    }
    
    /**
     * إنشاء شكل مع زاوية واحدة مدورة
     */
    fun createSingleRoundedCorner(corner: Corner, radius: androidx.compose.ui.unit.Dp): RoundedCornerShape {
        return when (corner) {
            Corner.TOP_START -> RoundedCornerShape(
                topStart = radius,
                topEnd = 0.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
            Corner.TOP_END -> RoundedCornerShape(
                topStart = 0.dp,
                topEnd = radius,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
            Corner.BOTTOM_START -> RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = radius,
                bottomEnd = 0.dp
            )
            Corner.BOTTOM_END -> RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 0.dp,
                bottomEnd = radius
            )
        }
    }
    
    /**
     * إنشاء شكل مع زاويتين مدورتين
     */
    fun createDoubleRoundedCorners(
        corner1: Corner,
        corner2: Corner,
        radius: androidx.compose.ui.unit.Dp
    ): RoundedCornerShape {
        val corners = mapOf(
            Corner.TOP_START to 0.dp,
            Corner.TOP_END to 0.dp,
            Corner.BOTTOM_START to 0.dp,
            Corner.BOTTOM_END to 0.dp
        ).toMutableMap()
        
        corners[corner1] = radius
        corners[corner2] = radius
        
        return RoundedCornerShape(
            topStart = corners[Corner.TOP_START]!!,
            topEnd = corners[Corner.TOP_END]!!,
            bottomStart = corners[Corner.BOTTOM_START]!!,
            bottomEnd = corners[Corner.BOTTOM_END]!!
        )
    }
}

// ══════════════════════════════════════════════════════════════════
// Enums مساعدة
// ══════════════════════════════════════════════════════════════════

enum class ShapeSize {
    EXTRA_SMALL,
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}

enum class ComponentType {
    BUTTON,
    CARD,
    INPUT,
    DIALOG,
    CHIP,
    BADGE
}

enum class Corner {
    TOP_START,
    TOP_END,
    BOTTOM_START,
    BOTTOM_END
}

// ══════════════════════════════════════════════════════════════════
// ثوابت مفيدة
// ══════════════════════════════════════════════════════════════════

object ShapeConstants {
    
    // نصف أقطار شائعة
    val RADIUS_NONE = 0.dp
    val RADIUS_EXTRA_SMALL = 2.dp
    val RADIUS_SMALL = 4.dp
    val RADIUS_MEDIUM = 8.dp
    val RADIUS_LARGE = 12.dp
    val RADIUS_EXTRA_LARGE = 16.dp
    val RADIUS_ROUND = 50.dp
    
    // أشكال شائعة الاستخدام
    val NO_RADIUS = RoundedCornerShape(RADIUS_NONE)
    val SLIGHTLY_ROUNDED = RoundedCornerShape(RADIUS_EXTRA_SMALL)
    val ROUNDED = RoundedCornerShape(RADIUS_MEDIUM)
    val VERY_ROUNDED = RoundedCornerShape(RADIUS_LARGE)
    val FULLY_ROUNDED = RoundedCornerShape(RADIUS_ROUND)
}