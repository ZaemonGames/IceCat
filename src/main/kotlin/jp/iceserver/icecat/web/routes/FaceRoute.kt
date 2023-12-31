package jp.iceserver.icecat.web.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jp.iceserver.icecat.IceCat
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

fun Route.faceRouting() {
    get("/{uuid}/{size}") {
        val uuid = call.parameters["uuid"] ?: ""
        val sizeStr = call.parameters["size"] ?: ""

        if (uuid.isNotBlank() && sizeStr.isNotBlank()) {
            try {
                val size = sizeStr.toInt()
                call.response.header("Content-Disposition", "inline; filename=\"$uuid.png\"")
                call.respondBytes(getImageBytes(uuid, size), contentType = ContentType.Image.PNG)
            } catch (e: Exception) {
                call.respondText("Invalid size", status = HttpStatusCode.BadRequest)
            }
        } else {
            call.respondText("Invalid path or size", status = HttpStatusCode.BadRequest)
        }
    }
}

private fun getImageBytes(path: String, size: Int): ByteArray {
    val filePath = Paths.get("${IceCat.datafolder.path}/faces", "$path.png")
    return if (Files.exists(filePath)) {
        resizeImage(filePath.toString(), size)
    } else {
        resizeImage("${IceCat.datafolder.path}/faces/steve.png", size)
    }
}

private fun resizeImage(path: String, size: Int): ByteArray {
    val originalImage = ImageIO.read(File(path))
    val resizedImage = scaleImage(originalImage, size, size)

    val outputStream = ByteArrayOutputStream()
    ImageIO.write(resizedImage, "png", outputStream)

    return outputStream.toByteArray()
}

private fun scaleImage(originalImage: Image, width: Int, height: Int): BufferedImage {
    val scaledImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    scaledImage.createGraphics().apply {
        drawImage(originalImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null)
        dispose()
    }

    return scaledImage
}
