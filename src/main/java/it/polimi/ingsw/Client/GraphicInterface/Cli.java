package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.*;
import it.polimi.ingsw.Server.Model.Island;
import it.polimi.ingsw.Server.Model.School;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;



public class Cli implements Graphic {

    private final BufferedReader input = new BufferedReader(new InputStreamReader(new InterruptibleInputStream(System.in)));

    protected DataCollector dC = null;

    private static Map<Integer, String> colorTowerMap = null;

    public Cli() {
        if (colorTowerMap == null){
            colorTowerMap = new HashMap<>(4);
            colorTowerMap.put(-1, "");
            colorTowerMap.put(1, "Black");
            colorTowerMap.put(2, "White");
            colorTowerMap.put(3, "Grey");
        }
    }

    public DataCollector getDataCollector() {
        return dC;
    }

    public void startGraphic() {
        this.dC = new DataCollector(this);

        //set now the callback for setting all the username info
        this.dC.getFirst(this::setInfo);
    }

    public void setInfo() {

        int first = this.dC.getFirst(null); //the value is not -1

        //todo min lenght
        String username;
        try {
            username = askString("Insert your username:");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Cli interrupted");
            return;
        }

        this.dC.setUsername(username);

        Wizard wizard;

        try {
            wizard = askWizard("Insert Your wizard");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Cli interrupted");
            return;
        }

        this.dC.setWizard(wizard);

        if (first == 0) {

            int gamemode = -1, numofplayer = -1;

            while ((gamemode < 0 || gamemode > 1 || numofplayer < 2 || numofplayer > 4) && !Thread.currentThread().isInterrupted()) {
                try {
                    gamemode = askInteger("Insert the gamemode 0 normal 1 advanced");
                    numofplayer = askInteger("Insert the number of player: 2, 3 or 4");

                    if (gamemode < 0 || gamemode > 1)
                        displayError("Gamemode must be 0 or 1");
                    if (numofplayer < 2 || numofplayer > 4)
                        displayError("Number of PLayer must be 2 or 3 or 4");

                } catch (IOException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Cli interrupted");
                    return;
                }
            }

            this.dC.setNumOfPlayers(numofplayer);
            this.dC.setGameMode(gamemode);
        }

        //setting the next callback
        this.dC.getDone(this::doneCallback);
    }


    public void doneCallback() {
        int done = this.dC.getDone(null); //the value is not -1

        if (done == 0) {
            displayError(this.dC.getErrorData());
            setInfo();
            return;
        }

        //info sent correctly

        this.dC.setCallbackForModel(this::modelCallback);
    }

    public void modelCallback() {

        ModelMessage model = this.dC.getModel();

        displayAllModel(model, this.dC);

        System.out.println();

        if(model.gameIsOver()){
            displayMessage(this.dC.getStandardWinMessage());
            return;
        }

        if (this.dC.isThisMyTurn()) {
            try {
                this.dC.setNextMove(askMove());
            } catch (IOException | InterruptedException e) {
                System.out.println("Interrupted when asking the move CLI");
                Thread t = Thread.currentThread();
                while (!t.isInterrupted())
                    t.interrupt();
            }
        }
        else {
            System.out.println("Is the turn of " + this.dC.getUsernameOfCurrentPlayer() + " with id " + model.getCurrentPlayerId());
            System.out.println("You have to wait");
        }
    }

    private ClientMessage askMove() throws IOException, InterruptedException {
        ModelMessage model = this.dC.getModel();
        int myId = this.dC.getId();

        Phase p = Phase.valueOf(model.getActualPhase());
        ActionPhase aP = ActionPhase.valueOf(model.getActualActionPhase());

        ClientMessage cM = null;
        if (Phase.Planning.equals(p)){
            cM =  askAssistant(model, myId);
        }
        else {
            if (model.getGameMode() == 1 && model.getActiveCharacterId() == -1){
                if (wantToPlayCharacter(model, myId))
                    cM = askCharacter(model, myId);
            }
            if (cM == null){
                switch (aP) {
                    case MoveStudent -> {
                        displayMessage("You have to move other " + model.getStudentsToMove());
                        cM = askStudentMovement(model, myId);
                    }
                    case MoveMotherNature -> cM = askMNMovement(model, myId);
                    case ChooseCloud -> cM = askCloud(model, myId);
                }
            }
        }
        System.out.println("Sending " + cM);
        return cM;
    }

    private ClientMessage askCloud(ModelMessage model, int myId) throws IOException, InterruptedException {

        displayClouds(model.getCloudList(), this.dC);
        displayEntrance(model.getPlayerById(myId).getSchool().getCopyOfEntrance(), this.dC);

        int cloud = -1;
        while (!Thread.currentThread().isInterrupted() && !model.isCloudValid(cloud)){
            cloud = askInteger("Insert the id of cloud to take the students");
            if (!model.isCloudValid(cloud)) {
                displayError("insert a valid cloud id");
            }
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCloud interrupted");

        return new ChooseCloudMessage(Errors.NO_ERROR, "Chose cloud", cloud);
    }

    private ClientMessage askMNMovement(ModelMessage model, int myId) throws InterruptedException, IOException {

        displayIslands(model.getIslandList(), model.getMotherNatureIslandId(), this.dC);
        displayProfessors(model.getProfessorsList(), this.dC);
        displaySchool(model.getPlayerById(myId).getSchool(), model.getPlayerById(myId).getTowerColor(), this.dC);

        //show if there is a character active
        int aC = model.getActiveCharacterId();
        if (model.getGameMode() == 1 && aC != -1) {
            System.out.println("## There is an active character ##");
            displayCharacter(model.getCharacterById(aC), this.dC);
        }

        int movement = -1;
        int max = Objects.requireNonNull(Assistant.getAssistantByValue(model.getPlayerById(myId).getActiveAssistant())).getMaxMovement();

        if (model.getGameMode() == 1 && model.getActiveCharacterId() == 9) //is postman
            max += 2;

        System.out.println("You can move for max " + max + " position");
        while (!Thread.currentThread().isInterrupted() && (movement < 1 || movement > max)){
            movement = askInteger("Insert the number of movement for mother nature");
            if (movement < 1 || movement > max) {
                displayError("insert a number between 1 and " + max);
            }
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askMNMovement interrupted");
        return new MoveMotherNatureMessage(Errors.NO_ERROR, "Moved Mother Nature", movement);
    }

    private ClientMessage askStudentMovement(ModelMessage model, int myId) throws InterruptedException, IOException {

        int[] entrance = model.getPlayerById(myId).getSchool().getCopyOfEntrance();

        displayEntrance(entrance, this.dC);

        int chooseColor = askExistingColor("Choose the color of the student to move from the entrance, using the number near the color", entrance);


        displayRoom(model.getPlayerById(myId).getSchool().getCopyOfRoom(), this.dC);
        displayIslands(model.getIslandList(), model.getMotherNatureIslandId(), this.dC);

        int destination = -2;
        while (destination != -1 && !model.isIslandIdValid(destination) && !Thread.currentThread().isInterrupted()){
            destination = askInteger("Choose if you want to move the students to your room (-1) or to an island (use his id)");
            if (destination != -1 && !model.isIslandIdValid(destination)){
                displayError("Insert -1 or a valid island id");
            }
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askMovement interrupted");

        return new MoveStudentMessage(Errors.NO_ERROR, "Moved a students", chooseColor, destination);
    }

    private ClientMessage askAssistant(ModelMessage model, int myId) throws InterruptedException, IOException {

        int[] deck = model.getPlayerById(myId).getAssistantDeck();

        displayAssistants(deck, this.dC);

        List<Integer> right = Arrays.stream(deck).boxed().toList();

        int choose = -1;
        while (!right.contains(choose) && !Thread.currentThread().isInterrupted()){
            choose = askInteger("Select your Assistant from the list above");
            if (!right.contains(choose))
                displayError("Please select a valid assistant");
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askAssistant interrupted");

        return new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", choose);
    }

    public boolean wantToPlayCharacter(ModelMessage model, int myId) throws IOException, InterruptedException {

        if (!this.dC.canPlayCharacter())
            return false;

        boolean choice = false;

        Collection<String> correctYes = new ArrayList<>(3);
        correctYes.add("Yes");
        correctYes.add("yes");
        correctYes.add("y");

        Collection<String> correctNo = new ArrayList<>(3);
        correctNo.add("No");
        correctNo.add("no");
        correctNo.add("n");


        System.out.println("## You have " + model.getPlayerById(myId).getCoins() + " coins ##");
        displayCharacters(model.getCharacterList(), model.getActiveCharacterId(), this.dC);

        String answer = null;
        while (answer == null && !Thread.currentThread().isInterrupted()){
            answer = askString("Do you want to play a character? (Yes/No)");
            if (correctYes.contains(answer))
                choice = true;
            else if (correctNo.contains(answer))
                choice = false;
            else{
                displayError("Insert a valid response (Yes, yes, y, No, no, n)");
                answer = null;
            }
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCharacter interrupted");

        return choice;
    }

    private ClientMessage askCharacter(ModelMessage model, int myId) throws InterruptedException, IOException {

        int characterId = -1;

        System.out.println("## You have " + model.getPlayerById(myId).getCoins() + " coins ##");
        displayCharacters(model.getCharacterList(), model.getActiveCharacterId(), this.dC);

        while (characterId == -1 && !Thread.currentThread().isInterrupted()){
            characterId = askInteger("Choose the id of character that you want to play");
            CharacterSerializable c = model.getCharacterById(characterId);
            if (c == null){
                displayError("Choose a valid character");
                characterId = -1;
                continue;
            }
            int cost = (c.isUsed()) ? (c.getCost() + 1) : c.getCost();
            PlayerSerializable p = model.getPlayerById(myId);
            if (p.getCoins() < cost){
                displayError("You don't have enough coins for play this character, you have: " + p.getCoins() + " coins and this character costs " + cost + " coins");
                characterId = -1;
            }
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCharacter interrupted");

        CharacterSerializable character = model.getCharacterById(characterId);

        int[] obj = null;
        switch (characterId){
            case 0 -> {
                //apothecary - ban card

                int destination = askIsland("Choose the island where put the ban card", model);

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askApothecary interrupted");

                obj = new int[1];
                obj[0] = destination;
            }
            case 1 -> {
                //bard - swap between entrance and room
                int number = -1;

                while (number == -1 && !Thread.currentThread().isInterrupted()){
                    number = askInteger("How many students you want to swap, 1, or 2?");
                    if (number != 1 && number != 2){
                        displayError("Insert a valid number, 1 or 2");
                        number = -1;
                    }
                }

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askBard interrupted");

                int i = 0;

                int[] x = new int[number << 1];

                int[] t = model.getPlayerById(myId).getSchool().getCopyOfEntrance();
                int[] en = Arrays.copyOf(t, t.length);

                t = model.getPlayerById(myId).getSchool().getCopyOfRoom();
                int[] ro = Arrays.copyOf(t, t.length);

                while (i < number && !Thread.currentThread().isInterrupted()){

                    int temp = askExistingColor("Choose the " + (i + 1) + "students to remove from the entrance and add to the room", en);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askBard interrupted");

                    x[i << 1] = temp;
                    en[temp]--;

                    temp = askExistingColor("Choose the " + (i + 1) + "students to remove from the room and add to the entrance", ro);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askBard interrupted");

                    x[(i << 1) + 1] = temp;
                    ro[temp]--;

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

                int color = askExistingColor("Chose the students from the cleric to move to an island", students);

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askCleric interrupted");

                x[0] = color;

                int island = askIsland("Choose where to move the students selected", model);

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askCleric interrupted");

                x[1] = island;

                obj = x;

            }
            case 3 -> {
                //cook - color that not count as influence
                obj = new int[1];
                obj[0] = askColor("Choose a color that will be ignored during the calc of influence");
            }
            case 5 -> {
                //herald calc influence of an island
                obj = new int[1];
                obj[0] = askIsland("Choose the island where calc the influence", model);
            }
            case 6 -> {
                //jester - swap between jester and entrance

                int number = -1;

                while (number == -1 && !Thread.currentThread().isInterrupted()){
                    number = askInteger("How many students you want to swap, 1, 2 or 3?");
                    if (number < 1 || number > 3){
                        displayError("Insert a valid number, 1, 2 or 3");
                        number = -1;
                    }
                }

                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askJester interrupted");

                int i = 0;

                int[] x = new int[number << 1];

                int[] t = model.getPlayerById(myId).getSchool().getCopyOfEntrance();
                int[] en = Arrays.copyOf(t, t.length);

                int[] st = character.getStudents();

                while (i < number && !Thread.currentThread().isInterrupted()){

                    int temp = askExistingColor("Choose the " + (i + 1) + "students to remove from the the jester and add to the entrance", st);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askJester interrupted");

                    x[i << 1] = temp;
                    st[temp]--;

                    temp = askExistingColor("Choose the " + (i + 1) + "students to remove from the entrance and add to the jester", en);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askJester interrupted");

                    x[(i << 1) + 1] = temp;
                    en[temp]--;

                    i++;
                }
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException("Cli: askBard interrupted");

                obj = x;
            }
            case 10 -> {
                int[] stu = character.getStudents();
                obj = new int[1];
                obj[0] = askExistingColor("Choose the students to add to your room", stu);
            }
            case 11 -> {
                obj = new int[1];
                obj[0] = askColor("Choose the color for thief effects");
            }
            default -> {}
        }

        return new PlayCharacterMessage(Errors.NO_ERROR, "Played character", characterId, obj);
    }

    @Override
    public void displayMessage(String message) {
        if (message == null)
            return;
        System.out.println("Cli: " + message);
    }

    public void displayError(String errorMessage) {
        if (errorMessage == null)
            return;
        System.out.println("Cli ERROR during input: " + errorMessage);
    }

    @Override
    public void stopGraphic() {

        try {
            this.input.close();
        } catch (IOException e) {
            System.err.println("Cli: Error while closing the input stream");
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public String askString(@Nullable String askMessage) throws InterruptedException, IOException {
        String s = null;
        boolean done = false;
        while (!done && !Thread.currentThread().isInterrupted()) {
            if (askMessage != null)
                displayMessage(askMessage);
            s = this.input.readLine();
            if (s == null || s.isEmpty() || s.isBlank()) {
                if (askMessage != null) {
                    displayError("The value can't be blank");
                }
                continue;
            }
            done = true;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: Thread Interrupted");

        return s;
    }

    public int askInteger(@Nullable String askMessage) throws InterruptedException, IOException {
        int temp = 0;
        boolean done = false;

        while (!done && !Thread.currentThread().isInterrupted()) {
            String s = askString(askMessage);
            try {
                temp = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                displayError("Insert a valid Number");
                continue;
            }
            done = true;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: Thread Interrupted");

        return temp;
    }

    public Wizard askWizard(@Nullable String askMessage) throws IOException, InterruptedException {
        Wizard w = null;

        while (w == null && !Thread.currentThread().isInterrupted()) {
            String s = askString(askMessage);
            try {
                w = Wizard.valueOf(s);
            } catch (IllegalArgumentException e) {
                displayError("Insert a valid wizard");
            }
        }

        return w;
    }

    public int askColor (@Nullable String askMessage) throws IOException, InterruptedException {

        int color = -1;

        while ((color < 0 || color > 4) && !Thread.currentThread().isInterrupted()){
            color = askInteger(askMessage);
            if (color < 0 || color > 4){
                displayError("Insert a valid color number: 0, 1, 2, 3, 4");
            }
        }

        return color;
    }

    public int askExistingColor (@Nullable String askMessage, int[] container) throws IOException, InterruptedException {

        boolean done = false;
        int existingColor = -1;

        while (!done && !Thread.currentThread().isInterrupted()){
            existingColor = askColor(askMessage);
            if (container[existingColor] > 0){
                done = true;
            }
            else {
                displayError("Insert a color that is in this container (container[colorChoose] > 0): " + Arrays.toString(container));
            }
        }

        return existingColor;
    }

    public int askIsland (@Nullable String askMessage, ModelMessage model) throws IOException, InterruptedException {
        int destination = -1;

        displayIslands(model.getIslandList(), model.getMotherNatureIslandId(), this.dC);

        while (!model.isIslandIdValid(destination) && !Thread.currentThread().isInterrupted()){
            destination = askInteger(askMessage);
            if (!model.isIslandIdValid(destination))
                displayError("Insert a valid Island id");
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askIsland interrupted");

        return destination;
    }


    private static void printTotHash(int number) {
        for (int i = 0; i < number; i++)
            System.out.print('#');
    }



    public static void displayAllModel(ModelMessage model, DataCollector data) {

        System.out.println();
        System.out.println();

        printTotHash(25);
        System.out.print(" MODEL ");
        printTotHash(25);
        System.out.println();

        displayIslands(model.getIslandList(), model.getMotherNatureIslandId(), data);

        displayProfessors(model.getProfessorsList(), data);

        displayBag(model.getBag(), data);

        displayClouds(model.getCloudList(), data);

        displayPlayers(model.getPlayerList(), data);

        try {
            if (data.getGameMode() == 1)
                displayCharacters(model.getCharacterList(), model.getActiveCharacterId(), data);
        } catch (InterruptedException ignored) {}
    }

    private static void displayIslands(List<Island> islands, int motherNatureId, DataCollector data) {

        System.out.println();
        System.out.println();

        printTotHash(15);
        System.out.print(" ISLANDS ");
        printTotHash(15);
        System.out.println();

        for (Island is : islands) {
            displayIsland(is, motherNatureId, data);
        }
    }

    private static void displayStudents (int[] students, DataCollector data){
        System.out.println();

        printTotHash(1);
        System.out.print(" STUDENTS ");
        printTotHash(1);
        System.out.println();

        for (Color c: Color.values()){
            System.out.println(c + " (" + c.getIndex() + ")" + "       " + students[c.getIndex()] + "   students");
        }
    }

    private static void displayIsland (Island is, int motherNatureId, DataCollector data) {

        System.out.println();

        printTotHash(5);
        System.out.print(" ISLAND " + is.getId() + " ");
        printTotHash(5);
        if (is.getId() == motherNatureId)
            System.out.print("  <-- MOTHER NATURE");
        System.out.println();

        displayStudents(is.getStudents(), data);

        String towerColor = colorTowerMap.get(is.getTowerColor());

        System.out.println();
        System.out.println("TOWERS: " + is.getTowerCount() + " " + towerColor);
        System.out.println("BAN CARD: " + is.getBanCard());

        System.out.println();
    }

    private static void displayProfessors(int[] professorList, DataCollector data) {

        System.out.println();
        System.out.println();

        printTotHash(15);
        System.out.print(" PROFESSORS ");
        printTotHash(15);
        System.out.println();

        for (Color c: Color.values()) {

            int id = professorList[c.getIndex()];
            String username = data.getUsernames().get(id);

            System.out.println(c + " Professor " +
                    ( (id == -1)
                            ? "is not property of anyone"
                            : ("is property of " + username + "with id " + id)));
        }
    }

    private static void displayBag(int[] bag, DataCollector data) {

        System.out.println();
        System.out.println();

        printTotHash(15);
        System.out.print(" BAG with " + Arrays.stream(bag).sum() + " students ");
        printTotHash(15);
        System.out.println();

        displayStudents(bag, data);
    }

    private static void displayPlayers(List<PlayerSerializable> playerList, DataCollector data) {

        System.out.println();

        printTotHash(15);
        System.out.print(" PLAYERS ");
        printTotHash(15);
        System.out.println();

        int id = data.getId();
        PlayerSerializable thisPlayer = null;
        for (PlayerSerializable p : playerList){
            if (id == p.getId()){
                thisPlayer = p;
                break;
            }
        }

        if (thisPlayer != null)
            displayPlayer(thisPlayer, true, data);

        for (PlayerSerializable p : playerList){
            if (!p.equals(thisPlayer)){
                displayPlayer(p, false, data);
            }
        }
    }

    private static void displayPlayer(PlayerSerializable player, boolean isThisPlayer, DataCollector data) {

        int id = player.getId();
        String user = data.getUsernames().get(id);

        printTotHash(5);
        if (isThisPlayer)
            System.out.print(" YOU (" + user + " with id " + id + ") ");
        else
            System.out.print(" PLAYER " + user + " with id " + id + " ");
        printTotHash(5);
        System.out.println();

        System.out.println();

        try {
            if (data.getGameMode() == 1)
                System.out.println("Coins: " + player.getCoins());
        } catch (InterruptedException ignored) {}

        System.out.println();

        Assistant active = Assistant.getAssistantByValue(player.getActiveAssistant());
        if (active != null){
            System.out.println("Active Assistant: " + active + " value " + active.getValue() + " max distance " + active.getMaxMovement());
        }

        displayAssistants(player.getAssistantDeck(), data);

        displaySchool(player.getSchool(), player.getTowerColor(), data);
    }

    private static void displayAssistants(int[] assistantDeck, DataCollector data) {

        System.out.println();

        printTotHash(5);
        System.out.print(" ASSISTANT ");
        printTotHash(5);
        System.out.println();

        System.out.println();

        for (int a: assistantDeck){
            Assistant assistant  = Assistant.getAssistantByValue(a);
            if (assistant != null)
                displayAssistant(assistant, data);
        }
    }

    private static void displayAssistant (Assistant assistant, DataCollector data){
        System.out.println("## "  + assistant + ": Value " + assistant.getValue() + " Max Movement " + assistant.getMaxMovement() + "##");
    }

    private static void displaySchool(School school, int towerColor, DataCollector data) {

        System.out.println();

        printTotHash(5);
        System.out.print(" SCHOOL ");
        printTotHash(5);
        System.out.println();

        System.out.println();

        System.out.println("TOWERS: " + school.getTowers() + " " + colorTowerMap.get(towerColor));

        displayEntrance(school.getCopyOfEntrance(), data);

        displayRoom(school.getCopyOfRoom(), data);

        System.out.println();
    }

    private static void displayEntrance(int[] entrance, DataCollector data) {

        System.out.println();

        printTotHash(2);
        System.out.print(" ENTRANCE ");
        printTotHash(2);
        System.out.println();

        displayStudents(entrance, data);
    }

    private static void displayRoom(int[] room, DataCollector data) {
        System.out.println();

        printTotHash(2);
        System.out.print(" ROOM ");
        printTotHash(2);
        System.out.println();

        displayStudents(room, data);
    }

    private static void displayClouds(List<CloudSerializable> cloudList, DataCollector data) {

        System.out.println();
        System.out.println();

        printTotHash(15);
        System.out.print(" CLOUDS ");
        printTotHash(15);
        System.out.println();

        for (CloudSerializable c : cloudList) {
            displayCloud(c, data);
        }
    }

    private static void displayCloud(CloudSerializable c, DataCollector data) {

        System.out.println();
        System.out.println();

        printTotHash(5);
        System.out.print(" CLOUD " + c.getId() + " ");
        printTotHash(5);
        System.out.println();

        displayStudents(c.getDrawnStudents(), data);
    }

    private static void displayCharacters(List<CharacterSerializable> characterList, int activeCharacterId, DataCollector data) {

        System.out.println();
        System.out.println();

        printTotHash(15);
        System.out.print(" CHARACTERS ");
        printTotHash(15);
        System.out.println();

        CharacterSerializable active = null;
        for (CharacterSerializable c: characterList){
            if (c.getId() == activeCharacterId){
                active = c;
                break;
            }
        }

        if (active != null){
            System.out.println("## There is an Active Character ##");
            System.out.println();
            displayCharacter(active, data);
        }

        System.out.println("## Other Playable Characters ##");
        System.out.println();

        for (CharacterSerializable c: characterList){
            if (!c.equals(active)){
                displayCharacter(c, data);
            }
        }
    }

    private static void displayCharacter (CharacterSerializable c, DataCollector data){

        System.out.println();

        int id = c.getId();

        printTotHash(5);
        System.out.print(" " + c.getName() + " with id " + id + " ");
        printTotHash(5);
        System.out.println();


        int cost = (c.isUsed()) ? (c.getCost() + 1) : c.getCost();

        System.out.println("COST: " + cost + " coins");
        System.out.println();

        switch (id){
            case 0 -> {
                System.out.println("BANCARD: " + c.getBanCard() + " ban card");
                System.out.println();
            }
            case 3 -> {
                Color color = Color.getColorById(c.getColorId());
                if (color != null){
                    System.out.println("ACTIVE COLOR: " + color);
                    System.out.println();
                }
            }
            case 2, 6, 10 -> displayStudents(c.getStudents(), data);
            default -> {}
        }
    }
}
