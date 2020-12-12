package pl.poznan.put.dogloverservice.modules.walk

enum class WalkStatus {
    ONGOING, ARRIVED_AT_DESTINATION, LEFT_DESTINATION, CANCELED;

    companion object {
        fun activeWalkStatuses() = setOf(ONGOING, ARRIVED_AT_DESTINATION)
    }
}