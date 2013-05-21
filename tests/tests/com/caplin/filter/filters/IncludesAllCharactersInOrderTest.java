package tests.com.caplin.filter.filters;

import com.caplin.filter.Match;
import com.caplin.filter.MatchList;
import com.caplin.filter.filters.IncludesAllCharactersInOrder;
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
public class IncludesAllCharactersInOrderTest {

    private MatchList before;
    private IncludesAllCharactersInOrder filter;

    @Before
    public void setUp() {
        before = new MatchList();
        before.add(new Match("axabbccddee", 0));
        before.add(new Match("bbccddeeff", 0));
        before.add(new Match("eeffgghhii", 0));
        before.add(new Match("ageik", 0));

        filter = new IncludesAllCharactersInOrder();
    }

    @Test
    public void basicFilter() {
        MatchList after = filter.run("aabb", before);
        assertEquals(1, after.size());
        assertEquals("axabbccddee", ((Match)after.get(0)).getName());
    }

    @Test
    public void multipleResults() {
        MatchList after = filter.run("ee", before);
        assertEquals(3, after.size());
        assertEquals("axabbccddee",((Match)after.get(0)).getName());
        assertEquals("bbccddeeff", ((Match)after.get(1)).getName());
        assertEquals("eeffgghhii", ((Match)after.get(2)).getName());
    }

    @Test
    public void characterSeparation() {
        MatchList after = filter.run("bf", before);
        assertEquals(1, after.size());
        assertEquals("bbccddeeff", ((Match)after.get(0)).getName());
    }

    @Test
    public void emptySearch() {
        MatchList after = filter.run("", before);
        assertEquals(4, after.size());
        assertEquals("axabbccddee",((Match)after.get(0)).getName());
        assertEquals("bbccddeeff", ((Match)after.get(1)).getName());
        assertEquals("eeffgghhii", ((Match)after.get(2)).getName());
        assertEquals("ageik", ((Match)after.get(3)).getName());
    }

    @Test
    public void ignoresWhitespace() {
        MatchList after = filter.run("   eef    ", before);
        assertEquals(2, after.size());
        assertEquals("bbccddeeff", ((Match)after.get(0)).getName());
        assertEquals("eeffgghhii", ((Match)after.get(1)).getName());
    }

    @Test
    public void multipleCharacterSeparation() {
        MatchList after = filter.run("ae", before);
        assertEquals(2, after.size());
        assertEquals("axabbccddee", ((Match)after.get(0)).getName());
        assertEquals("ageik", ((Match)after.get(1)).getName());
    }

    @Test
    public void multipleCharacterSeparationTrimmedAndCaseInsensitive() {
        MatchList after = filter.run("   AE  ", before);
        assertEquals(2, after.size());
        assertEquals("axabbccddee", ((Match)after.get(0)).getName());
        assertEquals("ageik", ((Match)after.get(1)).getName());
    }

    @Test
    public void noResults() {
        MatchList after = filter.run("awlkjlwekfjewe", before);
        assertEquals(0, after.size());
    }
}
