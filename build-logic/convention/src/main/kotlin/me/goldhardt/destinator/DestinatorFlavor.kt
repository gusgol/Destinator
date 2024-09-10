@file:Suppress("EnumEntryName")

package me.goldhardt.destinator

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

enum class FlavorDimension {
    environment
}

enum class DestinatorFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val default: Boolean = false
) {
    dev(FlavorDimension.environment, applicationIdSuffix = ".dev", default = true),

    /**
     * In case we ever wanted other flavors, e.g. different environment:
     *
     *     beta(FlavorDimension.audience, applicationIdSuffix = ".beta"),
     *     prod(FlavorDimension.audience)
     */

}

/**
 * Configures the product flavors for the app.
 * @param commonExtension The Android extension to configure.
 * @param flavorConfiguration The configuration block for each flavor. Useful if the module's gradle file needs any customization
 */
fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfiguration: ProductFlavor.(flavor: DestinatorFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.environment.name
        productFlavors {
            DestinatorFlavor.values().forEach {
                create(it.name) {
                    dimension = it.dimension.name
                    flavorConfiguration(this, it)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        isDefault = it.default
                        if (it.applicationIdSuffix != null) {
                            applicationIdSuffix = it.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}