package gg.essential.vigilance.impl

import gg.essential.vigilance.data.Migration
import gg.essential.vigilance.impl.nightconfig.core.Config

private const val META_KEY = "__meta"
private val VERSION_KEY = listOf(META_KEY, "version")
private fun migrationLogKey(migration: Int): List<String> = listOf(META_KEY, "migration_log", "$migration")

internal fun migrate(root: Config, migrations: List<Migration>) {
    val fileVersion = root[VERSION_KEY] ?: 0
    if (fileVersion < migrations.size) {
        var oldMap = root.toMap()
        for (migration in fileVersion until migrations.size) {
            val newMap = oldMap.toMutableMap()

            migrations[migration].apply(newMap)

            applyMigration(root, migration, oldMap, newMap)

            root.update(VERSION_KEY, migration + 1)

            oldMap = newMap
            assert(oldMap == root.toMap())
        }
    } else if (fileVersion > migrations.size) {
        for (migration in fileVersion - 1 downTo migrations.size) {
            rollbackMigration(root, migration)
            root.update(VERSION_KEY, migration)
        }
    }
}

private fun applyMigration(root: Config, migration: Int, oldMap: Map<String, Any?>, newMap: Map<String, Any?>) {
    val migrationLog = root.createSubConfig()

    for ((key, oldValue) in oldMap) {
        if (key !in newMap) {
            migrationLog.update(listOf("changed", key), oldValue)
            root.purge<Any?>(key.split("."))
        } else {
            val newValue = newMap[key]
            if (newValue != oldValue) {
                migrationLog.update(listOf("changed", key), oldValue)
                root.update(key, newValue)
            }
        }
    }

    val added = mutableListOf<String>()
    for ((key, newValue) in newMap) {
        if (key !in oldMap) {
            added.add(key)
            root.update(key, newValue)
        }
    }
    if (added.isNotEmpty()) {
        migrationLog.update(listOf("added"), added)
    }

    if (!migrationLog.isEmpty) {
        root.update(migrationLogKey(migration), migrationLog)
    }
}

private fun rollbackMigration(root: Config, migration: Int) {
    val migrationLog = root.purge<Config?>(migrationLogKey(migration)) ?: return
    val added = migrationLog.get<List<String>>(listOf("added")) ?: emptyList()
    val changed = migrationLog.get<Config>(listOf("changed"))?.valueMap() ?: emptyMap()

    for (key in added) {
        root.purge<Any?>(key.split("."))
    }
    for ((key, oldValue) in changed) {
        root.update(key, oldValue)
    }
}

/** Removes the value at the given key as well as any now-empty intermediate nodes. */
private fun <T> Config.purge(keys: List<String>): T? {
    return if (keys.size > 1) {
        val child = get<Any?>(listOf(keys[0])) as? Config ?: return null
        val removed = child.purge<T>(keys.subList(1, keys.size))
        if (child.isEmpty) {
            remove<Any?>(listOf(keys[0]))
        }
        removed
    } else {
        remove(keys)
    }
}

/** Converts the nested [Config] into a flat [Map]. Does not include any [META_KEY] entries. */
private fun Config.toMap(): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()
    fun visit(config: Config, prefix: String) {
        for ((key, value) in config.valueMap()) {
            if (key == META_KEY) continue
            if (value is Config) {
                visit(value, "$prefix$key.")
            } else {
                result[prefix + key] = value
            }
        }
    }
    visit(this, "")
    return result
}
