package edu.skku.cs.pa1.model

import androidx.annotation.ColorRes
import edu.skku.cs.pa1.R

enum class CharState(
    @ColorRes val colorRes: Int,
    @ColorRes val textColorRes: Int
) {
    CORRECT(colorRes = R.color.green, textColorRes = R.color.black),
    WRONG_POSITION(colorRes = R.color.yellow, textColorRes = R.color.black),
    ABSENT(colorRes = R.color.gray, textColorRes = R.color.white);
}