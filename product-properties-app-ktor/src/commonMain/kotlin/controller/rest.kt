package com.crowdproj.marketplace.app.controller

import com.crowdproj.marketplace.app.PropAppSettings
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1ProductProperty(appSettings: PropAppSettings) {
    route("product") {
        route("property") {
            post("create") {
                call.createProductProperty(appSettings)
            }
            post("update") {
                call.updateProductProperty(appSettings)
            }
            post("delete") {
                call.deleteProductProperty(appSettings)
            }
            post("search") {
                call.searchProductProperty(appSettings)
            }
        }
        route("properties") {
            post("read") {
                call.readProductProperties(appSettings)
            }
        }
    }
}