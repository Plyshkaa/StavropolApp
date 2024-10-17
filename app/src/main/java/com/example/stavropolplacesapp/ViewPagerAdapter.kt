package com.example.stavropolplacesapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PlacesListFragment() // Возвращаем фрагмент "Места"
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 1 // Только одна вкладка для "Места"
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Места"
            else -> null
        }
    }
}



