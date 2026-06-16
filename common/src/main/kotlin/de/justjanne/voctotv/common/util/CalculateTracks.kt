package de.justjanne.voctotv.common.util

import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import de.justjanne.voctotv.voctoweb.model.LectureModel

const val FilterKeyOther = "Other"

fun calculateTracks(
    conference: ConferenceModel,
    lecture: LectureModel,
): List<String> =
    lecture.tags
        .filter { filterTag(conference, it) }
        .ifEmpty { listOf(FilterKeyOther) }
