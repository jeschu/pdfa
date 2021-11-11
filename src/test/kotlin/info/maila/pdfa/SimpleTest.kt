package info.maila.pdfa

import info.maila.logging.log
import mu.KotlinLogging
import org.verapdf.core.EncryptedPdfException
import org.verapdf.core.ValidationException
import org.verapdf.pdfa.Foundries
import org.verapdf.pdfa.PdfBoxFoundryProvider
import org.verapdf.pdfa.flavours.PDFAFlavour
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test

class SimpleTest {

    @Test
    fun `SimpleTest`() {

        val countTotal = AtomicInteger(0)
        val countCompilant = AtomicInteger(0)
        val countNotCompilant = AtomicInteger(0)
        val countUnableToValidate = AtomicInteger(0)
        val countValidationError = AtomicInteger(0)
        val countParseError = AtomicInteger(0)
        val countEncrypted = AtomicInteger(0)

        VeraPdf.ensureInitialised()
        val flavour = PDFAFlavour.PDFA_1_B
        val validator = Foundries.defaultInstance().createValidator(flavour, /* log success */ false)
        resources(dir = "pdf", filter = "*.pdf").forEach { path ->

            countTotal.incrementAndGet()

            val file = path.toFile()
            LOGGER.info { "parsing '$file' ..." }
            try {
                Foundries.defaultInstance().createParser(file).use { parser ->
                    try {
                        val result = validator.validate(parser)
                        if (result.isCompliant) {
                            countCompilant.incrementAndGet()
                            LOGGER.info("  => compilant")
                        } else {
                            countNotCompilant.incrementAndGet()
                            LOGGER.info("  => NOT COMPILANT")
                        }
                    } catch (e: ValidationException) {
                        countUnableToValidate.incrementAndGet()
                        LOGGER.warn(e) { "  => unable to validate: ${e.localizedMessage}" }
                    } catch (e: RuntimeException) {
                        countValidationError.incrementAndGet()
                        LOGGER.error(e) { "  => Error validating PDF: ${e.localizedMessage}" }
                    }
                }
            } catch (e: EncryptedPdfException) {
                countEncrypted.incrementAndGet()
                LOGGER.info { "  => PDF is encrypted" }
            } catch (e: RuntimeException) {
                countParseError.incrementAndGet()
                LOGGER.error(e) { "  => Error parsing PDF: ${e.localizedMessage}" }
            }
        }

        LOGGER.info(
            """
                         total: ${countTotal.get()}
                     compilant: ${countCompilant.get()}
                 not compilant: ${countNotCompilant.get()}
                     encrypted: ${countEncrypted.get()}
            unable to validate: ${countUnableToValidate.get()}
              validation error: ${countValidationError.get()}
                   parse error: ${countParseError.get()}
        """.trimIndent()
        )

    }

    companion object {
        val LOGGER = KotlinLogging.logger { }
    }

}