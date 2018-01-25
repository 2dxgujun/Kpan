package me.gujun.android.span.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.text.style.TextAppearanceSpan
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sample.content
import me.gujun.android.span.Span
import me.gujun.android.span.addSpan
import me.gujun.android.span.image
import me.gujun.android.span.link
import me.gujun.android.span.quote
import me.gujun.android.span.span
import me.gujun.android.span.style
import me.gujun.android.span.subscript
import me.gujun.android.span.superscript


class SampleActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sample)

    Span.globalStyle = style {
      textSize = dp(14)
    }

    // You must set this movement method to make clickable span work
    content.movementMethod = LinkMovementMethod.getInstance()
    content.text = span {
      span("StyleSpan") {
        textStyle = "bold_italic"
      }
      +"\n"
      span("CustomTypefaceSpan") {
        typeface = ResourcesCompat.getFont(this@SampleActivity, R.font.pacifico)
      }
      +"\n"
      span("AbsoluteSizeSpan") {
        textSize = dp(20)
      }
      +"\n"
      span("Align normal") {
        alignment = "normal"
      }
      +"\n"
      span("Align center") {
        alignment = "center"
      }
      +"\n"
      span("Align opposite") {
        alignment = "opposite"
      }
      +"\n"
      span("Underline") {
        textDecorationLine = "underline"
      }
      +"\n"
      span("Strikethrough") {
        textDecorationLine = "line-through"
      }
      +"\n"
      span("Underline Strikethrough") {
        textDecorationLine = "underline line-through"
      }
      +"\n"
      span("TypefaceSpan") {
        fontFamily = "serif"
      }
      +"\n"
      link("http://google.com", "URLSpan")
      +"\n"
      span("ClickableSpan") {
        onClick = {
          Toast.makeText(this@SampleActivity, "ClickableSpan onClick", Toast.LENGTH_LONG).show()
        }
      }
      +"\n"
      span("CustomSpannable") {
        addSpan(TextAppearanceSpan(this@SampleActivity,
            R.style.TextAppearance_Sample))
      }
      +"\n"
      span {
        lineSpacing = dp(8)
        backgroundColor = Color.LTGRAY
        verticalPadding = dp(5)
        +"LineSpacingWithVerticalPadding"
        +"\n"
        +"LineSpacingWithVerticalPadding"
        +"\n"
        +"LineSpacingWithVerticalPadding"
      }
      +"\n"
      quote(Color.RED, "QuoteSpan")
      +"\n"
      span {
        +"Plain"
        subscript("Subscript") {
          textSize = dp(8)
        }
        superscript("Superscript") {
          textSize = dp(8)
        }
      }
      +"\n"
      span("BackgroundSpan") {
        backgroundColor = Color.CYAN
      }
      +"\n"
      span("ForegroundSpan") {
        textColor = Color.BLUE
      }
      +"\n"
      span {
        image(ResourcesCompat.getDrawable(resources, R.mipmap.ic_launcher_round, null)!!)
        +"ImageSpan"
      }
      +"\n"
      span {
        textColor = Color.BLUE
        textSize = dp(20)
        +"Origin text"
        +"\n"
        span("Override foreground") {
          textColor = Color.RED
        }
        +"\n"
        span("Override text size") {
          textSize = dp(10)
        }
      }
      +"\n"
      span {
        // Spans are hard?
        +"Spans "
        span {
          textStyle = "bold"
          +"are "
          span("hard") {
            textDecorationLine = "line-through"
          }
          +"?"
        }
      }
      +"\n"
      val reusableStyle = style {
        textColor = Color.BLACK
        verticalPadding = dp(3)
        backgroundColor = Color.LTGRAY
      }
      span("Reuse styles") {
        style = reusableStyle
      }
      +"\n"
      span("Reuse styles") {
        style = reusableStyle
      }
      +"\n"
      +"ParagraphStyle Test\n"
      span {
        span {
          lineSpacing = dp(8)
          verticalPadding = dp(8)
          backgroundColor = Color.RED
          text = "LineSpacingWithManuallyBreak\nLineSpacingWithManuallyBreak\nLineSpacingWithManuallyBreak"
        }
        +"\n"
        span {
          lineSpacing = dp(8)
          verticalPadding = dp(8)
          backgroundColor = Color.GREEN
          text = "LineSpacingWithWrapLineSpacingWithWrapLineSpacingWithWrapLineSpacingWithWrapLineSpacingWithWrapLineSpacingWithWrapLineSpacingWithWrapLineSpacingWithWrap"
        }
        +"\n"
        span {
          lineSpacing = dp(8)
          verticalPadding = dp(8)
          backgroundColor = Color.BLUE
          textColor = Color.WHITE
          text = "LineSpacingCombineManuallyBreakAndWrapLineSpacingCombineManuallyBreakAndWrapLineSpacingCombineManuallyBreakAndWrap\nLineSpacingCombineManuallyBreakAndWrap\nLineSpacingCombineManuallyBreakAndWrap\nLineSpacingCombineManuallyBreakAndWrapLineSpacingCombineManuallyBreakAndWrapLineSpacingCombineManuallyBreakAndWrapLineSpacingCombineManuallyBreakAndWrap"
        }
      }
    }
  }

  private fun dp(dp: Int): Int = (dp * resources.displayMetrics.density + .5f).toInt()
}