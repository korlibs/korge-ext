package korlibs.korge.bus

import korlibs.inject.*
import korlibs.io.async.*
import kotlin.test.*

class SyncBusTest {
    val out = arrayListOf<String>()

    inner class Scene1(val bus: SyncBus) {
        init {
            bus.register<Int> { out += "HELLO$it" }
        }
    }

    @Test
    fun test() = suspendTest {
        val injector = Injector()
        injector.mapSyncBus()
        injector.mapPrototype { Scene1(get()) }
        val injector2 = injector.child()
        val scene1 = injector2.get<Scene1>()
        val bus = injector.get<SyncBus>()
        bus.send(1)
        bus.send(2)
        //scene1.sceneDestroyInternal()
        injector2.deinit()
        bus.send(3)
        assertEquals("HELLO1,HELLO2", out.joinToString(","))
    }
}
