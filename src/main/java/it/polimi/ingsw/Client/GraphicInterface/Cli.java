package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.*;
import it.polimi.ingsw.Server.Model.Island;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

//todo stop scanner after shutdown
public class Cli implements Graphic{

    private final BufferedReader input = new BufferedReader(new InputStreamReader(new InterruptibleInputStream(System.in)));
    private String userName = null;

    //private Wizard wizard = null;


    //ask primary method

    private String askString (@Nullable String askMessage, @Nullable String errorMessage) throws IOException, InterruptedException {
        if (askMessage != null){
            displayMessage(askMessage);
        }
        String s = null;
        boolean done = false;
        while (!done && !Thread.currentThread().isInterrupted()){
            s = this.input.readLine();
            if (s == null || s.isEmpty() || s.isBlank()){
                if (askMessage != null){
                    displayMessage(errorMessage);
                }
                continue;
            }
            done = true;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: Thread Interrupted");

        return s;
    }

    private int askInteger (@Nullable String askMessage, @Nullable String errorMessage) throws IOException, InterruptedException {
        int temp = 0;
        boolean done = false;

        while (!done  && !Thread.currentThread().isInterrupted()){
            String s = askString(askMessage, errorMessage);
            try {
                temp = Integer.parseInt(s);
            }catch (NumberFormatException e){
                if (askMessage != null){
                    displayMessage(errorMessage);
                }
                continue;
            }
            done = true;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: Thread Interrupted");

        return temp;
    }

    private int askColor (@Nullable String askMessage, @Nullable String errorMessage, int[] container) throws IOException, InterruptedException {
        int color = -1;
        while ((color < 0 || color > 4 || container[color] == 0) && !Thread.currentThread().isInterrupted()){
            color = askInteger(askMessage, errorMessage);
            if (color < 0 || color > 4 || container[color] == 0)
                System.out.println(errorMessage);
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askColor interrupted");

        return color;
    }

    private int askIsland (@Nullable String askMessage, @Nullable String errorMessage, ModelMessage model) throws IOException, InterruptedException {
        int destination = -1;

        while (!model.isIslandIdValid(destination) && !Thread.currentThread().isInterrupted()){
            destination = askInteger(askMessage, errorMessage);
            if (!model.isIslandIdValid(destination))
                System.out.println(errorMessage);
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askIsland interrupted");

        return destination;
    }


    @Override
    public void SetFirst(boolean first) {

    }

    //override
    @Override
    public void displayMessage(String message) {
        System.out.println("Cli: " + message);
    }

    @Override
    public Wizard getWizard() throws IOException, InterruptedException {
        Wizard w = null;
        while (w == null && !Thread.currentThread().isInterrupted()){

            String s = askString("Insert your Wizard", "Please insert a valid name of wizard");

            try {
                w = Wizard.valueOf(s);
            } catch (IllegalArgumentException e) {
                displayMessage("Please insert a valid name of wizard");
            }
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getWizard interrupted");

        return w;
    }

    @Override
    public String getUsername() throws IOException, InterruptedException {
        String user = askString("Insert your Username", "Insert a valid username");

        this.userName = user;

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getUsername interrupted");

        return user;
    }

    @Override
    public int getNumOfPLayer() throws IOException, InterruptedException {
        int numOfPlayer = -1;

        while (numOfPlayer == -1 && !Thread.currentThread().isInterrupted()){

            int temp = askInteger("Insert the number of player for this game", "Insert a valid number between 2 and 4");

            if (temp < 2 || temp > 4){
                displayMessage("Insert a valid number between 2 and 4");
                continue;
            }

            numOfPlayer = temp;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getPlayerNumber interrupted");

        return numOfPlayer;
    }

    @Override
    public int getGameMode() throws IOException, InterruptedException {
        int gameMode = -1;
        while (gameMode == -1 && !Thread.currentThread().isInterrupted()){

            int temp = askInteger("Insert the game mode for this game, 0 normal 1, advanced", "Insert a valid number between 0 and 1");

            if (temp < 0 || temp > 1){
                displayMessage("Insert a valid number between 0 and 1");
                continue;
            }

            gameMode = temp;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getGameMode interrupted");

        return gameMode;
    }

    @Override
    public PlayAssistantMessage askAssistant(ModelMessage model, int playerId) throws IOException, InterruptedException {
        displayModel(model);
        //cannot play character in planning phase

        List<Integer> right = Arrays.stream(displayAssistant(model, playerId)).boxed().toList();


        int choose = -1;
        while (!right.contains(choose) && !Thread.currentThread().isInterrupted()){
            choose = askInteger("Select your Assistant from the list above", "Please select a valid assistant");
            if (!right.contains(choose))
                System.out.println("Please select a valid assistant");
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askAssistant interrupted");

        return new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", choose);
    }

    @Override
    public MoveStudentMessage askStudentMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {
        displayModel(model);

        int[] entrance = displayEntrance(model, playerId);

        int chooseColor = askColor("Choose the color of the student to move, using the number of the left of the color", "please insert a valid students that you have", entrance);

        displayRoom(model, playerId);

        displayIslands (model);

        int destination = -2;

        while (destination != -1 && !model.isIslandIdValid(destination) && !Thread.currentThread().isInterrupted()){
            destination = askInteger("Choose if you want to move the students to your room (-1) or to an island (use his id)", "Insert -1 or a valid island id");
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askMovement interrupted");

        return new MoveStudentMessage(Errors.NO_ERROR, "Moved a students", chooseColor, destination);
    }

    @Override
    public MoveMotherNatureMessage askMNMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {
        displayModel(model);

        displayIslands(model);

        //show if there is a character active
        int aC = model.getActiveCharacterId();
        if (model.getGameMode() == 1 && aC != -1) {
            System.out.println("There is an active character");
            displayCharacter(model.getCharacterById(aC));
        }

        int movement = -1;
        int max = Objects.requireNonNull(Assistant.getAssistantByValue(model.getPlayerById(playerId).getActiveAssistant())).getMaxMovement();
        if (model.getGameMode() == 1 && model.getActiveCharacterId() == 9) //is postman
            max += 2;

        System.out.println("You can move for max " + max + " position");

        while (!Thread.currentThread().isInterrupted() && (movement < 1 || movement > max)){
            movement = askInteger("Insert the number of movement for mother nature", "insert a number between 1 and " + max);
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askMNMovement interrupted");

        return new MoveMotherNatureMessage(Errors.NO_ERROR, "Moved Mother Nature", movement);
    }

    @Override
    public ChooseCloudMessage askCloud(ModelMessage model, int playerId) throws IOException, InterruptedException {
        displayModel(model);

        displayCloud (model);

        int cloud = -1;

        while (!Thread.currentThread().isInterrupted() && (!model.isCloudValid(cloud))){
            cloud = askInteger("Insert the id of cloud to take the students", "insert a valid cloud id");
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCloud interrupted");

        return new ChooseCloudMessage(Errors.NO_ERROR, "Chose cloud", cloud);
    }

    @Override
    public PlayCharacterMessage askCharacter(ModelMessage model, int playerId) throws IOException, InterruptedException {
        displayModel(model);
        displayCharacters(model);
        System.out.println("You have " + model.getPlayerById(playerId).getCoins() + " coins");

        boolean use = false;

        Collection<String> correctYes = new ArrayList<>(3);
        correctYes.add("Yes");
        correctYes.add("yes");
        correctYes.add("y");

        Collection<String> correctNo = new ArrayList<>(3);
        correctNo.add("No");
        correctNo.add("no");
        correctNo.add("n");

        String answer = null;
        while (answer == null && !Thread.currentThread().isInterrupted()){
            answer = askString("Do you want to play a character? (Yes/No)", "Insert a valid response (Yes, yes, y, No, no, n)");
            if (correctYes.contains(answer))
                use = true;
            else if (correctNo.contains(answer))
                use = false;
            else{
                System.out.println("Insert a valid response (Yes, yes, y, No, no, n)");
                answer = null;
            }
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCharacter interrupted");

        if (!use)
            return null;

        int characterId = -1;

        while (characterId == -1 && !Thread.currentThread().isInterrupted()){
            characterId = askInteger("Choose the id of character that you want to play", "Choose a valid character");
            CharacterSerializable c = model.getCharacterById(characterId);
            if (c == null){
                System.out.println("Choose a valid character");
                characterId = -1;
                continue;
            }
            int cost = (c.isUsed()) ? (c.getCost() + 1) : c.getCost();
            PlayerSerializable p = model.getPlayerById(playerId);
            if (p.getCoins() < cost){
                System.out.println("You don't have enough coins for play this character, you have: " + p.getCoins() + " coins and this character costs " + cost + " coins");
                characterId = -1;
            }
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCharacter interrupted");

        CharacterSerializable character = model.getCharacterById(characterId);
        displayCharacter(character);

        int[] obj = null;
        switch (characterId){
            case 0 -> {
                //apothecary - ban card
                displayIslands(model);

                int destination = askIsland("Choose the island where put the ban card", "Insert a valid island id", model);

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askApothecary interrupted");

                obj = new int[1];
                obj[0] = destination;
            }
            case 1 -> {
                //bard - swap between entrance and room
                int number = -1;

                while (number == -1 && !Thread.currentThread().isInterrupted()){
                    number = askInteger("How many students you want to swap, 1, or 2?", "Insert a valid number, 1 or 2");
                    if (number != 1 && number != 2){
                        System.out.println("Insert a valid number, 1 or 2");
                        number = -1;
                    }
                }

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askBard interrupted");

                int i = 0;

                int[] x = new int[number << 1];

                int[] en = displayEntrance(model, playerId);
                int[] ro = displayRoom(model, playerId);


                System.out.println("Choose the students to swap");

                while (i < number && !Thread.currentThread().isInterrupted()){

                    int temp = askColor("Choose the " + (i + 1) + "students to remove from the entrance and add to the room", "Insert a valid students that you have in the entrance", en);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askBard interrupted");

                    x[i << 1] = temp;
                    en[temp]--; //todo verify don't break anything

                    temp = askColor("Choose the " + (i + 1) + "students to remove from the room and add to the entrance", "Insert a valid students that you have in the room", ro);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askBard interrupted");

                    x[(i << 1) + 1] = temp;
                    ro[temp]--; //todo verify don't break anything

                    i++;
                }
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askBard interrupted");

                obj = x;
            }
            case 2 -> {

                //cleric - move a students from the character to an island

                int[] x = new int[2];

                int[] students = character.getStudents();

                int color = askColor("Chose the students from the cleric to move to an island", "Insert a valid students that is on the card", students);

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askCleric interrupted");

                x[0] = color;

                int island = askIsland("Choose where to move the students selected", "Insert a valid island id", model);

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askCleric interrupted");

                x[1] = island;

                obj = x;

            }
            case 3 -> {
                //cook - color that not count as influence

                int color = -1;
                while ((color < 0 || color > 4) && !Thread.currentThread().isInterrupted()){
                    color = askInteger("Choose a color that will be ignored during the calc of influence", "Insert a valid color between 0 and 4");
                    if (color < 0 || color > 4 )
                        System.out.println("Insert a valid color between 0 and 4");
                }
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askCook interrupted");

                obj = new int[1];
                obj[0] = color;
            }
            //herald - calc influence on a island
            case 5 -> {
                obj = new int[1];
                obj[0] = askIsland("Choose the island where calc the influence", "Insert a valid island", model);
            }
            case 6 -> {
                //jester - swap between jester and entrance

                int number = -1;

                while (number == -1 && !Thread.currentThread().isInterrupted()){
                    number = askInteger("How many students you want to swap, 1, 2 or 3?", "Insert a valid number, 1, 2 or 3");
                    if (number < 1 || number > 3){
                        System.out.println("Insert a valid number, 1, 2 or 3");
                        number = -1;
                    }
                }

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askJester interrupted");

                int i = 0;

                int[] x = new int[number << 1];

                int[] en = displayEntrance(model, playerId);
                int[] st = character.getStudents();


                System.out.println("Choose the students to swap");

                while (i < number && !Thread.currentThread().isInterrupted()){

                    int temp = askColor("Choose the " + (i + 1) + "students to remove from the the jester and add to the entrance", "Insert a valid students that is on jester", st);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askJester interrupted");

                    x[i << 1] = temp;
                    st[temp]--; //todo verify don't break anything

                    temp = askColor("Choose the " + (i + 1) + "students to remove from the entrance and add to the jester", "Insert a valid students that you have in your entrance", en);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askJester interrupted");

                    x[(i << 1) + 1] = temp;
                    en[temp]--; //todo verify don't break anything

                    i++;
                }
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askBard interrupted");

                obj = x;
            }
            case 10 -> {
                int[] stu = character.getStudents();
                obj = new int[1];
                obj[0] = askColor("Choose the students to add to your room", "Insert a valid students present in this card", stu);
            }
            case 11 -> {
                int color = -1;
                while ((color < 0 || color > 4) && !Thread.currentThread().isInterrupted()){
                    color = askInteger("Choose the color for thief effects", "Insert a valid color between 0 and 4");
                    if (color < 0 || color > 4 )
                        System.out.println("Insert a valid color between 0 and 4");
                }
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askThief interrupted");

                obj = new int[1];
                obj[0] = color;
            }
            default -> {}
        }

        return new PlayCharacterMessage(Errors.NO_ERROR, "Played character", characterId, obj);
    }

    @Override
    public void stopInput() {
        try {
            this.input.close();
        } catch (IOException e) {
            System.err.println("Cli: Error while closing the input stream");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void displayModel(ModelMessage model) {
        System.out.println();
        System.out.println();
        System.out.println("############################## Model ##############################");
        System.out.println();
        //print game information
        displayIslands(model);
        System.out.println("###### PROFESSORS ######");
        System.out.println(Arrays.toString(model.getProfessorsList()));
        System.out.println("########################");
        System.out.println("###### BAG ######");
        System.out.println("Students in bag: " + Arrays.stream(model.getBag()).sum()  + " -> " + Arrays.toString(model.getBag()));
        System.out.println("########################");

        System.out.println("###### SCHOOL ######");
        for (PlayerSerializable pl : model.getPlayerList()){
            String towerColor = null;
            int tColor = pl.getTowerColor();
            if (tColor == -1) {
                towerColor = " ";
            }
            else if (tColor == 1){
                towerColor = " Black";
            }
            else if (tColor == 2){
                towerColor = " White";
            }
            else if (tColor == 3){
                towerColor = " Grey";
            }
            System.out.println("#" + pl.getId() + " with " + pl.getSchool().getTowers() + towerColor + " towers");

            System.out.println("# ENTRANCE -> " + Arrays.toString(pl.getSchool().getCopyOfEntrance()));
            System.out.println("# ROOM -> " + Arrays.toString(pl.getSchool().getCopyOfRoom()));
        }
        System.out.println("###########################################################################");


        System.out.println();
        System.out.println();
    }




    //other display
    private void displayCharacters(ModelMessage model) {
        System.out.println();
        displayMessage(this.userName + ", these are the playable characters");
        System.out.println();

        for (CharacterSerializable c : model.getCharacterList()){
            displayCharacter(c);
        }


        System.out.println();
    }

    private void displayCharacter (CharacterSerializable c){
        int id = c.getId();
        int cost = (c.isUsed()) ? (c.getCost() + 1) : c.getCost();
        StringBuilder s = new StringBuilder(c.getName() + " id " + id + " cost " + cost);
        switch (id){
            case 0 -> s.append(" with ").append(c.getBanCard()).append(" ban card");
            case 3 -> {
                int color = c.getColorId();
                if (color != -1){
                    s.append(" with active color ").append(Objects.requireNonNull(Color.getColorById(color)).name());
                }
            }
            case 2, 6, 10 -> s.append(" with this students -> ").append(Arrays.toString(c.getStudents()));
            default -> {}
        }
        System.out.println(s);
    }

    private void displayCloud(ModelMessage model) {
        List <CloudSerializable> clouds = model.getCloudList();
        System.out.println();
        displayMessage(this.userName + ", these are the clouds");
        for (CloudSerializable c : clouds){
            System.out.println("Cloud " + c.getId() + " -> " + Arrays.toString(c.getDrawnStudents()));
        }


        System.out.println();
    }

    private void displayIslands(ModelMessage model) {
        System.out.println();

        System.out.println("#################### ISLANDS ###################");
        //printing islands
        List<Island> x = model.getIslandList();
        for(Island is : x){
            String towerColor = null;
            if (is.getTowerColor() == -1) {
                towerColor = " ";
            }
            else if (is.getTowerColor() == 1){
                towerColor = " Black";
            }
            else if (is.getTowerColor() == 2){
                towerColor = " White";
            }
            else if (is.getTowerColor() == 3){
                towerColor = " Grey";
            }
            System.out.println(
                    "{ID: " + is.getId() +
                            " , STUDENTS: " + Arrays.toString(is.getStudents()) +
                            " , TOWERS: " + is.getTowerCount()  + towerColor +
                            " , BAN CARD: " + is.getBanCard() +
                            "}" +
                            (model.getMotherNatureIslandId() == is.getId() ? "  <-- MOTHER NATURE" : ""));

        }
        System.out.println("#####################################################################");
        System.out.println();
    }

    private int[] displayRoom(ModelMessage model, int playerId) {
        displayMessage(this.userName + ", this is your room");
        System.out.println();

        PlayerSerializable actual = model.getPlayerById(playerId);
        int[] room = actual.getSchool().getCopyOfRoom();

        for (int i = 0; i < room.length; i++){
            System.out.println(i + ". " + Objects.requireNonNull(Color.getColorById(i)).name() + " " + room[i]);
        }
        System.out.println();
        return room;
    }

    private int[] displayEntrance (ModelMessage model, int playerId){
        displayMessage(this.userName + ", this is your entrance");
        System.out.println();

        PlayerSerializable actual = model.getPlayerById(playerId);

        int[] entrance = actual.getSchool().getCopyOfEntrance();

        for (int i = 0; i < entrance.length; i++){
            System.out.println(i + ". " + Objects.requireNonNull(Color.getColorById(i)).name() + " " + entrance[i]);
        }
        System.out.println();

        return entrance;
    }

    private int[] displayAssistant (ModelMessage model, int playerId) {
        displayMessage(this.userName + ", this are your assistant");
        System.out.println();

        PlayerSerializable actual = model.getPlayerById(playerId);

        //null pointer if no player in the game
        int[] ass = actual.getAssistantDeck();

        for (int a : ass){
            Assistant x = Assistant.getAssistantByValue(a);
            System.out.println("# "  + Objects.requireNonNull(x).name() + ": Value " + x.getValue() + " Max Movement " + x.getMaxMovement());
        }
        System.out.println();
        return ass;
    }

}
