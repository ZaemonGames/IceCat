package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.CompletableFuture

class StaffCommand : CommandExecutor, TabCompleter
{
    private val lp = IceCat.lp
    private val lang = IceCat.lang
    private val staffGroupName = "staff"
    private val console = Bukkit.getConsoleSender()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean
    {
        if (args.isNullOrEmpty())
        {
            if (sender is Player)
            {
                handlePlayerCommand(sender)
            } else
            {
                sender.msg(lang.selectPlayerMsg)
            }
        } else
        {
            handleTargetSelection(sender, args[0])
        }

        return true
    }

    private fun handlePlayerCommand(player: Player)
    {
        if (!player.hasPermission("icecat.command.staff"))
        {
            player.msg(lang.noPermissionMsg)
            return
        }
        toggle(player)
    }

    private fun handleTargetSelection(sender: CommandSender, targetName: String)
    {
        if (!sender.hasPermission("icecat.command.staff"))
        {
            sender.msg(lang.noPermissionMsg)
            return
        }

        val target = Bukkit.getOfflinePlayer(targetName)
        if (!target.hasPlayedBefore())
        {
            sender.msg(lang.playerNotFoundMsg)
            return
        }

        toggle(target, sender)
    }

    private fun isStaff(uuid: UUID): CompletableFuture<Boolean>
    {
        return lp.userManager.loadUser(uuid)
            .thenApplyAsync { user ->
                val inheritedGroups = user.getInheritedGroups(user.queryOptions)
                inheritedGroups.any { group -> group.name == staffGroupName }
            }
    }

    private fun toggle(target: OfflinePlayer, sender: CommandSender? = null)
    {
        val uuid = target.uniqueId
        isStaff(uuid).thenAcceptAsync { isStaff ->
            lp.userManager.modifyUser(uuid) { user ->
                val node = Node.builder("group.$staffGroupName").build()

                if (isStaff)
                {
                    user.data().remove(node)
                    if (target != sender && target is Player) target.msg(lang.noLongerStaffMsg)
                    if (sender !is ConsoleCommandSender) console.msg(lang.selectedPlayerNoLongerStaffMsg.replace("%player", target.name!!))
                    sender?.msg(lang.selectedPlayerNoLongerStaffMsg.replace("%player", target.name!!))
                } else
                {
                    user.data().add(node)
                    if (target != sender && target is Player) target.msg(lang.becameStaffMsg)
                    if (sender !is ConsoleCommandSender) console.msg(lang.selectedPlayerBecameStaffMsg.replace("%player", target.name!!))
                    sender?.msg(lang.selectedPlayerBecameStaffMsg.replace("%player", target.name!!))
                }
            }
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>
    {
        if (args.size == 1 && sender.hasPermission("icecat.command.staff"))
        {
            return Bukkit.getOnlinePlayers().map { it.name }
        }
        return emptyList()
    }
}
