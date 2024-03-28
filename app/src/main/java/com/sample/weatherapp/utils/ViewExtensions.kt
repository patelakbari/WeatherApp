package com.sample.weatherapp.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.internal.ViewUtils.showKeyboard
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textview.MaterialTextView


fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.toastLong(msg: String) {
    toast(msg, Toast.LENGTH_LONG)
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resources.getText(resId), duration).show()
}

fun Context.toastLong(@StringRes resId: Int) {
    toast(resId, Toast.LENGTH_LONG)
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), msg, duration).show()
}

fun Fragment.toastLong(msg: String) {
    toast(msg, Toast.LENGTH_LONG)
}

fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), resources.getText(resId), duration).show()
}

fun Fragment.toastLong(@StringRes resId: Int) {
    toast(resId, Toast.LENGTH_LONG)
}

fun View.showSnackbarWithAction(@StringRes resId: Int, @StringRes actionResId: Int, listener: (View) -> Unit): Snackbar {
    val snackBar = Snackbar.make(this, resId, Snackbar.LENGTH_INDEFINITE)
    snackBar.setAction(actionResId, View.OnClickListener(listener))
    snackBar.show()
    return snackBar
}

fun View.showSnackbar(@StringRes resId: Int): Snackbar {
    val snackBar = Snackbar.make(this, resId, Snackbar.LENGTH_INDEFINITE)
    snackBar.show()
    return snackBar
}

fun View.onClick(listener: () -> Unit) {
    setOnClickListener {
        listener()
    }
}

fun MaterialRadioButton.onCheckedChanged(listener: (Boolean) -> Unit) {
    this.setOnCheckedChangeListener { _, value ->
        listener(value)
    }
}

fun View.onLongClick(listener: () -> Unit) {
    this.setOnLongClickListener {
        listener()
        return@setOnLongClickListener true
    }
}


fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hidden() {
    visibility = View.INVISIBLE
}


