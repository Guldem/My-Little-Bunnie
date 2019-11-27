package nl.guldem.mylittlebunnie.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import nl.guldem.mylittlebunnie.util.extensions.inflate
import nl.guldem.mylittlebunnie.util.extensions.observeNotNull

abstract class MyBunnieFragment: Fragment(){
    abstract val layout: Int

    inline fun <T> LiveData<T>.observeNotNull(crossinline observer: (T) -> Unit) = observeNotNull(this, viewLifecycleOwner, observer)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = container?.inflate(layout)


}

