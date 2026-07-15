package de.justjanne.voctotv.common.service

import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import javax.inject.Inject
import kotlin.collections.map

class VoctowebConferenceService
    @Inject
    constructor(
        private val api: VoctowebApi,
    ) {
        suspend fun getConference(conferenceId: String): ConferenceModel? =
            try {
                api.conference
                    .get(conferenceId)
                    ?.let(::processConference)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

        suspend fun listConferences(): List<ConferenceModel> =
            try {
                api.conference
                    .list()
                    .conferences
                    .map(::processConference)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }

        private fun processConference(conference: ConferenceModel): ConferenceModel =
            conference.copy(bannerUrl = bannerUrl(conference.acronym))

        companion object {
            const val Size = 384
            const val BaseUrl = "https://voctotv-assets.s3.kuschku.de"

            fun bannerUrl(id: String): String = "$BaseUrl/$id/card-$Size.png"
        }
    }
