package all

fun Long.lcm(other: Long): Long {
    val larger = if (this > other) this else other
    val maxLcm = this * other
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % this == 0L && lcm % other == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun List<Long>.lcm() = this.reduce(Long::lcm)
