package com.mparticle.mockserver

import cocoapods.mParticle_Apple_SDK.MPNetworkCommunication
import com.mparticle.messages.ConfigResponseMessage
import com.mparticle.mockserver.*
import com.mparticle.mockserver.model.RawConnection
import platform.Foundation.NSThread
import platform.Foundation.allKeys
import platform.Foundation.setValue
import kotlin.native.concurrent.freeze

actual open class Platforms actual constructor() {

    actual fun injectMockServer() {
        MPNetworkCommunication.setConnectorFactory(MockConnectorFactory { rawConnection: RawConnection ->
            Server.onRequestMade(rawConnection).freeze()
        }.freeze())
    }

    actual fun sleep(millis: Long) {
//        platform.posix.sleep(millis.toUInt())
    }
    actual fun sendForeground() {
    }

    actual fun sendBackground() {
    }

    actual fun getDatabaseContents() = getDatabaseContents(null)
    actual fun getDatabaseContents(tables: List<String>?): Map<String, Any> = mapOf()
    actual fun getDatabaseSchema() = getDatabaseSchema(null)
    actual fun getDatabaseSchema(tables: List<String>?): Map<String, Any> = mapOf()

    actual fun setCachedConfig(configMessage: ConfigResponseMessage) {
    }

    actual fun currentThread(): String? {
        return """
        isMain: ${NSThread.currentThread.isMainThread}
        name: ${NSThread.currentThread.name}
        dictionary: ${NSThread.currentThread.threadDictionary}
        """
    }

    actual fun isServerThread(): Boolean {
        return NSThread.currentThread.threadDictionary.allKeys.contains("server thread")
    }

    actual fun setServerThread() {
        NSThread.currentThread.threadDictionary.setValue(true, "server thread")
    }

    actual fun prepareThread() {}
}

typealias OnRequestMade = (RawConnection) -> RawConnection