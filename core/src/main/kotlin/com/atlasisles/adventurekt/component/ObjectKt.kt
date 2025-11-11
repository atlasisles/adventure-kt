package com.atlasisles.adventurekt.component

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.`object`.ObjectContents
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents
import java.util.UUID

fun RootComponentKt.playerHead(id: UUID, hat: Boolean = true): TextComponentKt {
    val component = TextComponentKt(Component.`object`(ObjectContents.playerHead().id(id).hat(hat).build()))
    this.components.add(component)
    return component
}

fun RootComponentKt.playerHead(name: String, hat: Boolean = true): TextComponentKt {
    val component = TextComponentKt(Component.`object`(ObjectContents.playerHead().name(name).hat(hat).build()))
    this.components.add(component)
    return component
}

fun RootComponentKt.playerHead(profile: PlayerHeadObjectContents.ProfileProperty, hat: Boolean = true): TextComponentKt {
    val component = TextComponentKt(Component.`object`(ObjectContents.playerHead().profileProperty(profile).hat(hat).build()))
    this.components.add(component)
    return component
}

fun RootComponentKt.playerHead(source: PlayerHeadObjectContents.SkinSource, hat: Boolean = true): TextComponentKt {
    val component = TextComponentKt(Component.`object`(ObjectContents.playerHead().skin(source).hat(hat).build()))
    this.components.add(component)
    return component
}

fun RootComponentKt.sprite(atlas: Key, sprite: Key): TextComponentKt {
    val component = TextComponentKt(Component.`object`(ObjectContents.sprite(atlas, sprite)))
    this.components.add(component)
    return component
}

fun RootComponentKt.sprite(sprite: Key): TextComponentKt {
    val component = TextComponentKt(Component.`object`(ObjectContents.sprite(sprite)))
    this.components.add(component)
    return component
}