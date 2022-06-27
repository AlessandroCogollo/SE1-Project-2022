package it.polimi.ingsw.Client.GraphicInterface;

public class TestingGraphicHandler extends GraphicHandler{

    public TestingGraphicHandler(String graphicType) {
        super(graphicType);
    }

    @Override
    public void startGraphic() {
        TestingCli t = new TestingCli();
        t.startGraphic();
        this.graphic = t;
        this.dataCollector = t.getDataCollector();

    }
}
