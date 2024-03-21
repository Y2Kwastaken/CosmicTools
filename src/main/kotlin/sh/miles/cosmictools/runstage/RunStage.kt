package sh.miles.cosmictools.runstage

import joptsimple.OptionSet

/**
 * An Abstract RunStage which represents a stage in the running of CosmicTools
 */
interface RunStage {
    /**
     * Executes the stage
     *
     * @param propagate a map of data that can be passed between stages
     */
    fun runStage(options: OptionSet, propagate: MutableMap<String, Any>): Int
}
