package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.Wizard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class WizardController extends Controller{

    private Wizard tempWizard = null;

    public WizardController(Gui main, String resource) {
        super(main, resource);
    }

    @FXML
    public Pane usernameStage;
    @FXML
    public Button continueWizard;
    @FXML
    public ImageView sorcerer;
    @FXML
    public ImageView king;
    @FXML
    public ImageView witch;
    @FXML
    public ImageView wise;
    @FXML
    public ImageView flowersQueen;

    public void initialize() {

        if (this.continueWizard != null) {
            this.continueWizard.setDisable(true);
            this.continueWizard.setDefaultButton(true);
        }
    }

    @FXML
    void flowersEntered(MouseEvent event) {
        if (tempWizard == null || !tempWizard.equals(Wizard.Sorcerer))
            this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.King))
            this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Wise))
            this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Witch))
            this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.flowersQueen.setEffect(new DropShadow(50, Color.FORESTGREEN));
    }

    @FXML
    void kingEntered(MouseEvent event) {
        if (tempWizard == null || !tempWizard.equals(Wizard.Sorcerer))
            this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.king.setEffect(new DropShadow(50, Color.GOLD));
        if (tempWizard == null || !tempWizard.equals(Wizard.Wise))
            this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Witch))
            this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))
            this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void sorcererEntered(MouseEvent event) {
        this.sorcerer.setEffect(new DropShadow(50, Color.GREENYELLOW));
        if (tempWizard == null || !tempWizard.equals(Wizard.King))
            this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Wise))
            this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Witch))
            this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))
            this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wiseEntered(MouseEvent event) {
        if (tempWizard == null || !tempWizard.equals(Wizard.Sorcerer))
            this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.King))
            this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.wise.setEffect(new DropShadow(50, Color.DEEPSKYBLUE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Witch))
            this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))
            this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void witchEntered(MouseEvent event) {
        if (tempWizard == null || !tempWizard.equals(Wizard.Sorcerer))
            this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.King))
            this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Wise))
            this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.witch.setEffect(new DropShadow(50, Color.VIOLET));
        if (tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))
            this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wizardExited(MouseEvent event) {
        if (tempWizard == null || !tempWizard.equals(Wizard.Sorcerer))
            this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.King))
            this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Wise))
            this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Witch))
            this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if (tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))
            this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void SelectFlowersQueen(MouseEvent event) {
        flowersEntered(event);
        tempWizard = Wizard.Flowers_Queen;
        this.continueWizard.setDisable(false);
    }

    @FXML
    void SelectKing(MouseEvent event) {
        kingEntered(event);
        tempWizard = Wizard.King;
        this.continueWizard.setDisable(false);
    }

    @FXML
    void SelectSorcerer(MouseEvent event) {
        sorcererEntered(event);
        tempWizard = Wizard.Sorcerer;
        this.continueWizard.setDisable(false);
    }

    @FXML
    void SelectWise(MouseEvent event) {
        wiseEntered(event);
        tempWizard = Wizard.Wise;
        this.continueWizard.setDisable(false);
    }

    @FXML
    void SelectWitch(MouseEvent event) {
        witchEntered(event);
        tempWizard = Wizard.Witch;
        this.continueWizard.setDisable(false);
    }

    public void wizardSelected(ActionEvent actionEvent) {
        if (this.tempWizard != null)
            this.main.selectedWizard(actionEvent, tempWizard);
    }
}
