# Android Span in kotlin

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
which is a best way to build the spans, so I make it happen:

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
implementation 'me.gujun.android:span:1.0'
```

### Usage


### Reference


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