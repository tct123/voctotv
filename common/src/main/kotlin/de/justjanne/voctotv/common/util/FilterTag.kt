package de.justjanne.voctotv.common.util

import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import org.intellij.lang.annotations.Language

@Language("RegExp")
private val talkId = "[0-9]+".toRegex()

@Language("RegExp")
private val gpnTalkId = "[0-9A-Z]{6}".toRegex()

fun filterTag(
    conference: ConferenceModel,
    tag: String,
): Boolean {
    if (tag.matches(talkId)) return false
    if (tag.matches(gpnTalkId)) return false
    if (tag.startsWith("${conference.acronym}-")) return false
    if (tag == conference.acronym) return false

    return true
}
