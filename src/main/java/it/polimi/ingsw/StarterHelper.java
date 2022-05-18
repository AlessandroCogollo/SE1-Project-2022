package it.polimi.ingsw;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GraphicInterface.Cli;
import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Server.Server;
import org.apache.commons.cli.*;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Cli argument parser Class, for start the server or the client
 */
/* todo
*   - arguments sanitize
*/
public class StarterHelper{

    private final Collection<OptionHandler> options;
    private static boolean run = true;
    private boolean hasHelp; //help is a special option that if present in command, is the only one executed


    /**
     * Class Constructor
     */
    public StarterHelper() {
        this.options = new ArrayList<>();
        this.hasHelp = false;
        run = true;
    }

    /**
     * Class Constructor
     */
    public StarterHelper(boolean debug) {
        this.options = new ArrayList<>();
        this.hasHelp = false;
        run = debug;
    }

    /**
     * Return the options Set for parse the args
     * @return The org.apache.commons.cli.Options Object
     */
    public Options getOptions(){
        Options options = new Options();
        for (OptionHandler o: this.options)
            options.addOption(o.getOp());
        return options;
    }

    /**
     * Add the OptionHandler to the collection, duplicates are not allowed and there can only be one help option
     * @param optionHandler the option handler to add
     * @return true for successful operation, false otherwise
     */
    public boolean add(OptionHandler optionHandler) {
        //can't have more than 1 help options
        if (optionHandler.getIsHelp()) {
            if (this.hasHelp) {
                System.out.println("Error, cannot have more than two help options");
                return false;
            }
            else {
                //in this case is unuseful to search for a duplicate
                this.hasHelp = true;
                options.add(optionHandler);
                return true;
            }
        }

        //search for duplicated
        boolean yetAdded = false;
        for (OptionHandler optHand : options){
            if (optionHandler.equals(optHand)) {
                yetAdded = true;
                break;
            }
        }

        if (yetAdded){
            System.out.println("Error, cannot have duplicates options");
            return false;
        }
        options.add(optionHandler);
        return true;
    }

    /**
     * Start to handle all options included in the args
     * @param cmd options in the args
     * @return A collection with all the return value of the OptionHandler.manageOption()
     */
    public Collection<Integer> start (CommandLine cmd) {
        Options opts = getOptions();
        Collection<Integer> returnValues;
        if (this.hasHelp) {
            for (OptionHandler o : options)
                if (o.getIsHelp() && cmd.hasOption(o.getOp())) {
                    returnValues = new ArrayList<>(1);
                    returnValues.add(o.manageOption(cmd, opts));
                    return returnValues;
                }
        }
        boolean atLeastOne = false;
        returnValues = new ArrayList<>(opts.getOptions().size());
        for (OptionHandler o : options)
            if (cmd.hasOption(o.getOp())) {
                atLeastOne = true;
                returnValues.add(o.manageOption(cmd, opts));
            }
        if (!atLeastOne)
            System.out.println("No argument passed, nothing done");
        return returnValues;
    }

    /**
     * Retrieve all the OptionHandlers of this Project
     * @return A collection of OptionHandler
     */
    public static Collection<OptionHandler> getOptionHandlers (){

        Collection<OptionHandler> collection = new ArrayList<>(6);


        Option client = Option.builder("c").longOpt("client").desc("Start a client").hasArg(false).build();
        OptionHandler clientHandler = new OptionHandler(client, false){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                if (cmd.hasOption(opts.getOption("s"))) {
                    System.out.println("Error can't have client option -c and server option -s both in a single command. Type -help or -h.");
                    return -1;
                }
                //used only -c option

                Option port = opts.getOption("p");
                Option ip = opts.getOption("ip");
                Option graphic = opts.getOption("g");
                boolean hasPort = cmd.hasOption(port);
                boolean hasIp = cmd.hasOption(ip);
                boolean hasGraphic = cmd.hasOption(graphic);

                //Possible graphic value
                String cli = "Cli";
                String gui = "Gui";


                if (hasPort == hasIp) {
                    if (hasIp) {
                        //if they are equal true we will start the client with given ip and port of the server
                        int portNumber = Integer.parseInt(cmd.getOptionValue('p'));
                        String serverIp = cmd.getOptionValue("ip");

                        if (hasGraphic){
                            String graphicString = cmd.getOptionValue("g");
                            if (cli.equals(graphicString)){
                                System.out.println("Starting client with server ip: " + serverIp + " and port: " + portNumber + " with Cli.");
                                startClient(serverIp, portNumber, new Cli());
                                return 101;
                            }
                            if (gui.equals(graphicString)){
                                System.out.println("Starting client with server ip: " + serverIp + " and port: " + portNumber + " with Gui.");
                                startClient(serverIp, portNumber, new Gui());
                                return 101;
                            }
                            System.out.println("Error illegal argument for client -c option. Type -help or -h.");
                            return -1;
                        }
                        //no graphic passed
                        System.out.println("Starting client with server ip: " + serverIp + " and port: " + portNumber + " with default Cli.");
                        startClient(serverIp, portNumber, new Cli());
                        return 101;
                    }

                    //if they are equal false we will start the client and he will search for some udp message from the server

                    if (hasGraphic){
                        String graphicString = cmd.getOptionValue("g");
                        if (cli.equals(graphicString)){
                            System.out.println("Starting default client with Cli.");
                            System.out.println("Not yet implemented please use -c with -ip and -p options");
                            return 101;
                        }
                        if (gui.equals(graphicString)){
                            System.out.println("Starting default client with Gui.");
                            System.out.println("Not yet implemented please use -c with -ip and -p options");
                            return 101;
                        }
                        System.out.println("Error illegal argument for client -c option. Type -help or -h.");
                        return -1;
                    }
                    //no graphic passed

                    System.out.println("Starting default client with default Cli");
                    System.out.println("Not yet implemented please use -c with -ip and -p options");
                    return 102;
                }
                //need both ip and port for client or no of them if using udp message
                System.out.println("Error illegal argument for client -c option. Type -help or -h.");
                return -1;
            }
        };



        Option server = Option.builder("s").longOpt("server").desc("Start the server").hasArg(false).build();
        OptionHandler serverHandler = new OptionHandler(server, false){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                if (cmd.hasOption(opts.getOption("c"))) {
                    System.out.println("Error can't have server option -s and client option -c both in a single command. Type -help or -h.");
                    return -1;
                }
                boolean hasIP = cmd.hasOption(opts.getOption("ip"));
                boolean hasGraphic = cmd.hasOption("g");
                if (hasIP) //ip option in server is useless
                    System.out.println("Used -ip option with -s server option, ignored.");
                if (hasGraphic) //p option in server is useless
                    System.out.println("Used -g option with -s server option, ignored.");

                if (cmd.hasOption(opts.getOption("p"))){
                    //start the server at this port
                    int portNumber = Integer.parseInt(cmd.getOptionValue('p'));

                    System.out.println("Starting server with port: " + portNumber);
                    startServer(portNumber);
                    return (hasIP || hasGraphic) ? 503 : 103;
                }
                System.out.println("Starting default server");
                System.out.println("Not yet implemented, use -s and -p for start the server");

                return (hasIP || hasGraphic) ? 504 : 104;
            }
        };



        Option port = Option.builder("p").longOpt("port").desc("Set the port of the server").hasArg().build();
        OptionHandler portHandler = new OptionHandler(port, false){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                //data options, do nothing
                return 0;
            }
        };



        Option ip = Option.builder("ip").longOpt("ipAddress").desc("Set the ip address of the server, is useful only with -c option").hasArg().build();
        OptionHandler ipHandler = new OptionHandler(ip, false){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                //data options, do nothing
                return 0;
            }
        };


        Option graphic = Option.builder("g").longOpt("graphic").desc("Set the graphic of the client, is useful only with -c option").hasArg().build();
        OptionHandler graphicHandler = new OptionHandler(graphic, false) {
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                //data options, do nothing
                return 0;
            }
        };



        Option help = new Option("h", "help", false, "Display this help list");
        OptionHandler helpHandler = new OptionHandler(help, true) {
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                String executableFileName = "App";
                String header = "Starting the server or the client for for Eriantys\n\n";
                String footer = "\nPlease report issues at https://github.com/AlessandroCogollo/ingsw2022-AM56/issues";

                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(executableFileName, header, opts, footer, true);

                return 100;
            }
        };



        collection.add(clientHandler);
        collection.add(serverHandler);
        collection.add(portHandler);
        collection.add(ipHandler);
        collection.add(graphicHandler);
        collection.add(helpHandler);

        return collection;
    }


    //possible starter
    private static void startServer(int port){
        if (run)
            new Server(port).start();
    }

    private static void startClient(String ip, int port, Graphic graphic){
        if (run)
            new Client(graphic, ip, port).start();
    }

    /*
    reminder for manage options return value
     - Error invalid arguments or other error not specified /result = -1
     - Do nothing /result = 0
     - Help /result = 100
     - Done something /result > 100
     - Done something but something isn't completely correct /result > 500

     es:
     101 start client with server ip and port
     102 start client without server ip and port, must have a server with udp broadcast of his address
     103 start server with port given       503 ip or g arg was ignored
     104 start server without port given    504 ip or g arg was ignored

    */

    /**
     * Main of the project
     * @param args default args
     */
    public static void main(String[] args) {

        //setting all options and relative handler
        Collection<OptionHandler> collection = getOptionHandlers();


        StarterHelper main = new StarterHelper();
        for (OptionHandler o: collection)
            main.add(o);


        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(main.getOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        main.start(cmd);
    }
}