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
package ai.tock.stt

import ai.tock.shared.defaultLocale
import java.util.Locale

/**
 * Parse voice audio messages.
 */
interface STT {

    /**
     * Parse audio data for the specified language. Return null if the transcript fails.
     **/
    fun parse(bytes: ByteArray, language: Locale = defaultLocale): String?
}
