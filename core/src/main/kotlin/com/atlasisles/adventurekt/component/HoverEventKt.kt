package com.atlasisles.adventurekt.component

import net.kyori.adventure.text.event.HoverEvent

inline fun showText(text: RootComponentKt.() -> Unit): HoverEvent<*> {
    return HoverEvent.showText(RootComponentKt().apply(text).build())
}