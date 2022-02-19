package states.Wars.ForbiddenStatementsManagers;

import states.Wars.WarPlayers.AncapStatesWarrior;

public class ForbiddenStatementsThread extends Thread {

    AncapStatesWarrior warrior;

    public ForbiddenStatementsThread(AncapStatesWarrior warrior) {
        this.warrior = warrior;
    }

    public void run() {
        warrior.clearForbiddenOnWarsItems();
        warrior.clearForbiddenEffects();
    }
}
