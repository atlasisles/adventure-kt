package com.atlasisles.adventurekt

import com.atlasisles.adventurekt.component.ansi
import com.atlasisles.adventurekt.component.hex
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component.*
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
import kotlin.time.Duration
import kotlin.time.measureTime

fun main() {
    fun test(): Duration {
        val timed = measureTime {
            val text = text()
                .append(text("Hello world").color(hex("66ccff")).hoverEvent(HoverEvent.showText {
                    text(10)
                }))
                .appendNewline()
                .append(
                    text("With color red!", red).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)
                )
                .appendNewline()
                .append(text("Click me!", red).clickEvent(ClickEvent.callback {
                    it.sendMessage(text("You just clicked a text!"))
                }))
                .append(empty())
                .append(translatable("minecraft.enchantment.protection"))
                .appendSpace()
                .append(
                    MiniMessage.builder().strict(true).tags(TagResolver.standard()).build()
                        .deserialize("<grey>MiniMessage!</grey>")
                )
                .appendSpace()
                .append(keybind("key.keyboard.space"))
                .appendNewline()
                .append(
                    MiniMessage.builder().strict(true).tags(TagResolver.standard())
                        .build().deserialize("You can even 套娃 in the component")
                )
                .appendNewline()
                .append(
                    join(
                        JoinConfiguration.separator(text("!")), listOf(
                            text("first"),
                            text("second"),
                            text("third"),

                            text("Replace test 1").replaceText(
                                TextReplacementConfig.builder()
                                    .matchLiteral("aa")
                                    .replacement("bb")
                                    .build()
                            ),
                            text("Replace test 2").replaceText(
                                TextReplacementConfig.builder().match("aa").replacement { builder ->
                                    return@replacement text("aa", red)
                                }.build()
                            )
                        )
                    )
                )
                .appendNewline()
                .append(
                    MiniMessage.builder().strict(true).tags(TagResolver.standard()).build()
                        .deserialize("<gradient:red:yellow:blue>Lemme test this annoying gradient with a very very long string like this!</gradient>")
                )
                .build()

            val ansiSerializer = ANSIComponentSerializer.builder().colorLevel(ColorLevel.TRUE_COLOR).build()
            print(ansiSerializer.serialize(text))

            val newTest = text()
                .append(text(" • ", white))
                .append(text("This line should be grey", gray))
                .appendNewline()
                .append(text(" • ", white))
                .append(
                    text("This line should be in a custom font (in-game and in json)", white).font(
                        Key.key(
                            "minecraft", "illageralt"
                        )
                    )
                )
                .appendNewline()
                .append(text(" • ", white))
                .append(
                    MiniMessage.miniMessage().deserialize(
                        "<gradient:#E50000:#FF8D00:#FFEE00:#28121:#004CFF:#770088>This line should be in a rainbow gradient</gradient>"
                    )
                )
                .appendNewline()
                .append(text(" • ", white))
                .append(text("This line should be both ", gold))
                .append(text("ITALIC ", gold).decorate(TextDecoration.ITALIC))
                .append(text("BOLD ", gold).decorate(TextDecoration.BOLD))
                .append(text("and ", gold))
                .append(text("UNDERLINE", gold).decorate(TextDecoration.UNDERLINED))
                .append(text("!", gold))
                .append(newline())
                .build()

            print(ansiSerializer.serialize(newTest))

            /* Conditional text */
            val show: Boolean = Random.nextBoolean()

            val conditionalText = text()
                .content("\nConditional components test: much harder to do in traditional adventure. Sometimes some text will appear below.")
                .color(aqua)

            if (show) {
                conditionalText.append(newline())
                conditionalText.append(
                    MiniMessage.miniMessage().deserialize(
                        "<gradient:red:gold:yellow:aqua:blue:light_purple>The text is showing</gradient>"
                    )
                )
            }

            print(join(
                JoinConfiguration.newlines(),
                conditionalText
            ).ansi())
        }

        print("\n\n-- Completed in $timed --\n\n")
        return timed
    }

    val results: MutableList<Duration> = mutableListOf()
    repeat(50) {
        results.add(test())
    }

    print("\n\n------------\n\nRan test 50 times:\n\n")
    print("Average time: ${results.map { it.inWholeMicroseconds }.average() / 1000}ms\n")
    print("Total time: ${results.sumOf { it.inWholeMicroseconds } / 1000}ms\n")
}