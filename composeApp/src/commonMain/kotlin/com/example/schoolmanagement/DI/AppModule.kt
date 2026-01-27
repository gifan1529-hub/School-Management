package com.example.schoolmanagement.DI

import com.example.schoolmanagement.Data.Impl.AttendanceRepositoryImpl
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiClient
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import com.example.schoolmanagement.Domain.UseCase.LoginUC
import com.example.schoolmanagement.Domain.UseCase.SubmitAttendanceUC
import com.example.schoolmanagement.Domain.UseCase.getDetailUserUC
import com.example.schoolmanagement.Domain.UseCase.LogoutUseCase
import com.example.schoolmanagement.ViewModel.AuthViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.ViewModel.SignIn
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: org.koin.core.module.Module

val appModule = module {
    single { PrefsManager(get()) }
    single { ApiService(ApiClient.client) }

    single <AttendanceRepository>{  AttendanceRepositoryImpl(get()) }
//    // Network
//    single {
//        HttpClient {
//            install(ContentNegotiation) {
//                json(Json {
//                    ignoreUnknownKeys = true
//                })
//            }
//            install(Logging) {
//                level = LogLevel.ALL
//            }
//        }
//    }

    factoryOf(::LoginUC)
    factoryOf(::LogoutUseCase)
    factoryOf(::getDetailUserUC)
    factoryOf(::SubmitAttendanceUC)

    viewModel { SignIn(get(), get(), get())  }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { AuthViewModel(get()) }
}