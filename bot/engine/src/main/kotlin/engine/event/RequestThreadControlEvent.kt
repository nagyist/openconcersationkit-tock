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

package ai.tock.bot.engine.event

import ai.tock.bot.engine.user.PlayerId

/**
 * A request thread control event.
 */
class RequestThreadControlEvent(
    userId: PlayerId,
    recipientId: PlayerId,
    applicationId: String,
    val requestOwnerAppId: String,
    val metadata: String? = null
) : OneToOneEvent(userId, recipientId, applicationId) {
    override fun toString(): String {
        return "[RequestThreadControlEvent] to $requestOwnerAppId with metadata $metadata"
    }
}
