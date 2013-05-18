package com.caplin.filter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 15/05/13
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public interface Filter {
    public MatchList run(String searchFor, MatchList listToSearch);
}
