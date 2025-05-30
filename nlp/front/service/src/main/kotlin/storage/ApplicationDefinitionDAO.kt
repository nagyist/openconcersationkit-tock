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

package ai.tock.nlp.front.service.storage

import ai.tock.nlp.front.shared.config.ApplicationDefinition
import org.litote.kmongo.Id

/**
 *
 */
interface ApplicationDefinitionDAO {

    /**
     * Listen changes on entity type definitions.
     */
    fun listenApplicationDefinitionChanges(listener: () -> Unit)

    fun save(application: ApplicationDefinition): ApplicationDefinition

    fun deleteApplicationById(id: Id<ApplicationDefinition>)

    fun getApplicationByNamespaceAndName(namespace: String, name: String): ApplicationDefinition?

    fun getApplicationById(id: Id<ApplicationDefinition>): ApplicationDefinition?

    fun getApplications(): List<ApplicationDefinition>
}
