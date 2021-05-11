package utility.global.wordlist

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 8/30/2017
 */
class WorldEntry internal constructor(
    /**
     * The activity of the world entry
     */
    val activity: String,
    /**
     * The ip of the world entry
     */
    val ip: String,
    /**
     * The id of the country that the world is in
     */
    val countryId: Int,
    /**
     * The flags for the world country, multiple ones sent to build different types of worlds [members/high risk]
     */
    val flag: Int,
    /**
     * The name of the country
     */
    val countryName: String,
    /**
     * If the country is members only
     */
    val isMembers: Boolean
)