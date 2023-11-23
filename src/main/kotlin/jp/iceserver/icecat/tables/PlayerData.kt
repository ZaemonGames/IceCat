package jp.iceserver.icecat.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object PlayerData : Table("players")
{
    val uniqueId: Column<UUID> = uuid("uniqueId")
    override val primaryKey = PrimaryKey(uniqueId)

    val nickname: Column<String> = varchar("nickname", 16)
}