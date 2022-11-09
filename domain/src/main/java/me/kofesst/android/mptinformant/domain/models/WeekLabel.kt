package me.kofesst.android.mptinformant.domain.models

enum class WeekLabel(val displayName: String) {
    Numerator("Числитель"),
    Denominator("Знаменатель"),
    None("");

    companion object {
        fun fromDisplayName(displayName: String) = values().firstOrNull { value ->
            value.displayName.lowercase() == displayName.lowercase()
        } ?: None
    }
}
