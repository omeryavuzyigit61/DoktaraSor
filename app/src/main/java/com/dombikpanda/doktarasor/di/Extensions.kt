package com.dombikpanda.doktarasor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showLongToast(message: String) {
    context?.showLongToast(message)
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showShortToast(message: String) {
    context?.showShortToast(message)
}

val EditText.textValue: String
    get() = this.text.toString()

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun SimpleDateFormat(date: Long, pattern: String): String {
    val dtlong = Date(date)
    val sdfdate =
        SimpleDateFormat(pattern, Locale.getDefault()).format(dtlong)
    return sdfdate
}

fun goActivity(context: Context, intent: Intent) {
    intent.flags()
    context.startActivity(intent)
    (context as Activity).finish()
}

fun Intent.flags() {
    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}



fun Activity.hideKeyboard(event: MotionEvent) {
    if (event.action == MotionEvent.ACTION_DOWN) {
        val view = currentFocus
        if (view != null) {
            view.clearFocus()
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}


