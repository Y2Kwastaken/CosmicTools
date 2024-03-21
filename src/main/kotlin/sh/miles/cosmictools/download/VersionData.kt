package sh.miles.cosmictools.download

data class VersionData(val version: String, val link: String?) {
    val fileVersion: String = "v${version.split(".").joinToString("")}"

}
