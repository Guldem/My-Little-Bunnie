package nl.guldem.mylittlebunnie.util.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes l: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(l, this, attachToRoot)