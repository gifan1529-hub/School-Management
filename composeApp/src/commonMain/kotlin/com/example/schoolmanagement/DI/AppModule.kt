package com.example.schoolmanagement.DI

import com.example.schoolmanagement.Data.Impl.AnnouncmentRepositoryImpl
import com.example.schoolmanagement.Data.Impl.AttendanceReportImpl
import com.example.schoolmanagement.Data.Impl.AttendanceRepositoryImpl
import com.example.schoolmanagement.Data.Impl.GradeRepositoryImpl
import com.example.schoolmanagement.Data.Impl.HomeWorkRepositoryImpl
import com.example.schoolmanagement.Data.Impl.PermitRepositoryImpl
import com.example.schoolmanagement.Data.Impl.ScheduleRepositoryImpl
import com.example.schoolmanagement.Data.Impl.StatsRepositoryImpl
import com.example.schoolmanagement.Data.Impl.UpdateUserRepositoryImpl
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiClient
import com.example.schoolmanagement.Domain.UseCase.GetHomeWorkDetailUseCase
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Repository.AnnouncmentRepository
import com.example.schoolmanagement.Domain.Repository.AttendanceReportRepository
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository
import com.example.schoolmanagement.Domain.Repository.GradeRepository
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import com.example.schoolmanagement.Domain.Repository.PermitRepository
import com.example.schoolmanagement.Domain.Repository.ScheduleRepository
import com.example.schoolmanagement.Domain.Repository.StatsRepository
import com.example.schoolmanagement.Domain.Repository.UpdateUserRepository
import com.example.schoolmanagement.Domain.UseCase.AddUserUseCase
import com.example.schoolmanagement.Domain.UseCase.DeleteHomeWork
import com.example.schoolmanagement.Domain.UseCase.DeleteUserUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAdminStatsUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAllUserUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAnnouncmentUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceHistoryUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceReportUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceStatusUseCase
import com.example.schoolmanagement.Domain.UseCase.GetAttendanceTrendUseCase
import com.example.schoolmanagement.Domain.UseCase.GetGradeSummaryUseCase
import com.example.schoolmanagement.Domain.UseCase.GetHomeWorkUseCase
import com.example.schoolmanagement.Domain.UseCase.GetPermitHistoryUseCase
import com.example.schoolmanagement.Domain.UseCase.GetScheduleUseCase
import com.example.schoolmanagement.Domain.UseCase.GetTeacherDasboardUseCase
import com.example.schoolmanagement.Domain.UseCase.GetTeacherSchedulesUseCase
import com.example.schoolmanagement.Domain.UseCase.GiveGradeUseCase
import com.example.schoolmanagement.Domain.UseCase.LoginUC
import com.example.schoolmanagement.Domain.UseCase.SubmitAttendanceUC
import com.example.schoolmanagement.Domain.UseCase.getDetailUserUC
import com.example.schoolmanagement.Domain.UseCase.LogoutUseCase
import com.example.schoolmanagement.Domain.UseCase.PostAnnouncmentUseCase
import com.example.schoolmanagement.Domain.UseCase.PostHomeWorkUseCase
import com.example.schoolmanagement.Domain.UseCase.SubmitHomeworkUseCase
import com.example.schoolmanagement.Domain.UseCase.SubmitPermitUseCase
import com.example.schoolmanagement.Domain.UseCase.UpdatePermitStatusUseCase
import com.example.schoolmanagement.Domain.UseCase.UpdateUserUseCase
import com.example.schoolmanagement.ViewModel.AlertViewModel
import com.example.schoolmanagement.ViewModel.AttendanceReportViewModel
import com.example.schoolmanagement.ViewModel.AuthViewModel
import com.example.schoolmanagement.ViewModel.GradeViewModel
import com.example.schoolmanagement.ViewModel.HistoryViewModel
import com.example.schoolmanagement.ViewModel.HomeAdminViewModel
import com.example.schoolmanagement.ViewModel.HomeTeacherViewModel
import com.example.schoolmanagement.ViewModel.HomeViewModel
import com.example.schoolmanagement.ViewModel.HomeWorkViewModel
import com.example.schoolmanagement.ViewModel.MarkAttendanceViewModel
import com.example.schoolmanagement.ViewModel.PermitViewModel
import com.example.schoolmanagement.ViewModel.ProfileViewModel
import com.example.schoolmanagement.ViewModel.ScheduleGuruViewModel
import com.example.schoolmanagement.ViewModel.ScheduleViewModel
import com.example.schoolmanagement.ViewModel.SignIn
import com.example.schoolmanagement.ViewModel.UpdateUserViewModel
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
    single <PermitRepository>{ PermitRepositoryImpl(get()) }
    single <AnnouncmentRepository>{ AnnouncmentRepositoryImpl(get()) }
    single <HomeWorkRepository>{ HomeWorkRepositoryImpl(get()) }
    single <StatsRepository>{ StatsRepositoryImpl(get()) }
    single <AttendanceReportRepository>{ AttendanceReportImpl(get()) }
    single <UpdateUserRepository>{ UpdateUserRepositoryImpl(get()) }
    single <GradeRepository>{ GradeRepositoryImpl(get()) }
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
    factoryOf(::GetTeacherSchedulesUseCase)
    factoryOf(::SubmitPermitUseCase)
    factoryOf(::GetPermitHistoryUseCase)
    factoryOf(::UpdatePermitStatusUseCase)
    factoryOf(::GetAnnouncmentUseCase)
    factoryOf(::PostAnnouncmentUseCase)
    factoryOf(::GetHomeWorkUseCase)
    factoryOf(::PostHomeWorkUseCase)
    factoryOf(::DeleteHomeWork)
    factoryOf(::SubmitHomeworkUseCase)
    factoryOf(::GetHomeWorkDetailUseCase)
    factoryOf(::GetAdminStatsUseCase)
    factoryOf(::GetAttendanceTrendUseCase)
    factoryOf(::GetAttendanceReportUseCase)
    factoryOf(::GiveGradeUseCase)
    factoryOf(::GetAllUserUseCase)
    factoryOf(::UpdateUserUseCase)
    factoryOf(::DeleteUserUseCase)
    factoryOf(::AddUserUseCase)
    factoryOf(::GetGradeSummaryUseCase)


    viewModel { MarkAttendanceViewModel(get()) }
    viewModel { HomeTeacherViewModel(get(), get(), get(), get(), get()) }
    viewModel { SignIn(get(), get(), get())  }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { ScheduleGuruViewModel(get()) }
    viewModel { PermitViewModel(get(), get(), get(), get(), get()) }
    viewModel { AlertViewModel(get(), get())}
    viewModel { HomeWorkViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { HomeAdminViewModel(get(), get()) }
    viewModel { AttendanceReportViewModel(get()) }
    viewModel { UpdateUserViewModel(get(), get(), get(), get()) }
    viewModel { GradeViewModel(get()) }
}