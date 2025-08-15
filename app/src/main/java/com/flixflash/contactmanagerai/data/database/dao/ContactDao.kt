package com.flixflash.contactmanagerai.data.database.dao

import androidx.room.*
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import com.flixflash.contactmanagerai.data.database.entities.*

/**
 * FlixFlash Contact Manager AI
 * 
 * @module Data Layer
 * @description Contact DAO للتعامل مع جهات الاتصال
 * @author FlixFlash Technologies
 * @version 1.0.0
 * 
 * واجهة قاعدة البيانات لجهات الاتصال مع دعم:
 * - عمليات CRUD كاملة
 * - البحث المتقدم والفلترة
 * - Paging للقوائم الكبيرة
 * - Flow للتحديثات المباشرة
 * - استعلامات معقدة ومحسنة
 */

@Dao
interface ContactDao {
    
    // ══════════════════════════════════════════════════════════════════
    // العمليات الأساسية (CRUD)
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * إدراج جهة اتصال جديدة
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity): Long
    
    /**
     * إدراج عدة جهات اتصال
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)
    
    /**
     * تحديث جهة اتصال
     */
    @Update
    suspend fun updateContact(contact: ContactEntity)
    
    /**
     * حذف جهة اتصال
     */
    @Delete
    suspend fun deleteContact(contact: ContactEntity)
    
    /**
     * حذف جهة اتصال بالمعرف
     */
    @Query("DELETE FROM contacts WHERE id = :contactId")
    suspend fun deleteContactById(contactId: String)
    
    // ══════════════════════════════════════════════════════════════════
    // استعلامات الاستعلام والبحث
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * الحصول على جهة اتصال بالمعرف
     */
    @Query("SELECT * FROM contacts WHERE id = :contactId")
    suspend fun getContactById(contactId: String): ContactEntity?
    
    /**
     * الحصول على جهة اتصال بالمعرف مع تدفق مباشر
     */
    @Query("SELECT * FROM contacts WHERE id = :contactId")
    fun getContactByIdFlow(contactId: String): Flow<ContactEntity?>
    
    /**
     * الحصول على جميع جهات الاتصال
     */
    @Query("SELECT * FROM contacts ORDER BY displayName ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>
    
    /**
     * الحصول على جهات الاتصال مع Paging
     */
    @Query("SELECT * FROM contacts ORDER BY displayName ASC")
    fun getAllContactsPaged(): PagingSource<Int, ContactEntity>
    
    /**
     * البحث في جهات الاتصال بالاسم
     */
    @Query("""
        SELECT * FROM contacts 
        WHERE displayName LIKE '%' || :query || '%' 
           OR firstName LIKE '%' || :query || '%' 
           OR lastName LIKE '%' || :query || '%'
           OR nickname LIKE '%' || :query || '%'
           OR company LIKE '%' || :query || '%'
        ORDER BY 
            CASE 
                WHEN displayName LIKE :query || '%' THEN 1
                WHEN firstName LIKE :query || '%' OR lastName LIKE :query || '%' THEN 2
                ELSE 3
            END,
            displayName ASC
    """)
    fun searchContacts(query: String): Flow<List<ContactEntity>>
    
    /**
     * البحث في جهات الاتصال مع Paging
     */
    @Query("""
        SELECT * FROM contacts 
        WHERE displayName LIKE '%' || :query || '%' 
           OR firstName LIKE '%' || :query || '%' 
           OR lastName LIKE '%' || :query || '%'
           OR nickname LIKE '%' || :query || '%'
           OR company LIKE '%' || :query || '%'
        ORDER BY displayName ASC
    """)
    fun searchContactsPaged(query: String): PagingSource<Int, ContactEntity>
    
    /**
     * البحث برقم الهاتف
     */
    @Query("""
        SELECT c.* FROM contacts c
        INNER JOIN phone_numbers p ON c.id = p.contactId
        WHERE p.phoneNumber LIKE '%' || :phoneNumber || '%'
           OR p.normalizedNumber LIKE '%' || :phoneNumber || '%'
        ORDER BY c.displayName ASC
    """)
    fun searchContactsByPhone(phoneNumber: String): Flow<List<ContactEntity>>
    
    /**
     * البحث بالبريد الإلكتروني
     */
    @Query("""
        SELECT c.* FROM contacts c
        INNER JOIN email_addresses e ON c.id = e.contactId
        WHERE e.emailAddress LIKE '%' || :email || '%'
        ORDER BY c.displayName ASC
    """)
    fun searchContactsByEmail(email: String): Flow<List<ContactEntity>>
    
    // ══════════════════════════════════════════════════════════════════
    // استعلامات الفلترة والتصنيف
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * جهات الاتصال المفضلة
     */
    @Query("SELECT * FROM contacts WHERE isFavorite = 1 ORDER BY displayName ASC")
    fun getFavoriteContacts(): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال المحجوبة
     */
    @Query("SELECT * FROM contacts WHERE isBlocked = 1 ORDER BY displayName ASC")
    fun getBlockedContacts(): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال للطوارئ
     */
    @Query("SELECT * FROM contacts WHERE isEmergencyContact = 1 ORDER BY displayName ASC")
    fun getEmergencyContacts(): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال حسب المجموعة
     */
    @Query("""
        SELECT c.* FROM contacts c
        INNER JOIN contact_group_members cgm ON c.id = cgm.contactId
        WHERE cgm.groupId = :groupId
        ORDER BY c.displayName ASC
    """)
    fun getContactsByGroup(groupId: String): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال حسب الشركة
     */
    @Query("SELECT * FROM contacts WHERE company = :company ORDER BY displayName ASC")
    fun getContactsByCompany(company: String): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال الذين لديهم صور
     */
    @Query("SELECT * FROM contacts WHERE photoUri IS NOT NULL ORDER BY displayName ASC")
    fun getContactsWithPhotos(): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال المتزامنة مع السحابة
     */
    @Query("SELECT * FROM contacts WHERE isCloudSynced = 1 ORDER BY displayName ASC")
    fun getCloudSyncedContacts(): Flow<List<ContactEntity>>
    
    // ══════════════════════════════════════════════════════════════════
    // إحصائيات وتجميعات
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * عدد جهات الاتصال الإجمالي
     */
    @Query("SELECT COUNT(*) FROM contacts")
    fun getTotalContactsCount(): Flow<Int>
    
    /**
     * عدد جهات الاتصال المفضلة
     */
    @Query("SELECT COUNT(*) FROM contacts WHERE isFavorite = 1")
    fun getFavoriteContactsCount(): Flow<Int>
    
    /**
     * عدد جهات الاتصال المحجوبة
     */
    @Query("SELECT COUNT(*) FROM contacts WHERE isBlocked = 1")
    fun getBlockedContactsCount(): Flow<Int>
    
    /**
     * إحصائيات جهات الاتصال حسب النشاط
     */
    @Query("""
        SELECT 
            COUNT(*) as total,
            COUNT(CASE WHEN lastContactTime > :since THEN 1 END) as recent,
            COUNT(CASE WHEN totalCalls > 0 THEN 1 END) as withCalls,
            COUNT(CASE WHEN totalMessages > 0 THEN 1 END) as withMessages
        FROM contacts
    """)
    suspend fun getContactsActivityStats(since: Long): ContactActivityStats
    
    /**
     * أكثر جهات الاتصال تفاعلاً
     */
    @Query("""
        SELECT * FROM contacts 
        WHERE interactionScore > 0 
        ORDER BY interactionScore DESC, lastContactTime DESC 
        LIMIT :limit
    """)
    fun getMostActiveContacts(limit: Int = 10): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال الأحدث
     */
    @Query("SELECT * FROM contacts ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentContacts(limit: Int = 10): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال التي تم الاتصال بها مؤخراً
     */
    @Query("""
        SELECT * FROM contacts 
        WHERE lastContactTime IS NOT NULL 
        ORDER BY lastContactTime DESC 
        LIMIT :limit
    """)
    fun getRecentlyContactedContacts(limit: Int = 10): Flow<List<ContactEntity>>
    
    // ══════════════════════════════════════════════════════════════════
    // عمليات التحديث المتقدمة
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * تحديث حالة المفضلة
     */
    @Query("UPDATE contacts SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun updateFavoriteStatus(contactId: String, isFavorite: Boolean, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * تحديث حالة الحجب
     */
    @Query("UPDATE contacts SET isBlocked = :isBlocked, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun updateBlockedStatus(contactId: String, isBlocked: Boolean, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * تحديث نقاط التفاعل
     */
    @Query("UPDATE contacts SET interactionScore = :score, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun updateInteractionScore(contactId: String, score: Int, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * تحديث وقت آخر اتصال
     */
    @Query("UPDATE contacts SET lastContactTime = :timestamp, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun updateLastContactTime(contactId: String, timestamp: Long, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * زيادة عداد المكالمات
     */
    @Query("UPDATE contacts SET totalCalls = totalCalls + 1, lastCallTime = :callTime, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun incrementCallCount(contactId: String, callTime: Long, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * زيادة عداد الرسائل
     */
    @Query("UPDATE contacts SET totalMessages = totalMessages + 1, lastMessageTime = :messageTime, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun incrementMessageCount(contactId: String, messageTime: Long, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * تحديث صورة جهة الاتصال
     */
    @Query("UPDATE contacts SET photoUri = :photoUri, thumbnailUri = :thumbnailUri, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun updateContactPhoto(contactId: String, photoUri: String?, thumbnailUri: String?, updatedAt: Long = System.currentTimeMillis())
    
    // ══════════════════════════════════════════════════════════════════
    // عمليات المزامنة
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * تحديث حالة المزامنة
     */
    @Query("UPDATE contacts SET isCloudSynced = :isSynced, lastSyncTime = :syncTime, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun updateSyncStatus(contactId: String, isSynced: Boolean, syncTime: Long, updatedAt: Long = System.currentTimeMillis())
    
    /**
     * الحصول على جهات الاتصال غير المتزامنة
     */
    @Query("SELECT * FROM contacts WHERE isCloudSynced = 0 OR lastSyncTime < updatedAt")
    suspend fun getUnsyncedContacts(): List<ContactEntity>
    
    /**
     * الحصول على جهات الاتصال المحدثة بعد وقت معين
     */
    @Query("SELECT * FROM contacts WHERE updatedAt > :since ORDER BY updatedAt ASC")
    suspend fun getContactsUpdatedSince(since: Long): List<ContactEntity>
    
    // ══════════════════════════════════════════════════════════════════
    // عمليات التنظيف والصيانة
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * حذف جهات الاتصال بدون أرقام هواتف أو إيميلات
     */
    @Query("""
        DELETE FROM contacts 
        WHERE id NOT IN (
            SELECT DISTINCT contactId FROM phone_numbers
            UNION
            SELECT DISTINCT contactId FROM email_addresses
        )
    """)
    suspend fun deleteContactsWithoutPhoneOrEmail()
    
    /**
     * حذف جهات الاتصال المكررة
     */
    @Query("""
        DELETE FROM contacts 
        WHERE id NOT IN (
            SELECT MIN(id) 
            FROM contacts 
            GROUP BY displayName, IFNULL(firstName, ''), IFNULL(lastName, '')
        )
    """)
    suspend fun deleteDuplicateContacts()
    
    /**
     * تحديث أوقات آخر تحديث للجميع
     */
    @Query("UPDATE contacts SET updatedAt = :timestamp")
    suspend fun updateAllContactsTimestamp(timestamp: Long = System.currentTimeMillis())
    
    // ══════════════════════════════════════════════════════════════════
    // استعلامات متقدمة ومعقدة
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * البحث الشامل مع النتائج المرتبة حسب الصلة
     */
    @Query("""
        SELECT c.*, 
               (CASE 
                    WHEN c.displayName LIKE :query || '%' THEN 10
                    WHEN c.firstName LIKE :query || '%' OR c.lastName LIKE :query || '%' THEN 8
                    WHEN c.company LIKE :query || '%' THEN 6
                    WHEN c.displayName LIKE '%' || :query || '%' THEN 4
                    WHEN c.notes LIKE '%' || :query || '%' THEN 2
                    ELSE 1
                END + 
                CASE WHEN c.isFavorite = 1 THEN 5 ELSE 0 END +
                CASE WHEN c.totalCalls > 0 THEN 3 ELSE 0 END +
                CASE WHEN c.lastContactTime > :recentThreshold THEN 2 ELSE 0 END
               ) as relevanceScore
        FROM contacts c
        WHERE c.displayName LIKE '%' || :query || '%' 
           OR c.firstName LIKE '%' || :query || '%' 
           OR c.lastName LIKE '%' || :query || '%'
           OR c.company LIKE '%' || :query || '%'
           OR c.notes LIKE '%' || :query || '%'
           OR c.id IN (
               SELECT p.contactId FROM phone_numbers p 
               WHERE p.phoneNumber LIKE '%' || :query || '%'
           )
           OR c.id IN (
               SELECT e.contactId FROM email_addresses e 
               WHERE e.emailAddress LIKE '%' || :query || '%'
           )
        ORDER BY relevanceScore DESC, c.displayName ASC
    """)
    fun advancedSearch(query: String, recentThreshold: Long = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000): Flow<List<ContactEntity>>
    
    /**
     * اقتراحات جهات الاتصال الذكية
     */
    @Query("""
        SELECT * FROM contacts
        WHERE (
            interactionScore > 0 
            OR lastContactTime > :recentThreshold
            OR isFavorite = 1
        )
        AND isBlocked = 0
        ORDER BY 
            (interactionScore * 0.4 + 
             CASE WHEN lastContactTime > :recentThreshold THEN 30 ELSE 0 END +
             CASE WHEN isFavorite = 1 THEN 20 ELSE 0 END +
             totalCalls * 0.1) DESC
        LIMIT :limit
    """)
    fun getSmartSuggestions(
        recentThreshold: Long = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000,
        limit: Int = 5
    ): Flow<List<ContactEntity>>
    
    // ══════════════════════════════════════════════════════════════════
    // استعلامات خاصة بالذكاء الاصطناعي
    // ══════════════════════════════════════════════════════════════════
    
    /**
     * جهات الاتصال التي تسمح بالمكالمات بالذكاء الاصطناعي
     */
    @Query("SELECT * FROM contacts WHERE allowAICalls = 1 ORDER BY displayName ASC")
    fun getAICallEnabledContacts(): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال حسب اللهجة المفضلة
     */
    @Query("SELECT * FROM contacts WHERE preferredDialect = :dialect ORDER BY displayName ASC")
    fun getContactsByDialect(dialect: String): Flow<List<ContactEntity>>
    
    /**
     * جهات الاتصال التي تسمح بالتسجيل
     */
    @Query("SELECT * FROM contacts WHERE allowRecording = 1 ORDER BY displayName ASC")
    fun getRecordingEnabledContacts(): Flow<List<ContactEntity>>
}

/**
 * فئة البيانات لإحصائيات نشاط جهات الاتصال
 */
data class ContactActivityStats(
    val total: Int,
    val recent: Int,
    val withCalls: Int,
    val withMessages: Int
)