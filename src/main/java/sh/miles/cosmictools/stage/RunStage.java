package sh.miles.cosmictools.stage;

import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a stage within the run cycle
 */
public interface RunStage {

    /**
     * Runs the current stage
     *
     * @param options   the options
     * @param propagate mutable map to propagate different datum to other stages
     * @return the exit code or -1 for none
     */
    int runStage(@NotNull final OptionSet options, @NotNull final Map<String, Object> propagate) throws Exception;

}
