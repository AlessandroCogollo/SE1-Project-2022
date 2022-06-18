package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Message.LobbyInfoMessage;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Class that simplifies the interaction between client main thread and cli or gui, all method of this class should be called only from
 */
public class GraphicHandler {

    private final String graphicType;
    private final static String[] rightTypes = {"Gui", "GUI", "Cli", "CLI"};

    protected DataCollector dataCollector = null;
    protected Graphic graphic = null;

    public GraphicHandler(String graphicType) {
        if (GraphicHandler.isValidString(graphicType))
            this.graphicType = graphicType;
        else
            throw new IllegalArgumentException("Graphic type string can be only one of this " + Arrays.toString(GraphicHandler.rightTypes));
    }

    public String getGraphicType() {
        return graphicType;
    }

    public DataCollector getDataCollector() {
        return dataCollector;
    }



    public void startGraphic () {
        if (this.graphicType.equals("Gui") || this.graphicType.equals("GUI")) {
            Gui.startGraphic();
            this.dataCollector = Gui.getDataCollector();
            this.graphic = this.dataCollector.getGraphicInstance();
        }
        else {
            Cli temp = new Cli();
            temp.startGraphic();
            this.graphic = temp;
            this.dataCollector = temp.getDataCollector();
        }
    }

    public void setFirst (boolean first){
        this.dataCollector.setFirst(first);
    }

    public void setDone (boolean done, @Nullable String message){
        this.dataCollector.setDone(done, message);
    }

    public void displayMessage (String message){
        this.graphic.displayMessage(message);
    }

    public void setGameData (LobbyInfoMessage gameData, int id){
        this.dataCollector.setGameData(gameData, id);
    }

    public void stopInput() {
        this.graphic.stopGraphic();
    }

    public void updateModel (ModelMessage model){
        this.dataCollector.setModel(model);
    }

    public static boolean isValidString (String s){
        return Arrays.stream(GraphicHandler.rightTypes).toList().contains(s);
    }

    public void setGraphicStopCallback (Runnable callback){
        this.dataCollector.setGraphicStopped(callback);
    }
}
