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

package ai.tock.nlp.admin.model

import ai.tock.nlp.front.shared.config.ApplicationDefinition
import ai.tock.nlp.front.shared.config.SentencesQuery
import org.litote.kmongo.Id

/** Query sentences by text. */
data class SentencesTextQuery(
    val texts: List<String> = emptyList()
) : ApplicationScopedQuery() {

    fun toSentencesQueries(applicationId: Id<ApplicationDefinition>): List<SentencesQuery> {
        return texts.map {
            SentencesQuery(
                applicationId,
                language,
                search = it,
                onlyExactMatch = true
            )
        }
    }
}
