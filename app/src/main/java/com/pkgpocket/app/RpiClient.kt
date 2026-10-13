package com.pkgpocket.app

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object RpiClient {

    data class InstallResult(val taskId: Int?, val raw: String)

    fun install(ps4Ip: String, pkgUrl: String): InstallResult {
        val body = JSONObject()
            .put("type", "direct")
            .put("packages", org.json.JSONArray().put(pkgUrl))
            // Payload compatibility: it expects {"url": "..."}
            .put("url", pkgUrl)

        val raw = post(ps4Ip, "/api/install", body)
        val json = JSONObject(normalizeJson(raw))
        val id = json.optInt("task_id", -1).takeIf { it >= 0 }
        return InstallResult(id, raw)
    }

    fun findTask(ps4Ip: String, contentId: String, subType: Int): Int? {
        val raw = post(
            ps4Ip,
            "/api/find_task",
            JSONObject()
                .put("content_id", contentId)
                .put("sub_type", subType)
        )
        val json = JSONObject(normalizeJson(raw))
        if (!json.optString("status").equals("success", ignoreCase = true)) return null
        return json.optInt("task_id", -1).takeIf { it >= 0 }
    }
