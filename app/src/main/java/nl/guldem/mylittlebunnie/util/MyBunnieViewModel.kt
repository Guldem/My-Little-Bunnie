package nl.guldem.mylittlebunnie.util

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

abstract class MyBunnieViewModel : ViewModel(), CoroutineScope {

    override var coroutineContext: CoroutineContext = Job()

    override fun onCleared() {
        super.onCleared()
        try {
            coroutineContext[Job]?.cancel()

        } catch (e: Exception){
            Log.e("Coroutines", "Something gone wrong cancelling this Job")
        }
    }


}
