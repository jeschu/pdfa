package info.maila.pdfa

import java.nio.file.Files.newDirectoryStream
import java.nio.file.Paths

fun getResource(name: String) = Paths.get(Thread.currentThread().contextClassLoader.getResource(name).toURI())

fun resources(dir: String, filter: String) = newDirectoryStream(getResource(name = dir), filter)