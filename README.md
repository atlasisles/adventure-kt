# Adventure Kotlin Extensions

Original project: [Pluto-Studio/adventure-kt](https://github.com/Pluto-Studio/adventure-kt)

> [!IMPORTANT]
> This project was made for use in Atlas Isles projects, but is welcome to be used elsewhere.
> Some features may be missing, use this at your own risk!

Using Kotlin's extensions and infix functions, this project extends the [Adventure UI library](https://github.com/KyoriPowered/adventure)
with a more Kotlin-idiomatic (similar to Compose) syntax.

- Build components with multiple objects without appending
- Style with infix function (like 'color red with underline')
- Conditionally show objects in components
- Extend with custom components for reusability

---

## 📦 Packages

Currently only available on Maven snapshots:
```kotlin
/*  Add snapshot repository  */
maven("https://central.sonatype.com/repository/maven-snapshots/") {
    name = "snapshots"
}

/*  Add dependency  */
implementation("com.atlasisles:adventure-kt:2.0-SNAPSHOT")
/*  or whilst in snapshots, get the latest version:  */
implementation("com.atlasisles:adventure-kt:2.+")
```

## Examples

**Create components using a more Kotlin-like syntax:**

```kotlin
/*  With   */

Component {
    text { "Hello world!" } color NamedTextColor.RED with bold // infix syntax for styles
    text { "Hello green world!" } color NamedTextColor.GREEN   // multiple lines without .append
    
    // Optional parametrised syntax
    text("Hello world!") color NamedTextColor.RED with bold
}

/* Without */

Component.text()
    .append(Component.text("Hello world!").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
    .append(Component.text("Hello green world!").color(NamedTextColor.GREEN))
    .build()
```

**Easily add logic to your components:**

```kotlin
val show = Random.nextBoolean()

/*  With   */

Component {
    text { "This text will always show" } with bold
    if (show) 
        text { "This text will sometimes show" } with italic
}

/* Without */

val conditional = Component.text()
    .content("This text will always show")

if (show) conditional.append(
    Component.text("This text will sometimes show").decorate(TextDecoration.ITALIC)
)

Component.join(JoinConfiguration.newlines(), conditionalText.build())
```

**Click & hover actions and other text types:**

```kotlin
Component {
    text { "Hover or click me" } with underline onHover showText {
        text { "To read me" }
    } onClick openUrl("https://google.com")
    
    translatable { "accessibility.onboarding.screen.title" } // Welcome to Minecraft! [...]
    keybind { "key.inventory" } // E
    miniMessage { "<rainbow>Shiny!!" } // Shiny!!
    raw { Component.text("Original syntax").color(NamedTextColor.BLUE) } // Original syntax
    
    container { /* Contain components and styles */
        defaults { color(NamedTextColor.RED) }
        join { separator { Component.newline() } }
        
        text { "This is red!" } // This is red!
        text { "Example on a new line" } // Example on a new line
    }
}
```

**Create custom components for reusability:**

```kotlin
/*  Create the component  */
fun RootComponentKt.alert(text: () -> String) = buildCustomComponent {
    Text { "[ALERT] " } color NamedTextColor.RED with bold
    Text { text.invoke() } color NamedTextColor.RED
}

/*  Use it anywhere  */
Component {
    alert { "Message" } // [ALERT] Message
}
```

More examples and documentation on custom components can be [found here](https://github.com/atlasisles/adventure-kt/tree/master/custom_components.md).

**Extensions for audiences:**

```kotlin
player.send {
    text { sender.name } color NamedTextColor.YELLOW
    text { " invited you to a party" } color NamedTextColor.GOLD
}
```

See more examples specifically for [custom components here](https://github.com/atlasisles/adventure-kt/tree/master/custom_components.md), and others in the [test folder](https://github.com/atlasisles/adventure-kt/tree/master/test/src/main/kotlin/com/atlasisles/adventurekt).

## 🌈 Colours

Whilst you can use `NamedTextColor` with these components, it may be best to add your own colour variables.
These aren't included by default as Atlas Isles has a custom colour palette.

```kotlin
text { "Hello, world!" } color red    // instead of `color NamedTextColor.RED`
```

Here's a file containing variables for all base Minecraft colours which you can add to your project: [BaseColors.kt](https://github.com/atlasisles/adventure-kt/tree/master/test/src/main/kotlin/com/atlasisles/adventurekt/BaseColors.kt)

There are also functions for creating colours from other values, like `rgb(r:Int, g:Int, b:Int)`, `hex(String)` and `rgb(Int)`

### Gradients

Create gradients programmatically using the gradient function:

```kotlin
text { "Rainbow!" } color gradient(red, gold, yellow, aqua, blue, lightPurple)
```

## 🛫 Performance

Whilst this library may have overhead on top of adventure, under the hood it uses the more performant `TextComponent.Builder` instead of `Component`, so the performance impact on your project may vary.

You can test this for yourself in the test folder, running both `ComponentKtTest` and `OriginalComponentTest`.