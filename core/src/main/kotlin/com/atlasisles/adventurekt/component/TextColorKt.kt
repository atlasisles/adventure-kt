package com.atlasisles.adventurekt.component

import net.kyori.adventure.text.format.TextColor

class GradientColor(
    private val colors: List<TextColor>
) {
    fun buildString(): String {
        val builder = StringBuilder()
        for (color in this.colors) {
            builder.append(":")
                .append(color.asHexString())
        }
        return builder.toString()
    }
}

fun rgb(r: Int, g: Int, b: Int): TextColor {
    return TextColor.color(r, g, b)
}

fun rgb(rgb: Int): TextColor {
    return TextColor.color(rgb)
}

fun hex(hex: String): TextColor {
    if (!hex.startsWith("#"))
        return TextColor.fromHexString("#$hex")!!
    return TextColor.fromHexString(hex)!!
}

fun gradient(vararg colors: TextColor): GradientColor {
    return GradientColor(colors.map { it })
}