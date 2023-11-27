package jp.iceserver.icecat.web

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.module()
{
    routing {
        route("/face") { faceRouting() }
    }
}
