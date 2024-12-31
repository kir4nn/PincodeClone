import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.internassignment.ui.navigation.NavigationItem
import com.example.internassignment.ui.screens.AddItemScreen
import com.example.internassignment.ui.screens.ExploreGridScreen
import com.example.internassignment.ui.screens.ExploreListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFAFAFA),
                modifier = Modifier.height(100.dp)
            ) {
                val items = listOf(
                    NavigationItem.ExploreList,
                    NavigationItem.ExploreGrid,
                    NavigationItem.AddItem,
                    NavigationItem.ScreenFour,
                    NavigationItem.ScreenFive
                )

                val currentRoute = navController
                    .currentBackStackEntryAsState().value?.destination?.route

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(
                                        if (currentRoute == item.route) Color(0xFF4CAF50)
                                        else Color.LightGray,
                                        CircleShape
                                    )
                            )
                        },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Transparent,
                            unselectedIconColor = Color(0xFFE8E8E8),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.ExploreList.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationItem.ExploreList.route) {
                ExploreListScreen()
            }
            composable(NavigationItem.ExploreGrid.route) {
                ExploreGridScreen()
            }
            composable(NavigationItem.AddItem.route) {
                AddItemScreen()
            }
            composable(NavigationItem.ScreenFour.route) {
                Text("Screen Four") // Temporary placeholder
            }
            composable(NavigationItem.ScreenFive.route) {
                Text("Screen Five") // Temporary placeholder
            }
        }
    }
}