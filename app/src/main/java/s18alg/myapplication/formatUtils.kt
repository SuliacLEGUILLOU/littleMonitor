package s18alg.myapplication

import s18alg.myapplication.model.TargetWebsite

fun formatUptime(target: TargetWebsite): String {
    return when (target.tryNumber > 0) {
        true -> String.format("Uptime : %d%%", target.pingSuccess * 100 / target.tryNumber)
        false -> "No uptime information yet"
    }
}

fun formatPingText(target: TargetWebsite): String {
    return String.format("Current delay : %.1fms", target.average_delay)
}