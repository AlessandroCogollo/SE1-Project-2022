package it.polimi.ingsw;

import org.apache.commons.cli.*;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collection;

//todo arguments sanitize
public class StarterHelper{

    private final Collection<OptionHandler> options;
    private boolean hasHelp; //help is a special option that if present in command, is the only one executed


    @VisibleForTesting StarterHelper() {
        this.options = new ArrayList<>();
        this.hasHelp = false;
    }

    @VisibleForTesting Options getOptions(){
        Options options = new Options();
        for (OptionHandler o: this.options)
            options.addOption(o.getOp());
        return options;
    }

    @VisibleForTesting boolean add(OptionHandler optionHandler) {
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

    @VisibleForTesting Collection<Integer> start (CommandLine cmd) {
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

    @VisibleForTesting static Collection<OptionHandler> getOptionHandlers (){

        Collection<OptionHandler> collection = new ArrayList<>(5);

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
                boolean temp = cmd.hasOption(port);

                if (temp == cmd.hasOption(ip)) {
                    if (temp) {
                        //if they are equal true we will start the client with given ip and port of the server
                        int portNumber = Integer.parseInt(cmd.getOptionValue('p'));
                        System.out.println("Starting client with server ip: " + cmd.getOptionValue("ip") + " and port: " + portNumber);
                        return 101;
                    }
                    //if they are equal false we will start the client and he will search for some udp message from the server
                    System.out.println("Starting default client");
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
                if (hasIP) //ip option in server is useless
                    System.out.println("Used -ip option with -s server option, ignored.");

                if (cmd.hasOption(opts.getOption("p"))){
                    //start the server at this port
                    int portNumber = Integer.parseInt(cmd.getOptionValue('p'));

                    System.out.println("Starting server with port: " + portNumber);
                    return hasIP ? 503 : 103;
                }
                System.out.println("Starting default server");
                return hasIP ? 504 : 104;
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
        collection.add(helpHandler);

        return collection;
    }

    private static void manageReturnValue(Collection<Integer> tested) {
        //manage arguments exception when the arguments used produce nothing
        boolean allZero = true;
        for (Integer i: tested){
            if (i != 0){
                allZero = false;
                break;
            }
        }
        if (allZero)
            System.out.println("The arguments passed hasn't done anything. Type -help or -h.");
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
     103 start server with port given       503 ip arg was ignored
     104 start server without port given    504 ip arg was ignored

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

        manageReturnValue(main.start(cmd));
    }
}