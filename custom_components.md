# Create custom components

This document explains multiple ways to create custom components with adventure-kt to use in your project.

### Component types

**Simple custom component**

Using `buildCustomComponent` you can easily create a custom component. Here is an example:

```kotlin
/* Create custom component */
fun RootComponentKt.heart() = buildCustomComponent {
    text { "❤ " } color NamedTextColor.RED
}

/* Use it */
Component {
    heart()
    text { "example" }
}
```

**Custom component accepting other components**

You can make your custom component accept other components, here is an example:

```kotlin
/* Create component */
inline fun RootComponentKt.prefixed(content: RootComponentKt.() -> Unit) = buildCustomComponent {
    text { "[AdventureKt] " } color NamedTextColor.YELLOW
    
    this.apply(content) // add provided components
}

/* Use it */
Component {
    prefixed {
        text { "This message will be prefixed" }
    }
}
```

### How components are built

By default, custom components are separate from other items on the same level as the custom component, similar to wrapping the custom component contents in a `container { }`. See this example:

```kotlin
/* Normal custom component creation */
fun RootComponentKt.heart() = buildCustomComponent {
    defaults { color(NamedTextColor.RED) }
    text { "❤ " }
}

/* Example usage */
Component {
    heart()
    text { "example" }
}

/* RESULT, example how this component is built, would be the same as doing: */
Component {
    container {
        defaults { color(NamedTextColor.RED)}
        text { "❤ " } // the heart is red
    }
    text { "example" } // but this text stays white
}
```

If you were to use global modifiers like `join { }` or `defaults { }` in a custom component, it would only apply to the contents of the custom component.

But sometimes this behaviour is not what you want, and in this case, you can use `buildCustomRootComponent { }`. Here is an example:

```kotlin
/* Root custom component creation */
fun RootComponentKt.rootHeart() = buildCustomRootComponent {
    defaults { color(NamedTextColor.RED) }
    text { "❤ " }
}

/* Example usage */
Component {
    rootHeart()
    text { "example" }
}

/* RESULT */
Component {
    defaults { color(NamedTextColor.RED) }
    text { "❤ " } // the heart is red as expected
    
    text { "example" } // this time, the text is also red
}
```
The normal behaviour is probably what's best the majority of the time, although it does include an extra component in the resulting component, the isolation is expected and useful.