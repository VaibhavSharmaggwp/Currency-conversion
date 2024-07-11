package com.example.coinswap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coinswap.main_screen.MainScreeViewModel
import com.example.coinswap.main_screen.MainScreen
import com.example.coinswap.ui.presentation.CoinSwapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinSwapTheme {
                val viewModel: MainScreeViewModel = hiltViewModel()
                Surface {
                    MainScreen(
                        state = viewModel.state,
                        onEvent = viewModel::onEvents
                    )
                }

            }
        }
    }
}

