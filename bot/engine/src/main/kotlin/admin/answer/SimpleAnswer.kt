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

package ai.tock.bot.admin.answer

import ai.tock.bot.connector.media.MediaMessageDescriptor
import ai.tock.bot.engine.action.Footnote
import ai.tock.translator.I18nLabelValue

/**
 * Answer that contains only i18n label with an optional [delay] and [MediaMessageDescriptor].
 */
data class SimpleAnswer(
    val key: I18nLabelValue,
    val delay: Long = 0,
    val mediaMessage: MediaMessageDescriptor? = null,
    val footnotes: List<Footnote>? = null
)
