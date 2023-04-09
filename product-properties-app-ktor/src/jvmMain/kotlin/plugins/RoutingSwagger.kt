package com.crowdproj.marketplace.app.plugins

import com.crowdproj.marketplace.app.PropAppSettings
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Routing.swagger(appSettings: PropAppSettings) {
    get("/spec-product-properties-{ver}.yaml") {
        val ver = call.parameters["ver"]
        val origTxt: String = withContext(Dispatchers.IO) {
            this::class.java.classLoader
                .getResource("specs/spec-product-properties-$ver.yaml")
                ?.readText()
        } ?: ""
        val response = origTxt.replace(
            Regex(
                "(?<=^servers:\$\\n).*(?=\\ntags:\$)",
                setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.MULTILINE, RegexOption.IGNORE_CASE)
            ),
            appSettings.appUrls.joinToString(separator = "\n") { "  - url: $it$ver" }
        )
        call.respondText { response }
    }

    get("/build/base.yaml") {
        val origTxt: String = withContext(Dispatchers.IO) {
            this::class.java.classLoader
                .getResource("specs/base.yaml")
                ?.readText()
        } ?: ""
        call.respondText { origTxt }
    }


    static("/") {
        preCompressed {
            defaultResource("index.html", "swagger-ui")
            resource("/swagger-initializer.js", "/swagger-initializer.js", "")
            static {
                staticBasePackage = "specs"
                resources(".")
            }
            static {
                preCompressed(CompressedFileType.GZIP) {
                    staticBasePackage = "swagger-ui"
                    resources(".")
                }
            }
        }
    }
}