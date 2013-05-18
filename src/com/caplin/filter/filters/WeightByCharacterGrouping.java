package com.caplin.filter.filters;

import com.caplin.filter.Filter;
import com.caplin.filter.Match;
import com.caplin.filter.MatchList;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 15/05/13
 * Time: 20:21
 * To change this template use File | Settings | File Templates.
 */
public class WeightByCharacterGrouping implements Filter {
    @Override
    public MatchList run(String searchFor, MatchList listToSearch) {
        /*
        Filter chain 2: Weight by grouped characters and sort
         */
        for (int loop = 2, length = searchFor.length(); loop < length; loop++) {
            ArrayList<String> parts = getParts(searchFor, loop);

            for (int i = 0, l = listToSearch.size(); i < l; i = i + 1) {
                Match match = (Match)listToSearch.get(i);
                String matchName = match.getName().toLowerCase();

                for (int lc = 0, pl = parts.size(); lc < pl; lc++)     {
                    if (matchName.matches(".*" + parts.get(lc) + ".*")) {
                        match.increaseRating(pl * 1000);
                    };
                }
            }
        }
        Collections.sort(listToSearch);
        return listToSearch;
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
