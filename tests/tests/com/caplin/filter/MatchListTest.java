package tests.com.caplin.filter;

import com.caplin.filter.Match;
import com.caplin.filter.MatchList;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 21/05/13
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class MatchListTest {
    @Test
    public void toArrayListOfStrings() {
        MatchList matchList = new MatchList();

        Match match1 = new Match("Match 1", 10);
        Match match2 = new Match("Match 2", 10);

        matchList.add(match1);
        matchList.add(match2);

        ArrayList<String> list = matchList.toArrayListOfStrings();
        assertEquals("Match 1", list.get(0));
        assertEquals("Match 2", list.get(1));
    }
}
