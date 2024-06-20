import korlibs.korge.gradle.*
plugins { id("com.soywiz.korge") version korlibs.korge.gradle.common.KorgeGradlePluginVersion.VERSION }
korge { id = "org.korge.unknown.game"; loadYaml(file("korge.yaml")) }
dependencies { findProject(":deps")?.also { add("commonMainApi", it) } }
val baseFile = file("build.extra.gradle.kts").takeIf { it.exists() }?.also { apply(from = it) }
