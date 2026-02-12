package com.example.schoolmanagement.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.MaterialData
import com.example.schoolmanagement.Domain.Repository.MaterialRepository
import com.example.schoolmanagement.Domain.UseCase.GetMaterialsUseCase
import com.example.schoolmanagement.Domain.UseCase.SubmitMaterialUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MaterialTeacherViewModel (
    private val submitMaterialUC: SubmitMaterialUseCase,
    private val getMaterialsUC: GetMaterialsUseCase,
): ViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _subject = MutableStateFlow("")
    val subject = _subject.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _targetClass = MutableStateFlow("")
    val targetClass = _targetClass.asStateFlow()

    private val _type = MutableStateFlow("file") // default: file, link, video
    val type = _type.asStateFlow()

    private val _linkContent = MutableStateFlow("")
    val linkContent = _linkContent.asStateFlow()

    private val _fileName = MutableStateFlow("")
    val fileName = _fileName.asStateFlow()

    private val _fileBytes = MutableStateFlow<ByteArray?>(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _materials = MutableStateFlow<List<MaterialData>>(emptyList())
    val materials: StateFlow<List<MaterialData>> = _materials.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val availableClasses = listOf("11-IPA-2", "12-IPA-1", "10-IPA-3")

    private val exceptionHandler = HandleException()

    init {
        loadMaterials()
    }

    fun onClassChange(value: String) { _targetClass.value = value }
    fun onTitleChange(value: String) { _title.value = value }
    fun onSubjectChange(value: String) { _subject.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    fun onTypeChange(value: String) {
        _type.value = value
        if (value == "link") {
            _fileBytes.value = null
            _fileName.value = ""
        } else {
            _linkContent.value = ""
        }
    }
    fun onLinkChange(value: String) { _linkContent.value = value }

    fun onFileSelected(name: String, bytes: ByteArray) {
        _fileName.value = name
        _fileBytes.value = bytes
    }

    fun submitMaterial() {
        viewModelScope.launch {
            if (_title.value.isBlank() || _subject.value.isBlank()) {
                _errorMessage.value = "Judul dan Mapel wajib diisi!"
                return@launch
            }

            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            val result = submitMaterialUC.invoke(
                title = _title.value,
                description = _description.value,
                subject = _subject.value,
                type = _type.value,
                fileBytes = _fileBytes.value,
                fileName = _fileName.value,
                linkContent = _linkContent.value
            )

                result.onSuccess {
                    _isSuccess.value = true
                    resetForm()
                }.onFailure { e ->
                    val handled = exceptionHandler.handleException(e as Exception)
                    _errorMessage.value = handled.message
                }
            _isLoading.value = false
        }
    }

    fun loadMaterials() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = getMaterialsUC.invoke()

            result.onSuccess {
                _materials.value = it
            }.onFailure { e ->
                val handled = exceptionHandler.handleException(e as Exception)
                _errorMessage.value = handled.message
            }
            _isLoading.value = false
        }
    }

    private fun resetForm() {
        _title.value = ""
        _description.value = ""
        _subject.value = ""
        _linkContent.value = ""
        _fileName.value = ""
        _fileBytes.value = null
    }
}