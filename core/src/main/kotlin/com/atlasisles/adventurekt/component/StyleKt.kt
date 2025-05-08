@file:Suppress("unused")

package com.atlasisles.adventurekt.component

import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration

interface StyleKt {
    fun with(original: Style): Style
    fun without(original: Style): Style
}

class TextDecorationKt(private val textDecoration: TextDecoration) : StyleKt {
    override fun with(original: Style): Style {
        return original.decoration(this.textDecoration, true)
    }

    override fun without(original: Style): Style {
        return original.decoration(this.textDecoration, false)
    }
}

open class InsertionWithoutKt : StyleKt {
    override fun with(original: Style): Style {
        throw IllegalAccessException("This style is only for without use")
    }

    override fun without(original: Style): Style {
        return original.insertion(null)
    }
}

class InsertionKt(private val insertion: String) : InsertionWithoutKt() {
    override fun with(original: Style): Style {
        return original.insertion(this.insertion)
    }
}