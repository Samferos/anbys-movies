package iut.s4.sae.ui

import android.content.Context
import android.content.ContextWrapper
import java.util.*

class LanguageContextWrapper(base: Context) : ContextWrapper(base) {

    companion object {

        fun wrap(context: Context, language: String): ContextWrapper {
            if (language.isBlank()) return LanguageContextWrapper(context)
            val config = context.resources.configuration
            val sysLocale = config.locales[0]

            if (sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                config.setLocale(locale)
            }
            val updatedContext = context.createConfigurationContext(config)
            return LanguageContextWrapper(updatedContext)
        }
    }
}
