package nl.guldem.mylittlebunnie.ui.home

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import nl.guldem.mylittlebunnie.services.NotificationService
import nl.guldem.mylittlebunnie.services.SupriseStorage
import nl.guldem.mylittlebunnie.services.SuprisesDao
import nl.guldem.mylittlebunnie.util.MyBunnieViewModel
import nl.guldem.mylittlebunnie.util.extensions.birthdayInAmsterdam
import nl.guldem.mylittlebunnie.util.extensions.toLiveData
import org.joda.time.DateTime

enum class Presents(val resource: String, val source: Uri) {
    PresentOne("present_1.json", Uri.parse("asset:///example_video.mp4")),
    PresentTwo("present_2.json", Uri.parse("asset:///example_video.mp4")),
    PresentThree("present_3.json", Uri.parse("asset:///example_video.mp4")),
    PresentFour("present_4.json", Uri.parse("asset:///example_video.mp4")),
    PresentFive("present_3.json", Uri.parse("asset:///example_video.mp4")),
    PresentSix("present_2.json", Uri.parse("asset:///example_video.mp4")),
    PresentSeven("present_1.json", Uri.parse("asset:///example_video.mp4")),
    PresentEight("present_4.json", Uri.parse("asset:///example_video.mp4"))
}

@Parcelize
data class Suprise(
    val id: String,
    val date: DateTime,
    val supriseVideo: Uri,
    val opened: Boolean,
    val presentImage: String
) : Parcelable

data class SuprisesViewState(
    val birthDay: DateTime,
    val suprises: List<Suprise>
)

class HomeViewModel(private val suprisesDao: SuprisesDao, private val notificationService: NotificationService) :
    MyBunnieViewModel() {
    private val mutablePosition = MutableLiveData<Int>()
    val suprisesPosition = mutablePosition.toLiveData()
    private val mutableSuprises = MutableLiveData<SuprisesViewState>()
    val suprisesViewState = mutableSuprises.toLiveData()

    fun init() {

        val birthDay = birthdayInAmsterdam()

        if (mutableSuprises.value == null) {
            initState(birthDay)
        }
    }

    fun refresh() {
        if (mutableSuprises.value != null) {
            launch {
                val results = suprisesDao.getAll()
                mutableSuprises.postValue(mutableSuprises.value?.copy(suprises = mapSuprises(results)))
            }
        }
    }


    private fun initState(birthDay: DateTime) {


        launch {
            val results = suprisesDao.getAll()

            if (results.isNullOrEmpty()) {
                val suprises = initSuprises(birthDay)
                val storageSupries = suprises.map {
                    SupriseStorage(
                        id = it.id,
                        date = it.date,
                        opened = it.opened
                    )
                }
                suprisesDao.insertAll(storageSupries)
                mutableSuprises.postValue(SuprisesViewState(birthDay = birthDay, suprises = suprises))
            } else {
                mutableSuprises.postValue(SuprisesViewState(birthDay = birthDay, suprises = mapSuprises(results)))
            }
        }
    }

    private fun mapSuprises(results: List<SupriseStorage>): List<Suprise> {
        return results.mapIndexed { index, supriseStorage ->
            Suprise(
                id = supriseStorage.id,
                date = supriseStorage.date,
                supriseVideo = Presents.values()[index].source,
                opened = supriseStorage.opened,
                presentImage = Presents.values()[index].resource
            )
        }
    }

    private fun initSuprises(birthDay: DateTime): List<Suprise> {
        return List(Presents.values().count()) {
            val suprise = Suprise(
                id = "suprise $it",
                date = birthDay.plusDays(it),
                supriseVideo = Presents.values()[it].source,
                opened = false,
                presentImage = Presents.values()[it].resource
            )
            notificationService.scheduleNotification(suprise)
            suprise
        }

    }

    private fun updateSuprise(suprise: Suprise?) {
        if (suprise == null) return
        launch {
            suprisesDao.updateSuprise(SupriseStorage(id = suprise.id, date = suprise.date, opened = suprise.opened))

        }
    }

    fun openPresentWithViewUpdate(id: String) {
        val suprises = mutableSuprises.value?.suprises?.map {
            if (it.id == id) it.copy(opened = true) else it.copy()
        } ?: return

        updateSuprise(suprises.find { it.id == id })
    }


    fun setPosition(position: Int) {
        mutablePosition.value = position
    }


}