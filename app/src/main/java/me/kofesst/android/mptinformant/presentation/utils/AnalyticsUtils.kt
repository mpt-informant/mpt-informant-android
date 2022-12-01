package me.kofesst.android.mptinformant.presentation.utils

sealed class MptAnalytics {
    sealed class Events {
        companion object {
            const val SET_GROUP_INFO = "set_group_info"
        }
    }

    sealed class Params {
        companion object {
            const val DEPARTMENT_NAME = "department"
            const val GROUP_NAME = "group"
        }
    }
}