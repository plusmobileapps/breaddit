package com.plusmobileapps.breaddit

import kotlin.math.min

val Any.logTag: String
    get() {
        val className = this.javaClass.simpleName
        val endIndex = min(className.length, 23)
        return className.substring(0, endIndex)
    }