package com.dxgujun.span.style

import android.graphics.Paint.FontMetricsInt
import android.text.Spanned
import android.text.style.LineHeightSpan

class LineSpacingSpan(private val add: Int) : LineHeightSpan {

  override fun chooseHeight(text: CharSequence, start: Int, end: Int, spanstartv: Int, v: Int,
      fm: FontMetricsInt) {
    text as Spanned
    val spanStart = text.getSpanStart(this)
    val spanEnd = text.getSpanEnd(this)

    if (start >= spanStart && end <= spanEnd) {
      fm.descent += add
    }
  }
}