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

package ai.tock.bot.connector.web

import ai.tock.bot.connector.SerializableConnectorMessage
import ai.tock.bot.jackson.MixinSerializableConnectorMessage
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.module.SimpleModule
import org.litote.jackson.JacksonModuleServiceLoader

private object WebOrchestrationJacksonConfiguration {

    val module: SimpleModule
        get() {
            val module = SimpleModule()
            with(module) {
                setMixInAnnotation(SerializableConnectorMessage::class.java, MixinSerializableConnectorMessage::class.java)
                registerSubtypes(NamedType(WebMessage::class.java, "WebMessage"))
            }
            return module
        }
}

internal class WebOrchestrationJacksonModuleServiceLoader : JacksonModuleServiceLoader {
    override fun module(): Module = WebOrchestrationJacksonConfiguration.module
}
