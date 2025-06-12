package com.atlasisles.adventurekt.component

/**
 * Build a custom component.
 *
 * The contents in this component will be wrapped in a `container { }` before being applied.
 * If this is not intended, use `buildCustomRootComponent { }`
 *
 * @see buildCustomRootComponent
 * */
inline fun RootComponentKt.buildCustomComponent(content: RootComponentKt.() -> Unit): TextComponentKt {
    val component = TextComponentKt(RootComponentKt().apply(content).build())
    this.components.add(component)
    return component
}

/**
 * Build a custom root component.
 *
 * The contents of this will be applied on the same level as the custom component itself.
 * For the default behaviour, use `buildCustomComponent { }`
 *
 * More examples and docs in /custom_components.md
 *
 * @see buildCustomComponent
 * */
inline fun RootComponentKt.buildCustomRootComponent(content: RootComponentKt.() -> Unit) {
    this.apply(content)
}