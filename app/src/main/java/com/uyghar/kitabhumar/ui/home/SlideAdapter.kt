package com.uyghar.kitabhumar.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.uyghar.kitabhumar.models.Slider

class SlideAdapter(val slider: Array<Slider>, fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return slider.size
    }

    override fun createFragment(position: Int): Fragment {
        val slideFragment = SlideFragment()
        slideFragment.slider = slider.get(position)
        return slideFragment
    }

}