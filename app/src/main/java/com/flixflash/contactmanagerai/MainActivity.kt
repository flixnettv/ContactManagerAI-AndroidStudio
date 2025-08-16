package com.flixflash.contactmanagerai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * FlixFlash Contact Manager AI
 * 
 * @module MainApp
 * @description الشاشة الرئيسية للتطبيق مع Jetpack Compose
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * نظام متطور لإدارة جهات الاتصال مع ذكاء اصطناعي مصري:
 * - واجهة مستخدم حديثة بـ Material Design 3
 * - دعم كامل للغة العربية والـ RTL
 * - تكامل مع جميع وحدات النظام
 * - إدارة الأذونات المتقدمة
 * - تجربة مستخدم سلسة ومتجاوبة
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    
    // مدير طلبات الأذونات
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        viewModel.onPermissionsResult(permissions)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // طلب الأذونات المطلوبة عند بدء التطبيق
        requestRequiredPermissions()
        
        setContent {
            FlixFlashTheme {
                // تخطيط التطبيق الرئيسي
                MainAppContent()
            }
        }
    }
    
    /**
     * طلب الأذونات المطلوبة للتطبيق
     */
    private fun requestRequiredPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )
        
        val missingPermissions = requiredPermissions.filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }
        
        if (missingPermissions.isNotEmpty()) {
            permissionLauncher.launch(missingPermissions.toTypedArray())
        }
    }
}

/**
 * المحتوى الرئيسي للتطبيق
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                FlixFlashTopAppBar()
            },
            bottomBar = {
                FlixFlashBottomNavigation(navController)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("add_contact")
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "إضافة جهة اتصال",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home") {
                    HomeScreen()
                }
                composable("contacts") {
                    ContactsScreen()
                }
                composable("calls") {
                    CallsScreen()
                }
                composable("sms") {
                    com.flixflash.contactmanagerai.ui.screens.SmsScreen()
                }
                composable("ai_assistant") {
                    AIAssistantScreen()
                }
                composable("settings") {
                    SettingsScreen(navController)
                }
                composable("advanced_settings") {
                    com.flixflash.contactmanagerai.ui.screens.AdvancedSettingsScreen()
                }
                composable("add_contact") {
                    AddContactScreen(navController)
                }
                composable("caller_id") {
                    com.flixflash.contactmanagerai.ui.screens.CallerIdScreen()
                }
            }
        }
    }
}

/**
 * شريط التطبيق العلوي
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlixFlashTopAppBar() {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContactPhone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "FlixFlash مدير الاتصال",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            IconButton(
                onClick = { /* Search functionality */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "بحث"
                )
            }
            IconButton(
                onClick = { /* Notifications */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "الإشعارات"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

/**
 * شريط التنقل السفلي
 */
@Composable
fun FlixFlashBottomNavigation(navController: NavHostController) {
    val items = listOf(
        NavigationItem("home", "الرئيسية", Icons.Default.Home),
        NavigationItem("contacts", "الأشخاص", Icons.Default.Contacts),
        NavigationItem("sms", "الرسائل", Icons.Default.Message),
        NavigationItem("calls", "المكالمات", Icons.Default.Phone),
        NavigationItem("ai_assistant", "المساعد AI", Icons.Default.SmartToy),
        NavigationItem("settings", "الإعدادات", Icons.Default.Settings)
    )
    
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = false, // سيتم تحديث هذا لاحقاً بناءً على الشاشة الحالية
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

/**
 * الشاشة الرئيسية
 */
@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // بطاقة الترحيب
            WelcomeCard()
        }
        
        item {
            // إحصائيات سريعة
            QuickStatsRow()
        }
        
        item {
            // الإجراءات السريعة
            QuickActionsGrid()
        }
        
        item {
            // آخر المكالمات
            RecentCallsSection()
        }
        
        item {
            // تحديثات المساعد AI
            AIUpdatesSection()
        }
    }
}

/**
 * بطاقة الترحيب
 */
@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.WavingHand,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "أهلاً بك في FlixFlash!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "مدير الاتصال الذكي مع الذكاء الاصطناعي المصري",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * صف الإحصائيات السريعة
 */
@Composable
fun QuickStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "جهات الاتصال",
            value = "156",
            icon = Icons.Default.Contacts,
            color = MaterialTheme.colorScheme.secondary
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "المكالمات اليوم",
            value = "12",
            icon = Icons.Default.Phone,
            color = MaterialTheme.colorScheme.tertiary
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "مكالمات محجوبة",
            value = "3",
            icon = Icons.Default.Block,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * بطاقة إحصائية
 */
@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * شبكة الإجراءات السريعة
 */
@Composable
fun QuickActionsGrid() {
    Column {
        Text(
            text = "إجراءات سريعة",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyColumn(
            modifier = Modifier.height(200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                listOf(
                    QuickAction("إضافة جهة اتصال جديدة", Icons.Default.PersonAdd),
                    QuickAction("مكالمة بمساعد AI", Icons.Default.SmartToy),
                    QuickAction("فحص المكالمات المزعجة", Icons.Default.Security),
                    QuickAction("تسجيل مكالمة", Icons.Default.RecordVoiceOver),
                    QuickAction("تحليل المكالمات", Icons.Default.Analytics)
                )
            ) { action ->
                QuickActionItem(action)
            }
        }
    }
}

/**
 * عنصر إجراء سريع
 */
@Composable
fun QuickActionItem(action: QuickAction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = { /* Handle action */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = action.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * قسم آخر المكالمات
 */
@Composable
fun RecentCallsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "آخر المكالمات",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(
                onClick = { /* Navigate to calls screen */ }
            ) {
                Text("عرض الكل")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "لا توجد مكالمات حديثة",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * قسم تحديثات المساعد AI
 */
@Composable
fun AIUpdatesSection() {
    Column {
        Text(
            text = "المساعد الذكي",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = "الذكاء الاصطناعي المصري جاهز!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "يمكنه التحدث باللهجة المصرية والرد على المكالمات",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

// الشاشات الأخرى (placeholder)
@Composable
fun ContactsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("شاشة جهات الاتصال - قيد التطوير")
    }
}

@Composable
fun CallsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("شاشة المكالمات - قيد التطوير")
    }
}

@Composable
fun AIAssistantScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("شاشة المساعد الذكي - قيد التطوير")
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("الإعدادات", style = MaterialTheme.typography.titleLarge)
        Divider()
        Button(onClick = { navController.navigate("advanced_settings") }) { Text("تفضيلات متقدمة") }
        Button(onClick = { navController.navigate("caller_id") }) { Text("Caller ID") }
    }
}

@Composable
fun AddContactScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("شاشة إضافة جهة اتصال - قيد التطوير")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text("عودة")
            }
        }
    }
}

// Data Classes
data class NavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

data class QuickAction(
    val title: String,
    val icon: ImageVector
)