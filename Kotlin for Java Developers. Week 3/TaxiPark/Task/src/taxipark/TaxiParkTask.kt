package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    this.allDrivers.subtract(this.trips.map { it.driver })

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    if (minTrips > 0)
        this.trips.flatMap { it.passengers }
            .groupBy { it }
            .filterValues { it.size >= minTrips }
            .keys
    else this.allPassengers

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */

fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    this.trips.filter { it.driver == driver }
        .flatMap { it.passengers }
        .groupBy { it }
        .filterValues { it.size > 1 }
        .values
        .flatten()
        .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    this.trips.flatMap { it.passengers.map { passenger -> Pair(passenger, it) } }
        .groupBy { it.first }.filter {
            it.value.count { (_, v) -> v.discount != null } >
                    it.value.count { (_, v) -> v.discount == null }
        }.keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (this.trips.isEmpty()) return null
    val groupByDuration = this.trips.groupBy { (it.duration - it.duration % 10).rangeTo((it.duration / 10) * 10 + 9) }
        .mapValues { it.value.count() }
//    val numberOfFrequentTrip = groupByDuration.maxOf { it.value }
    val numberOfFrequentTrip = groupByDuration.values.max()
    val result = groupByDuration.filter { it.value == numberOfFrequentTrip }.keys
    return if (result.isEmpty()) null else result.first()
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty()) return false
    val costOfAllTripsEachDriver = this.trips.groupBy { it.driver }
        .mapValues { it.value.map { trip -> trip.cost }.sum() }
        .toList()
        .sortedByDescending { (_, v) -> v }
        .toMap()
    val twentyPercentOfDrivers = (this.allDrivers.count() * 0.2).toLong()
    val costOfAllTrips = costOfAllTripsEachDriver.values.sum()
    val costOfTripsOfTwentyPercentOfDrivers =
        costOfAllTripsEachDriver.values.stream()
            .limit(twentyPercentOfDrivers)
            .reduce { total, next -> total + next }.get()
    return costOfAllTripsEachDriver.values.any { costOfTripsOfTwentyPercentOfDrivers / costOfAllTrips >= 0.8 }
}