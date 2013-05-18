package com.caplin.filter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 15/05/13
 * Time: 18:52
 * To change this template use File | Settings | File Templates.
 */
public class FilterChain {

    private ArrayList<Filter> chain = new ArrayList<Filter>();

    public FilterChain() {
    }

    public void addFilter(Filter filter) {
        this.chain.add(filter);
    }

    public ArrayList<String> run(String searchFor, ArrayList<String> listToSearch) {
        MatchList result = new MatchList();
        result.populateFromArrayList(listToSearch);

        for (Filter filter : this.chain) {
            result = filter.run(searchFor, result);
        }

        return result.toArrayListOfStrings();
    }


}
