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

package ai.tock.bot.admin.story.dump

import ai.tock.bot.admin.answer.ScriptAnswerVersionedConfiguration
import ai.tock.bot.definition.IntentWithoutNamespace

interface StoryDefinitionConfigurationDumpController {
    val targetNamespace: String
    val botId: String
    fun keepFeature(feature: StoryDefinitionConfigurationFeatureDump): Boolean
    fun buildScript(script: ScriptAnswerVersionedConfigurationDump, compile: Boolean): ScriptAnswerVersionedConfiguration
    fun checkIntent(intent: IntentWithoutNamespace?): IntentWithoutNamespace?
}
