@file:Suppress("unused")

package com.atlasisles.adventurekt.component

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import java.net.URL

fun openUrl(url: String): ClickEvent {
    return ClickEvent.openUrl(url)
}

fun openUrl(url: URL): ClickEvent {
    return ClickEvent.openUrl(url)
}

fun openFile(file: String): ClickEvent {
    return ClickEvent.openFile(file)
}

fun runCommand(command: String): ClickEvent {
    return ClickEvent.runCommand(command)
}

fun suggestCommand(command: String): ClickEvent {
    return ClickEvent.suggestCommand(command)
}

fun changePage(page: Int): ClickEvent {
    return ClickEvent.changePage(page)
}

fun copyToClipboard(text: String): ClickEvent {
    return ClickEvent.copyToClipboard(text)
}

fun callback(function: (Audience) -> Unit): ClickEvent {
    return ClickEvent.callback(function)
}

fun callback(options: ClickCallback.Options, function: (Audience) -> Unit): ClickEvent {
    return ClickEvent.callback(function, options)
}

fun custom(id: Key, payload: String = ""): ClickEvent {
    return ClickEvent.custom(id, BinaryTagHolder.binaryTagHolder(payload))
}