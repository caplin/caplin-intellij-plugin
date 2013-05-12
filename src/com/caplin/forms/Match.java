package com.caplin.forms;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 12/05/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class Match implements Comparable {
    private String name;
    private Integer rating;

    public Match(String name, Integer rating)  {
        this.name = name;
        this.rating = rating;

        increaseRating(10);
    }

    public void increaseRating(int i) {
        this.rating = this.rating + i;
    }

    public Integer getRating() {
        return this.rating;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(Object o) {
        Match compareto = ((Match)o);

        if (this.getRating() < compareto.getRating()) {
            return 1;
        } else if (this.getRating() > compareto.getRating()) {
            return -1;
        }

        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
