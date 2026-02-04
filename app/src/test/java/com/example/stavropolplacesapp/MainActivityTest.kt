package com.example.stavropolplacesapp

import com.example.stavropolplacesapp.ui.components.topBarTitle
import org.junit.Assert.assertEquals
import org.junit.Test

class MainActivityTest {

    @Test
    fun topBarTitle_mapsRoutes() {
        assertEquals("Ставротека", topBarTitle("home"))
        assertEquals("Места", topBarTitle("places"))
        assertEquals("Избранное", topBarTitle("favorites"))
        assertEquals("О приложении", topBarTitle("about"))
        assertEquals("Земляки", topBarTitle("zemlyaki"))
    }

    @Test
    fun topBarTitle_defaultsOnUnknown() {
        assertEquals("Ставротека", topBarTitle("unknown"))
        assertEquals("Ставротека", topBarTitle(""))
    }
}
