/*
 * Copyright (C) 2017/2025 SNCF Connect & Tech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.tock.bot.admin.model.genai

import ai.tock.bot.admin.bot.rag.BotRAGConfiguration
import ai.tock.bot.admin.service.VectorStoreService
import ai.tock.genai.orchestratorclient.requests.PromptTemplate
import ai.tock.genai.orchestratorcore.mappers.EMSettingMapper
import ai.tock.genai.orchestratorcore.mappers.LLMSettingMapper
import ai.tock.genai.orchestratorcore.models.Constants
import ai.tock.genai.orchestratorcore.models.em.EMSettingDTO
import ai.tock.genai.orchestratorcore.models.em.toDTO
import ai.tock.genai.orchestratorcore.models.llm.LLMSettingDTO
import ai.tock.genai.orchestratorcore.models.llm.toDTO
import ai.tock.genai.orchestratorcore.utils.VectorStoreUtils
import org.litote.kmongo.newId
import org.litote.kmongo.toId

data class BotRAGConfigurationDTO(
    val id: String? = null,
    val namespace: String,
    val botId: String,
    val enabled: Boolean = false,
    val questionCondensingLlmSetting: LLMSettingDTO? = null,
    val questionCondensingPrompt: PromptTemplate? = null,
    val questionAnsweringLlmSetting: LLMSettingDTO,
    val questionAnsweringPrompt: PromptTemplate,
    val emSetting: EMSettingDTO,
    val indexSessionId: String? = null,
    val indexName: String? = null,
    val noAnswerSentence: String,
    val noAnswerStoryId: String? = null,
    val documentsRequired: Boolean = true,
    val debugEnabled: Boolean,
    val maxDocumentsRetrieved: Int,
    val maxMessagesFromHistory: Int,
) {
    constructor(configuration: BotRAGConfiguration) : this(
        id = configuration._id.toString(),
        namespace = configuration.namespace,
        botId = configuration.botId,
        enabled = configuration.enabled,
        questionCondensingLlmSetting = configuration.questionCondensingLlmSetting?.toDTO(),
        questionCondensingPrompt = configuration.questionCondensingPrompt,
        questionAnsweringLlmSetting = configuration.getQuestionAnsweringLLMSetting().toDTO(),
        questionAnsweringPrompt = configuration.questionAnsweringPrompt
            ?: configuration.initQuestionAnsweringPrompt(),
        emSetting = configuration.emSetting.toDTO(),
        indexSessionId = configuration.indexSessionId,
        indexName = configuration.generateIndexName(),
        noAnswerSentence = configuration.noAnswerSentence,
        noAnswerStoryId = configuration.noAnswerStoryId,
        documentsRequired = configuration.documentsRequired,
        debugEnabled = configuration.debugEnabled,
        maxDocumentsRetrieved = configuration.maxDocumentsRetrieved,
        maxMessagesFromHistory = configuration.maxMessagesFromHistory,
    )

    fun toBotRAGConfiguration(): BotRAGConfiguration =
        BotRAGConfiguration(
            _id = id?.toId() ?: newId(),
            namespace = namespace,
            botId = botId,
            enabled = enabled,
            questionCondensingLlmSetting = LLMSettingMapper.toEntity(
                namespace = namespace,
                botId = botId,
                feature = Constants.GEN_AI_RAG_QUESTION_CONDENSING,
                dto = questionCondensingLlmSetting!!
            ),
            questionCondensingPrompt = questionCondensingPrompt,
            questionAnsweringLlmSetting = LLMSettingMapper.toEntity(
                namespace = namespace,
                botId = botId,
                feature = Constants.GEN_AI_RAG_QUESTION_ANSWERING,
                dto = questionAnsweringLlmSetting
            ),
            questionAnsweringPrompt = questionAnsweringPrompt,
            emSetting = EMSettingMapper.toEntity(
                namespace = namespace,
                botId = botId,
                feature = Constants.GEN_AI_RAG_EMBEDDING_QUESTION,
                dto = emSetting
            ),
            indexSessionId = indexSessionId,
            noAnswerSentence = noAnswerSentence,
            noAnswerStoryId = noAnswerStoryId,
            documentsRequired = documentsRequired,
            debugEnabled = debugEnabled,
            maxDocumentsRetrieved = maxDocumentsRetrieved,
            maxMessagesFromHistory = maxMessagesFromHistory,
        )
}

private fun BotRAGConfiguration.generateIndexName(): String? {
    return indexSessionId?.takeIf { it.isNotBlank() }?.let {
        VectorStoreUtils.getVectorStoreElements(
            namespace,
            botId,
            it,
            maxDocumentsRetrieved,
            VectorStoreService.getVectorStoreConfiguration(namespace, botId, enabled = true)
                ?.setting
        ).second
    }
}



