package com.atlasisles.adventurekt.component

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
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

class TextComponentKt(internal var component: Component) : ComponentKt {
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
            var root: Component = Component.empty()
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
            return applyReplacement(root)
        } else {
            return Component.join(this.join!!, this.components.map { applyReplacement(it.build()) })
        }
    }

    private fun applyReplacement(component: Component): Component {
        var variable = component
        if (this.replaces != null) {
            var replacer = this.replaces
            while (replacer?.parent != null && !replacer.overriden) {
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

class RootReplaces(
    internal val parent: RootReplaces?,
) {
    internal var overriden: Boolean = false
    internal val replaces: MutableList<TextReplacementConfig> = mutableListOf()
}

inline fun Component(content: RootComponentKt.() -> Unit): Component {
    return RootComponentKt().apply(content).build()
}

fun RootComponentKt.defaults(content: RootDefaults.() -> Unit) {
    this.defaults = RootDefaults().apply(content)
}

fun RootComponentKt.provide(miniMessage: MiniMessage) {
    this.miniMessage = miniMessage
}

fun RootComponentKt.provide(builder: MiniMessage.Builder.() -> Unit) {
    this.miniMessage = MiniMessage.builder().apply(builder).build()
}

fun RootComponentKt.Component(content: RootComponentKt.() -> Unit) {
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

fun RootReplaces.Replacement(content: TextReplacementConfig.Builder.() -> Unit) {
    this.replaces.add(TextReplacementConfig.builder().apply(content).build())
}

val RootReplaces.Override: Unit
    get() {
        this.overriden = true
    }

fun RootComponentKt.Raw(text: Component): TextComponentKt {
    val component = TextComponentKt(text)
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.text(text.invoke()))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: () -> String, color: () -> TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text.invoke(), color.invoke()))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: String): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: String, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Char): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Char, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Boolean): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Boolean, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Int): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Int, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Long): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Long, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Float): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Float, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Double): TextComponentKt {
    val component = TextComponentKt(Component.text(text))
    this.components.add(component)
    return component
}

fun RootComponentKt.Text(text: Double, color: TextColor): TextComponentKt {
    val component = TextComponentKt(Component.text(text, color))
    this.components.add(component)
    return component
}

fun RootComponentKt.Translatable(key: String): TextComponentKt {
    val component = TextComponentKt(Component.translatable(key))
    this.components.add(component)
    return component
}

fun RootComponentKt.Translatable(key: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.translatable(key.invoke()))
    this.components.add(component)
    return component
}

fun RootComponentKt.Translatable(fallback: String?, key: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.translatable(key.invoke(), fallback))
    this.components.add(component)
    return component
}

fun RootComponentKt.Keybind(keybind: () -> String): TextComponentKt {
    val component = TextComponentKt(Component.keybind(keybind.invoke()))
    this.components.add(component)
    return component
}

fun RootComponentKt.MiniMessage(rawText: () -> String): TextComponentKt {
    val component = TextComponentKt(this.miniMessage.deserialize(rawText.invoke()))
    this.components.add(component)
    return component
}

val RootComponentKt.Newline: TextComponentKt
    get() {
        val component = TextComponentKt(Component.newline())
        this.components.add(component)
        return component
    }

val RootComponentKt.Empty: TextComponentKt
    get() {
    val component = TextComponentKt(Component.empty())
    this.components.add(component)
    return component
}

val RootComponentKt.Space: TextComponentKt
    get() {
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