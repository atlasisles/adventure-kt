package com.atlasisles.adventurekt.component

inline fun RootComponentKt.buildCustomComponent(content: RootComponentKt.() -> Unit): TextComponentKt {
    val component = TextComponentKt(RootComponentKt().apply(content).build())
    this.components.add(component)
    return component
}