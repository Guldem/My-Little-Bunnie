package nl.guldem.mylittlebunnie.util.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

//todo invullen van correcte datetime
const val BIRTHDATE: String = "01-09-2019 20:41"

fun DateTime.formatDateString(): String{
    return DateTimeFormat.forPattern("dd-MM-yyyy").withZone(DateTimeZone.forID("Europe/Amsterdam")).print(this)
}

val AMSTERDAM_TIMEZONE: DateTimeZone = DateTimeZone.forID("Europe/Amsterdam")

fun nowInAmsterdam() = DateTime.now(AMSTERDAM_TIMEZONE)

fun birthdayInAmsterdam() = DateTime.parse(BIRTHDATE, formatter)

private val formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm").withZone(AMSTERDAM_TIMEZONE)
