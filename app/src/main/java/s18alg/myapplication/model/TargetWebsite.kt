package s18alg.myapplication.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Targets")
data class TargetWebsite(
        @ColumnInfo(name = "uri") var uri: String,
        @ColumnInfo(name = "ping_avg_delay") var average_delay: Double = 0.0,
        @ColumnInfo(name = "ping_try") var tryNumber: Int = 0,
        @ColumnInfo(name = "ping_success") var pingSuccess: Int = 0
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var detailView: Boolean = false

    fun select() {
        detailView = !detailView
    }
}