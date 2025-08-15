package com.flixflash.contactmanagerai.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.flixflash.contactmanagerai.data.database.dao.*
import com.flixflash.contactmanagerai.data.database.entities.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * FlixFlash Contact Manager AI
 * 
 * @module Database
 * @description قاعدة البيانات الرئيسية للتطبيق
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * قاعدة بيانات شاملة مع:
 * - جميع الـ Entities
 * - DAOs للعمليات المختلفة
 * - Migrations للتحديثات
 * - تكوين Hilt
 */

@Database(
    entities = [
        // Contact Entities
        ContactEntity::class,
        PhoneNumberEntity::class,
        EmailAddressEntity::class,
        AddressEntity::class,
        ContactGroupEntity::class,
        ContactGroupMemberEntity::class,
        ContactTagEntity::class,
        ContactTagRelationEntity::class,
        
        // Call Entities
        CallRecordEntity::class,
        AIConversationEntity::class,
        CallStatisticsEntity::class,
        BlockedNumberEntity::class,
        ScheduledCallEntity::class,
        CallQualityAnalysisEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FlixFlashDatabase : RoomDatabase() {
    
    // DAOs
    abstract fun contactDao(): ContactDao
    abstract fun phoneNumberDao(): PhoneNumberDao
    abstract fun emailAddressDao(): EmailAddressDao
    abstract fun addressDao(): AddressDao
    abstract fun contactGroupDao(): ContactGroupDao
    abstract fun contactTagDao(): ContactTagDao
    abstract fun callRecordDao(): CallRecordDao
    abstract fun aiConversationDao(): AIConversationDao
    abstract fun callStatisticsDao(): CallStatisticsDao
    abstract fun blockedNumberDao(): BlockedNumberDao
    abstract fun scheduledCallDao(): ScheduledCallDao
    abstract fun callQualityAnalysisDao(): CallQualityAnalysisDao
    
    companion object {
        const val DATABASE_NAME = "flixflash_contact_manager_ai.db"
        const val DATABASE_VERSION = 1
    }
}

/**
 * Type Converters للتحويل بين الأنواع
 */
class Converters {
    
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }
    
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.filter { it.isNotBlank() }
    }
    
    @TypeConverter
    fun fromLongList(value: List<Long>?): String? {
        return value?.joinToString(",")
    }
    
    @TypeConverter
    fun toLongList(value: String?): List<Long>? {
        return value?.split(",")?.mapNotNull { it.toLongOrNull() }
    }
}

/**
 * Hilt Module لتوفير قاعدة البيانات
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideFlixFlashDatabase(
        @ApplicationContext context: Context
    ): FlixFlashDatabase {
        return Room.databaseBuilder(
            context,
            FlixFlashDatabase::class.java,
            FlixFlashDatabase.DATABASE_NAME
        )
            .addMigrations(*getAllMigrations())
            .addCallback(DatabaseCallback())
            .fallbackToDestructiveMigration() // For development only
            .build()
    }
    
    @Provides
    fun provideContactDao(database: FlixFlashDatabase): ContactDao {
        return database.contactDao()
    }
    
    @Provides
    fun providePhoneNumberDao(database: FlixFlashDatabase): PhoneNumberDao {
        return database.phoneNumberDao()
    }
    
    @Provides
    fun provideEmailAddressDao(database: FlixFlashDatabase): EmailAddressDao {
        return database.emailAddressDao()
    }
    
    @Provides
    fun provideAddressDao(database: FlixFlashDatabase): AddressDao {
        return database.addressDao()
    }
    
    @Provides
    fun provideContactGroupDao(database: FlixFlashDatabase): ContactGroupDao {
        return database.contactGroupDao()
    }
    
    @Provides
    fun provideContactTagDao(database: FlixFlashDatabase): ContactTagDao {
        return database.contactTagDao()
    }
    
    @Provides
    fun provideCallRecordDao(database: FlixFlashDatabase): CallRecordDao {
        return database.callRecordDao()
    }
    
    @Provides
    fun provideAIConversationDao(database: FlixFlashDatabase): AIConversationDao {
        return database.aiConversationDao()
    }
    
    @Provides
    fun provideCallStatisticsDao(database: FlixFlashDatabase): CallStatisticsDao {
        return database.callStatisticsDao()
    }
    
    @Provides
    fun provideBlockedNumberDao(database: FlixFlashDatabase): BlockedNumberDao {
        return database.blockedNumberDao()
    }
    
    @Provides
    fun provideScheduledCallDao(database: FlixFlashDatabase): ScheduledCallDao {
        return database.scheduledCallDao()
    }
    
    @Provides
    fun provideCallQualityAnalysisDao(database: FlixFlashDatabase): CallQualityAnalysisDao {
        return database.callQualityAnalysisDao()
    }
}

/**
 * Database Callback للتهيئة الأولية
 */
class DatabaseCallback : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        
        // Initialize default data
        initializeDefaultData(db)
    }
    
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        
        // Enable foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON")
    }
    
    /**
     * تهيئة البيانات الافتراضية
     */
    private fun initializeDefaultData(db: SupportSQLiteDatabase) {
        try {
            // إنشاء مجموعات افتراضية
            createDefaultGroups(db)
            
            // إنشاء وسوم افتراضية
            createDefaultTags(db)
            
            // إنشاء أرقام محجوبة افتراضية
            createDefaultBlockedNumbers(db)
            
        } catch (e: Exception) {
            // Log error but don't crash
            android.util.Log.e("DatabaseCallback", "Failed to initialize default data", e)
        }
    }
    
    /**
     * إنشاء مجموعات افتراضية
     */
    private fun createDefaultGroups(db: SupportSQLiteDatabase) {
        val defaultGroups = listOf(
            "('family', 'العائلة', 'مجموعة أفراد العائلة', '#4CAF50', 'family', 1, 0, null, 0, 1, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('work', 'العمل', 'زملاء العمل والشركاء', '#2196F3', 'work', 1, 0, null, 0, 2, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('friends', 'الأصدقاء', 'الأصدقاء والمعارف', '#FF9800', 'group', 1, 0, null, 0, 3, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('emergency', 'الطوارئ', 'أرقام الطوارئ المهمة', '#F44336', 'emergency', 1, 0, null, 0, 4, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})"
        )
        
        defaultGroups.forEach { groupData ->
            db.execSQL("INSERT INTO contact_groups (id, name, description, color, icon, isSystem, isAutoGroup, autoGroupRule, memberCount, sortOrder, createdAt, updatedAt) VALUES $groupData")
        }
    }
    
    /**
     * إنشاء وسوم افتراضية
     */
    private fun createDefaultTags(db: SupportSQLiteDatabase) {
        val defaultTags = listOf(
            "('important', 'مهم', 'جهات اتصال مهمة', '#F44336', 'general', null, 0, 1, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('business', 'عمل', 'متعلق بالعمل', '#2196F3', 'work', null, 0, 1, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('personal', 'شخصي', 'أشخاص مقربون', '#4CAF50', 'personal', null, 0, 1, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('doctor', 'طبيب', 'أطباء ومتخصصون طبيون', '#9C27B0', 'general', null, 0, 1, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('delivery', 'توصيل', 'خدمات التوصيل', '#FF9800', 'general', null, 0, 1, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})"
        )
        
        defaultTags.forEach { tagData ->
            db.execSQL("INSERT INTO contact_tags (id, name, description, color, category, icon, usageCount, isSystem, createdAt, updatedAt) VALUES $tagData")
        }
    }
    
    /**
     * إنشاء أرقام محجوبة افتراضية
     */
    private fun createDefaultBlockedNumbers(db: SupportSQLiteDatabase) {
        val defaultBlockedNumbers = listOf(
            "('spam_pattern_1', null, '^\\+1.*555.*', null, null, 'pattern', 'calls', 'spam', 1, 1, 1, 0, 0, null, 0, null, 'system', 1.0, 'system', null, 0, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})",
            "('telemarketing_pattern', null, '^\\+1.*800.*', null, null, 'pattern', 'calls', 'telemarketing', 1, 1, 1, 0, 0, null, 0, null, 'system', 0.9, 'system', null, 0, ${System.currentTimeMillis()}, ${System.currentTimeMillis()})"
        )
        
        defaultBlockedNumbers.forEach { blockedData ->
            db.execSQL("INSERT INTO blocked_numbers (id, phoneNumber, pattern, countryCode, areaCode, blockType, blockLevel, blockReason, severity, isActive, allowEmergency, allowContacts, autoDelete, expiryTime, blockedCount, lastBlockedTime, reportedBy, confidence, source, sourceId, isShared, createdAt, updatedAt) VALUES $blockedData")
        }
    }
}

/**
 * Database Migrations
 */
private fun getAllMigrations(): Array<Migration> {
    return arrayOf(
        // Future migrations will be added here
    )
}

// Placeholder DAO interfaces - we'll create proper implementations
interface PhoneNumberDao {
    // Placeholder
}

interface EmailAddressDao {
    // Placeholder
}

interface AddressDao {
    // Placeholder
}

interface ContactGroupDao {
    // Placeholder
}

interface ContactTagDao {
    // Placeholder
}

interface CallRecordDao {
    // Placeholder
}

interface AIConversationDao {
    // Placeholder
}

interface CallStatisticsDao {
    // Placeholder
}

interface BlockedNumberDao {
    // Placeholder
}

interface ScheduledCallDao {
    // Placeholder
}

interface CallQualityAnalysisDao {
    // Placeholder
}