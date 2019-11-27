package nl.guldem.mylittlebunnie.util.extensions

import androidx.viewpager.widget.ViewPager

fun ViewPager.addOnPageSelectedListener(onPageSelected: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {}
        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }
    })
}