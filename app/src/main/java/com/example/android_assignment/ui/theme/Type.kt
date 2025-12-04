package com.example.android_assignment.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.android_assignment.R


val Urbanist = FontFamily(
    Font(R.font.urbanist_regular, weight = FontWeight.Normal),
    Font(R.font.urbanist_bold, weight = FontWeight.Bold),
    Font(R.font.urbanist_extrabold, weight = FontWeight.ExtraBold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Urbanist,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)