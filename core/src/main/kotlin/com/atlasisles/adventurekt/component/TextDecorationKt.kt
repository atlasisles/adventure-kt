package com.atlasisles.adventurekt.component

import net.kyori.adventure.text.format.TextDecoration

val obfuscated = TextDecoration.OBFUSCATED
val bold = TextDecoration.BOLD
val strikethrough = TextDecoration.STRIKETHROUGH
val underline = TextDecoration.UNDERLINED
val italic = TextDecoration.ITALIC

fun obfuscated(): TextDecorationKt {
    return TextDecorationKt(TextDecoration.OBFUSCATED)
}

fun bold(): TextDecorationKt {
    return TextDecorationKt(TextDecoration.BOLD)
}

fun strikethrough(): TextDecorationKt {
    return TextDecorationKt(TextDecoration.STRIKETHROUGH)
}

fun underlined(): TextDecorationKt {
    return TextDecorationKt(TextDecoration.UNDERLINED)
}

fun italic(): TextDecorationKt {
    return TextDecorationKt(TextDecoration.ITALIC)
}

fun TextDecoration.kt(): TextDecorationKt {
    return TextDecorationKt(this)
}