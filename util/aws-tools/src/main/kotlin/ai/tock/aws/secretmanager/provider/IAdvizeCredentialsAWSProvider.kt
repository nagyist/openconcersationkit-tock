/*
 * Copyright (C) 2017/2022 e-voyageurs technologies
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

package ai.tock.aws.secretmanager.provider


import ai.tock.aws.EnvConfig
import ai.tock.aws.secretmanager.dao.SecretDAO
import ai.tock.shared.injector
import ai.tock.shared.provide
import ai.tock.shared.security.credentials.Credentials
import ai.tock.shared.security.credentials.CredentialsProvider
import ai.tock.shared.security.credentials.CredentialsProviderType
import kotlinx.serialization.json.Json

/**
 * IAdvize credentials AWS provider
 */
class IAdvizeCredentialsAWSProvider: CredentialsProvider {

    private val secretDAO: SecretDAO get() = injector.provide()

    override val type: CredentialsProviderType
        get() = CredentialsProviderType.AWS_SECRET_MANAGER

    /**
     * Return iAdvize credentials, from an AWS Secret Manager vault.
     */
    override fun getCredentials(): Credentials {
        return Json.decodeFromString(
            secretDAO.getSecret(EnvConfig.awsIAdvizeCredentialsSecretId)
        )
    }
}