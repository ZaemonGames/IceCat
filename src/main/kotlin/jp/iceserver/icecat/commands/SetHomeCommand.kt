package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.tables.HomeData
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class SetHomeCommand : CommandExecutor, TabCompleter
{
    private val lang = IceCat.lang

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean
    {
        sender as Player

        if (!sender.hasPermission("icecat.command.sethome")) return sender.msg(lang.noPermissionMsg).let { true }

        val currentLoc = sender.location

        if (currentLoc.world.name != "world") return sender.msg(lang.invalidHomeWorldMsg).let { true }

        if (isAir(currentLoc.clone().add(0.0, -1.0, 0.0).block.type))
        {
            val groundHeight = findGroundHeight(currentLoc)

            if (groundHeight != -1)
            {
                currentLoc.y = groundHeight.toDouble() + 1
                sender.msg(lang.homeHeightChangedMsg)
            } else return true
        }

        transaction {
            HomeData.update({ HomeData.uniqueId eq sender.uniqueId }) {
                it[x] = currentLoc.x
                it[y] = currentLoc.y
                it[z] = currentLoc.z
                it[yaw] = currentLoc.yaw
                it[pitch] = currentLoc.pitch
            }
        }
        sender.msg(lang.homeChangedMsg)
        return true
    }


    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>
    {
        return emptyList()
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