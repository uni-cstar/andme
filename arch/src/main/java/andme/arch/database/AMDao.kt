package andme.arch.database

import androidx.room.*

/**
 * Created by Lucio on 2021/1/7.
 */
@Dao
interface AMDao<T> {

    /**
     * @return 插入之后的rowIds
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: T): LongArray

    /**
     * @return 插入之后的rowIds
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<T>): LongArray

    @Update
    fun update(vararg entity: T)

    @Update
    fun update(entity: List<T>)

    @Delete
    fun delete(vararg entity: T)

    @Delete
    fun delete(entity: List<T>)

}