package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.ApiService
import com.example.schoolmanagement.Domain.Model.AttendanceStats
import com.example.schoolmanagement.Domain.Model.ClassAttendanceStats
import com.example.schoolmanagement.Domain.Repository.AttendanceRepository

import kotlinx.coroutines.flow.first

class GetTeacherDasboardUseCase (
    private val repository: AttendanceRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<ClassAttendanceStats> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""
            val myClass = prefsManager.getClass.first() ?: ""

            if (token.isEmpty() || myClass.isEmpty()) {
                return Result.failure(Exception("Session tidak valid atau kelas kosong"))
            }

            val result = repository.getClassAttendance(token, myClass)

            if (result.isSuccess) {
                val students = result.getOrThrow()

                val hadir = students.count { it.status == "Present" }.toString()
                val telat = students.count { it.status == "Late" }.toString()
                val absen = students.count {
                    it.status == "Not Present Yet" || it.status == "Absent"
                }.toString()

                Result.success(
                    ClassAttendanceStats(
                        hadir = hadir,
                        telat = telat,
                        absen = absen,
                        studentList = students
                    )
                )
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Gagal mendapatkan data kelas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}