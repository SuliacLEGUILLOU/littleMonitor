package s18alg.myapplication.model

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE

@Dao interface TargetDAO {
    @Query("SELECT * FROM Targets")
    fun getAll(): MutableList<TargetWebsite>

    @Query("select * from Targets where id = :id")
    fun findById(id: Int): TargetWebsite

    @Query("select * from Targets where uri = :uri")
    fun findByUri(uri: String): TargetWebsite

    @Insert(onConflict = REPLACE)
    fun insert(task: TargetWebsite)

    @Update(onConflict = REPLACE)
    fun update(task: TargetWebsite)

    @Delete
    fun delete(task: TargetWebsite)
}