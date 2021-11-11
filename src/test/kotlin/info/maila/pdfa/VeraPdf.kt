package info.maila.pdfa

import info.maila.logging.log
import mu.KotlinLogging
import org.verapdf.pdfa.PdfBoxFoundryProvider

object VeraPdf {

    init {
        PdfBoxFoundryProvider.initialise()
            .log(KotlinLogging.logger { }) { trace { "VeraPdf initialised" } }
    }

    fun ensureInitialised() {}

}