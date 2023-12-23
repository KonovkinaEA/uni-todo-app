package com.example.unitodoapp.ui.screens.settings.model

import com.example.unitodoapp.R

enum class ThemeMode {
    LIGHT {
        override fun toStringResource(): Int = R.string.system_theme_light
    },
    DARK {
        override fun toStringResource(): Int = R.string.system_theme_dark
    },
    SYSTEM {
        override fun toStringResource(): Int = R.string.system_theme_system
    };

    abstract fun toStringResource(): Int
}
