package nl.guldem.mylittlebunnie.util.extensions

import android.view.View

@Suppress("UNCHECKED_CAST")
inline fun <T : View> T.onClick(crossinline block: (T) -> Unit) = setOnClickListener { block(it as T) }