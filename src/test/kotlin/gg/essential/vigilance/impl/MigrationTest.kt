package gg.essential.vigilance.impl

import gg.essential.vigilance.data.Migration
import gg.essential.vigilance.impl.nightconfig.core.Config
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MigrationTest {
    @Test
    fun testUpToDate() {
        val input = config("__meta" to mapOf("version" to 2), "test" to 42)
        migrate(input, listOf(Migration {}, Migration {}))
        assertEquals(config("__meta" to mapOf("version" to 2), "test" to 42), input)
    }

    @Test
    fun testNoOpMigration() {
        val input = config("__meta" to mapOf("version" to 1), "test" to 42)
        migrate(input, listOf(Migration {}, Migration {}))
        assertEquals(config("__meta" to mapOf("version" to 2), "test" to 42), input)
    }

    @Test
    fun testAddMigration() {
        val input = config("__meta" to mapOf("version" to 1), "test" to 42)
        migrate(input, listOf(Migration {}, Migration { it["new"] = 43 }))
        assertEquals(config(
            "__meta" to mapOf("version" to 2, "migration_log" to mapOf("1" to mapOf("added" to listOf("new")))),
            "test" to 42,
            "new" to 43,
        ), input)
    }

    @Test
    fun testChangeMigration() {
        val input = config("__meta" to mapOf("version" to 1), "test" to 42)
        migrate(input, listOf(Migration {}, Migration { it["test"] = it["test"] as Int + 1 }))
        assertEquals(config(
            "__meta" to mapOf("version" to 2, "migration_log" to mapOf("1" to mapOf("changed" to mapOf("test" to 42)))),
            "test" to 43,
        ), input)
    }

    @Test
    fun testRemoveMigration() {
        val input = config("__meta" to mapOf("version" to 1), "test" to 42)
        migrate(input, listOf(Migration {}, Migration { it.remove("test") }))
        assertEquals(config(
            "__meta" to mapOf("version" to 2, "migration_log" to mapOf("1" to mapOf("changed" to mapOf("test" to 42)))),
        ), input)
    }

    @Test
    fun testMultipleMigrations() {
        val input = config("__meta" to mapOf("version" to 1), "test" to 42)
        migrate(input, listOf(Migration {}, Migration { it["test"] = 1 }, Migration { it["test"] = it["test"] as Int + 1 }))
        assertEquals(config(
            "__meta" to mapOf("version" to 3, "migration_log" to mapOf(
                "1" to mapOf("changed" to mapOf("test" to 42)),
                "2" to mapOf("changed" to mapOf("test" to 1)),
            )),
            "test" to 2,
        ), input)
    }

    @Test
    fun testAddRollback() {
        val input = config(
            "__meta" to mapOf("version" to 2, "migration_log" to mapOf("1" to mapOf("added" to listOf("new")))),
            "test" to 42,
            "new" to 43,
        )
        migrate(input, listOf(Migration {}))
        assertEquals(config("__meta" to mapOf("version" to 1), "test" to 42), input)
    }

    @Test
    fun testChangeRollback() {
        val input = config(
            "__meta" to mapOf("version" to 2, "migration_log" to mapOf("1" to mapOf("changed" to mapOf("test" to 42)))),
            "test" to 43,
        )
        migrate(input, listOf(Migration {}))
        assertEquals(config("__meta" to mapOf("version" to 1), "test" to 42), input)
    }

    @Test
    fun testRemoveRollback() {
        val input = config(
            "__meta" to mapOf("version" to 2, "migration_log" to mapOf("1" to mapOf("changed" to mapOf("test" to 42)))),
        )
        migrate(input, listOf(Migration {}))
        assertEquals(config("__meta" to mapOf("version" to 1), "test" to 42), input)
    }

    @Test
    fun testMultipleRollback() {
        val input = config(
            "__meta" to mapOf("version" to 3, "migration_log" to mapOf(
                "1" to mapOf("changed" to mapOf("test" to 42)),
                "2" to mapOf("changed" to mapOf("test" to 1)),
            )),
            "test" to 2,
        )
        migrate(input, listOf(Migration {}))
        assertEquals(config("__meta" to mapOf("version" to 1), "test" to 42), input)
    }

    @Test
    fun testNoMigrations() {
        val input = config("test" to 42)
        migrate(input, listOf())
        assertEquals(config("test" to 42), input)
    }

    @Test
    fun testInitialMigration() {
        val input = config("test" to 42)
        migrate(input, listOf(Migration { config ->
            assert(config == mapOf("test" to 42))
            config["test"] = 43
        }))
        assertEquals(config(
            "__meta" to mapOf("version" to 1, "migration_log" to mapOf("0" to mapOf("changed" to mapOf("test" to 42)))),
            "test" to 43,
        ), input)
    }

    private fun config(vararg entries: Pair<String, Any?>): Config =
        config(entries.toMap())

    private fun config(map: Map<String, Any?>): Config {
        fun visit(map: Map<String, Any?>, config: Config) {
            for ((key, value) in map) {
                if (value is Map<*, *>) {
                    val inner = config.createSubConfig()
                    @Suppress("UNCHECKED_CAST")
                    visit(value as Map<String, Any?>, inner)
                    config.update(listOf(key), inner)
                } else {
                    config.update(listOf(key), value)
                }
            }
        }
        val config = Config.inMemory()
        visit(map, config)
        return config
    }
}
