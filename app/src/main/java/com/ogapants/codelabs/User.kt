package com.ogapants.codelabs

import android.arch.persistence.room.*
import android.graphics.Bitmap

//@see http://yaraki.github.io/slides/aac-room/index.html

/* Entity */

//@Entity(tableName = "users")
@Entity(indices = [Index(value = ["first_name",
    "last_name"], unique = true)])//インデックス
data class User(
        @PrimaryKey(autoGenerate = true)//主キーの自動採番
        val id: Int,
        @ColumnInfo(name = "first_name")
        var firstName: String,
        @ColumnInfo(name = "last_name")
        var lastName: String,
        @Embedded(prefix = "address_")//埋め込み
        val address: Address,
        @Ignore//フィールドの除外
        var picture: Bitmap?
)

@Entity(foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"])])//外部キー制約
data class Message(
        @PrimaryKey
        val id: Long,
        val userId: Long,
        val content: String
)

data class Address(val province: String, val city: String)


@Entity(primaryKeys = ["category", "code"])//複合キー
data class Product_複合キー1(
        val category: String,
        val code: String,
        val name: String)
//または

data class ProductId(val category: String, val code: String)
@Entity
data class Product_複合キー2(
        @PrimaryKey
        @Embedded
        val id: ProductId,
        val name: String)


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message): Long

    @Insert
    fun insertAll(messages: List<Message>): List<Long>

    @Insert
    fun insertBoth(message: Message, user: User)

    @Update
    fun update(message: Message)

    @Update
    fun updateAll(messages: List<Message>)

    @Update
    fun updateTogether(message: Message, user: User)

    @Delete
    fun delete(message: Message)

    @Delete
    fun deleteAll(messages: List<Message>)

    @Delete
    fun deleteTogether(message: Message, user: User)

    @Query("SELECT * FROM Message")
    fun all(): List<Message>

    @Query("SELECT * FROM Message WHERE id = :id")
    fun byId(id: Long): Message?
}

//@Query: カラム選択///カラムの名前と型さえ一致していればエンティティーでなくても値を受け取る器として利用できる
@Entity
data class Cheese(@PrimaryKey val id: Long, val name: String/* 他にたくさんのカラム */)

data class CheeseSummary(val id: Long, val name: String)
interface CheeseDao {
    @Query("SELECT id, name FROM Cheese")
    fun summaryAll(): List<CheeseSummary>
}
//________________________________________________