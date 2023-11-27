package jp.iceserver.icecat.listeners

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import jp.iceserver.icecat.IceCat
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import java.awt.image.BufferedImage
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64
import javax.imageio.ImageIO

class PlayerLogin : Listener {
    private val skinApiUrl = "https://api.geysermc.org/v2/skin/"

    @EventHandler
    fun onPlayerLogin(e: PlayerLoginEvent) {
        savePlayerFace(e.player)
    }

    private fun savePlayerFace(player: Player) {
        val fg = IceCat.fg
        var skinUrl = URL("https://minecraft.novaskin.me/skin/4057653878/download")

        if (fg.isFloodgatePlayer(player.uniqueId)) {
            val url = URL("$skinApiUrl${fg.getPlayer(player.uniqueId).xuid}")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = StringBuilder()
                BufferedReader(InputStreamReader(connection.inputStream)).useLines { it.forEach { line -> response.append(line) } }

                val json = Parser.default().parse(StringBuilder(response.toString())) as JsonObject
                val decodedSkinInfo = String(Base64.getDecoder().decode(json.string("value")))
                val skinInfo = Parser.default().parse(StringBuilder(decodedSkinInfo)) as JsonObject

                skinUrl = URL(skinInfo.obj("textures")?.obj("SKIN")?.string("url") ?: "https://minecraft.novaskin.me/skin/4057653878/download")
            }
        } else {
            skinUrl = player.playerProfile.textures.skin ?: URL("https://minecraft.novaskin.me/skin/4057653878/download")
        }

        val skin: BufferedImage = ImageIO.read(skinUrl)
        val face = skin.getSubimage(8, 8, 8, 8)
        val outputDir = File(IceCat.datafolder, "faces").apply { if (!exists()) mkdirs() }
        val outputFile = File(outputDir, "${player.uniqueId}.png")

        try {
            ImageIO.write(face, "png", outputFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}