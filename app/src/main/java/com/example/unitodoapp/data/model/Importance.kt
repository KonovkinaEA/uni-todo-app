package com.example.unitodoapp.data.model

import com.example.unitodoapp.R

enum class Importance {
    LOW {
        override fun toStringResource(): Int = R.string.importance_low
        override fun toStringForItemServer(): String = "low"
    },
    BASIC {
        override fun toStringResource(): Int = R.string.importance_basic
        override fun toStringForItemServer(): String = "basic"
    },
    IMPORTANT {
        override fun toStringResource(): Int = R.string.importance_important
        override fun toStringForItemServer(): String = "important"
    };

    abstract fun toStringResource(): Int
    abstract fun toStringForItemServer(): String
}