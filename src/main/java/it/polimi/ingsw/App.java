package it.polimi.ingsw;

import org.apache.commons.cli.*;

//parsing of arguments
public class App {

    public static void main(String[] args) {

        String executableFileName = "App";

        Option client = Option.builder("c").longOpt("client").desc("Start a client").hasArg(false).build();
        Option server = Option.builder("s").longOpt("server").desc("Start the server").hasArg(false).build();
        Option port = Option.builder("p").longOpt("port").desc("Set the port of the server").hasArg().build();
        Option ip = Option.builder("ip").longOpt("ipAddress").desc("Set the ip address of the server, can only be use with -c option").hasArg().build();
        Option help = new Option("h", "help", false, "Display this help list");

        Options options = new Options();

        options.addOption(client);
        options.addOption(server);
        options.addOption(port);
        options.addOption(ip);

        options.addOption(help);

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        if (cmd.hasOption(help)) {
            //help

            String header = "Starting the server or the client for for Eriantys\n\n";
            String footer = "\nPlease report issues at https://github.com/AlessandroCogollo/ingsw2022-AM56/issues";

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(executableFileName, header, options, footer, true);
        }
        else {

            //todo parameter sanitize and reduce the line number in main function

            if (cmd.hasOption(server) && cmd.hasOption(client)) {
                //both -c and -s options

                System.out.println("Error can't have client option -c and server option -s both in a single command. Type -help or -h.");
            }
            else {
                if (cmd.hasOption(client)) {
                    //only -c option

                    if (!cmd.hasOption(port) && !cmd.hasOption(ip)) {
                        //default client search for udp message

                        System.out.println("Starting default client");
                    }
                    else {
                        if (cmd.hasOption(port) && cmd.hasOption(ip)) {
                            //client has server ip and port
                            int portNumber = Integer.parseInt(cmd.getOptionValue('p'));

                            System.out.println("Starting client with server ip: " + cmd.getOptionValue("ip") + " and port: " + portNumber);
                        }
                        else {
                            System.out.println("Error illegal argument for client -c option. Type -help or -h.");
                        }
                    }
                }
                else {
                    //only -s option or nothing

                    if (!cmd.hasOption(port) && !cmd.hasOption(ip)) {
                        //default server, get the ip and acquire a port then broadcast some udp message in local network

                        System.out.println("Starting default server");
                    }
                    else {
                        if (cmd.hasOption(ip)) {
                            System.out.println("Error can't use -ip option without -c option. Type -help or -h.");
                        }
                        else {
                            if (cmd.hasOption(port)){
                                int portNumber = Integer.parseInt(cmd.getOptionValue('p'));

                                System.out.println("Starting server with port: " + portNumber);
                            }
                        }
                    }
                }
            }
        }
    }
}