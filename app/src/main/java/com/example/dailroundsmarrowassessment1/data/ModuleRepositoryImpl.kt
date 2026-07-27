package com.example.dailroundsmarrowassessment1.data

import com.example.dailroundsmarrowassessment1.data.local.ModuleDao
import com.example.dailroundsmarrowassessment1.data.local.ModuleEntity
import com.example.dailroundsmarrowassessment1.data.local.QuestionDao
import com.example.dailroundsmarrowassessment1.data.local.QuestionEntity
import com.example.dailroundsmarrowassessment1.data.remote.ModuleDto
import com.example.dailroundsmarrowassessment1.data.remote.QuestionDto
import com.example.dailroundsmarrowassessment1.data.remote.QuizApi
import com.example.dailroundsmarrowassessment1.domain.Module
import com.example.dailroundsmarrowassessment1.domain.ModuleRepository
import com.example.dailroundsmarrowassessment1.domain.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModuleRepositoryImpl(
    private val api: QuizApi,
    private val moduleDao: ModuleDao,
    private val questionDao: QuestionDao,
) : ModuleRepository {

    override suspend fun getModules(): Result<List<Module>> = withContext(Dispatchers.IO) {
        runCatching {
            val cached = moduleDao.getAll()
            if (cached.isNotEmpty()) {
                cached.map { it.toDomain() }
            } else {
                val remote = api.fetchModules(QuizApi.MODULES_URL)
                require(remote.isNotEmpty()) { "Module list is empty" }
                moduleDao.upsertAll(remote.mapIndexed { position, dto -> dto.toEntity(position) })
                remote.map { it.toDomain() }
            }
        }
    }

    override suspend fun getQuestions(questionsUrl: String): Result<List<Question>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val cached = questionDao.getByUrl(questionsUrl)
                if (cached.isNotEmpty()) {
                    cached.map { it.toDomain() }
                } else {
                    val remote = api.fetchModuleQuestions(questionsUrl)
                    require(remote.isNotEmpty()) { "Question list is empty" }
                    questionDao.upsertAll(
                        remote.mapIndexed { position, dto -> dto.toEntity(questionsUrl, position) },
                    )
                    remote.map { it.toDomain() }
                }
            }
        }
}

private fun ModuleDto.toEntity(position: Int) = ModuleEntity(
    id = id,
    position = position,
    title = title,
    description = description,
    questionsUrl = questionsUrl,
)

private fun ModuleEntity.toDomain() = Module(
    id = id,
    title = title,
    description = description,
    questionsUrl = questionsUrl,
)

private fun QuestionDto.toEntity(questionsUrl: String, position: Int) = QuestionEntity(
    questionsUrl = questionsUrl,
    position = position,
    questionId = id,
    text = question,
    options = options,
    correctIndex = correctOptionIndex,
)

private fun QuestionEntity.toDomain() = Question(
    id = questionId,
    text = text,
    options = options,
    correctIndex = correctIndex,
)
