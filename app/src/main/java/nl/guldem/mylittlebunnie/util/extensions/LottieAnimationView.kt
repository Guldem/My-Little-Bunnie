package nl.guldem.mylittlebunnie.util.extensions

import android.animation.Animator
import com.airbnb.lottie.LottieAnimationView

inline fun LottieAnimationView.addOnAnimationEnded(crossinline animationEnded: () -> Unit){
    addAnimatorListener(object : Animator.AnimatorListener{
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            animationEnded.invoke()
        }
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
}