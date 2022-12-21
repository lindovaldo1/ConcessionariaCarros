package com.example.concessionariacarros.bottomnav

import com.example.concessionariacarros.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Home : BottomNavItem("Início", R.drawable.ic_home,"home")
    object Statistics: BottomNavItem("Estatísticas",R.drawable.ic_chart,"statistics")

}