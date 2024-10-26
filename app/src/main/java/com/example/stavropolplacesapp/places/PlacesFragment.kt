package com.example.stavropolplacesapp.places

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stavropolplacesapp.R

class PlacesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("PlacesFragment", "onCreateView вызван для PlacesFragment")
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

}

