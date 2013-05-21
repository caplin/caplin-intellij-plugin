package tests.com.caplin.filter;

import com.caplin.filter.Match;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 21/05/13
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 */
public class MatchTest {
    @Test
    public void match() {
        Match match = new Match("Match Name", 10);
        assertEquals("Match Name", match.getName());
        assertEquals((int)10, (int)match.getRating());

        match.increaseRating(10);
        assertEquals((int)20, (int)match.getRating());
    }

    @Test
    public void matchCompareTo() {
        Match match1 = new Match("Match", 10);
        Match match2 = new Match("Match", 20);

        assertEquals(1, match1.compareTo(match2));

        match1.increaseRating(10);
        assertEquals(0, match1.compareTo(match2));

        match1.increaseRating(10);
        assertEquals(-1, match1.compareTo(match2));
    }
}
