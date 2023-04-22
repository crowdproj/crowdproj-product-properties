package com.crowdproj.marketplace.app.controller

import com.crowdproj.marketplace.app.PropAppSettings
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1ProductProperty(appSettings: PropAppSettings) {
    val loggerProductProperty = appSettings.corSettings.loggerProvider.logger(Route::v1ProductProperty)

    route("product") {
        route("property") {
            post("create") {
                call.createProductProperty(appSettings, loggerProductProperty)
            }
            post("update") {
                call.updateProductProperty(appSettings, loggerProductProperty)
            }
            post("delete") {
                call.deleteProductProperty(appSettings, loggerProductProperty)
            }
            post("search") {
                call.searchProductProperty(appSettings, loggerProductProperty)
            }
        }
        route("properties") {
            post("read") {
                call.readProductProperties(appSettings, loggerProductProperty)
            }
        }
    }
}