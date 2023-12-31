package jp.iceserver.icecat.web

import io.ktor.server.application.*
import io.ktor.server.routing.*
import jp.iceserver.icecat.web.routes.faceRouting

fun Application.module()
{
    routing {
        route("/face") { faceRouting() }
    }
}
