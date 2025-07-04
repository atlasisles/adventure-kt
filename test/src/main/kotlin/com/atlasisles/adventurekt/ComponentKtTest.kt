package com.atlasisles.adventurekt

import com.atlasisles.adventurekt.component.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
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

            print(newTest.ansi())

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

            print(conditionalText.ansi())
        }

        print("\n\n-- Completed in $timed --\n\n")

        return timed
    }

    val results: MutableList<Duration> = mutableListOf()
    repeat(50) {
        results.add(test())
    }

    print("\n------------\n\nRan test 50 times:\n\n")
    print("Average time: ${results.map { it.inWholeMicroseconds }.average() / 1000}ms\n")
    print("Total time: ${results.sumOf { it.inWholeMicroseconds } / 1000}ms\n")
    print("\n------------\n\nOther tests not included in timings:\n")

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

    print(usingComponents.json())
    print("\n")

    /* Root component */
    fun RootComponentKt.rootAlert(message: () -> String) = buildCustomRootComponent {
        text { "ALERT: " } color red with bold
        text { message.invoke() } color red
    }

    val rootComponents = Component {
        newline()
        rootAlert { "Alert message at root" }
        newline()
    }

    print(rootComponents.json())

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