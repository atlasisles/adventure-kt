package com.atlasisles.adventurekt

import com.atlasisles.adventurekt.component.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import kotlin.random.Random
import kotlin.time.measureTime

fun main() {
    val timed = measureTime {
        val text = Component {
            Text("Hello world") onHover showText {
                Text(10)
            } color hex("66ccff")
            Newline

            Text("With color red!") color red with bold without italic
            Newline

            Text("Click me!") onClick callback {
                it.send {
                    Text("You just clicked a text!")
                }
            } color red

            Empty // this can do nothing, but I added it lol

            Translatable("Fallback") { "enchantment.minecraft.protection" }  // should be "Protection" under English (US)
            Space
            MiniMessage { "<gray>MiniMessage!</gray>" }
            Space
            Keybind { "key.jump" } // should be "空格" under Simplified Chinese

            Newline

            Component {
                provide {
                    strict(false)
                    tags(TagResolver.standard())
                }

                Text("You can even 套娃 in the component")
            }

            Newline

            Component { // Components and content start with a capital
                join { // Component options start with a lowercase
                    separator(Component.text("!"))
                }

                Text("first")
                Text("second")
                Text("third")

                Raw(Component.text("Replace test 1").replace("aa", "bb", true))
                Raw(Component.text("Replace test 2").replace("aa") {
                    return@replace com.atlasisles.adventurekt.component.Component {
                        Raw(it) color red // Change original content to red
                    }
                })
            }

            Newline

            Text { "Lemme test this annoying gradient with a very very long string like this!" } color gradient(
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

            Text { " • " }
            Text { "This line should be grey" } color gray
            Newline
            Text { " • " }
            Text { "This line should be in a custom font (in-game and in json)" } font Key.key(
                "minecraft",
                "illageralt"
            )
            Newline
            Text { " • " }
            Text { "This line should be a rainbow gradient" } color gradient(
                hex("#E50000"),
                hex("FF8D00"),
                hex("#FFEE00"), hex("#28121"),
                hex("#004CFF"), hex("#770088")
            )
            Newline
            Text { " • " }
            Text { "This line should be both " } color gold
            Text { "ITALIC " } with italic color gold
            Text { "BOLD " } with bold color gold
            Text { "and " } color gold
            Text { "UNDERLINE" } with underline color gold
            Text { "!" } color gold
        }

        print(newTest.ansi())
    }

    print("\n\nCompleted in $timed")

    print("\nOther tests not included in timings:\n\n")

    val show: Boolean = Random.nextBoolean()

    val conditionalText = Component {
        Text { "Conditional components test: much harder to do in traditional adventure. Sometimes some text will appear below." } color aqua
        if (show) {
            Newline
            Text { "The text is showing!" } color gradient(red, gold, yellow, aqua, blue, lightPurple)
        }
    }

    print(conditionalText.ansi())

    /* Create reusable components */
    fun RootComponentKt.Alert(message: () -> String): TextComponentKt {
        val component = TextComponentKt(Component.empty()
            .append {
                Component.text("ALERT: ", red)
                    .decorate(TextDecoration.BOLD)
            }
            .append {
                Component.text(message.invoke(), red)
            }
        )
        this.components.add(component)
        return component
    }

    val usingComponents = Component {
        Newline
        Alert { "Alert message" }
        Newline
    }

    print(usingComponents.ansi())
}