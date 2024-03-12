package sh.miles.cosmictools.util;

/**
 * A record full of all needed CosmicReach information
 *
 * @param version the cosmic reach version
 */
public record CosmicReachInfo(String version) {

    public String fileVersionName() {
        return "v" + String.join("", version.split("\\."));
    }

}
