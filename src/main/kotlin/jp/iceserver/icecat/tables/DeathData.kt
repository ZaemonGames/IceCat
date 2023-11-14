package jp.iceserver.icecat.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object DeathData : Table()
{
    val uniqueId: Column<UUID> = uuid("uniqueId")
    override val primaryKey = PrimaryKey(uniqueId)

    val x: Column<Double> = double("x")
    val y: Column<Double> = double("y")
    val z: Column<Double> = double("z")
    val yaw: Column<Float> = float("yaw")
    val pitch: Column<Float> = float("pitch")
}