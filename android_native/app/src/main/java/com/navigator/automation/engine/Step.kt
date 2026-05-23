package com.navigator.automation.engine

import org.json.JSONObject

sealed class Step {
    data class TapText(val text: String, val delayMs: Long = 0L) : Step()
    data class TapCoords(val x: Float, val y: Float, val delayMs: Long = 0L, val repeatCount: Int = 1) : Step()
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 500L, val delayMs: Long = 0L, val repeatCount: Int = 1) : Step()
    data class WaitSeconds(val seconds: Float, val delayMs: Long = 0L) : Step()
    data class TypeText(val text: String, val delayMs: Long = 0L) : Step()
    data class Swipe(val direction: String, val delayMs: Long = 0L) : Step()
    data class SwipeCoords(val x1: Float, val y1: Float, val x2: Float, val y2: Float, val durationMs: Long = 300L, val delayMs: Long = 0L) : Step()
    data class PressKey(val key: String, val delayMs: Long = 0L) : Step()
    data class LaunchApp(val target: String, val delayMs: Long = 0L) : Step()
    data class WatchCorners(val timeoutSeconds: Int = 25, val delayMs: Long = 0L) : Step()
    data class TapWhen(val text: String, val timeoutSeconds: Int = 30, val delayMs: Long = 0L) : Step()
    data class CheckBranch(val triggerText: String, val thenSequence: String, val delayMs: Long = 0L) : Step()
    data class PressBack(val delayMs: Long = 0L) : Step()
    data class PressHome(val delayMs: Long = 0L) : Step()
    data class DismissAd(val delayMs: Long = 0L) : Step()

    fun label(): String = when (this) {
        is TapText      -> buildString { append("Tap: $text"); if (delayMs > 0) append(" +${delayMs}ms") }
        is TapCoords    -> buildString {
            append("Tap (%.0f, %.0f)".format(x, y))
            if (repeatCount > 1) append(" ×$repeatCount")
            if (delayMs > 0) append(" +${delayMs}ms")
        }
        is LongPress    -> buildString {
            append("Long press (%.0f, %.0f) ${durationMs}ms".format(x, y))
            if (repeatCount > 1) append(" ×$repeatCount")
            if (delayMs > 0) append(" +${delayMs}ms")
        }
        is WaitSeconds  -> buildString { append("Wait: ${seconds}s"); if (delayMs > 0) append(" +${delayMs}ms") }
        is TypeText     -> buildString { append("Type: $text"); if (delayMs > 0) append(" +${delayMs}ms") }
        is Swipe        -> buildString {
            append("Swipe $direction")
            if (delayMs > 0) append(" +${delayMs}ms")
        }
        is SwipeCoords  -> buildString {
            append("Swipe (%.0f,%.0f)→(%.0f,%.0f) ${durationMs}ms".format(x1, y1, x2, y2))
            if (delayMs > 0) append(" +${delayMs}ms")
        }
        is PressKey     -> buildString { append("Keys: $key"); if (delayMs > 0) append(" +${delayMs}ms") }
        is LaunchApp    -> buildString { append("Launch: $target"); if (delayMs > 0) append(" +${delayMs}ms") }
        is WatchCorners -> buildString { append("Watch corners (${timeoutSeconds}s)"); if (delayMs > 0) append(" +${delayMs}ms") }
        is TapWhen      -> buildString { append("Wait & tap: \"$text\" (up to ${timeoutSeconds}s)"); if (delayMs > 0) append(" +${delayMs}ms") }
        is CheckBranch  -> buildString { append("If '$triggerText' → $thenSequence"); if (delayMs > 0) append(" +${delayMs}ms") }
        is PressBack    -> buildString { append("Press Back"); if (delayMs > 0) append(" +${delayMs}ms") }
        is PressHome    -> buildString { append("Press Home"); if (delayMs > 0) append(" +${delayMs}ms") }
        is DismissAd    -> buildString { append("Dismiss Ad"); if (delayMs > 0) append(" +${delayMs}ms") }
    }

    fun toJson(): JSONObject = JSONObject().apply {
        when (val s = this@Step) {
            is TapText     -> {
                put("type", "click"); put("target", s.text)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is TapCoords   -> {
                put("type", "tap_coords"); put("x", s.x); put("y", s.y)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
                if (s.repeatCount != 1) put("repeat", s.repeatCount)
            }
            is LongPress   -> {
                put("type", "long_press"); put("x", s.x); put("y", s.y); put("duration_ms", s.durationMs)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
                if (s.repeatCount != 1) put("repeat", s.repeatCount)
            }
            is WaitSeconds -> {
                put("type", "wait"); put("seconds", s.seconds)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is TypeText    -> {
                put("type", "type"); put("target", s.text)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is Swipe       -> {
                put("type", "swipe"); put("direction", s.direction)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is SwipeCoords -> {
                put("type", "swipe_coords")
                put("x1", s.x1); put("y1", s.y1); put("x2", s.x2); put("y2", s.y2)
                put("duration_ms", s.durationMs)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is PressKey    -> {
                put("type", "key"); put("target", s.key)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is LaunchApp   -> {
                put("type", "launch"); put("target", s.target)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is WatchCorners -> {
                put("type", "watch_corners"); put("timeout", s.timeoutSeconds)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is TapWhen     -> {
                put("type", "tap_when"); put("target", s.text); put("timeout", s.timeoutSeconds)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is CheckBranch -> {
                put("type", "check_branch"); put("target", s.triggerText); put("then_sequence", s.thenSequence)
                if (s.delayMs > 0) put("delay_ms", s.delayMs)
            }
            is PressBack   -> { put("type", "press_back"); if (s.delayMs > 0) put("delay_ms", s.delayMs) }
            is PressHome   -> { put("type", "press_home"); if (s.delayMs > 0) put("delay_ms", s.delayMs) }
            is DismissAd   -> { put("type", "dismiss_ad"); if (s.delayMs > 0) put("delay_ms", s.delayMs) }
        }
    }

    companion object {
        fun fromJson(json: JSONObject): Step? = when (json.optString("type")) {
            "click"        -> TapText(json.optString("target"), json.optLong("delay_ms", 0L))
            "tap_coords"   -> TapCoords(
                                  json.optDouble("x").toFloat(),
                                  json.optDouble("y").toFloat(),
                                  json.optLong("delay_ms", 0L),
                                  json.optInt("repeat", 1)
                              )
            "long_press"   -> LongPress(
                                  json.optDouble("x").toFloat(),
                                  json.optDouble("y").toFloat(),
                                  json.optLong("duration_ms", 500L),
                                  json.optLong("delay_ms", 0L),
                                  json.optInt("repeat", 1)
                              )
            "wait"         -> WaitSeconds(json.optDouble("seconds", 1.0).toFloat(), json.optLong("delay_ms", 0L))
            "type"         -> TypeText(json.optString("target"), json.optLong("delay_ms", 0L))
            "swipe"        -> Swipe(json.optString("direction", "up"), json.optLong("delay_ms", 0L))
            "swipe_coords" -> SwipeCoords(
                                  json.optDouble("x1").toFloat(),
                                  json.optDouble("y1").toFloat(),
                                  json.optDouble("x2").toFloat(),
                                  json.optDouble("y2").toFloat(),
                                  json.optLong("duration_ms", 300L),
                                  json.optLong("delay_ms", 0L)
                              )
            "key"          -> PressKey(json.optString("target"), json.optLong("delay_ms", 0L))
            "launch"       -> LaunchApp(json.optString("target"), json.optLong("delay_ms", 0L))
            "watch_corners" -> WatchCorners(json.optInt("timeout", 25), json.optLong("delay_ms", 0L))
            "tap_when"      -> TapWhen(json.optString("target"), json.optInt("timeout", 30), json.optLong("delay_ms", 0L))
            "check_branch"  -> CheckBranch(json.optString("target"), json.optString("then_sequence"), json.optLong("delay_ms", 0L))
            "press_back"   -> PressBack(json.optLong("delay_ms", 0L))
            "press_home"   -> PressHome(json.optLong("delay_ms", 0L))
            "dismiss_ad"   -> DismissAd(json.optLong("delay_ms", 0L))
            else           -> null
        }
    }
}
