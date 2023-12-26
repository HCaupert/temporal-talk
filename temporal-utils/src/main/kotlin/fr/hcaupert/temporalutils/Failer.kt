package fr.hcaupert.temporalutils

class Failer(private val on: Int) {

    private var currentDraw = on

    fun run() {
        currentDraw--

        if (currentDraw <= 0) {
            currentDraw = on
            throw RuntimeException("Failed to run, I'll fail 1/$on")
        }
    }
}
