package com.atlasisles.adventurekt.component

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

/**
 * Set a theme on components which can be used elsewhere.
 *
 * Defaults to white colours, the default Minecraft font and no click/hover events.
 * */
class RootTheme {
    var primaryColour: TextColor = NamedTextColor.WHITE
    var secondaryColour: TextColor = NamedTextColor.WHITE
    var tertiaryColour: TextColor = NamedTextColor.WHITE
    var font: Key = Key.key("default")
    var hoverEvent: HoverEvent<*>? = null
    var clickEvent: ClickEvent? = null
}