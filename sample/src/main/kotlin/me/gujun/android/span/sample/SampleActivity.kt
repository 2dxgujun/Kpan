package me.gujun.android.span.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.TextAppearanceSpan
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sample.text
import me.gujun.android.span.Span
import me.gujun.android.span.Span.TextAlignment.CENTER
import me.gujun.android.span.Span.TextAlignment.NORMAL
import me.gujun.android.span.Span.TextAlignment.OPPOSITE
import me.gujun.android.span.image
import me.gujun.android.span.quote
import me.gujun.android.span.span
import me.gujun.android.span.style
import me.gujun.android.span.subscript
import me.gujun.android.span.superscript


class SampleActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sample)

    Span.globalStyles.add(AbsoluteSizeSpan(dp(14)))

    // You must set this movement method to make clickable span work
    text.movementMethod = LinkMovementMethod.getInstance()
    text.text = span {
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
        textAlign = NORMAL
      }
      +"\n"
      span("Align center") {
        textAlign = CENTER
      }
      +"\n"
      span("Align opposite") {
        textAlign = OPPOSITE
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
      span("URLSpan") {
        url = "http://google.com"
      }
      +"\n"
      span("ClickableSpan") {
        onClick = {
          Toast.makeText(this@SampleActivity, "ClickableSpan onClick", Toast.LENGTH_LONG).show()
        }
      }
      +"\n"
      span("CustomStyle") {
        style(TextAppearanceSpan(this@SampleActivity,
            R.style.TextAppearance_Sample))
      }
      +"\n"
      span {
        lineSpacing = dp(8)
        +"LineSpacingSpan"
        +"\n"
        +"LineSpacingSpan"
        +"\n"
        +"LineSpacingSpan"
      }
      +"\n"
      quote(Color.RED, "QuoteSpan")
      +"\n"
      span {
        +"Plain text"
        subscript("SubscriptSpan")
        superscript("SuperscriptSpan")
      }
      +"\n"
      span("BackgroundSpan") {
        backgroundColor = Color.LTGRAY
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
        +"Simple text"
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
    }
  }

  private fun dp(dp: Int): Int = (dp * resources.displayMetrics.density + .5f).toInt()
}