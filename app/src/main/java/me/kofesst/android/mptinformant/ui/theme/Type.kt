package me.kofesst.android.mptinformant.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import me.kofesst.android.mptinformant.R

@OptIn(ExperimentalTextApi::class)
private val GoogleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
private val AppFont = GoogleFont(name = "Source Sans Pro")

@OptIn(ExperimentalTextApi::class)
private val AppFontFamily = FontFamily(
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Thin
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.ExtraLight
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Light
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Medium
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.SemiBold
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Bold
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.ExtraBold
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Bold
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Thin,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Light,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Medium,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic
    ),
    Font(
        googleFont = AppFont,
        fontProvider = GoogleFontProvider,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
)
val AppTypography = Typography(
    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp,
        lineHeight = 16.sp,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp,
        lineHeight = 16.sp,
        fontSize = 11.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 16.sp,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.1.sp,
        lineHeight = 16.sp,
        fontSize = 12.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 40.sp,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 36.sp,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 32.sp,
        fontSize = 24.sp
    ),
    displayLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 64.sp,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 52.sp,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 44.sp,
        fontSize = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.W400,
        letterSpacing = 0.sp,
        lineHeight = 28.sp,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight = 24.sp,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
        fontSize = 14.sp
    ),
)
