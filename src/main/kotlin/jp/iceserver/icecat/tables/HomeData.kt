package jp.iceserver.icecat.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object HomeData : Table()
{
    private val id: Column<Int> = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val uniqueId: Column<UUID> = uuid("uniqueId")

    val x: Column<Double> = double("x")
    val y: Column<Double> = double("y")
    val z: Column<Double> = double("z")
    val yaw: Column<Float> = float("yaw")
    val pitch: Column<Float> = float("pitch")
}