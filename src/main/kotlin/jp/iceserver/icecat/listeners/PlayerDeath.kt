package jp.iceserver.icecat.listeners

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.tables.DeathData
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class PlayerDeath : Listener
{
    private val lang = IceCat.lang

    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent)
    {
        val player = e.player

        /* ----- /death ----- */
        if (!player.hasPermission("icecat.command.death")) return

        player.msg(lang.deathDetectedMsg)

        val deathLoc = player.location
        if (isAir(deathLoc.block.type))
        {
            val groundHeight = findGroundHeight(deathLoc)

            if (groundHeight != -1)
            {
                deathLoc.y = groundHeight.toDouble() + 1
                player.msg(lang.deathPointHeightChangedMsg)
            }
        }

        transaction {
            val exist = DeathData.select { DeathData.uniqueId eq player.uniqueId }.any()
            if (exist)
            {
                DeathData.update({ DeathData.uniqueId eq player.uniqueId }) {
                    it[x] = deathLoc.x
                    it[y] = deathLoc.y
                    it[z] = deathLoc.z
                    it[yaw] = deathLoc.yaw
                    it[pitch] = deathLoc.pitch
                }
            } else
            {
                DeathData.insert {
                    it[uniqueId] = player.uniqueId
                    it[x] = deathLoc.x
                    it[y] = deathLoc.y
                    it[z] = deathLoc.z
                    it[yaw] = deathLoc.yaw
                    it[pitch] = deathLoc.pitch
                }
            }
        }
    }

    private fun isAir(material: Material): Boolean
    {
        return material == Material.AIR
    }

    private fun findGroundHeight(location: Location): Int
    {
        val world: World = location.world
        val x: Int = location.blockX
        val y: Int = location.blockY
        val z: Int = location.blockZ
        for (i in y downTo 0)
        {
            if (!isAir(world.getBlockAt(x, i, z).type))
            {
                return i
            }
        }
        return -1
    }
}