package jp.iceserver.icecat

import dev.m1n1don.smartinvsr.inventory.InventoryManager
import hazae41.minecraft.kutils.bukkit.init
import jp.iceserver.icecat.commands.*
import jp.iceserver.icecat.config.MainConfig
import jp.iceserver.icecat.languages.ja
import jp.iceserver.icecat.listeners.AfkListener
import jp.iceserver.icecat.listeners.PlayerConnection
import jp.iceserver.icecat.listeners.PlayerDeath
import jp.iceserver.icecat.models.Language
import jp.iceserver.icecat.tables.DeathData
import jp.iceserver.icecat.tables.HomeData
import jp.iceserver.icecat.tables.PlayerData
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection

class IceCat : AbstractIceCat()
{
    companion object
    {
        lateinit var plugin: IceCat
        lateinit var lang: Language
    }

    val invManager: InventoryManager = InventoryManager(this)

    override fun onEnable()
    {
        plugin = this

        init(MainConfig)
        MainConfig.autoSave = true

        lang = when (MainConfig.language)
        {
            "ja" -> ja
            else -> ja
        }

        val dbFolder = File(dataFolder, "/database")
        if (!dbFolder.exists()) dbFolder.mkdirs()

        val dbFile = File(dataFolder, "/database/icecat.db")
        if (!dbFile.exists()) dbFile.createNewFile()

        val mysqlConfig = MainConfig.MysqlSection
        Database.connect(
            "jdbc:mysql://${mysqlConfig.host}:${mysqlConfig.port}/${mysqlConfig.name}",
            driver = "com.mysql.cj.jdbc.Driver",
            user = mysqlConfig.user,
            password = mysqlConfig.pass
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(
                PlayerData,
                HomeData,
                DeathData
            )
        }

        invManager.init()

        registerListeners(
            PlayerConnection(), PlayerDeath(), AfkListener()
        )

        registerCommands(
            "coi" to CoiCommand(),
            "nickname" to NickNameCommand(),
            "report" to ReportCommand()
        )

        registerCommandsAndCompleters(
            Pair(Pair("gamemode", GamemodeCommand()), GamemodeCommand()),
            Pair(Pair("home", HomeCommand()), HomeCommand()),
            Pair(Pair("sethome", SetHomeCommand()), SetHomeCommand()),
            Pair(Pair("death", DeathCommand()), DeathCommand()),
            Pair(Pair("afk", AfkCommand()), AfkCommand()),
        )
    }
}