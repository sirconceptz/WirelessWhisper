package com.hermanowicz.wirelesswhisper.di

import android.content.Context
import androidx.room.Room
import com.hermanowicz.wirelesswhisper.data.local.db.DeviceDao
import com.hermanowicz.wirelesswhisper.data.local.db.LocalDb
import com.hermanowicz.wirelesswhisper.utils.Constants.LOCAL_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideLocalDb(@ApplicationContext context: Context): LocalDb {
        return Room.databaseBuilder(
            context,
            LocalDb::class.java,
            LOCAL_DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDeviceDao(localDb: LocalDb): DeviceDao {
        return localDb.deviceDao()
    }
}
