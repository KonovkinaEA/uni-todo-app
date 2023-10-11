package com.example.unitodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.ui.navigation.AppNavHost
import com.example.unitodoapp.ui.screens.settings.SettingsState
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import com.example.unitodoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val settings = dataStoreManager.getSettings().collectAsState(initial = SettingsState())

            TodoAppTheme(
                darkTheme = when (settings.value.themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(
                        modifier = Modifier,
                        navController = navController
                    )
                }
            }
        }
    }
}

