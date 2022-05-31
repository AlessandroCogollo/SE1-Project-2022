package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.ModelMessage.PlayerSerializable;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public abstract class GraphicV2 implements Graphic{

    public abstract void startGraphic ();

    @Override
    public abstract void setFirst (boolean first);
    @Override
    public abstract void setDone(boolean done);
    @Override
    public abstract String askString (String askMessage) throws InterruptedException, IOException;
    @Override
    public abstract void displayError (String errorMessage);
    @Override
    public abstract void displayMessage (String message);



    /**
     * Ask the player to choose a valid wizard
     * @return the wizard chosen to the caller
     */
    @Override
    public abstract Wizard getWizard() throws IOException, InterruptedException;

    /**
     * Ask the player to choose a username
     * @return the username chosen to the caller
     */
    public abstract String getUsername() throws IOException, InterruptedException;

    /**
     * Ask the player to choose the player number of the game, 2, 3 or 4
     * @return the number chosen
     */
    @Override
    public abstract int getNumOfPlayers() throws IOException, InterruptedException;

    /**
     * Ask the player to choose the gamemode of the game: 0 normal, 1 advanced
     * @return the number chosen
     */
    public abstract int getGameMode() throws IOException, InterruptedException;


    /**
     * Stop any io method that is running throwing some exception
     */
    @Override
    public abstract void stopInput();




    private int askInteger (@Nullable String askMessage, @Nullable String errorMessage) throws InterruptedException, IOException {
        int temp = 0;
        boolean done = false;

        while (!done  && !Thread.currentThread().isInterrupted()){
            String s = askString(askMessage);
            try {
                temp = Integer.parseInt(s);
            }catch (NumberFormatException e){
                if (askMessage != null){
                    displayError(errorMessage);
                }
                continue;
            }
            done = true;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: Thread Interrupted");

        return temp;
    }

    private int askColor (@Nullable String askMessage, @Nullable String errorMessage, int @Nullable [] container) throws IOException, InterruptedException {
        int color = -1;
        while ((color < 0 || color > 4 || (container != null && container[color] == 0)) && !Thread.currentThread().isInterrupted()){
            color = askInteger(askMessage, errorMessage);
            if (color < 0 || color > 4 || (container != null && container[color] == 0))
                displayError(errorMessage);
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
                displayError(errorMessage);
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askIsland interrupted");

        return destination;
    }

    //need to display all his assistant and the assistant already played by other player
    public PlayAssistantMessage askAssistant(ModelMessage model, int playerId) throws IOException, InterruptedException {

        //get the assistant list of this player from the model
        List<Integer> right = Arrays.stream(model.getPlayerById(playerId).getAssistantDeck()).boxed().toList();


        int choose = -1;
        while (!right.contains(choose) && !Thread.currentThread().isInterrupted()){
            choose = askInteger("Select your Assistant from the list above", "Please select a valid assistant");
            if (!right.contains(choose))
                displayError("Please select a valid assistant");
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askAssistant interrupted");

        return new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", choose);
    }

    //need to display his school and the islands
    public MoveStudentMessage askStudentMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {


        int[] entrance = model.getPlayerById(playerId).getSchool().getCopyOfEntrance();

        int chooseColor = askColor("Choose the color of the student to move, using the number of the left of the color", "please insert a valid students that you have", entrance);

        int destination = -2;

        while (destination != -1 && !model.isIslandIdValid(destination) && !Thread.currentThread().isInterrupted()){
            destination = askInteger("Choose if you want to move the students to your room (-1) or to an island (use his id)", "Insert -1 or a valid island id");
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askMovement interrupted");

        return new MoveStudentMessage(Errors.NO_ERROR, "Moved a students", chooseColor, destination);
    }

    //need to see all the model, and if present the active character
    public MoveMotherNatureMessage askMNMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {

        //show if there is a character active
        int aC = model.getActiveCharacterId();

        if (model.getGameMode() == 1 && aC != -1) {
            System.out.println("There is an active character: " + aC); //todo remove and done somewhere else
        }

        int movement = -1;
        int max = Objects.requireNonNull(Assistant.getAssistantByValue(model.getPlayerById(playerId).getActiveAssistant())).getMaxMovement();
        if (model.getGameMode() == 1 && aC == 9) //is postman
            max += 2;

        System.out.println("Debug: You can move for max " + max + " position");

        while (!Thread.currentThread().isInterrupted() && (movement < 1 || movement > max)){
            movement = askInteger("Insert the number of movement for mother nature", "insert a number between 1 and " + max);
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askMNMovement interrupted");

        return new MoveMotherNatureMessage(Errors.NO_ERROR, "Moved Mother Nature", movement);
    }

    //need to see only the clouds
    public ChooseCloudMessage askCloud(ModelMessage model, int playerId) throws IOException, InterruptedException {

        int cloud = -1;

        while (!Thread.currentThread().isInterrupted() && (!model.isCloudValid(cloud))){
            cloud = askInteger("Insert the id of cloud to take the students", "insert a valid cloud id");
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCloud interrupted");

        return new ChooseCloudMessage(Errors.NO_ERROR, "Chose cloud", cloud);
    }

    //actual coins, characters playable
    public boolean wantToPlayCharacter(ModelMessage model, int playerId) throws IOException, InterruptedException {

        List<CharacterSerializable> list = model.getCharacterList();

        int playerCoins = model.getPlayerById(playerId).getCoins();

        if (playerCoins < 1)
            return false;

        boolean canPlayCharacter = false;

        for (CharacterSerializable c: list){
            int cost = (c.isUsed()) ? (c.getCost() + 1) : c.getCost();
            if (playerCoins >= cost){
                canPlayCharacter = true;
                break;
            }
        }

        if (!canPlayCharacter)
            return false;

        boolean choise = false;

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
            answer = askString("Do you want to play a character? (Yes/No)");
            if (correctYes.contains(answer))
                choise = true;
            else if (correctNo.contains(answer))
                choise = false;
            else{
                displayError("Insert a valid response (Yes, yes, y, No, no, n)");
                answer = null;
            }
        }
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askCharacter interrupted");

        return choise;
    }

    //need to see all the model, characters active also
    public PlayCharacterMessage askCharacter(ModelMessage model, int playerId) throws IOException, InterruptedException {

        int characterId = -1;

        while (characterId == -1 && !Thread.currentThread().isInterrupted()){
            characterId = askInteger("Choose the id of character that you want to play", "Choose a valid character");
            CharacterSerializable c = model.getCharacterById(characterId);
            if (c == null){
                displayError("Choose a valid character");
                characterId = -1;
                continue;
            }
            int cost = (c.isUsed()) ? (c.getCost() + 1) : c.getCost();
            PlayerSerializable p = model.getPlayerById(playerId);
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

                int[] t = model.getPlayerById(playerId).getSchool().getCopyOfEntrance();
                int[] en = Arrays.copyOf(t, t.length);

                t = model.getPlayerById(playerId).getSchool().getCopyOfRoom();
                int[] ro = Arrays.copyOf(t, t.length);

                while (i < number && !Thread.currentThread().isInterrupted()){

                    int temp = askColor("Choose the " + (i + 1) + "students to remove from the entrance and add to the room", "Insert a valid students that you have in the entrance", en);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askBard interrupted");

                    x[i << 1] = temp;
                    en[temp]--;

                    temp = askColor("Choose the " + (i + 1) + "students to remove from the room and add to the entrance", "Insert a valid students that you have in the room", ro);

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
                obj = new int[1];
                obj[0] = askColor("Choose a color that will be ignored during the calc of influence", "Insert a valid color between 0 and 4", null);
            }
            case 5 -> {
                //herald calc influence of an island
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

                int[] t = model.getPlayerById(playerId).getSchool().getCopyOfEntrance();
                int[] en = Arrays.copyOf(t, t.length);

                int[] st = character.getStudents();

                while (i < number && !Thread.currentThread().isInterrupted()){

                    int temp = askColor("Choose the " + (i + 1) + "students to remove from the the jester and add to the entrance", "Insert a valid students that is on jester", st);

                    if (Thread.currentThread().isInterrupted())
                        throw new InterruptedException("Cli: askJester interrupted");

                    x[i << 1] = temp;
                    st[temp]--;

                    temp = askColor("Choose the " + (i + 1) + "students to remove from the entrance and add to the jester", "Insert a valid students that you have in your entrance", en);

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
                obj[0] = askColor("Choose the students to add to your room", "Insert a valid students present in this card", stu);
            }
            case 11 -> {
                int color = -1;
                while ((color < 0 || color > 4) && !Thread.currentThread().isInterrupted()){
                    color = askInteger("Choose the color for thief effects", "Insert a valid color between 0 and 4");
                    if (color < 0 || color > 4 )
                        displayError("Insert a valid color between 0 and 4");
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











    /**
     * Send the information about the model
     * @param model model to display or the update of it
     */
    @Override
    public abstract void updateModel(ModelMessage model);



}
