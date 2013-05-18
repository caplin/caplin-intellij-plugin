package tests.com.caplin.filter.filters;

import com.caplin.filter.Match;
import com.caplin.filter.MatchList;
import com.caplin.filter.filters.IncludesAllCharactersInOrder;
import com.caplin.filter.filters.WeightByCharacterGrouping;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 18/05/13
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public class WeightByCharacterGroupingTest {

    private MatchList before;
    private WeightByCharacterGrouping filter;

    @Before
    public void setUp() {
        before = new MatchList();
        before.add(new Match("axabbccddee", 0));
        before.add(new Match("bbccddeeff", 0));
        before.add(new Match("eeffgghhii", 0));
        before.add(new Match("ageddkkddkkdd", 0));

        filter = new WeightByCharacterGrouping();
    }

    @Test
    public void basicGrouping() {
        MatchList after = filter.run("aabb", before);
        assertEquals(4, after.size());
        assertEquals("axabbccddee", ((Match)after.get(0)).getName());
        assertEquals("bbccddeeff", ((Match)after.get(1)).getName());
        assertEquals("ageddkkddkkdd", ((Match)after.get(2)).getName());
        assertEquals("eeffgghhii", ((Match)after.get(3)).getName());
    }

    @Test
    public void sameWeightingKeepsOrder() {
        MatchList after = filter.run("ef", before);
        assertEquals(4, after.size());
        assertEquals("bbccddeeff", ((Match)after.get(0)).getName());
        assertEquals("eeffgghhii", ((Match)after.get(1)).getName());
        assertEquals("axabbccddee", ((Match)after.get(2)).getName());
        assertEquals("ageddkkddkkdd", ((Match)after.get(3)).getName());
    }

    @Test
    public void longerSearch() {
        MatchList after = filter.run("bbddf", before);
        assertEquals(4, after.size());
        assertEquals("bbccddeeff", ((Match)after.get(0)).getName());
        assertEquals("axabbccddee", ((Match)after.get(1)).getName());
        assertEquals("ageddkkddkkdd", ((Match)after.get(2)).getName());
        assertEquals("eeffgghhii", ((Match)after.get(3)).getName());
    }

    @Test
     public void anotherSearch() {
        MatchList after = filter.run("ahii", before);
        assertEquals(4, after.size());
        assertEquals("eeffgghhii", ((Match)after.get(0)).getName());
        assertEquals("axabbccddee", ((Match)after.get(1)).getName());
        assertEquals("ageddkkddkkdd", ((Match)after.get(2)).getName());
        assertEquals("bbccddeeff", ((Match)after.get(3)).getName());
    }

    @Test
    public void moreMatchesWins() {
        before = new MatchList();
        before.add(new Match("abskkjkjkjk", 0));
        before.add(new Match("akskjsdkjaksjksja", 0));
        before.add(new Match("akjskjsjkjkakjjskjskjakjkjskja", 0));
        before.add(new Match("aksjskajkakjajajaaaa", 0));

        MatchList after = filter.run("a", before);
        assertEquals(4, after.size());

        assertEquals("aksjskajkakjajajaaaa", ((Match)after.get(0)).getName());
        assertEquals("akjskjsjkjkakjjskjskjakjkjskja", ((Match)after.get(1)).getName());
        assertEquals("akskjsdkjaksjksja", ((Match)after.get(2)).getName());
        assertEquals("abskkjkjkjk", ((Match)after.get(3)).getName());
    }


}
