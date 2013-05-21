package com.caplin.filter.filters;

import com.caplin.filter.Filter;
import com.caplin.filter.Match;
import com.caplin.filter.MatchList;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 15/05/13
 * Time: 19:55
 * To change this template use File | Settings | File Templates.
 */
public class IncludesAllCharactersInOrder implements Filter {

    @Override
    public MatchList run(String searchFor, MatchList listToSearch) {
        searchFor = searchFor.trim().toLowerCase();

        String regex = ".*";
        for (int i = 0; i < searchFor.length(); i++){
            char c = searchFor.charAt(i);
            regex = regex + c + ".*";
        }

        MatchList matches = new MatchList();

        for (int i = 0, l = listToSearch.size(); i < l; i++) {
            String item = ((Match)listToSearch.get(i)).getName();
            if (item.toLowerCase().matches(regex)) {
                matches.add(new Match(item, 0));
            }
        }

        return matches;
    }
}
