package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;

import java.io.*;

public class Cli implements Graphic{

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void displayMessage(String message) {
        System.out.println("Cli: " + message);
    }

    @Override
    public Wizard getWizard() {
        Wizard w = null;
        while (w == null){
            String s = null;
            try {
                System.out.println("Cli: Insert a wizard");
                s = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("IOException Cli line 24");
            }
            if (s != null) {
                try {
                    w = Wizard.valueOf(s);
                } catch (IllegalArgumentException e) {
                    System.out.println("Cli: Please insert a valid name of wizard");
                }
            }
        }
        return w;
    }

    @Override
    public String getUsername() {
        String user = null;
        while (user == null){
            String s = null;
            try {
                System.out.println("Cli: Insert your username");
                s = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("IOException Cli line 24");
            }
            user = s;
        }
        return user;
    }

    @Override
    public int getNumOfPLayer() {
        int numOfPlayer = -1;
        while (numOfPlayer == -1){
            String s = null;
            try {
                System.out.println("Cli: Insert the number of player for this game");
                s = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("IOException Cli line 24");
            }
            if (s != null) {
                int temp;
                try {
                    temp = Integer.parseInt(s);
                    if (temp < 2 || temp > 4)
                        throw new NumberFormatException();
                }catch (NumberFormatException e){
                    System.out.println("Cli: Insert a valid number between 2 and 4");
                    temp = -1;
                }
                if (temp != -1){
                    numOfPlayer = temp;
                }
            }
        }
        return numOfPlayer;
    }

    @Override
    public int getGameMode() {
        int gameMode = -1;
        while (gameMode == -1){
            String s = null;
            try {
                System.out.println("Cli: Insert the gamemode for this game, 0 normal 1, advanced");
                s = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("IOException Cli line 24");
            }
            if (s != null) {
                int temp;
                try {
                    temp = Integer.parseInt(s);
                    if (temp < 0 || temp > 1)
                        throw new NumberFormatException();
                }catch (NumberFormatException e){
                    System.out.println("Cli: Insert a valid number between 0 and 1");
                    temp = -1;
                }
                if (temp != -1){
                    gameMode = temp;
                }
            }
        }
        return gameMode;
    }
}
