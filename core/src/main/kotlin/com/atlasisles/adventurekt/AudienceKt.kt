@file:Suppress("unused")

package com.atlasisles.adventurekt

import com.atlasisles.adventurekt.component.RootComponentKt
import net.kyori.adventure.audience.Audience

inline fun Audience.send(message: RootComponentKt.() -> Unit) {
    this.sendMessage(RootComponentKt().apply(message).build())
}

inline fun Audience.sendActionBar(actionbar: RootComponentKt.() -> Unit) {
    this.sendActionBar(RootComponentKt().apply(actionbar).build())
}

inline fun Audience.sendPlayerListHeader(content: RootComponentKt.() -> Unit) {
    this.sendPlayerListHeader(RootComponentKt().apply(content).build())
}

inline fun Audience.sendPlayerListFooter(content: RootComponentKt.() -> Unit) {
    this.sendPlayerListFooter(RootComponentKt().apply(content).build())
}