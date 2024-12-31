package com.example.internassignment.ui.navigation

sealed class NavigationItem(val route:String) {
    object ExploreList : NavigationItem("explore_list")
    object ExploreGrid : NavigationItem("explore_grid")
    object AddItem : NavigationItem("add_item")
    object ScreenFour : NavigationItem("screen_four")
    object ScreenFive : NavigationItem("screen_five")
}