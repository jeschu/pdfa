package info.maila.logging

import mu.KLogger

/**
 * Diese Erweiterungsfunktion dient der Unterstützung des Loggings.
 *
 * Verwendung:
 *
 * `{beliebiges Objekt}.log(LOGGER) { obj -> debug { "Erzeugung einer Log-Ausgabe: $obj" } }`
 *
 * Bsp.: `UUID.randomUUID().toString().log(LOGGER).{ uuid -> debug { "The new UUID: `$uuid" } }
 *
 * Der Aufruf ist die Kurzform von
 * `val uuid = UUID.randomUUID().toString()`
 * `LOGGER.debug { "The new UUID: `$uuid" }`
 * `return uuid`
 */
inline fun <T> T.log(logger: KLogger, call: KLogger.(T) -> Any?): T = apply { logger.call(this) }