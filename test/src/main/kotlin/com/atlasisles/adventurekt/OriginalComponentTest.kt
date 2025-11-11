package com.atlasisles.adventurekt

import com.atlasisles.adventurekt.component.ansi
import com.atlasisles.adventurekt.component.hex
import com.atlasisles.adventurekt.component.json
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component.*
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.`object`.ObjectContents
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer
import net.kyori.ansi.ColorLevel
import java.util.*
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
            println(ansiSerializer.serialize(text))

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

            println(ansiSerializer.serialize(newTest))

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

            println(join(
                JoinConfiguration.newlines(),
                conditionalText
            ).ansi())

            val objects = text()
                .append(`object`().contents(ObjectContents.playerHead().id(UUID.randomUUID()).hat(true).build()))
                .append(`object`().contents(ObjectContents.playerHead().name("Steve").hat(false).build()))
                .append(`object`().contents(ObjectContents.playerHead().profileProperty(PlayerHeadObjectContents.property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTc2MjE1MTMwOTcyNSwKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGV2ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82MGE1YmQwMTZiM2M5YTFiOTI3MmU0OTI5ZTMwODI3YTY3YmU0ZWJiMjE5MDE3YWRiYmM0YTRkMjJlYmQ1YjEiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk1M2NhYzhiNzc5ZmU0MTM4M2U2NzVlZTJiODYwNzFhNzE2NThmMjE4MGY1NmZiY2U4YWEzMTVlYTcwZTJlZDYiCiAgICB9CiAgfQp9", "SugNJBxcOukFxb2Ab1qGukuG2F7BhYUCnBWi6DKBcWVnVn4COdpAwWHMdOLx1BXNy9El3eoDue1FbZCSeVkRWuLjV9YEjG/Xh7FeEq2vDzKlEQYWSyNEi5q+Y1Cd9RYpmIfBx8ZNUFZu0xSMqi7gXfHudlexfZlq16x+Df/Ix0688JSok6NT5sk0+8/bSCb8hdVDvh+GANiADJVexBzp43Iw/kIvyPsaomDIq0Qw4S3oWl5KxkcDNqxwfd2PnWgmeRWazy7GcYJaFdCN9RvNtHEmcpwjmlmSMG9lH1J3F6X4iLu4wWAgZkcmIUnxBe/577DBqjfxf5WV/sfb3f0PjnMgJLFpmC2SLJSEQD1bg1DIkM3pXMjIRhLr7BkuW0WddQ7qsL6zrr63NN4EYPZroIGYOKxCeXO1HzyrPpZBQ2wCCVKoG9DLTCoSN8W78HRUEU0QOIMoX8GWgNHcNHvwQ6uWcmDbPEDnYkmaPI+bBD83fCdMf5y9tK0AwK3gHfFwTB3vcGJWXl4y9px4F1Up0Nkg/e+8ORbAJSBXgBIFtrRf4LmlXM4BWZKqM8+PfWg/h2t/ZH259OCdI6vCxrw7eopUb3JzaFV99BMRAgcf7FG6jSQx1l4UwsDY0Jb2/gU8MMUo2R0tfVVZjhEIOeJTEkiQLgZ0U2T77xAWER2yuQE=")).build()))
                .append(`object`().contents(ObjectContents.sprite(Key.key("item/diamond")))).append(`object`().contents(ObjectContents.sprite(Key.key("item/diamond"))))
                .append(`object`().contents(ObjectContents.sprite(Key.key("chests"), Key.key("entity/chest/copper"))))
                .build()

            println(objects.json())
        }

        println("\n-- Completed in $timed --\n\n")
        return timed
    }

    val results: MutableList<Duration> = mutableListOf()
    repeat(50) {
        results.add(test())
    }

    println("\n------------\n\nRan test 50 times:\n\n")
    println("Average time: ${results.map { it.inWholeMicroseconds }.average() / 1000}ms\n")
    println("Total time: ${results.sumOf { it.inWholeMicroseconds } / 1000}ms\n")
}