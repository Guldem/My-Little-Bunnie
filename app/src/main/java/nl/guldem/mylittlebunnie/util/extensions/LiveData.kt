package nl.guldem.mylittlebunnie.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> observeNotNull(liveData: LiveData<T>, lifecycleOwner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    liveData.observe(lifecycleOwner, Observer { value ->
        value?.let { observer(it) }
    })
}