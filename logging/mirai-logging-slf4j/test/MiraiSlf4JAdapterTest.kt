/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.utils.logging

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.loadService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.slf4j.LoggerFactory
import org.slf4j.helpers.NOPLogger
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs

@Disabled
internal class MiraiSlf4JAdapterTest {

    @Order(1)
    @Test
    fun `using log4j`() {
        assertIs<MiraiLog4JFactory>(loadService(MiraiLogger.Factory::class))
        val logger = MiraiLogger.Factory.create(this::class)
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        assertIs<net.mamoe.mirai.internal.utils.Log4jLoggerAdapter>(logger)
    }

    @Order(0)
    @Test
    fun `print test`() {
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out, true))
        System.setErr(PrintStream(out, true))

        assertIs<NOPLogger>(LoggerFactory.getLogger("s"))
        HttpClient(OkHttp)

        val logger = MiraiLogger.Factory.create(this::class)
        logger.error("Hi")
        /*
        SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...

         */
        out.flush()
        println(out.toString())
        assertFalse { out.toString().contains("Log4j2 could not find a logging implementation", ignoreCase = true) }
//        assertTrue {
//            out.toString().contains("SLF4J: Defaulting to no-operation (NOP) logger implementation", ignoreCase = true)
//        }
    }
}
