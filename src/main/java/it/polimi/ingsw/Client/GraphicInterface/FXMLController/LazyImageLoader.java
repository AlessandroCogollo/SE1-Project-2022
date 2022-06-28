package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Enum.Wizard;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class LazyImageLoader {

    private static LazyImageLoader instance = null;

    private LazyImageLoader() {}

    public static LazyImageLoader getInstance() {
        if (instance == null)
            instance = new LazyImageLoader();

        return instance;
    }

    private Image tower3DBlack = null;
    private Image tower3DWhite = null;
    private Image tower3DGrey = null;

    public Image getTower3DBlack(double size) {
        if (tower3DBlack == null || tower3DBlack.getRequestedWidth() != size)
            tower3DBlack = new Image("/token/black_tower.png", size, 2*size, false, false);

        return tower3DBlack;
    }

    public Image getTower3DWhite(double size) {
        if (tower3DWhite == null || tower3DWhite.getRequestedWidth() != size)
            tower3DWhite = new Image("/token/white_tower.png", size, 2*size, false, false);

        return tower3DWhite;
    }

    public Image getTower3DGrey(double size) {
        if (tower3DGrey == null || tower3DGrey.getRequestedWidth() != size)
            tower3DGrey = new Image("/token/grey_tower.png", size, 2*size, false, false);

        return tower3DGrey;
    }

    private Image sorcerer = null;
    private Image king = null;
    private Image wise = null;
    private Image witch = null;
    private Image flowersQueen = null;

    public Image getWizard (Wizard w){
        switch (w){
            case Sorcerer -> {
                if (sorcerer == null)
                    sorcerer = new Image("/Assistenti/retro/" + w.getFileName());
                return sorcerer;
            }
            case King -> {
                if (king == null)
                    king = new Image("/Assistenti/retro/" + w.getFileName());
                return king;
            }
            case Wise -> {
                if (wise == null)
                    wise = new Image("/Assistenti/retro/" + w.getFileName());
                return wise;
            }
            case Witch -> {
                if (witch == null)
                    witch = new Image("/Assistenti/retro/" + w.getFileName());
                return witch;
            }
            case Flowers_Queen -> {
                if (flowersQueen == null)
                    flowersQueen = new Image("/Assistenti/retro/" + w.getFileName());
                return flowersQueen;
            }
        }
        return null;
    }

    private Image sorcererS = null;
    private Image kingS = null;
    private Image wiseS = null;
    private Image witchS = null;
    private Image flowersQueenS = null;

    public Image getWizardSquare (Wizard w){
        switch (w){
            case Sorcerer -> {
                if (sorcererS == null){
                    PixelReader r = getWizard(w).getPixelReader();
                    sorcererS = new WritableImage(r, 0, 0, 494, 494);
                }
                return sorcererS;
            }
            case King -> {
                if (kingS == null){
                    PixelReader r = getWizard(w).getPixelReader();
                    kingS = new WritableImage(r, 0, 0, 494, 494);
                }
                return kingS;
            }
            case Wise -> {
                if (wiseS == null){
                    PixelReader r = getWizard(w).getPixelReader();
                    wiseS = new WritableImage(r, 0, 0, 494, 494);
                }
                return wiseS;
            }
            case Witch -> {
                if (witchS == null){
                    PixelReader r = getWizard(w).getPixelReader();
                    witchS = new WritableImage(r, 0, 0, 494, 494);
                }
                return witchS;
            }
            case Flowers_Queen -> {
                if (flowersQueenS == null){
                    PixelReader r = getWizard(w).getPixelReader();
                    flowersQueenS = new WritableImage(r, 0, 0, 494, 494);
                }
                return flowersQueenS;
            }
        }
        return null;
    }

    private Image circle3DBlue = null;
    private Image circle3DPink = null;
    private Image circle3DYellow = null;
    private Image circle3DRed = null;
    private Image circle3DGreen = null;

    public Image get3DCircleBlue(double size) {
        if (circle3DBlue == null || circle3DBlue.getRequestedWidth() != size)
            circle3DBlue = new Image("/token/circle_pawn_blue.png", size, size, false, false);

        return circle3DBlue;
    }
    public Image get3DCirclePink(double size) {
        if (circle3DPink == null || circle3DPink.getRequestedWidth() != size)
            circle3DPink = new Image("/token/circle_pawn_pink.png", size, size, false, false);

        return circle3DPink;
    }
    public Image get3DCircleYellow(double size) {
        if (circle3DYellow == null || circle3DYellow.getRequestedWidth() != size)
            circle3DYellow = new Image("/token/circle_pawn_yellow.png", size, size, false, false);

        return circle3DYellow;
    }
    public Image get3DCircleRed(double size) {
        if (circle3DRed == null || circle3DRed.getRequestedWidth() != size)
            circle3DRed = new Image("/token/circle_pawn_red.png", size, size, false, false);

        return circle3DRed;
    }
    public Image get3DCircleGreen(double size) {
        if (circle3DGreen == null || circle3DGreen.getRequestedWidth() != size)
            circle3DGreen = new Image("/token/circle_pawn_green.png", size, size, false, false);

        return circle3DGreen;
    }


    private Image hexagon3DBlue = null;
    private Image hexagon3DPink = null;
    private Image hexagon3DYellow = null;
    private Image hexagon3DRed = null;
    private Image hexagon3DGreen = null;

    public Image get3DHexagonBlue(double size) {
        if (hexagon3DBlue == null || hexagon3DBlue.getRequestedWidth() != size)
            hexagon3DBlue = new Image("/token/hexa_pawn_blue.png", size, size, false, false);

        return hexagon3DBlue;
    }
    public Image get3DHexagonPink(double size) {
        if (hexagon3DPink == null || hexagon3DPink.getRequestedWidth() != size)
            hexagon3DPink = new Image("/token/hexa_pawn_pink.png", size, size, false, false);

        return hexagon3DPink;
    }
    public Image get3DHexagonYellow(double size) {
        if (hexagon3DYellow == null || hexagon3DYellow.getRequestedWidth() != size)
            hexagon3DYellow = new Image("/token/hexa_pawn_yellow.png", size, size, false, false);

        return hexagon3DYellow;
    }
    public Image get3DHexagonRed(double size) {
        if (hexagon3DRed == null || hexagon3DRed.getRequestedWidth() != size)
            hexagon3DRed = new Image("/token/hexa_pawn_red.png", size, size, false, false);

        return hexagon3DRed;
    }
    public Image get3DHexagonGreen(double size) {
        if (hexagon3DGreen == null || hexagon3DGreen.getRequestedWidth() != size)
            hexagon3DGreen = new Image("/token/hexa_pawn_green.png", size, size, false, false);

        return hexagon3DGreen;
    }

    private Image circleBlue = null;
    private Image circlePink = null;
    private Image circleYellow = null;
    private Image circleRed = null;
    private Image circleGreen = null;

    public Image getCircleBlue(double size) {
        if (circleBlue == null || circleBlue.getRequestedWidth() != size)
            circleBlue = new Image("/token/circle_blue.png", size, size, false, false);

        return circleBlue;
    }
    public Image getCirclePink(double size) {
        if (circlePink == null || circlePink.getRequestedWidth() != size)
            circlePink = new Image("/token/circle_pink.png", size, size, false, false);

        return circlePink;
    }
    public Image getCircleYellow(double size) {
        if (circleYellow == null || circleYellow.getRequestedWidth() != size)
            circleYellow = new Image("/token/circle_yellow.png", size, size, false, false);

        return circleYellow;
    }
    public Image getCircleRed(double size) {
        if (circleRed == null || circleRed.getRequestedWidth() != size)
            circleRed = new Image("/token/circle_red.png", size, size, false, false);

        return circleRed;
    }
    public Image getCircleGreen(double size) {
        if (circleGreen == null || circleGreen.getRequestedWidth() != size)
            circleGreen = new Image("/token/circle_green.png", size, size, false, false);

        return circleGreen;
    }


    private Image motherNature = null;

    public Image getMotherNature(double size) {
        if (motherNature == null || motherNature.getRequestedWidth() != size)
            motherNature = new Image("/token/mothernature.png",size,size,false,false);

        return motherNature;
    }

    private Image banCard = null;

    public Image getBanCard (double size){
        if (banCard == null || banCard.getRequestedWidth() != size)
            banCard = new Image("/token/token_apothecary.png",size,size,false,false);

        return banCard;
    }
}
