package nl.guldem.mylittlebunnie.util.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

fun Fragment.findNavController() = NavHostFragment.findNavController(this)