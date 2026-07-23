package de.justjanne.voctotv.common.service

import com.apollographql.apollo.ApolloClient
import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.graphql.LectureByGuidQuery
import de.justjanne.voctotv.voctoweb.model.LectureModel
import de.justjanne.voctotv.voctoweb.model.ResourceModel
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.collections.emptyList

class VoctowebLectureService
    @Inject
    constructor(
        private val api: VoctowebApi,
        private val apolloClient: ApolloClient,
    ) {
        suspend fun getLecture(lectureId: String): LectureModel? {
            val response = apolloClient.query(LectureByGuidQuery(guid = lectureId)).execute()
            val lecture = response.data?.lecture ?: return null
            return LectureModel(
                guid = lecture.guid,
                title = lecture.title,
                subtitle = lecture.subtitle,
                slug = lecture.slug as String,
                link = lecture.link as String?,
                description = lecture.description,
                originalLanguage = lecture.originalLanguage as String,
                persons = lecture.persons.orEmpty(),
                tags = lecture.tags.orEmpty(),
                viewCount = lecture.viewCount?.toLong() ?: 0L,
                promoted = lecture.promoted ?: false,
                date = lecture.date as String?,
                releaseDate = OffsetDateTime.parse(lecture.releaseDate as String),
                updatedAt = OffsetDateTime.parse(lecture.updatedAt as String),
                duration = lecture.duration?.toLong() ?: 0L,
                length = lecture.duration?.toLong() ?: 0L,
                thumbUrl = lecture.images?.thumbUrl as String,
                posterUrl = lecture.images?.posterUrl as String,
                timelineUrl = lecture.timelens?.timelineUrl as String,
                thumbnailsUrl = lecture.timelens?.thumbnailsUrl as String,
                frontendLink = lecture.url as String,
                url = lecture.url as String,
                conferenceTitle = lecture.conference.title,
                conferenceUrl = lecture.conference.acronym,
                related = emptyList(),
                resources =
                    lecture.videos.map {
                        ResourceModel(
                            size = it.size,
                            length = it.duration,
                            mimeType = it.mimeType,
                            language = it.language,
                            filename = it.filename,
                            highQuality = it.highQuality,
                            width = it.width,
                            height = it.height,
                            updatedAt = OffsetDateTime.parse(it.updatedAt as String),
                            recordingUrl = it.url,
                            url = it.url,
                        )
                    },
                subtitles =
                    lecture.subtitles?.map {
                        ResourceModel(
                            size = it.size,
                            length = it.duration,
                            mimeType = it.mimeType,
                            language = it.language,
                            filename = it.filename,
                            highQuality = it.highQuality,
                            width = it.width,
                            height = it.height,
                            updatedAt = OffsetDateTime.parse(it.updatedAt as String),
                            recordingUrl = it.url,
                            url = it.url,
                        )
                    },
            )
        }

        suspend fun listPopular(): List<LectureModel> =
            try {
                val now = OffsetDateTime.now(ZoneOffset.UTC)
                val cutoff = now.minus(1, ChronoUnit.YEARS)

                val current = api.lecture.listPopular(now.year).lectures
                val previous = api.lecture.listPopular(now.year - 1).lectures

                current
                    .plus(previous)
                    .filter { it.releaseDate?.isAfter(cutoff) == true }
                    .sortedByDescending { it.viewCount }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }

        suspend fun listRecent(): List<LectureModel> =
            try {
                api.lecture.listRecent().lectures
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }

        suspend fun listPromoted(): List<LectureModel> =
            try {
                api.lecture.listPromoted().lectures
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
    }
