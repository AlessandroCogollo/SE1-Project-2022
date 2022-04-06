package it.polimi.ingsw.Server.Model;

class Professors {

    private final int[] professors;
    private final GameInitializer gInit;

    Professors(GameInitializer gInit){
        this.professors = new int[Color.getNumberOfColors()];
        for(int i=0; i<Color.getNumberOfColors(); i++){
            professors[i] = -1;
        }
        this.gInit = gInit;
    }

    void updateProfessors(){
        int maxp = -1;
        for (int i = 0; i < Color.getNumberOfColors(); i++){
            int max = 0;
            for (Player p : gInit){
                if (p.getNumberOfStudentInRoomByColor(Color.getColorById(i)) > max) {
                    max = p.getNumberOfStudentInRoomByColor(Color.getColorById(i));
                    maxp = p.getId();
                }
            }
            if(maxp != -1){
                this.professors[i] = maxp;
            }
        }
    }

    void setProfessors(Color color, Player player){
        this.professors[color.getIndex()] = player.getId();
    }

    Player getPlayerWithProfessor (Color color){
        int pId = professors[color.getIndex()];
        if (pId != -1)
            return gInit.getPlayerById(pId);
        return null;
    }
}
