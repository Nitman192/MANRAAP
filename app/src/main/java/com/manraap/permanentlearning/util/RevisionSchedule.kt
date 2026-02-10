package com.manraap.permanentlearning.util

object RevisionSchedule {
    // Fixed neuroscience-informed intervals (in days): 1, 4, 9, 30, 90, 180
    private val intervalsDays = listOf(1, 4, 9, 30, 90, 180)

    fun intervalCount(): Int = intervalsDays.size

    fun stageToDelayMs(stage: Int): Long {
        val boundedStage = stage.coerceIn(0, intervalsDays.lastIndex)
        return intervalsDays[boundedStage] * 24L * 60L * 60L * 1000L
    }
}
