package states.Wars.ForbiddenStatementsManagers;

import states.Wars.WarPlayers.AncapWarrior;

public class ForbiddenStatementsThread extends Thread {

    AncapWarrior warrior;

    public ForbiddenStatementsThread(AncapWarrior warrior) {
        this.warrior = warrior;
    }

    public void run() {
        warrior.clearForbiddenOnWarsItems();
        warrior.clearForbiddenEffects();
    }
}
