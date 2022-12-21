package com.example.concessionariacarros.model

data class PieChartData(
    var status: String?,
    var value: Float?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PieChartData

        if (status != other.status) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status?.hashCode() ?: 0
        result = 31 * result + value.hashCode()
        return result
    }
}

val getPieChartData = listOf(
    PieChartData("vendidos", 0F),
    PieChartData("disponíveis", 0F),
)