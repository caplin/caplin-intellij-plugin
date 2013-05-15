package com.caplin.filter;

import com.caplin.forms.Match;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 15/05/13
 * Time: 18:52
 * To change this template use File | Settings | File Templates.
 */
public class FilterChain {

    public FilterChain() {

    }

    public String[] run(String searchFor, ArrayList<String> listToSearch) {

        /*
        Filter chain 1: Ensure all characters are present and in the correct order
         */
        String regex = ".*";
        for (int i = 0; i < searchFor.length(); i++){
            char c = searchFor.charAt(i);
            regex = regex + c + ".*";
        }

        ArrayList<Match> matches = new ArrayList<Match>();

        for (int i = 0, l = listToSearch.size(); i < l; i++) {
            if (listToSearch.get(i).toLowerCase().matches(regex)) {
                matches.add(new Match(listToSearch.get(i), 0));
            }
        }

        /*
        Filter chain 2: Weight by grouped characters and sort
         */
        for (int loop = 2, length = searchFor.length(); loop < length; loop++) {
            ArrayList<String> parts = getParts(searchFor, loop);

            for (int i = 0, l = matches.size(); i < l; i = i + 1) {
                Match match = matches.get(i);
                String matchName = match.getName().toLowerCase();

                for (int lc = 0, pl = parts.size(); lc < pl; lc++)     {
                    if (matchName.matches(".*" + parts.get(lc) + ".*")) {
                        match.increaseRating(pl * 100);
                    };
                }
            }
        }

        Collections.sort(matches);


        String[] sorted = new String[matches.size()];
        int count = 0;
        for (Match match : matches) {
            sorted[count] = match.getName();
            count++;
        }

        return sorted;


    }

    private ArrayList<String> getParts(String currentValue, int sectionLength) {
        ArrayList<String> parts = new ArrayList<String>();
        for (int i = 0, l = currentValue.length(); i + sectionLength < l; i++) {
            try {
                parts.add(currentValue.substring(i, sectionLength));
            } catch (Exception e) {

            }
        }
        return parts;
    };
}
