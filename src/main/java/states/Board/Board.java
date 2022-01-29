package states.Board;

import java.util.ArrayList;
import java.util.Collections;

public class Board {

    private String board;

    public Board(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, args);
        list.remove(0);
        this.board = "";
        for (String str : list) {
            this.board = this.board+" "+str;
        }
    }

    @Override
    public String toString() {
        return this.board;
    }
}
