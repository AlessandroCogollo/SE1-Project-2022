package it.polimi.ingsw;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GraphicInterface.TestingGraphicHandler;
import it.polimi.ingsw.Server.PortGetter;
import it.polimi.ingsw.Server.Server;
import org.apache.commons.cli.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StarterHelperTest {

    static Random rand = new Random(System.currentTimeMillis());

    private static long factorial (int x){
        if (x < 0) {
            System.out.println("Factorial of negative number");
            return 0;
        }
        if (x < 2)
            return 1;
        else
            return x * factorial(x - 1);
    }
    private static long combinationNoRep(int n, int k){
        if (n < 0 || k < 0){
            System.out.println("n or k can't be negative");
            return 0;
        }
        if (k > n){
            System.out.println("k can't be more than n");
            return 0;
        }
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    public static OptionHandler getTestOH (int number){
        if (number < 0)
            return null;
        String option = "t" + number;
        String description = "test" + number;
        Option temp = new Option(option, description);
        return new OptionHandler(temp, false){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                return 0;
            }
        };
    }
    public static Collection<OptionHandler> getOptionHandlers(int size) {
        Collection<OptionHandler> coll = new ArrayList<>(size);
        for (int i = 0; i < size; i++){
            OptionHandler tempH = getTestOH(i);
            coll.add(tempH);
        }
        return  coll;
    }


    //for generate all possible arguments

    private static Stream<Arguments> allPossibilities (Collection<String[]> possibleArgs){
        int length = possibleArgs.size();
        long possibilities = 0;
        for (int i = 1; i <= length; i++)
            possibilities += combinationNoRep(length, i);
        System.out.println("Testing over " + possibilities + ".");
        Collection<String[]> arg = new ArrayList<>((int) possibilities);
        for (int i = 1; i <= length; i++){ //length of string
            arg.addAll(combinationWithoutRepetition(possibleArgs, length, i));
        }
        /*for (String[] str: arg){
            printC (str);
        }*/
        return arg.stream().map(strings -> Arguments.of((Object) strings));
    }
    private static Collection<String> convert(Integer[] comb, List<String[]> values) {
        Collection<String> c = new ArrayList<>();
        for (Integer i: comb){
                if (i == 4){
                    String p = "-p";
                    int po = rand.nextInt(5000, 8000);
                    String stringPort = String.valueOf(po);
                    String[] portRandom = new String[]{p, stringPort};
                    c.addAll(Arrays.asList(portRandom));
                }
                else {
                    c.addAll(Arrays.asList(values.get(i)));
                }
        }
        return c;
    }
    private static <T> void printC (T[] c){

        int j;
        System.out.print("{ ");

        for ( j = 0; j < c.length; ++j )
            System.out.print((c[j]) + " , ");

        System.out.print( "\b\b}\n" );
    }
    private static boolean nextComb(Integer[] comb, int n, int k) {

        int i = k - 1;
        ++comb[i];
        while ((i > 0) && (comb[i] >= n - k + 1 + i)) {
            --i;
            ++comb[i];
        }

        if (comb[0] > n - k) /* Combination (n-k, n-k+1, ..., n) reached */
            return false; /* No more combinations can be generated */

    /* comb now looks like (..., x, n, n, n, ..., n).
    Turn it into (..., x, x + 1, x + 2, ...) */
        for (i = i + 1; i < k; ++i)
            comb[i] = comb[i - 1] + 1;

        return true;
    }
    private static Collection<String[]> combinationWithoutRepetition(Collection<String[]> possibleArg, int n, int k) {
        int size = possibleArg.size();
        if (k > n || size != n || k == 0){
            System.out.println("Error");
            return null;
        }
        Collection<String[]> collection = new ArrayList<>((int) combinationNoRep(n, k));

        List<String[]> values = possibleArg.stream().toList();
        /*for (String[] x: values){
            printC(x);
        }*/

        Integer[] comb = new Integer[k];
        int i;
        //first combination
        for (i = 0; i < k; i++)
            comb[i] = i;

        //printC(comb);

        Collection<String> combConverted = convert(comb, values);
        String[] temp = new String[combConverted.size()];
        i = 0;
        for (String str: combConverted)
            temp[i++] = str;
        collection.add(temp);



        while (nextComb(comb, n, k)){
            //printC(comb);
            combConverted = convert(comb, values);
            temp = new String[combConverted.size()];
            i = 0;
            for (String str: combConverted)
                temp[i++] = str;
            collection.add(temp);
        }
        return collection;
    }
    public static Stream<Arguments> argsMethod() {

        //testing over all possible sets of arguments
        String c = "-c";
        String s = "-s";
        String ip = "-ip";
        String stringIp = "localhost";
        String p = "-p";
        String stringPort = "8123";
        String h = "-h";
        String g = "-g";
        String stringGraphic = "Cli";
        Collection<String[]> collection = new ArrayList<>(5);
        collection.add(new String[]{c}); //0
        collection.add(new String[]{s}); //1
        collection.add(new String[]{h}); //2
        collection.add(new String[]{ip, stringIp}); //3
        collection.add(new String[]{p, stringPort}); //4
        collection.add(new String[]{g, stringGraphic}); //5

        return allPossibilities(collection);
    }

    @Test
    void getOptions() {

        StarterHelper x = new StarterHelper();
        Options t = new Options();
        Collection<OptionHandler> collection = getOptionHandlers(6);
        for (OptionHandler o: collection){
            x.add(o);
            t.addOption(o.getOp());
        }

        Collection<Option> expected = t.getOptions();
        Collection<Option> actual = x.getOptions().getOptions();

        assertEquals(expected.size(), actual.size());
        int i = expected.size();
        int size = i;
        Iterator<Option> e = expected.iterator();
        Iterator<Option> a = actual.iterator();
        Option[] ex = new Option[i];
        Option[] ac = new Option[i];
        while (i > 0){
            assertTrue(a.hasNext());
            assertTrue(e.hasNext());
            ex[i-1] = e.next();
            ac[i-1] = a.next();
            i--;
        }
        assertFalse(a.hasNext());
        assertFalse(e.hasNext());

        boolean allEqual = true;
        boolean find;
        for (i = 0; i < size; i++){
            find = false;
            for (int j = 0; j < size; j++){
                if (ex[i].equals(ac[j])){
                    find = true;
                    break;
                }
            }
            if (!find) {
                allEqual = false;
                break;
            }
        }
        assertTrue(allEqual);
    }

    @Test
    void add() {
        StarterHelper x = new StarterHelper();
        Collection<OptionHandler> collection = getOptionHandlers(6);
        for (OptionHandler o : collection){
            assertTrue(x.add(o));
        }

        //duplicates
        OptionHandler tempH = getTestOH(3);
        assertNotNull(tempH);
        assertFalse(x.add(tempH));


        //first help
        String option = "t" + 6;
        String description = "test" + 6;
        Option temp = new Option(option, description);
        tempH = new OptionHandler(temp, true){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                return 100;
            }
        };
        assertTrue(x.add(tempH));


        //second help
        option = "t" + 7;
        description = "test" + 7;
        temp = new Option(option, description);
        tempH = new OptionHandler(temp, true){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                return 100;
            }
        };
        assertFalse(x.add(tempH));

        //duplicate and second help
        option = "t" + 6;
        description = "test" + 6;
        temp = new Option(option, description);
        tempH = new OptionHandler(temp, true){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                return 100;
            }
        };
        assertFalse(x.add(tempH));
    }

    @Test
    void start() {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        Collection<OptionHandler> collection = getOptionHandlers(6);
        StarterHelper x = new StarterHelper();
        for (OptionHandler o : collection){
            assertTrue(x.add(o));
        }
        String[] args = {
                "-t1",
                "-t2",
                "-t3",
                "-t4",
                "-t0",
        };


        try {
            cmd = parser.parse(x.getOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Collection<Integer> c = x.start(cmd);
        assertEquals(5, c.size());
        for (Integer i: c)
            assertEquals(0, i);


        String[] args0 = { }; //empty

        try {
            cmd = parser.parse(x.getOptions(), args0);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        c = x.start(cmd);
        assertEquals(0, c.size());

        //added help
        String option = "t" + 6;
        String description = "test" + 6;
        Option temp = new Option(option, description);
        OptionHandler tempH = new OptionHandler(temp, true){
            @Override
            public int manageOption(CommandLine cmd, Options opts) {
                return 100;
            }
        };
        x.add(tempH);

        String[] argsH = {
                "-t1",
                "-t5",
                "-t3",
                "-t4",
                "-t6",
                "-t2",
                "-t0",
        };

        try {
            cmd = parser.parse(x.getOptions(), argsH);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        c = x.start(cmd);

        assertEquals(1, c.size());
        Iterator<Integer> it = c.iterator();
        assertTrue(it.hasNext());
        assertEquals(100, it.next());
    }


    @ParameterizedTest
    @MethodSource("argsMethod")
    void main (String[] args) throws InterruptedException {
        //testing only our arguments
        Collection<OptionHandler> collection = StarterHelper.getOptionHandlers();


        StarterHelper main = new StarterHelper(false);
        for (OptionHandler o : collection)
            main.add(o);


        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(main.getOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Collection<Integer> tested = main.start(cmd);

        boolean help = cmd.hasOption("h");
        boolean port = cmd.hasOption("p");
        boolean ip = cmd.hasOption("ip");
        boolean client = cmd.hasOption("c");
        boolean server = cmd.hasOption("s");
        boolean graphic = cmd.hasOption("g");


        if (help) {
            assertEquals(1, tested.size());
            Iterator<Integer> it = tested.iterator();
            assertTrue(it.hasNext());
            assertEquals(100, it.next());
            return;
        }
        if (client && server) {
            int minusOneFind = 0;
            for (Integer i : tested)
                if (i == -1) {
                    minusOneFind++;
                }
            assertEquals(2, minusOneFind);
            return;
        }
        if (client && (ip == port)) {
            boolean find = false;
            for (Integer i : tested)
                if (i == 101 || i == 102) {
                    find = true;
                    break;
                }
            assertTrue(find);
            return;
        }
        if (client) { //ip != port
            int minusOneFind = 0;
            for (Integer i : tested)
                if (i == -1) {
                    minusOneFind++;
                    break;
                }
            assertEquals(1, minusOneFind);
            return;
        }
        if (server) {
            boolean find = false;
            for (Integer i : tested)
                if (i == 103 || i == 104 || i == 503 || i == 504) {
                    find = true;
                    break;
                }
            assertTrue(find);
        }
        //ip and port doesn't need any test

        //added delay for random port resulting in bind error if too much close
        Thread.sleep(100);
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
    @Test
    void mainCoverage (){
        //done only for main coverage, real test will be done in main method
        String[] args0 = { }; //empty
        StarterHelper.main(args0);
        assertTrue(true);
    }

    /**
     * Test over a complete game from the start of the server and the clients to the game over using the testing cli that simulate a user
     */
    @Test
    void completeTest () throws InterruptedException {
        int port = PortGetter.getPort();
        Server server = new Server(port);

        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(server::start);

        Thread.sleep(10);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            TestingGraphicHandler tgh = new TestingGraphicHandler("Cli");
            tgh.startGraphic();
            Client c = new Client(tgh, "127.0.0.1", port);
            executorService.execute(c::start);
        }

        ex.shutdown();
        //now all the client are connected and the game is running
        assertTrue(ex.awaitTermination(10, TimeUnit.MINUTES));
    }
}