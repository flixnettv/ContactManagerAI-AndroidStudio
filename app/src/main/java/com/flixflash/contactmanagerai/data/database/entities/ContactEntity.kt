package com.flixflash.contactmanagerai.data.database.entities

import androidx.room.*
import java.util.*

/**
 * FlixFlash Contact Manager AI
 * 
 * @module Data Layer
 * @description Contact Entity لقاعدة بيانات Room
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * كيان جهة الاتصال مع دعم:
 * - جميع معلومات جهة الاتصال
 * - ربط مع أرقام الهواتف والإيميلات
 * - تصنيف وتجميع جهات الاتصال
 * - تتبع التغييرات والمزامنة
 * - دعم الصور والملاحظات
 */

@Entity(
    tableName = "contacts",
    indices = [
        Index(value = ["firstName"], name = "idx_contact_first_name"),
        Index(value = ["lastName"], name = "idx_contact_last_name"),
        Index(value = ["displayName"], name = "idx_contact_display_name"),
        Index(value = ["deviceContactId"], name = "idx_device_contact_id"),
        Index(value = ["isFavorite"], name = "idx_contact_favorite"),
        Index(value = ["isBlocked"], name = "idx_contact_blocked"),
        Index(value = ["groupId"], name = "idx_contact_group"),
        Index(value = ["lastContactTime"], name = "idx_last_contact_time")
    ]
)
data class ContactEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // المعلومات الأساسية
    val firstName: String? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val displayName: String,
    val nickname: String? = null,
    
    // معلومات إضافية
    val company: String? = null,
    val jobTitle: String? = null,
    val department: String? = null,
    val notes: String? = null,
    
    // الصورة
    val photoUri: String? = null,
    val thumbnailUri: String? = null,
    
    // الحالة والتصنيف
    val isFavorite: Boolean = false,
    val isBlocked: Boolean = false,
    val isSpam: Boolean = false,
    val isVerified: Boolean = false,
    val isEmergencyContact: Boolean = false,
    
    // التجميع
    val groupId: String? = null,
    val tags: String? = null, // JSON array of tags
    
    // معلومات المزامنة
    val deviceContactId: String? = null,
    val accountName: String? = null,
    val accountType: String? = null,
    val isCloudSynced: Boolean = false,
    val cloudId: String? = null,
    
    // إحصائيات الاتصال
    val totalCalls: Int = 0,
    val totalMessages: Int = 0,
    val totalEmailsSent: Int = 0,
    val lastContactTime: Long? = null,
    val lastCallTime: Long? = null,
    val lastMessageTime: Long? = null,
    val interactionScore: Int = 0, // نقاط التفاعل
    
    // معلومات الذكاء الاصطناعي
    val aiPreferences: String? = null, // JSON object with AI preferences
    val communicationStyle: String? = null, // formal, casual, friendly, etc.
    val preferredLanguage: String = "ar", // العربية افتراضياً
    val preferredDialect: String = "egyptian", // اللهجة المصرية افتراضياً
    
    // تواريخ مهمة
    val birthday: Long? = null,
    val anniversary: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastSyncTime: Long? = null,
    
    // إعدادات الخصوصية
    val privacyLevel: Int = 0, // 0=عام، 1=خاص، 2=سري
    val shareWithApps: Boolean = true,
    val allowAICalls: Boolean = true,
    val allowRecording: Boolean = false,
    
    // معلومات النسخ الاحتياطي
    val isBackedUp: Boolean = false,
    val backupTime: Long? = null,
    val version: Int = 1
)

/**
 * كيان أرقام الهواتف
 */
@Entity(
    tableName = "phone_numbers",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["contactId"], name = "idx_phone_contact_id"),
        Index(value = ["phoneNumber"], name = "idx_phone_number"),
        Index(value = ["normalizedNumber"], name = "idx_normalized_number"),
        Index(value = ["isPrimary"], name = "idx_phone_primary"),
        Index(value = ["type"], name = "idx_phone_type")
    ]
)
data class PhoneNumberEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    val contactId: String,
    val phoneNumber: String,
    val normalizedNumber: String, // رقم منسق لسهولة البحث
    val countryCode: String? = null,
    val type: String = "mobile", // mobile, home, work, fax, etc.
    val label: String? = null, // تسمية مخصصة
    val isPrimary: Boolean = false,
    val isVerified: Boolean = false,
    val isWhatsApp: Boolean = false,
    val isTelegram: Boolean = false,
    val isViber: Boolean = false,
    
    // إحصائيات الاستخدام
    val callCount: Int = 0,
    val messageCount: Int = 0,
    val lastUsedTime: Long? = null,
    val isSpamReported: Boolean = false,
    val spamScore: Float = 0.0f,
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان عناوين البريد الإلكتروني
 */
@Entity(
    tableName = "email_addresses",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["contactId"], name = "idx_email_contact_id"),
        Index(value = ["emailAddress"], name = "idx_email_address"),
        Index(value = ["isPrimary"], name = "idx_email_primary"),
        Index(value = ["type"], name = "idx_email_type")
    ]
)
data class EmailAddressEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    val contactId: String,
    val emailAddress: String,
    val type: String = "personal", // personal, work, other
    val label: String? = null,
    val isPrimary: Boolean = false,
    val isVerified: Boolean = false,
    
    // إحصائيات الاستخدام
    val emailCount: Int = 0,
    val lastUsedTime: Long? = null,
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان العناوين الجغرافية
 */
@Entity(
    tableName = "addresses",
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["contactId"], name = "idx_address_contact_id"),
        Index(value = ["type"], name = "idx_address_type"),
        Index(value = ["city"], name = "idx_address_city"),
        Index(value = ["country"], name = "idx_address_country")
    ]
)
data class AddressEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    val contactId: String,
    val type: String = "home", // home, work, other
    val label: String? = null,
    
    // تفاصيل العنوان
    val street: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val region: String? = null,
    
    // الموقع الجغرافي
    val latitude: Double? = null,
    val longitude: Double? = null,
    val formattedAddress: String? = null,
    
    val isPrimary: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان مجموعات جهات الاتصال
 */
@Entity(
    tableName = "contact_groups",
    indices = [
        Index(value = ["name"], name = "idx_group_name"),
        Index(value = ["isSystem"], name = "idx_group_system"),
        Index(value = ["isAutoGroup"], name = "idx_group_auto")
    ]
)
data class ContactGroupEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    val name: String,
    val description: String? = null,
    val color: String = "#2196F3", // لون المجموعة
    val icon: String? = null, // أيقونة المجموعة
    
    val isSystem: Boolean = false, // مجموعة نظام
    val isAutoGroup: Boolean = false, // مجموعة تلقائية
    val autoGroupRule: String? = null, // قواعد التجميع التلقائي
    
    val memberCount: Int = 0,
    val sortOrder: Int = 0,
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان علاقة جهات الاتصال بالمجموعات (Many-to-Many)
 */
@Entity(
    tableName = "contact_group_members",
    primaryKeys = ["contactId", "groupId"],
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ContactGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["contactId"], name = "idx_member_contact_id"),
        Index(value = ["groupId"], name = "idx_member_group_id")
    ]
)
data class ContactGroupMemberEntity(
    val contactId: String,
    val groupId: String,
    val addedAt: Long = System.currentTimeMillis(),
    val addedBy: String = "user" // user, system, auto
)

/**
 * كيان التسميات والوسوم
 */
@Entity(
    tableName = "contact_tags",
    indices = [
        Index(value = ["name"], name = "idx_tag_name"),
        Index(value = ["category"], name = "idx_tag_category"),
        Index(value = ["color"], name = "idx_tag_color")
    ]
)
data class ContactTagEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    val name: String,
    val description: String? = null,
    val color: String = "#757575",
    val category: String = "general", // general, work, personal, family, etc.
    val icon: String? = null,
    
    val usageCount: Int = 0,
    val isSystem: Boolean = false,
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * كيان علاقة جهات الاتصال بالوسوم (Many-to-Many)
 */
@Entity(
    tableName = "contact_tag_relations",
    primaryKeys = ["contactId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ContactTagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ContactTagRelationEntity(
    val contactId: String,
    val tagId: String,
    val addedAt: Long = System.currentTimeMillis()
)