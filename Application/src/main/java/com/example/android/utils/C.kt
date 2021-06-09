package com.example.android.utils
/**
 * Created by
 */

object C {
    const val IS_APP_DEBUGGABLE = true

    @JvmField
    var SERVER_URL: String = "http://"
    const val APP_NAME = "Heart Rate Monitor"
    const val PACKAGE_NAME = "com.softgit.glucose_tracker"
    const val COLOR_THEME_HEX = "0x044fa2"
    const val PLAY_STORE = "https://play.google.com/store/apps/details?id=$PACKAGE_NAME"
    const val isHardwareCodecEnabled = false

    @JvmField
    val GENDERS = arrayOf("Male", "Female", "Hidden")
    const val NULL_STR = "null"

    /** Settings */
    var datePattern = "dd-MMM-yy" //TODO Came from settings
    var timePattern = "hh:mm a" //TODO Came From settings
    val is24HourFormat = false
    val unitType = arrayOf("mmole/dL", "mg/dL") // TODO from user settings
    val unitSelected = unitType[1] // TODO from user settings

    object KEYS {
        const val DATA = "data"
        const val URL = "_url"
        const val STATUS = "status"
        const val MSG = "message"
        const val SUCCESS = "success"
        const val REGISTERED_CONTACTS = "registeredContatcs"

        // Esp. file related keys
        const val NAME = "name"
        const val MIME = "mimeType"
        const val KEY = "key"
        const val SIZE = "size"
        const val EVENT = "event"
        const val ID = "id"

        const val FCM = "FCM"
        const val PUSHY = "PUSHY"
        const val TITLE = "title"

        const val DIALOG_DELAY = 500
        const val PING_TIMER_INTERVAL = 40 * 1000 //15*60*1000
        const val CALL_CLOSE_INTERVAL = 60//seconds

        const val REGISTERED = "registered"

        const val USER = "_user_item"
        const val OII_USER = "_oii_user_item"
    }

    object PROFILE {
        const val PROPIC = "proPicLarge"
        const val PROPIC_SMALL = "proPicSmall"
        const val USERNAME = "username"
        const val NAME = "name"
        const val DOB = "dateOfBirth"
        const val ADDRESS = "address"
        const val NID_NO = "nid"
        const val NID_PIC = "nidPhoto"
        const val PASSPORT_PIC = "photo"
        const val SIP_DATA = "sipData"
        const val BALANCE = "balance"
        const val EXPIRY_DATE = "expiry"
    }

    object HTTP {
        const val OK = 200
        const val OK_MAX = 299
        const val INVALID = 400
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val TIMEOUT = 408
        const val INVALID_MAX = 499
        const val ERROR = 500
        const val ERROR_MAX = 599
        const val GATEWAY_TIMEOUT = 504
    }

    object NOTIFY {
        const val MESSAGE = 2
        const val INFO = 1
        const val WARNING = 0
        const val ERROR = -1
    }

    object MeasureWhenType {
        const val BEFORE_BREAKFAST = "Before breakfast"
        const val AFTER_BREAKFAST = "After breakfast"
        const val BEFORE_LUNCH = "Before lunch"
        const val AFTER_LUNCH = "After lunch"
        const val BEFORE_DINNER = "Before dinner"
        const val AFTER_DINNER = "After dinner"
        const val BEFORE_BEDTIME = "Bedtime"
        const val FASTING_GLUCOSE = "Fasting glucose"
        const val RANDOM_CHECK = "Random check"
    }

    val WHEN_MEASURED_ALL_TYPE = arrayOf(
        MeasureWhenType.BEFORE_BREAKFAST,
        MeasureWhenType.AFTER_BREAKFAST, MeasureWhenType.BEFORE_LUNCH,
        MeasureWhenType.AFTER_LUNCH, MeasureWhenType.BEFORE_DINNER,
        MeasureWhenType.AFTER_DINNER, MeasureWhenType.BEFORE_BEDTIME,
        MeasureWhenType.FASTING_GLUCOSE, MeasureWhenType.RANDOM_CHECK
    )

    object UNIT {
        const val MMOLE_PER_DL = "mmole/dL"
        const val MGM_PER_DL = "mg/dL"
    }

    const val SELECTED_UNIT = UNIT.MMOLE_PER_DL // Should came from settings

    object TimeTypes {
        const val DAY = "Day"
        const val WEEK = "Week"
        const val MONTH = "Month"
        const val YEAR = "Year"
    }

    val AllTimeTypes = arrayOf(
        TimeTypes.DAY, TimeTypes.WEEK,
        TimeTypes.MONTH,
        TimeTypes.YEAR
    )

    object REST {
        const val DATA = "data"
        const val THREAD_TYPE = "type"
        const val THREAD_USERS = "users"
        const val LOGGED_IN = "loggedIn"
    }
}