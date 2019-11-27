package nl.guldem.mylittlebunnie.di.modules

import androidx.room.Room
import nl.guldem.mylittlebunnie.services.AppDatabase
import nl.guldem.mylittlebunnie.services.NotificationService
import nl.guldem.mylittlebunnie.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "database-surprises"
        ).build()
    }

    single { NotificationService(get()) }
    single { get<AppDatabase>().supriseDao() }

    viewModel { HomeViewModel(get(), get()) }
}