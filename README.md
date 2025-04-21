# Adventure Kotlin Extensions

Original project: [Pluto-Studio/adventure-kt](https://github.com/Pluto-Studio/adventure-kt)

> [!WARNING]
>
> This project was made for use in Atlas Isles projects, but is welcome to be used elsewhere.
> Some features may be missing, use this at your own risk!

Using Kotlin's extensions and infix functions, this project extends the [adventure UI library](https://github.com/KyoriPowered/adventure)
with a more Kotlin-like (similar to Jetpack Compose) syntax.

- Build components with multiple objects without append
- Style with infix function (like 'color red with underline')
- Conditionally show objects in components
- Extend with custom components for reusability

## 📦 Packages

Get from Maven Central:
```kotlin
implementation("com.atlasisles:adventure-kt:0.1.0")
```

## 🍿 Examples

Create components using a more Kotlin-like syntax:

```kotlin
/*  With   */

Component {
    Text { "Hello world!" } color NamedTextColor.RED with bold // infix syntax for styles
    Text { "Hello green world!" } color NamedTextColor.GREEN   // multiple lines without .append
}

/* Without */

Component.empty() // append to empty to not keep style
    .append(Component.text("Hello world!").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
    .append(Component.text("Hello green world!").color(NamedTextColor.GREEN))
```

Easily add logic to your components:

```kotlin
val show = Random.nextBoolean()

/*  With   */

Component {
    Text { "This text will always show" } with bold
    if (show)
        Text { "This text will sometimes show" } with italic
}

/* Without */

val conditional = mutableListOf(Component.text("This text will always show")
    .decorate(TextDecoration.BOLD))

if (show) conditional.add(Component.text("This text will sometimes show")
    .decorate(TextDecoration.ITALIC))

Component.join(JoinConfiguration.newlines(), conditionalText)
```

Click & hover actions and other text types:

```kotlin
Component {
    Text { "Hover or click me" } with underline onHover showText {
        Text { "To read me" }
    } onClick openUrl("https://google.com")
    
    Translatable { "accessibility.onboarding.screen.title" } // Welcome to Minecraft! [...]
    Keybind { "key.inventory" } // E
    MiniMessage { "<rainbow>Shiny!!" } // Shiny!!
    Raw { Component.text("Original syntax").color(NamedTextColor.BLUE) } // Original syntax
}
```

Extensions for audiences:

```kotlin
player.send {
    Text { "${sender.name} invited you to a party" } color NamedTextColor.GOLD
}
```

See more examples with a comparison to the original components in the [test folder](https://github.com/atlasisles/adventure-kt/blob/main/test/src/main/kotlin/ink/pmc/advkt).

## 🌈 Colours

Whilst you can use `NamedTextColor` with these components, it may be best to add your own colour variables.
These aren't included by default as Atlas Isles has a custom colour palette.

```kotlin
Text { "Hello, world!" } color red    // instead of `color NamedTextColor.RED`
```

Here's a file containing variables for all base Minecraft colours which you can add to your project: [BaseColors.kt](https://github.com/atlasisles/adventure-kt/blob/main/test/src/main/kotlin/ink/pmc/advkt/BaseColors.kt)

There are also functions for creating colours from other values, like `rgb(r:Int, g:Int, b:Int)`, `hex(String)` and `rgb(Int)`

### Gradients

Create gradients programmatically using the gradient function:

```kotlin
Text { "Rainbow!" } color gradient(red, gold, yellow, aqua, blue, lightPurple)
```

## 🛫 Performance

From my small amount of testing comparing the same components to adventure, they seem to have similar performance.
You can test this for yourself in the test folder, running both `ComponentKtTest` and `OriginalComponentTest`, but it's worth it to no longer be stuck in .append hell. 