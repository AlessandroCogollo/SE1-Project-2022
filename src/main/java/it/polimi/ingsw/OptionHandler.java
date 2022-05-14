package it.polimi.ingsw;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Objects;

/**
 * Abstract Class for add to all option a method manage option and clean the code
 */
public abstract class OptionHandler {

    private final Option op;

    private final boolean isHelp;

    /**
     * Constructor of class
     * @param op Option
     * @param isHelp boolean is help option
     */
    public OptionHandler(Option op, boolean isHelp) {
        this.op = op;
        this.isHelp = isHelp;
    }

    /**
     * Getter
     * @return the Option
     */
    public Option getOp() {
        return op;
    }

    /**
     * Getter
     * @return is help option
     */
    public boolean getIsHelp() {
        return isHelp;
    }

    /**
     * Method for manage the option if its used
     * @param cmd is the parsed command line for know if other options are used
     * @param opts is the list of all possible option that can be used
     * @return <pre>
     *      - Error invalid arguments or other error not specified /result = -1 <br>
     *      - Do nothing /result = 0 <br>
     *      - Help /result = 100 <br>
     *      - Done something /result > 100 <br>
     *      - Done something but something isn't completely correct /result > 500 <br>
     *
     *      es: <br>
     *      101 start client with server ip and port <br>
     *      102 start client without server ip and port, must have a server with udp broadcast of his address <br>
     *      103 start server with port given       503 ip arg was ignored <br>
     *      104 start server without port given    504 ip arg was ignored <br>
     *      </pre>
     */
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