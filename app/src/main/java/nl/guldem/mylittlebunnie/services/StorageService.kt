package nl.guldem.mylittlebunnie.services

import androidx.room.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

@Entity
data class SupriseStorage(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val date: DateTime,
    @ColumnInfo
    val opened: Boolean
)

@Dao
interface SuprisesDao {
    @Query("select * from SupriseStorage order by id")
    suspend fun getAll(): List<SupriseStorage>

    @Insert
    suspend fun insertAll(suprise: List<SupriseStorage>)

    @Update
    suspend fun updateSuprise(suprise: SupriseStorage)
}

@Database(entities = [SupriseStorage::class], version = 1)
@TypeConverters(DateTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun supriseDao(): SuprisesDao
}

class DateTypeConverters {
    private val formatter = DateTimeFormat.forPattern("dd-MM-yyyy").withZone(DateTimeZone.forID("Europe/Amsterdam"))

    @TypeConverter
    fun toDateTime(value: String): DateTime {
        return value.let {
            DateTime.parse(value, formatter)
        }
    }

    @TypeConverter
    fun fromDateTime(date: DateTime): String {
        return date.let {
            formatter.print(date)
        }

    }
}