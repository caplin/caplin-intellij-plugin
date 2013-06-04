package com.caplin.filter.filters;

import com.caplin.filter.Filter;
import com.caplin.filter.Match;
import com.caplin.filter.MatchList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        searchFor = searchFor.trim().toLowerCase();

        for (int loop = 1, length = searchFor.length(); loop <= length; loop++) {
            ArrayList<String> parts = getParts(searchFor, loop);

            for (int i = 0, l = listToSearch.size(); i < l; i = i + 1) {
                Match match = (Match)listToSearch.get(i);
                String matchName = match.getName().toLowerCase();
                String[] words = getWords(match.getName());

                for (int lc = 0, pl = parts.size(); lc < pl; lc++)     {

                    String part = parts.get(lc);
                    Pattern pattern = Pattern.compile(part);
                    Matcher matcher = pattern.matcher(matchName);

                    int matchCount = 0;
                    while (matcher.find()) matchCount++;

                    if (matchCount > 0) {
                        match.increaseRating(loop * loop * 100 + (matchCount * loop));
                    };

                    for (int wordCount = 0, wordLength = words.length; wordCount < wordLength; wordCount++) {
                        if (part.equals(words[wordCount])) {
                            match.increaseRating(10000 * loop * words[wordCount].length());
                        }
                    };
                }
            }
        }
        Collections.sort(listToSearch);
        return listToSearch;
    }

    private String[] getWords(String matchName) {
        return matchName.split("\\W+|(?=\\p{Upper})");
    }

    private ArrayList<String> getParts(String currentValue, int sectionLength) {
        ArrayList<String> parts = new ArrayList<String>();
        for (int i = 0, l = currentValue.length(); i + sectionLength <= l; i++) {
            try {
                parts.add(currentValue.substring(i, i + sectionLength));
            } catch (Exception e) {

            }
        }
        return parts;
    };

}
