package com.atlasisles.adventurekt

import com.atlasisles.adventurekt.component.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents
import java.util.UUID
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.measureTime

fun main() {
    fun test(): Duration {
        val timed = measureTime {
            val text = Component {
                text { "Hello world" } onHover showText {
                    text(10)
                } color hex("66ccff")
                newline()

                text { "With color red!" } color red with bold without italic
                newline()

                text { "Click me!" } onClick callback {
                    it.send {
                        text("You just clicked a text!")
                    }
                } color red

                empty() // this can do nothing, but I added it lol

                translatable("Fallback") { "enchantment.minecraft.protection" }  // should be "Protection" for English
                space()
                miniMessage { "<gray>MiniMessage!</gray>" }
                space()
                keybind { "key.jump" } // should be "空格" under Simplified Chinese

                newline()

                Component {
                    provide {
                        strict(false)
                        tags(TagResolver.standard())
                    }

                    text { "You can even 套娃 in the component" }
                }

                newline()

                Component { // Components and content start with a capital
                    join { // Component options start with a lowercase
                        separator(Component.text("!"))
                    }

                    text("first")
                    text("second")
                    text("third")

                    raw { Component.text("Replace test 1")
                            .replace("aa", "bb", true) }

                    raw(Component.text("Replace test 2").replace("aa") {
                        return@replace Component {
                            raw(it) color red // Change original content to red
                        }
                    })
                }

                newline()

                text { "Lemme test this annoying gradient with a very very long string like this!" } color gradient(
                    red,
                    yellow,
                    blue
                )
            }

            println(text.ansi())

            val newTest = Component {
                defaults {
                    color(white)
                }

                text { " • " }
                text { "This line should be grey" } color gray
                newline()
                text { " • " }
                text { "This line should be in a custom font (in-game and in json)" } font Key.key(
                    "minecraft",
                    "illageralt"
                )
                newline()
                text { " • " }
                text { "This line should be a rainbow gradient" } color gradient(
                    hex("#E50000"),
                    hex("FF8D00"),
                    hex("#FFEE00"), hex("#28121"),
                    hex("#004CFF"), hex("#770088")
                )
                newline()
                text { " • " }
                text { "This line should be both " } color gold
                text { "ITALIC " } with italic color gold
                text { "BOLD " } with bold color gold
                text { "and " } color gold
                text { "UNDERLINED" } with underline color gold
                text { "!" } color gold
                newline()
            }

            println(newTest.ansi())

            /* Conditional text */
            val show: Boolean = Random.nextBoolean()

            val conditionalText = Component {
                newline()
                text { "Conditional components test: much harder to do in traditional adventure. Sometimes some text will appear below." } color aqua
                if (show) {
                    newline()
                    text { "The text is showing!" } color gradient(red, gold, yellow, aqua, blue, lightPurple)
                }
            }

            println(conditionalText.ansi())

            val objcts = Component {
                playerHead(UUID.randomUUID(), true)
                playerHead("Steve", false) shadowColor ShadowColor.none()
                playerHead(PlayerHeadObjectContents.property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTc2MjE1MTMwOTcyNSwKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGV2ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82MGE1YmQwMTZiM2M5YTFiOTI3MmU0OTI5ZTMwODI3YTY3YmU0ZWJiMjE5MDE3YWRiYmM0YTRkMjJlYmQ1YjEiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk1M2NhYzhiNzc5ZmU0MTM4M2U2NzVlZTJiODYwNzFhNzE2NThmMjE4MGY1NmZiY2U4YWEzMTVlYTcwZTJlZDYiCiAgICB9CiAgfQp9", "SugNJBxcOukFxb2Ab1qGukuG2F7BhYUCnBWi6DKBcWVnVn4COdpAwWHMdOLx1BXNy9El3eoDue1FbZCSeVkRWuLjV9YEjG/Xh7FeEq2vDzKlEQYWSyNEi5q+Y1Cd9RYpmIfBx8ZNUFZu0xSMqi7gXfHudlexfZlq16x+Df/Ix0688JSok6NT5sk0+8/bSCb8hdVDvh+GANiADJVexBzp43Iw/kIvyPsaomDIq0Qw4S3oWl5KxkcDNqxwfd2PnWgmeRWazy7GcYJaFdCN9RvNtHEmcpwjmlmSMG9lH1J3F6X4iLu4wWAgZkcmIUnxBe/577DBqjfxf5WV/sfb3f0PjnMgJLFpmC2SLJSEQD1bg1DIkM3pXMjIRhLr7BkuW0WddQ7qsL6zrr63NN4EYPZroIGYOKxCeXO1HzyrPpZBQ2wCCVKoG9DLTCoSN8W78HRUEU0QOIMoX8GWgNHcNHvwQ6uWcmDbPEDnYkmaPI+bBD83fCdMf5y9tK0AwK3gHfFwTB3vcGJWXl4y9px4F1Up0Nkg/e+8ORbAJSBXgBIFtrRf4LmlXM4BWZKqM8+PfWg/h2t/ZH259OCdI6vCxrw7eopUb3JzaFV99BMRAgcf7FG6jSQx1l4UwsDY0Jb2/gU8MMUo2R0tfVVZjhEIOeJTEkiQLgZ0U2T77xAWER2yuQE="))
                sprite(Key.key("item/diamond"))
                sprite(Key.key("chests"), Key.key("entity/chest/copper"))
            }

            println(objcts.json())
        }

        println("\n-- Completed in $timed --\n\n")

        return timed
    }

    val results: MutableList<Duration> = mutableListOf()
    repeat(50) {
        results.add(test())
    }

    println("------------\n\nRan test 50 times:\n\n")
    println("Average time: ${results.map { it.inWholeMicroseconds }.average() / 1000}ms\n")
    println("Total time: ${results.sumOf { it.inWholeMicroseconds } / 1000}ms\n")
    println("------------\n\nOther tests not included in timings:\n")

    /* Create reusable components */
    fun RootComponentKt.alert(message: () -> String) = buildCustomComponent {
        text { "ALERT: " } color red with bold
        text { message.invoke() } color red
    }

    val usingComponents = Component {
        newline()
        alert { "Alert message" }
        newline()
    }

    println(usingComponents.json())

    /* Root component */
    fun RootComponentKt.rootAlert(message: () -> String) = buildCustomRootComponent {
        text { "ALERT: " } color red with bold
        text { message.invoke() } color red
    }

    val rootComponents = Component {
        newline()
        rootAlert { "Alert message at root" }
    }

    println(rootComponents.json())

    /* Theme test */
    fun RootComponentKt.rankPrefix(rank: String) = buildCustomRootComponent { // themes work best on root components
        when (rank) {
            "DEFAULT" -> {
                this.theme {
                    primaryColour = white
                    secondaryColour = gray
                }
                text { "" } color theme.primaryColour
            }
            "VIP" -> {
                this.theme {
                    primaryColour = green
                    secondaryColour = gray
                }
                text { "[VIP] " } color theme.primaryColour
            }
            "MVP" -> {
                this.theme {
                    primaryColour = gold
                    secondaryColour = white
                }
                text { "[MVP] " } color theme.primaryColour
            }
        }
    }

    val themedComponents = Component {
        newline()
        join { separator { Component.newline() } }

        container {
            rankPrefix("DEFAULT")
            text { "<Alex> " } color theme.primaryColour
            text { "example default rank" } color theme.secondaryColour
        }
        container {
            rankPrefix("VIP")
            text { "<Zuri> " } color theme.primaryColour
            text { "example vip rank" } color theme.secondaryColour
        }
        container {
            rankPrefix("MVP")
            text { "<Sunny> " } color theme.primaryColour
            text { "example mvp rank" } color theme.secondaryColour
        }
    }

    println(themedComponents.ansi())

    val insertionComponents = Component {
        text { "Shift click this to insert a message into your chat box" } insertion "Hello, world!"
    }

    println(insertionComponents.json())

    val customClickEvent = Component {
        text { "Click me to send a packet to the server!" } onClick custom(Key.key("testing"), "1,2,3")
    }

    println(customClickEvent.json())

    val shadowColor = Component {
        text { "Shadow color from adventure" } shadowColor ShadowColor.shadowColor(NamedTextColor.RED, 255)
    }

    println(shadowColor.json())
}