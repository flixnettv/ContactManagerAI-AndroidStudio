package com.flixflash.contactmanagerai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

/**
 * FlixFlash Contact Manager AI
 * 
 * @module UI Theme
 * @description نظام الألوان والتصميم للتطبيق
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * نظام تصميم متطور مع دعم:
 * - Material Design 3
 * - الوضع المظلم والفاتح
 * - دعم RTL للغة العربية
 * - ألوان FlixFlash المخصصة
 * - تجربة مستخدم متسقة
 */

// ألوان FlixFlash المخصصة - الوضع المظلم
private val DarkColorScheme = darkColorScheme(
    primary = FlixFlashColors.Primary,
    onPrimary = FlixFlashColors.OnPrimary,
    primaryContainer = FlixFlashColors.PrimaryContainer,
    onPrimaryContainer = FlixFlashColors.OnPrimaryContainer,
    
    secondary = FlixFlashColors.Secondary,
    onSecondary = FlixFlashColors.OnSecondary,
    secondaryContainer = FlixFlashColors.SecondaryContainer,
    onSecondaryContainer = FlixFlashColors.OnSecondaryContainer,
    
    tertiary = FlixFlashColors.Tertiary,
    onTertiary = FlixFlashColors.OnTertiary,
    tertiaryContainer = FlixFlashColors.TertiaryContainer,
    onTertiaryContainer = FlixFlashColors.OnTertiaryContainer,
    
    error = FlixFlashColors.Error,
    onError = FlixFlashColors.OnError,
    errorContainer = FlixFlashColors.ErrorContainer,
    onErrorContainer = FlixFlashColors.OnErrorContainer,
    
    background = FlixFlashColors.DarkBackground,
    onBackground = FlixFlashColors.DarkOnBackground,
    surface = FlixFlashColors.DarkSurface,
    onSurface = FlixFlashColors.DarkOnSurface,
    
    surfaceVariant = FlixFlashColors.DarkSurfaceVariant,
    onSurfaceVariant = FlixFlashColors.DarkOnSurfaceVariant,
    outline = FlixFlashColors.DarkOutline,
    outlineVariant = FlixFlashColors.DarkOutlineVariant,
    
    scrim = FlixFlashColors.Scrim,
    inverseSurface = FlixFlashColors.LightSurface,
    inverseOnSurface = FlixFlashColors.LightOnSurface,
    inversePrimary = FlixFlashColors.Primary
)

// ألوان FlixFlash المخصصة - الوضع الفاتح
private val LightColorScheme = lightColorScheme(
    primary = FlixFlashColors.Primary,
    onPrimary = FlixFlashColors.OnPrimary,
    primaryContainer = FlixFlashColors.PrimaryContainer,
    onPrimaryContainer = FlixFlashColors.OnPrimaryContainer,
    
    secondary = FlixFlashColors.Secondary,
    onSecondary = FlixFlashColors.OnSecondary,
    secondaryContainer = FlixFlashColors.SecondaryContainer,
    onSecondaryContainer = FlixFlashColors.OnSecondaryContainer,
    
    tertiary = FlixFlashColors.Tertiary,
    onTertiary = FlixFlashColors.OnTertiary,
    tertiaryContainer = FlixFlashColors.TertiaryContainer,
    onTertiaryContainer = FlixFlashColors.OnTertiaryContainer,
    
    error = FlixFlashColors.Error,
    onError = FlixFlashColors.OnError,
    errorContainer = FlixFlashColors.ErrorContainer,
    onErrorContainer = FlixFlashColors.OnErrorContainer,
    
    background = FlixFlashColors.LightBackground,
    onBackground = FlixFlashColors.LightOnBackground,
    surface = FlixFlashColors.LightSurface,
    onSurface = FlixFlashColors.LightOnSurface,
    
    surfaceVariant = FlixFlashColors.LightSurfaceVariant,
    onSurfaceVariant = FlixFlashColors.LightOnSurfaceVariant,
    outline = FlixFlashColors.LightOutline,
    outlineVariant = FlixFlashColors.LightOutlineVariant,
    
    scrim = FlixFlashColors.Scrim,
    inverseSurface = FlixFlashColors.DarkSurface,
    inverseOnSurface = FlixFlashColors.DarkOnSurface,
    inversePrimary = FlixFlashColors.Primary
)

/**
 * FlixFlash Theme الرئيسي للتطبيق
 */
@Composable
fun FlixFlashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // التحكم الديناميكي في الألوان (Android 12+)
    dynamicColor: Boolean = false,
    // دعم RTL للغة العربية
    isRtl: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val context = LocalContext.current
    
    // تطبيق ألوان شريط الحالة
    SideEffect {
        val activity = context as? Activity
        activity?.let {
            val window = it.window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    
    // دعم RTL
    val layoutDirection = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    
    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = FlixFlashTypography,
            shapes = FlixFlashShapes,
            content = content
        )
    }
}

/**
 * Theme ألوان خاصة لـ FlixFlash
 */
@Composable
fun FlixFlashAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Theme خاص بالذكاء الاصطناعي مع ألوان مميزة
    val aiColorScheme = if (darkTheme) {
        DarkColorScheme.copy(
            primary = FlixFlashColors.AIPrimary,
            primaryContainer = FlixFlashColors.AIPrimaryContainer,
            tertiary = FlixFlashColors.AIAccent,
            tertiaryContainer = FlixFlashColors.AIAccentContainer
        )
    } else {
        LightColorScheme.copy(
            primary = FlixFlashColors.AIPrimary,
            primaryContainer = FlixFlashColors.AIPrimaryContainer,
            tertiary = FlixFlashColors.AIAccent,
            tertiaryContainer = FlixFlashColors.AIAccentContainer
        )
    }
    
    MaterialTheme(
        colorScheme = aiColorScheme,
        typography = FlixFlashTypography,
        shapes = FlixFlashShapes,
        content = content
    )
}

/**
 * Theme للمكالمات الطارئة
 */
@Composable
fun FlixFlashEmergencyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val emergencyColorScheme = if (darkTheme) {
        DarkColorScheme.copy(
            primary = FlixFlashColors.Emergency,
            primaryContainer = FlixFlashColors.EmergencyContainer,
            error = FlixFlashColors.EmergencyDark
        )
    } else {
        LightColorScheme.copy(
            primary = FlixFlashColors.Emergency,
            primaryContainer = FlixFlashColors.EmergencyContainer,
            error = FlixFlashColors.EmergencyDark
        )
    }
    
    MaterialTheme(
        colorScheme = emergencyColorScheme,
        typography = FlixFlashTypography,
        shapes = FlixFlashShapes,
        content = content
    )
}

/**
 * إضافات مفيدة للألوان
 */
object FlixFlashThemeUtils {
    
    /**
     * الحصول على لون بناءً على نوع المكالمة
     */
    @Composable
    fun getCallTypeColor(callType: String): androidx.compose.ui.graphics.Color {
        return when (callType.lowercase()) {
            "incoming" -> FlixFlashColors.Success
            "outgoing" -> FlixFlashColors.Primary
            "missed" -> FlixFlashColors.Warning
            "blocked" -> FlixFlashColors.Error
            "ai_handled" -> FlixFlashColors.AIPrimary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
    }
    
    /**
     * الحصول على لون بناءً على حالة الإزعاج
     */
    @Composable
    fun getSpamStatusColor(isSpam: Boolean): androidx.compose.ui.graphics.Color {
        return if (isSpam) FlixFlashColors.Error else FlixFlashColors.Success
    }
    
    /**
     * الحصول على لون بناءً على حالة الذكاء الاصطناعي
     */
    @Composable
    fun getAIStatusColor(isActive: Boolean): androidx.compose.ui.graphics.Color {
        return if (isActive) FlixFlashColors.AIPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    /**
     * الحصول على لون بناءً على مستوى الأولوية
     */
    @Composable
    fun getPriorityColor(priority: Int): androidx.compose.ui.graphics.Color {
        return when (priority) {
            0 -> MaterialTheme.colorScheme.onSurfaceVariant // منخفض
            1 -> FlixFlashColors.Warning // متوسط
            2 -> FlixFlashColors.Emergency // عالي
            else -> MaterialTheme.colorScheme.primary
        }
    }
}

/**
 * إضافات للحصول على ألوان معكوسة
 */
val ColorScheme.successColor: androidx.compose.ui.graphics.Color
    @Composable get() = FlixFlashColors.Success

val ColorScheme.warningColor: androidx.compose.ui.graphics.Color
    @Composable get() = FlixFlashColors.Warning

val ColorScheme.aiColor: androidx.compose.ui.graphics.Color
    @Composable get() = FlixFlashColors.AIPrimary

val ColorScheme.emergencyColor: androidx.compose.ui.graphics.Color
    @Composable get() = FlixFlashColors.Emergency

/**
 * دالة للحصول على اللون المناسب للنص على خلفية معينة
 */
@Composable
fun getOnBackgroundColor(backgroundColor: androidx.compose.ui.graphics.Color): androidx.compose.ui.graphics.Color {
    val luminance = androidx.compose.ui.graphics.luminance(backgroundColor)
    return if (luminance > 0.5f) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.surface
    }
}

/**
 * ألوان خاصة بحالات مختلفة
 */
object FlixFlashStatusColors {
    
    @get:Composable
    val online: androidx.compose.ui.graphics.Color
        get() = FlixFlashColors.Success
    
    @get:Composable
    val offline: androidx.compose.ui.graphics.Color
        get() = MaterialTheme.colorScheme.onSurfaceVariant
    
    @get:Composable
    val busy: androidx.compose.ui.graphics.Color
        get() = FlixFlashColors.Warning
    
    @get:Composable
    val doNotDisturb: androidx.compose.ui.graphics.Color
        get() = FlixFlashColors.Error
    
    @get:Composable
    val aiActive: androidx.compose.ui.graphics.Color
        get() = FlixFlashColors.AIPrimary
    
    @get:Composable
    val recording: androidx.compose.ui.graphics.Color
        get() = FlixFlashColors.Emergency
    
    @get:Composable
    val encrypted: androidx.compose.ui.graphics.Color
        get() = FlixFlashColors.Success
    
    @get:Composable
    val verified: androidx.compose.ui.graphics.Color
        get() = FlixFlashColors.Primary
}