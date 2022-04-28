package it.polimi.ingsw;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Objects;

public abstract class OptionHandler {
    private final Option op;
    private final boolean isHelp;
    public OptionHandler(Option op, boolean isHelp) {
        this.op = op;
        this.isHelp = isHelp;
    }

    public Option getOp() {
        return op;
    }

    public boolean getIsHelp() {
        return isHelp;
    }

    abstract public int manageOption (CommandLine cmd, Options opts);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionHandler that = (OptionHandler) o;
        return isHelp == that.isHelp && op.equals(that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, isHelp);
    }
}