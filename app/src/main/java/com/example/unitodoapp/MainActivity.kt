package com.example.unitodoapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.unitodoapp.data.datastore.DataStoreManager
import com.example.unitodoapp.data.datastore.UserPreferences
import com.example.unitodoapp.data.workmanager.CustomWorkManager
import com.example.unitodoapp.ui.navigation.AppNavHost
import com.example.unitodoapp.ui.screens.settings.model.ThemeMode
import com.example.unitodoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var workManager: CustomWorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager.setWorkers()

        setContent {
            val navController = rememberNavController()
            val pref = dataStoreManager.userPreferences.collectAsState(initial = UserPreferences())

            TodoAppTheme(
                darkTheme = when (pref.value.themeMode) {
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

        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    lifecycleScope.launch {
                        dataStoreManager.saveNotificationsPermission(true)
                    }

                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {}

                else -> {
                    val pLauncher = registerForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                        if (isGranted) {
                            lifecycleScope.launch {
                                dataStoreManager.saveNotificationsPermission(true)
                            }
                        } else {
                            lifecycleScope.launch {
                                dataStoreManager.saveNotificationsPermission(false)
                            }
                            Toast.makeText(this, R.string.notifications_disable, Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    pLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}

