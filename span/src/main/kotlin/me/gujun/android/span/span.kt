package me.gujun.android.span

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.Dimension
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.QuoteSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.view.View
import me.gujun.android.span.style.CustomTypefaceSpan
import me.gujun.android.span.style.LineSpacingSpan
import me.gujun.android.span.style.TextDecorationLineSpan
import me.gujun.android.span.style.VerticalPaddingSpan

class Span(val parent: Span? = null) : SpannableStringBuilder() {

  companion object {
    const val UNSPECIFIED = -1

    val EMPTY_STYLE = Span()

    var globalStyle: Span = EMPTY_STYLE
  }

  var text: CharSequence = ""

  @ColorInt var textColor: Int = parent?.textColor ?: UNSPECIFIED

  @ColorInt var backgroundColor: Int = parent?.backgroundColor ?: UNSPECIFIED

  @Dimension(unit = Dimension.PX) var textSize: Int = parent?.textSize ?: UNSPECIFIED

  var fontFamily: String? = parent?.fontFamily

  var typeface: Typeface? = parent?.typeface

  var textStyle: String? = parent?.textStyle

  var alignment: String? = parent?.alignment

  var textDecorationLine: String? = parent?.textDecorationLine

  @Dimension(unit = Dimension.PX) var lineSpacing: Int = UNSPECIFIED

  @Dimension(unit = Dimension.PX) var paddingTop: Int = UNSPECIFIED

  @Dimension(unit = Dimension.PX) var paddingBottom: Int = UNSPECIFIED

  @Dimension(unit = Dimension.PX) var verticalPadding: Int = UNSPECIFIED

  var onClick: (() -> Unit)? = null

  var spans: ArrayList<Any> = ArrayList()

  var style: Span = EMPTY_STYLE

  private fun buildCharacterStyle(builder: ArrayList<Any>) {
    if (textColor != UNSPECIFIED) {
      builder.add(ForegroundColorSpan(textColor))
    }

    if (backgroundColor != UNSPECIFIED) {
      builder.add(BackgroundColorSpan(backgroundColor))
    }

    if (textSize != UNSPECIFIED) {
      builder.add(AbsoluteSizeSpan(textSize))
    }

    if (!TextUtils.isEmpty(fontFamily)) {
      builder.add(TypefaceSpan(fontFamily))
    }

    if (typeface != null) {
      builder.add(CustomTypefaceSpan(typeface!!))
    }

    if (!TextUtils.isEmpty(textStyle)) {
      builder.add(StyleSpan(when (textStyle) {
        "normal" -> Typeface.NORMAL
        "bold" -> Typeface.BOLD
        "italic" -> Typeface.ITALIC
        "bold_italic" -> Typeface.BOLD_ITALIC
        else -> throw RuntimeException("Unknown text style")
      }))
    }

    if (!TextUtils.isEmpty(textDecorationLine)) {
      builder.add(TextDecorationLineSpan(textDecorationLine!!))
    }

    if (onClick != null) {
      builder.add(object : ClickableSpan() {
        override fun onClick(widget: View) {
          onClick?.invoke()
        }
      })
    }
  }

  private fun buildParagraphStyle(builder: ArrayList<Any>) {
    if (!TextUtils.isEmpty(alignment)) {
      builder.add(AlignmentSpan.Standard(when (alignment) {
        "normal" -> Layout.Alignment.ALIGN_NORMAL
        "opposite" -> Layout.Alignment.ALIGN_OPPOSITE
        "center" -> Layout.Alignment.ALIGN_CENTER
        else -> throw RuntimeException("Unknown text alignment")
      }))
    }

    if (lineSpacing != UNSPECIFIED) {
      builder.add(LineSpacingSpan(lineSpacing))
    }

    paddingTop = when {
      paddingTop != UNSPECIFIED -> paddingTop
      verticalPadding != UNSPECIFIED -> verticalPadding
      else -> 0
    }
    paddingBottom = when {
      paddingBottom != UNSPECIFIED -> paddingBottom
      verticalPadding != UNSPECIFIED -> verticalPadding
      else -> 0
    }
    if (paddingTop != 0 || paddingBottom != 0) {
      builder.add(VerticalPaddingSpan(paddingTop, paddingBottom))
    }
  }

  private fun prebuild() {
    override(style)
  }

  fun build(): Span {
    prebuild()
    val builder = ArrayList<Any>()
    if (!TextUtils.isEmpty(text)) {
      var p = this.parent
      while (p != null) {
        if (!TextUtils.isEmpty(p.text)) {
          throw RuntimeException("Can't nest \"$text\" in spans")
        }
        p = p.parent
      }
      append(text)
      buildCharacterStyle(builder)
      buildParagraphStyle(builder)
    } else {
      buildParagraphStyle(builder)
    }

    builder.addAll(spans)
    builder.forEach {
      setSpan(it, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return this
  }

  fun override(style: Span) {
    if (textColor == UNSPECIFIED) {
      textColor = style.textColor
    }
    if (backgroundColor == UNSPECIFIED) {
      backgroundColor = style.backgroundColor
    }
    if (textSize == UNSPECIFIED) {
      textSize = style.textSize
    }
    if (fontFamily == null) {
      fontFamily = style.fontFamily
    }
    if (typeface == null) {
      typeface = style.typeface
    }
    if (textStyle == null) {
      textStyle = style.textStyle
    }
    if (alignment == null) {
      alignment = style.alignment
    }
    if (textDecorationLine == null) {
      textDecorationLine = style.textDecorationLine
    }
    if (lineSpacing == UNSPECIFIED) {
      lineSpacing = style.lineSpacing
    }
    if (paddingTop == UNSPECIFIED) {
      paddingTop = style.paddingTop
    }
    if (paddingBottom == UNSPECIFIED) {
      paddingBottom = style.paddingBottom
    }
    if (verticalPadding == UNSPECIFIED) {
      verticalPadding = style.verticalPadding
    }
    if (onClick != null) {
      onClick = style.onClick
    }
    spans.addAll(style.spans)
  }

  operator fun CharSequence.unaryPlus(): CharSequence {
    return append(Span(parent = this@Span).apply {
      text = this@unaryPlus
      build()
    })
  }

  operator fun Span.plus(other: CharSequence): CharSequence {
    return append(Span(parent = this@Span).apply {
      text = other
      build()
    })
  }
}

fun span(init: Span.() -> Unit): Span = Span().apply {
  override(Span.globalStyle)
  init()
  build()
}

fun span(text: CharSequence, init: Span.() -> Unit): Span = Span().apply {
  override(Span.globalStyle)
  this.text = text
  init()
  build()
}

fun style(init: Span.() -> Unit): Span = Span().apply {
  init()
}

fun Span.span(init: Span.() -> Unit = {}): Span = run {
  append(Span(parent = this).apply {
    init()
    build()
  })
  this
}

fun Span.span(text: CharSequence, init: Span.() -> Unit = {}): Span = run {
  append(Span(parent = this).apply {
    this.text = text
    init()
    build()
  })
  this
}

fun Span.link(url: String, text: CharSequence = "",
    init: Span.() -> Unit = {}): Span = run {
  append(Span(parent = this).apply {
    this.text = text
    this.spans.add(URLSpan(url))
    init()
    build()
  })
  this
}

fun Span.quote(@ColorInt color: Int, text: CharSequence = "",
    init: Span.() -> Unit = {}): Span = run {
  append(Span(parent = this).apply {
    this.text = text
    this.spans.add(QuoteSpan(color))
    init()
    build()
  })
  this
}

fun Span.superscript(text: CharSequence = "", init: Span.() -> Unit = {}): Span = run {
  append(Span(parent = this).apply {
    this.text = text
    this.spans.add(SuperscriptSpan())
    init()
    build()
  })
  this
}

fun Span.subscript(text: CharSequence = "", init: Span.() -> Unit = {}): Span = run {
  append(Span(parent = this).apply {
    this.text = text
    this.spans.add(SubscriptSpan())
    init()
    build()
  })
  this
}

fun Span.image(drawable: Drawable, alignment: String = "bottom",
    init: Span.() -> Unit = {}): Span = run {
  drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
  append(Span(parent = this).apply {
    this.text = " "
    this.spans.add(ImageSpan(drawable, when (alignment) {
      "bottom" -> ImageSpan.ALIGN_BOTTOM
      "baseline" -> ImageSpan.ALIGN_BASELINE
      else -> throw RuntimeException("Unknown image alignment")
    }))
    init()
    build()
  })
  this
}

fun Span.addSpan(what: Any) = run {
  this.spans.add(what)
}