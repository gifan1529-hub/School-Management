package com.example.schoolmanagement.DI

import com.example.schoolmanagement.Data.Impl.AttendanceRepositoryImpl
import com.example.schoolmanagement.Data.Impl.ScheduleRepositoryImpl
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiClient
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import com.example.schoolmanagement.Domain.Repository.ScheduleRepository
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceHistoryUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceStatusUseCase
import com.example.schoolmanagement.Domain.UseCase.GetScheduleUseCase
import com.example.schoolmanagement.Domain.UseCase.GetTeacherDasboardUseCase
import com.example.schoolmanagement.Domain.UseCase.LoginUC
import com.example.schoolmanagement.Domain.UseCase.SubmitAttendanceUC
import com.example.schoolmanagement.Domain.UseCase.getDetailUserUC
import com.example.schoolmanagement.Domain.UseCase.LogoutUseCase
import com.example.schoolmanagement.ViewModel.AuthViewModel
import com.example.schoolmanagement.ViewModel.HistoryViewModel
import com.example.schoolmanagement.ViewModel.HomeTeacherViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.ViewModel.MarkAttendanceViewModel
import com.example.schoolmanagement.ViewModel.ProfileViewModel
import com.example.schoolmanagement.ViewModel.ScheduleViewModel
import com.example.schoolmanagement.ViewModel.SignIn
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import qrgenerator.qrkitpainter.location

expect val platformModule: org.koin.core.module.Module

val appModule = module {
    single { PrefsManager(get()) }
    single { ApiService(ApiClient.client) }

    single <AttendanceRepository>{ AttendanceRepositoryImpl(get()) }
    single <ScheduleRepository>{ ScheduleRepositoryImpl(get()) }
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
    factoryOf(::GetScheduleUseCase)
    factoryOf(::GetAttendanceHistoryUseCase)
    factoryOf(::GetAttendanceStatusUseCase)
    factoryOf(::GetTeacherDasboardUseCase)

    viewModel { MarkAttendanceViewModel(get()) }
    viewModel { HomeTeacherViewModel(get(), get(), get(), get(), get()) }
    viewModel { SignIn(get(), get(), get())  }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { HistoryViewModel(get()) }
}