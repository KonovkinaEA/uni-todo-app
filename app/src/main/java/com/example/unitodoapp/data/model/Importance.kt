package com.example.unitodoapp.data.model

import com.example.unitodoapp.R

enum class Importance {
    LOW {
        override fun toStringResource(): Int = R.string.importance_low
    },
    BASIC {
        override fun toStringResource(): Int = R.string.importance_normal
    },
    IMPORTANT {
        override fun toStringResource(): Int = R.string.importance_urgent
    };

    abstract fun toStringResource(): Int
}