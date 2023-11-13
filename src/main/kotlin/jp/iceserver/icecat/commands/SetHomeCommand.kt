package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.tables.HomeData
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

        val current = sender.location
        transaction {
            HomeData.update({ HomeData.uniqueId eq sender.uniqueId }) {
                it[x] = current.x
                it[y] = current.y
                it[z] = current.z
                it[yaw] = current.yaw
                it[pitch] = current.pitch
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
}