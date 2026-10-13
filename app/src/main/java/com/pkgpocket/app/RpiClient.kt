    private fun normalizeJson(raw: String): String {
        return raw.replace(Regex("""0[xX][0-9a-fA-F]+""")) { match ->
            val hex = match.value.substring(2)
            runCatching { hex.toULong(16).toString() }.getOrElse { "0" }
        }
    }

    fun bytesTotal(j: JSONObject): Long =
        j.optLong("length_total", j.optLong("length", 0L))

    fun bytesDone(j: JSONObject): Long =
        j.optLong("transferred_total", j.optLong("transferred", 0L))

    fun restSeconds(j: JSONObject): Long {
        val total = j.optLong("rest_sec_total", -1L)
        if (total > 0L) return total
        return j.optLong("rest_sec", -1L)
    }

    fun percent(j: JSONObject): Int {
        val total = bytesTotal(j)
        val done = bytesDone(j)
        return if (total > 0) {
            ((done * 100L) / total).toInt().coerceIn(0, 100)
        } else {
            j.optInt("preparing_percent", 0).coerceIn(0, 100)
        }
    }

    fun isFinished(j: JSONObject): Boolean {
        val total = bytesTotal(j)
        val done = bytesDone(j)
        return total > 0 && done >= total
    }
}
