# Android Spannable in kotlin

> Kotlin wrappers around SpannableStringBuilder. Inspired by binaryfork's [Spanny](https://github.com/binaryfork/Spanny).

[SpannableStringBuilder](https://developer.android.com/reference/android/text/SpannableStringBuilder.html)
API is attractive, it's a nice way to styling text without split your text into multiple views, 
but the usage of the raw API is awful.

Let's say a simple example: Spans **are ~~hard~~**?

- The raw `SpannableStringBuilder` API:

```kotlin
val builder = SpannableStringBuilder()
builder.append("Spans ")
var str = "are "
builder.append(str)
builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - str.length, builder.length,
    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
str = "hard"
builder.append(str)
builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - str.length, builder.length,
    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
builder.setSpan(StrikethroughSpan(), builder.length - str.length, builder.length,
    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
builder.append("?")
```

- The [Spanny](https://github.com/binaryfork/Spanny) helper API:
```kotlin
val spanny = Spanny("Spans ")
    .append("are ", StyleSpan(Typeface.BOLD))
    .append("hard", StyleSpan(Typeface.BOLD), StrikethroughSpan())
    .append("?")
```

That is cool in Java, but I was using kotlin, and it's not cool enough, it's verbose and limited. Kotlin allows to [*type-check* builders](https://kotlinlang.org/docs/reference/type-safe-builders.html), 
which is the best way to build spans, so I make it happen:

```kotlin
val text = span {
  +"Spans "
  span {
    textStyle = "bold"
    +"are "
    span("hard") {
      textDecorationLine = "line-through"
    }
  }
  +"?"
}
```

![sample](/screenshots/sample.png)

### Download

You can grab it via Gradle:

```
implementation 'me.gujun.android:span:1.2'
```

### Usage

```kotlin
val text = span {
  textColor = Color.BLACK
  backgroundColor = Color.WHITE
  textSize = dp(13)
  fontFamily = "monospace"
  typeface = getFont(R.font.pacifico)
  textStyle = "bold"
  alignment = "normal"
  textDecorationLine = "underline"
  lineSpacing = dp(3)
  verticalPadding = dp(5)
  paddingTop = dp(5)
  paddingBottom = dp(5)
  onClick = {
  }
  
  span("Text") {
    // text = "Text"
  }
  
  link("http://google.com", "Text")
  
  quote(Color.RED, "Text")
  
  subscript("Text")
  
  superscript("Text")
  
  image(getDrawable(R.drawable.ic_fun))
  
  // Add custom span
  style(TextAppearanceSpan(context, R.style.TextAppearance))
}
```

**Caution: Avoid nested text in spans**

The following code will cause *RuntimeException: Can't nest "World" in spans*. 
This is because you try to nest span with text "World" in its parent span with text "Hello", 
it does not make any sense.

```kotlin
val text = span {
  text = "Hello"
  span("World")
}
```

#### Overwrite styles

Styles can be overwrite in nested spans, inner span overwrite the outer span.

```kotlin
val text = span {
  textColor = Color.BLUE
  textSize = dp(20)
  +"Simple text"
  +"\n"
  span("Overwrite foreground") {
    textColor = Color.RED
  }
  +"\n"
  span("Overwrite text size") {
    textSize = dp(10)
  }
}
```

#### Global styles

You can change global styles by add spans to the companion object `Span.globalStyles`.

For example, if your project using a custom typeface, you can construct a `CustomTypefaceSpan` 
and add it to the `Span.globalStyles` then you will not specified typeface each time you using spans. 


```kotlin
Span.globalStyles.add(AbsoluteSizeSpan(dp(14)))
```

### Reference

#### Supported attributes

- text

- textColor

- backgroundColor

- textSize

- typeface

- lineSpacing

- verticalPadding

- paddingTop

- paddingBottom

- onClick

- fontFamily: include "monospace", "serif", and "sans-serif"

- textStyle: include "normal", "bold", "italic", and "bold_italic"

- alignment: include "normal", "opposite", and "center"

- textDecorationLine: include "none", "underline", "line-through", and "underline line-through"


#### Extended spans

- link: URLSpan

- quote: QuoteSpan

- superscript: SuperscriptSpan

- subscript: SubscriptSpan

- image: ImageSpan, include "bottom" and "baseline" alignment

- style: Specify custom spans


### License

```
Copyright 2018 Jun Gu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```