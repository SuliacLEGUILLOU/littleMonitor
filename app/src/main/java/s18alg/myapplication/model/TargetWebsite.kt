package s18alg.myapplication.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "Targets")
data class TargetWebsite(
        @ColumnInfo(name = "uri") var uri: String,
        @ColumnInfo(name = "ping_avg_delay") var average_delay: Double = 0.0,
        @ColumnInfo(name = "ping_try") var tryNumber: Int = 0,
        @ColumnInfo(name = "ping_success") var pingSuccess: Int = 0,
        @ColumnInfo(name = "last_return_code") var returnCode: Int = 0
) : Parcelable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var isUriValide = true

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
        isUriValide = parcel.readByte() != 0.toByte()
    }

    fun updatePing(status: Boolean, result: Double = 0.0) {
        if (status) {
            average_delay = ((average_delay * pingSuccess) + result) / (pingSuccess + 1)
            pingSuccess++
        }
        tryNumber++
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uri)
        parcel.writeDouble(average_delay)
        parcel.writeInt(tryNumber)
        parcel.writeInt(pingSuccess)
        parcel.writeInt(returnCode)
        parcel.writeByte(if (isUriValide) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TargetWebsite> {
        override fun createFromParcel(parcel: Parcel): TargetWebsite {
            return TargetWebsite(parcel)
        }

        override fun newArray(size: Int): Array<TargetWebsite?> {
            return arrayOfNulls(size)
        }
    }
}