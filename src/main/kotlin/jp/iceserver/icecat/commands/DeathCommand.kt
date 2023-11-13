package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.tables.DeathData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class DeathCommand : CommandExecutor, TabCompleter
{
    private val lang = IceCat.lang

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean
    {
        sender as Player

        if (!sender.hasPermission("icecat.command.home")) return sender.msg(lang.noPermissionMsg).let { true }

        transaction {
            val query = DeathData.select { DeathData.uniqueId eq sender.uniqueId }

            if (!query.any()) {
                sender.msg(lang.deathPointNotFoundMsg)
                return@transaction
            }

            query.forEach {
                val world = Bukkit.getWorld("world")
                val x = it[DeathData.x]
                val y = it[DeathData.y]
                val z = it[DeathData.z]
                val yaw = it[DeathData.yaw]
                val pitch = it[DeathData.pitch]
                if (world != null)
                {
                    val home = Location(world, x, y, z, yaw, pitch)
                    sender.teleport(home)
                    sender.msg(lang.teleportedToDeathPointMsg)
                    DeathData.deleteWhere { DeathData.uniqueId eq sender.uniqueId }
                }
                return@transaction
            }
        }
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
}