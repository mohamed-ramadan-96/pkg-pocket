package com.pkgpocket.app

import android.content.Context
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object AnalyticsTracker {

    fun setUserType(context: Context, isPro: Boolean) {}

    fun pkgSelected(context: Context, items: List<PkgItem>) {}

    fun ps4Detected(context: Context, rpiPort: Int) {}

    fun installStarted(context: Context, items: List<PkgItem>) {}

    fun installCompleted(
        context: Context,
        pkgCount: Int,
        totalBytes: Long,
        durationMs: Long
    ) {}

    fun installFailed(
        context: Context,
        reason: String,
        pkgCount: Int,
        totalBytes: Long,
        durationMs: Long,
        progressPercent: Int
    ) {}

    fun installCancelled(
        context: Context,
        pkgCount: Int,
        totalBytes: Long,
        durationMs: Long,
        progressPercent: Int
    ) {}

    fun failureReason(error: Throwable): String {
        return when (error) {
            is RpiClient.HttpException ->
                "rpi_http_${error.statusCode}"

            is SocketTimeoutException ->
                "rpi_timeout"

            is ConnectException ->
                "rpi_connect"

            is UnknownHostException ->
                "rpi_host"

            else ->
                error.javaClass.simpleName
                    .lowercase()
                    .replace(Regex("[^a-z0-9_]+"), "_")
                    .trim('_')
                    .take(100)
                    .ifBlank { "unknown" }
        }
    }
}