package com.atlasisles.adventurekt

import com.atlasisles.adventurekt.component.ansi
import com.atlasisles.adventurekt.component.hex
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer
import net.kyori.ansi.ColorLevel
import kotlin.random.Random
import kotlin.time.measureTime

fun main() {
    val timed = measureTime {
        val text = Component.empty()
            .append(Component.text("Hello world").color(hex("66ccff")).hoverEvent(HoverEvent.showText {
                Component.text(10)
            }))
            .appendNewline()
            .append(Component.text("With color red!", red).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
            .appendNewline()
            .append(Component.text("Click me!", red).clickEvent(ClickEvent.callback {
                it.sendMessage(Component.text("You just clicked a text!"))
            }))
            .append(Component.empty())
            .append(Component.translatable("minecraft.enchantment.protection"))
            .appendSpace()
            .append(MiniMessage.builder().strict(true).tags(TagResolver.standard()).build().deserialize("<grey>MiniMessage!</grey>"))
            .appendSpace()
            .append(Component.keybind("key.keyboard.space"))
            .appendNewline()
            .append(MiniMessage.builder().strict(true).tags(TagResolver.standard())
                .build().deserialize("You can even 套娃 in the component"))
            .appendNewline()
            .append(Component.join(JoinConfiguration.separator(Component.text("!")), listOf(
                Component.text("first"),
                Component.text("second"),
                Component.text("third"),

                Component.text("Replace test 1").replaceText(
                    TextReplacementConfig.builder()
                        .matchLiteral("aa")
                        .replacement("bb")
                        .build()
                ),
                Component.text("Replace test 2").replaceText(TextReplacementConfig.builder().match("aa").replacement { builder ->
                    return@replacement Component.text("aa", red)
                }.build())
            )))
            .appendNewline()
            .append(MiniMessage.builder().strict(true).tags(TagResolver.standard()).build()
                .deserialize("<gradient:red:yellow:blue>Lemme test this annoying gradient with a very very long string like this!</gradient>"))

        val ansiSerializer = ANSIComponentSerializer.builder().colorLevel(ColorLevel.TRUE_COLOR).build()
        print(ansiSerializer.serialize(text))

        val newTest = Component.newline()
            .append(Component.text(" • ", white))
            .append(Component.text("This line should be grey", gray))
            .appendNewline()
            .append(Component.text(" • ", white))
            .append(Component.text("This line should be in a custom font (in-game and in json)", white).font(Key.key(
                "minecraft", "illageralt"
            )))
            .appendNewline()
            .append(Component.text(" • ", white))
            .append(MiniMessage.miniMessage().deserialize(
                "<gradient:#E50000:#FF8D00:#FFEE00:#28121:#004CFF:#770088>This line should be in a rainbow gradient</gradient>"
            ))
            .appendNewline()
            .append(Component.text(" • ", white))
            .append(Component.text("This line should be both ", gold))
            .append(Component.text("ITALIC ", gold).decorate(TextDecoration.ITALIC))
            .append(Component.text("BOLD ", gold).decorate(TextDecoration.BOLD))
            .append(Component.text("and ", gold))
            .append(Component.text("UNDERLINE", gold).decorate(TextDecoration.UNDERLINED))
            .append(Component.text("!", gold))

        print(ansiSerializer.serialize(newTest))
    }

    print("\n\nCompleted in $timed")

    print("\nOther tests not included in timings:\n\n")

    val show: Boolean = Random.nextBoolean()

    val conditionalText: MutableList<Component> = mutableListOf(
        Component.text("Conditional components test: much harder to do in traditional adventure. Sometimes some text will appear below.")
            .color(aqua)
    )

    if (show) conditionalText.add(
        MiniMessage.miniMessage().deserialize(
            "<gradient:red:gold:yellow:aqua:blue:light_purple>The text is showing</gradient>"
        )
    )

    print(Component.join(
        JoinConfiguration.newlines(),
        conditionalText
    ).ansi())
}