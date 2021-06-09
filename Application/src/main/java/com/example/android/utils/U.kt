package com.example.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import android.text.*
import android.text.format.DateUtils
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.NetworkInterface
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
/**
 */

object U {

    private val TAG = U::class.java.simpleName

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getFirstSimCardNumber(tContext: Context): String = try {
        val tm = tContext
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm.line1Number
    } catch (e: Exception) {
        Lg.e(
            TAG,
            "getFirstSimCardNumber : Could not retrieve the SIM card number"
        )
        ""
    }

    // region Validation Section
    @JvmStatic
    fun isEmailValid(email: String): Boolean {
        return email.indexOf('@') > 0 && isValidEmailAddress(email)
    }

    @JvmStatic
    private fun isValidEmailAddress(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    @JvmStatic
    fun isPhoneNumberValid(phoneNo: String): Boolean {
        return (phoneNo.startsWith("880") && phoneNo.length > 11 || !phoneNo.startsWith("880") && phoneNo.length > 5) && phoneNo.matches("\\d+".toRegex())
    }

    @JvmOverloads
    @JvmStatic
    fun sendEmail(context: Context, mailTos: Array<String>, subject: String, body: String,
                  type: String = "plain/text", chooserTitle: String = "Send eMail") {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = type
        intent.putExtra(Intent.EXTRA_EMAIL, mailTos)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(intent, chooserTitle))
    }
    @JvmStatic
    fun parseSecondsToTimeStr(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return "$minutes:$secs"
    }

    @JvmStatic
    fun getFormattedSize(sizeInBytes: Long): String = when {
        sizeInBytes < 1024 ->// Less than 1KB
            sizeInBytes.toString() + "B"
        sizeInBytes < 1024 * 1024 -> // less than 1MB
            String.format(Locale.US, "%.2fKB", sizeInBytes.toFloat() / 1024.0)
        sizeInBytes < 1024 * 1024 * 1024 -> // less than 1GB
            String.format(Locale.US, "%.2fMB", sizeInBytes.toFloat() / (1024.0 * 1024.0))
        else ->
            String.format(Locale.US, "%.3fGB", sizeInBytes.toFloat() / (1024.0 * 1024.0 * 1024.0))
    }

    @JvmStatic
    fun getColor(context: Context, colorResId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            context.resources.getColor(colorResId, null)
        else
            context.resources.getColor(colorResId)
    }

    @JvmStatic
    fun getDrawerWidth(context: Context): Int {
        return (getScreenWidth(context) * 0.75).toInt()
    }

    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    @JvmStatic
    fun get3LetterFormattedCount(count: Int): String = when {
        count > 999999999 -> (count / 1000000000).toString() + "b"
        count > 999999 -> (count / 1000000).toString() + "m"
        count > 999 -> (count / 1000).toString() + "k"
        else -> count.toString() + ""
    }

    @JvmStatic
    fun get2LetterFormattedCount(count: Int): String = when {
        count > 9 -> "9+"
        count < 0 -> "0"
        else -> count.toString() + ""
    }

    /**
     * Default return is 0.0
     */
    @JvmStatic
    fun getDoubleJ(jo: JSONObject, key: String): Double {
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return jo.getDouble(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return 0.0
    }

    /**
     * @param defRetVal The default return value in case of failed parsing
     */
    @JvmStatic
    fun getDoubleJ(jo: JSONObject, key: String, defRetVal: Double): Double {
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return jo.getDouble(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return defRetVal
    }

    /**
     * Default return is 0
     */
    @JvmStatic
    fun getLongJ(jo: JSONObject?, key: String): Long {
        if (jo == null)
            return 0L
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return java.lang.Long.parseLong(jo.get(key).toString() + "")
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return 0L
    }

    /**
     * Default return is 0
     */
    @JvmStatic
    fun getIntJ(jo: JSONObject?, key: String): Int {
        if (jo == null)
            return 0
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return Integer.parseInt(jo.get(key).toString() + "")
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return 0
    }

    /**
     * @param defaultRetInt The default return value in case of failed parsing
     */
    @JvmStatic
    fun getIntJ(jo: JSONObject?, key: String, defaultRetInt: Int): Int {
        if (jo == null)
            return defaultRetInt
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return Integer.parseInt(jo.get(key).toString() + "")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return defaultRetInt
    }

    /**
     * @param defMsg Default return if there is null/empty string inside JSONObject under the key
     */
    @JvmStatic
    fun getStringJ(jo: JSONObject?, key: String, defMsg: String): String {
        if (jo == null)
            return defMsg
        val s = getStringJ(jo, key)
        return if (s.isEmpty())
            defMsg
        else
            s
    }

    /**
     * Default return empty string("")
     */
    @JvmStatic
    fun getStringJ(jo: JSONObject?, key: String): String {
        if (jo == null) return ""
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return jo.getString(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Default return is an empty JSONArray [ new JSONArray() ]
     */
    @JvmStatic
    fun getJSONArrayJ(jo: JSONObject, key: String): JSONArray {
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return jo.getJSONArray(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return JSONArray()
    }

    /**
     * Default return is an empty JSONObject [ new JSONObject() ]
     */
    @JvmStatic
    fun getJSONObjectJ(jo: JSONObject?, key: String): JSONObject {
        try {
            if (jo != null && jo.has(key))
                if (jo.get(key) != null)
                    return jo.getJSONObject(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return JSONObject()
    }

    /**
     * Default return is false
     */
    @JvmStatic
    fun getBooleanJ(jo: JSONObject?, key: String): Boolean = getBooleanJ(jo, key, false)


    /**
     * @param defRet Default return in case of no value under the specified key.
     */
    @JvmStatic
    fun getBooleanJ(jo: JSONObject?, key: String, defRet: Boolean): Boolean {
        if (jo == null) return defRet
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return jo.getBoolean(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return defRet
    }

    /**
     * Default return is an empty object [ new Object() ]
     */
    @JvmStatic
    fun getJ(jo: JSONObject, key: String): Any {
        try {
            if (jo.has(key))
                if (jo.get(key) != null)
                    return jo.get(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return Any()
    }

    @JvmStatic
    fun parseStringList(ja: JSONArray): ArrayList<String> {
        val strList = ArrayList<String>()

        val sz = ja.length()
        for (i in 0 until sz) {
            try {
                strList.add(ja.getString(i))
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }

        return strList
    }

    @JvmStatic
    fun getTimeStr(timeStamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH)
        return sdf.format(timeStamp)
    }

    @JvmStatic
    fun randomString(length: Int): String {
        val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < length) { // length of the random string.
            val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
            salt.append(SALTCHARS[index])
        }
        return salt.toString()
    }

    @JvmStatic
    fun randomNumber(length: Int): String {
        val SALTCHARS = "1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < length) { // length of the random string.
            val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
            salt.append(SALTCHARS[index])
        }
        return salt.toString()
    }

    //region Time-related calculations
    @JvmStatic
    fun getTimeElapsedAfterUpdate(updatedAt: String): CharSequence {
        var result: CharSequence = ""
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            try {
                result = getTimeElapsedAfter(sdf.parse(updatedAt).time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            Lg.e("getTimeElapsedAfterUpdate", "Date format can't be parsed: $updatedAt", e)
        }
        return result
    }

    @JvmStatic
    fun getTimeElapsedAfter(millis: Long): CharSequence {
        try {
            return DateUtils.getRelativeTimeSpanString(millis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
        } catch (e: Exception) {
            Lg.e(
                "getTimeElapsedAfter",
                "$millis can't be parsed to readable relative time-format",
                e
            )
        }
        return millis.toString()
    }

    @JvmStatic
    fun getTimeElapsedAfterInMin(millis: Long): CharSequence {

        try {
            val timeDiff = System.currentTimeMillis() - millis
            return timeDifference(timeDiff)
//            return DateUtils.getRelativeTimeSpanString(millis,
//                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
        } catch (e: Exception) {
            Lg.e(
                "getTimeElapsedAfter",
                "$millis can't be parsed to readable relative time-format",
                e
            )
        }
        return millis.toString()
    }

    fun timeDifference(diff: Long): String {

        var different = diff
        var time = "1s" // Default 1s
        var alreadyGet = false

        val secondsInMilli = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val weekInMils = daysInMilli * 7

        val elapsedWeek = different / weekInMils
        different %= weekInMils

        if (elapsedWeek > 0 && !alreadyGet) {
            time = elapsedWeek.toString() + "w"
            alreadyGet = true
        }

        val elapsedDays = different / daysInMilli
        different %= daysInMilli

        if (elapsedDays > 0 && !alreadyGet) {
            time = elapsedDays.toString() + "d"
            alreadyGet = true
        }

        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli

        if (elapsedHours > 0 && !alreadyGet) {
            time = elapsedHours.toString() + "h"
            alreadyGet = true
        }

        val elapsedMinutes = different / minutesInMilli

        different %= minutesInMilli
        if (elapsedMinutes > 0 && !alreadyGet) {
            time = elapsedMinutes.toString() + "m"
            alreadyGet = true
        }

        val elapsedSeconds = different / secondsInMilli

        if (elapsedSeconds > 0 && !alreadyGet) {
            time = elapsedSeconds.toString() + "s"
            alreadyGet = true
        }

        Lg.d(
            TAG, elapsedDays.toString() + " day " +
                    elapsedHours + " hour " + elapsedMinutes + " min " + elapsedSeconds + " sec"
        )

        return time
    }

    @JvmStatic
    fun getTimeStamp(formattedTimeStr: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        var timeStamp = java.lang.Long.MIN_VALUE
        try {
            val mDate = sdf.parse(formattedTimeStr)
            timeStamp = mDate.time
        } catch (e: ParseException) {
            Lg.e(
                "U.getTimeStamp()",
                "Failed to format the string: $formattedTimeStr", e
            )
        }
        return timeStamp
    }

    @JvmStatic
    fun parseTimeInMillisToTimeStr(tor: String): String {
        val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val now = Date(parseLong(tor, System.currentTimeMillis()))
        return sdfDate.format(now)
    }

    @JvmStatic
    fun getSmallFormattedTime(timeInSeconds: Int): String {
        var timeInSecs = timeInSeconds
        var ret = ""

        if (timeInSecs < 0)
            timeInSecs *= -1

        if (timeInSecs / 60 > 0) {
            val x = timeInSecs / 60
            ret += if (x < 10)
                "0$x"
            else
                "" + x
            timeInSecs %= 60
        } else
            ret += "00"
        if (timeInSecs >= 0) {
            ret += if (timeInSecs < 10)
                "  :  0$timeInSecs"
            else
                "  :  $timeInSecs"
        }
        return ret
    }

    @JvmStatic
    val currentTimeStamp: String
        get() {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val now = Date()
            return sdfDate.format(now)
        }

    @JvmStatic
    val timeElapsedCurrent: String
        get() = getTimeElapsedAfterUpdate(currentTimeStamp) as String
    //endregion

    @JvmStatic
    fun getDrawable(context: Context, resId: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            context.resources.getDrawable(resId, null)
        else
            context.resources.getDrawable(resId)
    }

    @JvmStatic
    fun logHashKey(activity: Activity) {
        try {
            val info = activity.packageManager.getPackageInfo(
                    "org.durbinbd.DurbinStudent", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val k = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Lg.d("KeyHash:", k)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Lg.e(TAG, "showHashKey()", e)
        } catch (e: NoSuchAlgorithmException) {
            Lg.e(TAG, "showHashKey()", e)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //region Animation
    @JvmStatic
    fun expand(v: View?) {
        if (v == null) return
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight
        expand(v, targetHeight)
    }

    @JvmStatic
    fun expand(v: View?, targetHeight: Int) {
        if (v == null || targetHeight < 1) return

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun willChangeBounds(): Boolean {
                return true
            }

            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }


        }

        // 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    @JvmStatic
    fun expandHoriz(v: View) {
        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetWidth = v.measuredWidth

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.width = 1
        if (v.visibility != View.VISIBLE)
            v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun willChangeBounds(): Boolean {
                return true
            }

            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.width = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetWidth * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        // 1dp/ms
        a.duration = (targetWidth / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    @JvmStatic
    fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    @JvmStatic
    fun collapseHoriz(v: View) {
        val initialWidth = v.measuredWidth

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.width = initialWidth - (initialWidth * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (initialWidth / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    @JvmStatic
    fun collapseWithout1dp(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms upto 1dp remains
        a.duration = ((initialHeight / v.context.resources.displayMetrics.density).toInt() - 1).toLong()
        v.startAnimation(a)
    }
    //endregion

    //region Picture-utilities

    @JvmStatic
    @Throws(IOException::class)
    fun copyI2OStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(4 * 1024)
        var bytesRead: Int = input.read(buffer)
        while (bytesRead != -1) {
            output.write(buffer, 0, bytesRead)
            bytesRead = input.read(buffer)
        }
    }

    @JvmStatic
    fun getAlteredBoldStr(alteredStrArr: Array<String>,
                          isFirstPartBold: Boolean): SpannableStringBuilder {
        val str = SpannableStringBuilder()
        var i = 0
        val l = alteredStrArr.size
        while (i < l) {
            val s = alteredStrArr[i]
            val pl = str.length
            if (i > 0) str.append(" ")
            str.append(s)
            if (isFirstPartBold && i % 2 == 0 || !isFirstPartBold && i % 2 != 0)
                str.setSpan(StyleSpan(Typeface.BOLD),
                        pl, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            i++
        }
        return str
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getCorrectionAngleForCam(picFile: File): Int {
        val exif = ExifInterface(picFile.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL)

        var angle = 0
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            angle = 90
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            angle = 180
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            angle = 270
        }
        Lg.d(TAG, "angle = $angle")
        return angle
    }

    @JvmStatic
    fun decodeFile(picFile: File, requiredSize: Int): Bitmap? {
        try {
            // decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val stream1 = FileInputStream(picFile)
            BitmapFactory.decodeStream(stream1, null, o)
            stream1.close()

            // Find the correct scale value. It should be the power of 2.
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1

            while (true) {
                if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                    break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }
            Lg.i("SCALE", "scale = $scale")

            // decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val stream2 = FileInputStream(picFile)
            val bitmap = BitmapFactory.decodeStream(stream2, null, o2)
            stream2.close()
            return bitmap
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    @Deprecated("This method doesn't work properly, hence needs a better version. -> TODO")
    @JvmStatic
    fun setLeftImageSpannable(context: Context, tv: TextView,
                              imgResId: Int, str: String) {
        val ss = SpannableString(str)
        Lg.e(TAG, "Text Span : " + ss.toString())
        val d = getDrawable(context, imgResId)
        d.setBounds(0, 0, tv.lineHeight, tv.lineHeight)
        // d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        val span = ImageSpan(d, ImageSpan.ALIGN_BASELINE)
        ss.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        // FIX-TEXT_ME Texts are lost after setting the image-span;
        Lg.e(TAG, "Final image-attached Span : " + ss.toString())
        tv.text = ss

        /*SpannableString spannable = new SpannableString(str);
        Drawable myIcon = getDrawable(context, imgResId);
        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
        spannable.setSpan(new ImageSpan(myIcon, ImageSpan.ALIGN_BASELINE),
                matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannable, TextView.BufferType.SPANNABLE);*/
    }
    //endregion

    @JvmStatic
    fun getImageBase64Str(bitmap: Bitmap): String {
        val bao = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao)
        val ba = bao.toByteArray()
        return Base64.encodeToString(ba, Base64.DEFAULT)
    }

    @JvmStatic
    fun getUnderlinedString(string: String): SpannableString {
        val content = SpannableString(string)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        return content
    }

    @JvmStatic
    fun gotoPlayStorePage(context: Context) {
        val appPackageName = context.packageName // getPackageName() from Context or Activity object
        try {
            context.startActivity(
                    Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(
                    Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    @JvmStatic
    fun shareApp(context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hi, I am using ${C.APP_NAME} to chat, " +
                "voice-call & video-call to my friends totally free of cost.\n" +
                "Install and get in touch: ${C.PLAY_STORE}")
        context.startActivity(Intent.createChooser(shareIntent, "Share ${C.APP_NAME}"))
    }

    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @JvmStatic
    fun hideKeyboardFromFragment(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @JvmStatic
    fun getHTMLStr(source: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(source, Html.FROM_HTML_OPTION_USE_CSS_COLORS)
        else
            Html.fromHtml(source)
    }

    @JvmStatic
    fun justifyListViewHeightBasedOnChildren(listView: ListView) {

        val adapter = listView.adapter ?: return

        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        val par = listView.layoutParams
        par.height = totalHeight + listView.dividerHeight * (adapter.count - 1)
        listView.layoutParams = par
        listView.requestLayout()
    }

    @JvmStatic
    fun parseInt(numObj: Any?, defValue: Int): Int = try {
        Integer.parseInt(numObj?.toString() ?: "")
    } catch (e: Exception) {
        e.printStackTrace()
        defValue
    }

    @JvmStatic
    fun parseLong(numObj: Any, defValue: Long): Long = try {
        java.lang.Long.parseLong(numObj.toString() + "")
    } catch (e: Exception) {
        e.printStackTrace()
        defValue
    }

    @JvmStatic
    fun parseDouble(numObj: Any, defValue: Double): Double = try {
        java.lang.Double.parseDouble(numObj.toString() + "")
    } catch (e: Exception) {
        e.printStackTrace()
        defValue
    }

    @JvmStatic
    fun limitTextViewLine(textView: TextView, limit: Int) {
        val vto = textView.viewTreeObserver ?: null
        vto?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val obs = textView.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (textView.lineCount > limit) {
                    val lineEndIndex = textView.layout.getLineEnd(2)
                    val text = textView.text.subSequence(0, lineEndIndex - 3).toString() + "..."
                    textView.text = text
                }
            }
        })
    }

    @JvmStatic
    fun notEmpty(data: String?): Boolean = !isEmpty(data)

    @JvmStatic
    fun isEmpty(data: SpannableString?): Boolean =
            isEmpty(data?.toString())

    @JvmStatic
    fun isEmpty(data: String?): Boolean =
            (data == null
                    || data.isEmpty()
                    || data.trim { it <= ' ' }.toLowerCase() == C.NULL_STR)

    @JvmStatic
    fun isValidUri(url: String?, scheme: String): Boolean =
            url != null && notEmpty(url)
                    && url.startsWith("$scheme://") && url.length > (scheme.length + 3)

    @JvmStatic
    fun isValidUrl(url: String?): Boolean = isValidUri(url, "https") || isValidUri(url, "http")

    @JvmStatic
    fun isValidLocalUrl(url: String?): Boolean = isValidUri(url, "content:")

    @JvmStatic
    fun isAlphaOnly(data: String?): Boolean = !isEmpty(data)
            && (data?.matches("[a-zA-Z]+".toRegex()) ?: false)

    @JvmStatic
    fun isNumber(str: String?): Boolean = !isEmpty(str) && str?.matches("\\d+".toRegex()) ?: false

    @JvmStatic
    fun isValidMimeType(mimeType: String?) =
            notEmpty(mimeType) && mimeType != "*/*" && MimeTypeMap.getSingleton().hasMimeType(mimeType)

    @JvmStatic
    fun getMimeType(context: Context, file: File): String = getMimeType(context, Uri.fromFile(file))

    @JvmStatic
    fun getMimeType(context: Context, uri: Uri): String =
            if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                context.contentResolver.getType(uri) ?: ""
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                        .toString()) ?: ""
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        fileExtension.toLowerCase()) ?: ""
            }

    @JvmStatic
    fun concatForUrl(head: String, tail: String): String {
        val he = head.endsWith("/")
        val ts = tail.startsWith("/")
        return when {
            he && ts ->
                head + tail.substring(1)
            he && !ts || !he && ts ->
                head + tail
            else -> "$head/$tail"
        }
    }

    @JvmStatic
    fun getOiiNameFromThreadDetails(username: String, jaUserDetailsArray: JSONArray): String? {
        var oiiName = C.NULL_STR
        for (i in 0 until jaUserDetailsArray?.length()!!) {

            val joUserDetails = jaUserDetailsArray.getJSONObject(i)
            val userNameItem = joUserDetails.getString(C.PROFILE.USERNAME)
            if (userNameItem == username) {
                oiiName = joUserDetails.getString(C.PROFILE.NAME)
            }
        }
        return oiiName
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    /**
     * Return as Time as Digital Clock Digit
     * @param value long value
     * @return string
     */
    @JvmStatic
    fun addZeroBeforeDecimalLessTen(value: Long): String {
        val timeString: String
        if (value in 0..9)
            timeString = "0$value"
        else
            timeString = value.toString()
        return timeString
    }


    /**
     * Return as Time as Digital Clock Digit
     * @param value long value
     * @return string
     */
    @JvmStatic
    fun getTalkTimeInString(talkTime: Long): String {
        val talkTimeString: String
        val seconds: Long
        val minutes: Long
        val hour: Long
        var temp: Long
        seconds = talkTime % 60
        temp = talkTime / 60
        minutes = temp % 60
        temp = temp / 60
        hour = temp % 60
        if (hour == 0L) {
            talkTimeString = addZeroBeforeDecimalLessTen(minutes) + " : " + addZeroBeforeDecimalLessTen(seconds)
        } else {
            talkTimeString = addZeroBeforeDecimalLessTen(hour) + " : " + addZeroBeforeDecimalLessTen(minutes) + " : " + addZeroBeforeDecimalLessTen(seconds)
        }
        return talkTimeString
    }

    /**
     * Return as Time as 1m10s
     * @param talkTime long value
     * @return string
     */
    @JvmStatic
    fun getTalkTimeInStringWithMinuteAndSecond(talkTime: Long): String {
        val talkTimeString: String
        val seconds = talkTime % 60
        val minutes: Long
        val hour: Long
        var temp = talkTime / 60
        minutes = temp % 60
        temp /= 60
        hour = temp % 60
        talkTimeString = if (minutes == 0L && hour == 0L)
            "${seconds}s"
        else if (hour == 0L)
            "${minutes}m${seconds}s"
        else
            "${hour}h${minutes}m${seconds}s"
        return talkTimeString
    }

    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    @JvmStatic
    val isEmulator: Boolean
        get() = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("blue")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk")

    @JvmStatic
    val curSDK: Int
        get() = Build.VERSION.SDK_INT

    @JvmStatic
    val isAboveLollipop: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    /**
     * @return Screen width or height, depending on which is minimum
     */
    @JvmStatic
    fun getScreenMinHW(context: Context): Int {
        val dm = context.resources.displayMetrics
        val w = dm.widthPixels
        val h = dm.heightPixels
        return if (w < h) w else h
    }

    @JvmStatic
    fun getDeviceDetailsJSONString(ctx: Context): String {
        val jo = JSONObject()
        try {
            jo.put("os", Build.VERSION.SDK_INT)
            jo.put("model", Build.MODEL)
            jo.put("manufacturer", Build.MANUFACTURER)
            jo.put("ip", getDeviceIp(true))
            // jo.put("imei", getDeviceIMEI(ctx));
            // jo.put("mac", getDeviceMAC(ctx));
            jo.put("dpi", getDeviceDpi(ctx).toDouble())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jo.toString()
    }

    @JvmStatic
    fun getDeviceDpi(context: Context): Float {
        return context.resources.displayMetrics.density * 160.0f
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun getDeviceIp(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            // for now eat exceptions
        }

        return ""
    }

    @JvmStatic
    fun isNetConnected(context: Context?): Boolean {
        if (context == null)
            return false
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission")
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

//    @JvmStatic
//    fun isWifiConnected(context: Context): Boolean {
//        val connectivityManager = context
//                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        @SuppressLint("MissingPermission")
//        val wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//        return wifiInfo.isConnected
//    }


    @JvmStatic
    fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }


    @JvmStatic
    fun isGPSProviderEnabled(locationManager: LocationManager): Boolean {
        // getting GPS status
        return locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @JvmStatic
    fun isNetProviderEnabled(locationManager: LocationManager): Boolean {
        // getting network status
        return locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @JvmStatic
    fun isDevicePortrait(res: Resources): Boolean {
        return res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    @JvmStatic
    fun saveBitmapToFile(bmpToSave: Bitmap, directory: File,
                         fileName: String) {
        if (!directory.exists())
            directory.mkdirs()
        val file = File(directory, fileName)
        val fOut: FileOutputStream
        try {
            fOut = FileOutputStream(file)
            bmpToSave.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    fun log(): StringBuilder {
        val builder = StringBuilder()

        try {
            val command = arrayOf("logcat", "-d", "-v", "threadtime")

            val process = Runtime.getRuntime().exec(command)

            val bufferedReader = BufferedReader(
                    InputStreamReader(process.inputStream))

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                builder.append(line)
                line = bufferedReader.readLine()
            }
        } catch (ex: IOException) {
            Lg.e(TAG, "getLog failed", ex)
        }

        return builder
    }

    /**
     * @return **Logcat** data at the time of debugging as String<br></br>
     * **Note:** This method requires permissions **
     * "android.permission.READ_LOGS" **
     */
    @JvmStatic
    fun logCatDetails(): String {
        var logData = ""
        try {
            val process = Runtime.getRuntime().exec("logcat -d")

            val bufferedReader = BufferedReader(
                    InputStreamReader(process.inputStream))

            val log = StringBuilder()

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                log.append(line)
                line = bufferedReader.readLine()
            }

            logData = log.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return logData
    }

    @JvmStatic
    val isSDCardMounted: Boolean
        get() {
            val status = Environment.getExternalStorageState()
            return status == Environment.MEDIA_MOUNTED
        }
}