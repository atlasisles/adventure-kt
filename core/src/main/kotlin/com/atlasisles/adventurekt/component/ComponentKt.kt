@file:Suppress("unused")

package com.atlasisles.adventurekt.component

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.ansi.ColorLevel

interface ComponentKt {
    fun build(): Component
}

class TextComponentKt(var component: Component) : ComponentKt {
    override fun build(): Component {
        return this.component
    }
}

class RootComponentKt : ComponentKt {
    internal var miniMessage: MiniMessage = MiniMessage.miniMessage()
    internal var defaults: RootDefaults? = null
    internal var replaces: RootReplaces? = null
    internal var join: JoinConfiguration? = null
    val components: MutableList<ComponentKt> = mutableListOf()
    var theme: RootTheme = RootTheme()

    infix fun TextComponentKt.with(styleKt: StyleKt): TextComponentKt {
        this.component = this.component.style(styleKt.with(this.component.style()))
        return this
    }

    infix fun TextComponentKt.color(color: TextColor): TextComponentKt {
        this.component = this.component.color(color)
        return this
    }

    infix fun TextComponentKt.color(color: GradientColor): TextComponentKt {
        this.component = this@RootComponentKt.miniMessage.deserialize(
            "<gradient${color.buildString()}>${this@RootComponentKt.miniMessage.serialize(this.component)}</gradient>"
        )
        return this
    }

    infix fun TextComponentKt.with(decoration: TextDecoration): TextComponentKt {
        this.component = this.component.decorate(decoration)
        return this
    }

    infix fun TextComponentKt.font(font: Key): TextComponentKt {
        this.component = this.component.font(font)
        return this
    }

    infix fun TextComponentKt.insertion(content: String): TextComponentKt {
        this.component = this.component.insertion(content)
        return this
    }

    infix fun TextComponentKt.shadowColor(color: ShadowColor): TextComponentKt {
        this.component = this.component.shadowColor(color)
        return this
    }

    infix fun TextComponentKt.onHover(hoverEvent: HoverEvent<*>): TextComponentKt {
        this.component = this.component.hoverEvent(hoverEvent)
        return this
    }

    infix fun TextComponentKt.onClick(clickEvent: ClickEvent): TextComponentKt {
        this.component = this.component.clickEvent(clickEvent)
        return this
    }

    infix fun TextComponentKt.without(styleKt: StyleKt): TextComponentKt {
        this.component = this.component.style(styleKt.without(this.component.style()))
        return this
    }

    infix fun TextComponentKt.without(decoration: TextDecoration): TextComponentKt {
        this.component = this.component.decoration(decoration, false)
        return this
    }

    override fun build(): Component {
        if (join == null) {
            var root: TextComponent.Builder = Component.text()
            if (this.defaults != null) {
                root = root.style(this.defaults!!.style)
                if (this.defaults!!.hoverEvent != null) {
                    root = root.hoverEvent(this.defaults!!.hoverEvent)
                }
                if (this.defaults!!.clickEvent != null) {
                    root = root.clickEvent(this.defaults!!.clickEvent)
                }
            }
            for (component in this.components) {
                root = root.append(component.build())
            }
            return applyReplacement(root.build())
        } else {
            return Component.join(this.join!!, this.components.map { applyReplacement(it.build()) })
        }
    }

    private fun applyReplacement(component: Component): Component {
        var variable = component
        if (this.replaces != null) {
            var replacer = this.replaces
            while (replacer?.parent != null && !replacer.overridden) {
                for (replacementConfig in replacer.replaces) {
                    variable = variable.replaceText(replacementConfig)
                }
                replacer = replacer.parent
            }
        }
        return variable
    }

}

class RootDefaults {
    internal var style: Style = Style.empty()
    internal var hoverEvent: HoverEvent<*>? = null
    internal var clickEvent: ClickEvent? = null

    fun with(styleKt: StyleKt) {
        this.style = styleKt.with(this.style)
    }

    fun color(color: TextColor) {
        this.style = this.style.color(color)
    }

    fun with(decoration: TextDecoration) {
        this.style = this.style.decorate(decoration)
    }

    fun onHover(hoverEvent: HoverEvent<*>) {
        this.style = this.style.hoverEvent(hoverEvent)
    }

    fun onClick(clickEvent: ClickEvent) {
        this.style = this.style.clickEvent(clickEvent)
    }

    fun without(styleKt: StyleKt) {
        this.style = styleKt.without(this.style)
    }

    fun without(decoration: TextDecoration) {
        this.style = this.style.decoration(decoration, false)
    }
}

class RootReplaces(internal val parent: RootReplaces?) {
    internal var overridden: Boolean = false
    internal val replaces: MutableList<TextReplacementConfig> = mutableListOf()
}

inline fun Component(content: RootComponentKt.() -> Unit): Component {
    return RootComponentKt().apply(content).build()
}

fun RootComponentKt.defaults(content: RootDefaults.() -> Unit) {
    this.defaults = RootDefaults().apply(content)
}

fun RootComponentKt.theme(content: RootTheme.() -> Unit) {
    this.theme = RootTheme().apply(content)
}

fun RootComponentKt.provide(miniMessage: MiniMessage) {
    this.miniMessage = miniMessage
}

fun RootComponentKt.provide(builder: MiniMessage.Builder.() -> Unit) {
    this.miniMessage = MiniMessage.builder().apply(builder).build()
}

fun RootComponentKt.container(content: RootComponentKt.() -> Unit) {
    val component = RootComponentKt()
    component.miniMessage = this.miniMessage
    component.replaces = this.replaces
    this.components.add(component.apply(content))
}

fun RootComponentKt.join(content: JoinConfiguration.Builder.() -> Unit) {
    this.join = JoinConfiguration.builder().apply(content).build()
}

fun RootComponentKt.replacements(content: RootReplaces.() -> Unit) {
    if (this.replaces != null) {
        this.replaces!!.apply(content)
    } else {
        this.replaces = RootReplaces(null).apply(content)
    }
}

fun RootReplaces.replacement(content: TextReplacementConfig.Builder.() -> Unit) {
    this.replaces.add(TextReplacementConfig.builder().apply(content).build())
}

fun RootReplaces.override() {
    this.overridden = true
}

inline fun RootComponentKt.raw(content: () -> Component): TextComponentKt {
    val component = TextComponentKt(content.invoke())
    this.components.add(component)
    return component
}

fun RootComponentKt.raw(text: Component): TextComponentKt {
    val component = TextComponentKt(text)
    this.components.add(component)
    return component
}

inline fun RootComponentKt.text(text: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.text(text.invoke()))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: String): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: String, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Char): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Char, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Boolean): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Boolean, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Int): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Int, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Long): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Long, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Float): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Float, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Double): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.text(text: Double, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.translatable(key: String): TextComponentKt {
    val component = TextComponentKt(Component.translatable(key))
    this.components.add(component)
    return component
}

inline fun RootComponentKt.translatable(key: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.translatable(key.invoke()))
    this.components.add(component)
    return component
}

inline fun RootComponentKt.translatable(fallback: String?, key: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.translatable(key.invoke(), fallback))
    this.components.add(component)
    return component
}

inline fun RootComponentKt.keybind(keybind: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.keybind(keybind.invoke()))
    this.components.add(component)
    return component
}

fun RootComponentKt.miniMessage(rawText: () -> String): TextComponentKt {
    val component = TextComponentKt(this.miniMessage.deserialize(rawText.invoke()))
    this.components.add(component)
    return component
}

fun RootComponentKt.newline(): TextComponentKt {
    val component = TextComponentKt(Component.newline())
    this.components.add(component)
    return component
}

fun RootComponentKt.empty(): TextComponentKt {
    val component = TextComponentKt(Component.empty())
    this.components.add(component)
    return component
}

fun RootComponentKt.space(): TextComponentKt {
    val component = TextComponentKt(Component.space())
    this.components.add(component)
    return component
}

fun Component.json(): String {
    return GsonComponentSerializer.gson().serialize(this)
}

fun Component.legacy(): String {
    return LegacyComponentSerializer.legacySection().serialize(this)
}

fun Component.plain(): String {
    return PlainTextComponentSerializer.plainText().serialize(this)
}

fun Component.ansi(): String {
    return ANSIComponentSerializer.builder().colorLevel(ColorLevel.TRUE_COLOR).build().serialize(this)
}

fun Component.replace(string: String, text: String, literal: Boolean = false): Component {
    return this.replace(string, Component.text(text), literal)
}

fun Component.replace(string: String, component: Component, literal: Boolean = false): Component {
    val replaceConfig = TextReplacementConfig.builder()
        .apply {
            if (literal)
                this.matchLiteral(string)
            else
                this.match(string)
        }
        .replacement(component)
        .build()

    return this.replaceText(replaceConfig)
}

fun Component.replace(string: String, literal: Boolean = false, replacement: (Component) -> Component): Component {
    val replaceConfig = TextReplacementConfig.builder()
        .apply {
            if (literal)
                this.matchLiteral(string)
            else
                this.match(string)
        }
        .replacement { builder ->
            return@replacement replacement(builder.build())
        }
        .build()

    return this.replaceText(replaceConfig)
}

fun Component.replaceColor(targetColor: TextColor, newColor: TextColor): Component {
    val updatedComponent = if (this.color() == targetColor) {
        this.color(newColor)
    } else {
        this
    }

    return updatedComponent.children().fold(updatedComponent.children(emptyList())) { component, child ->
        component.append(child.replaceColor(targetColor, newColor))
    }
}

fun TextReplacementConfig.Builder.replace(content: RootComponentKt.(original: Component) -> Unit) {
    this.replacement { builder ->
        return@replacement RootComponentKt().apply {
            this.content(builder.build())
        }.build()
    }
}