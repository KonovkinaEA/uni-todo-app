package com.example.unitodoapp.data.model

import com.example.unitodoapp.R

enum class Importance {
    LOW {
        override fun toStringResource(): Int = R.string.importance_low
    },
    BASIC {
        override fun toStringResource(): Int = R.string.importance_basic
    },
    IMPORTANT {
        override fun toStringResource(): Int = R.string.importance_important
    };

    abstract fun toStringResource(): Int
}