package com.caplin.filter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 15/05/13
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public class MatchList extends ArrayList {

    public ArrayList<String> toArrayListOfStrings() {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Match> thisList = this;

        for (Match match : thisList) {
            result.add(match.getName());
        }

        return result;
    }

    public void populateFromArrayList(ArrayList<String> listToSearch) {
        for (String entry : listToSearch) {
            this.add(new Match(entry, 0));
        }
    }
}
