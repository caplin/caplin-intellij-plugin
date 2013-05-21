package tests.com.caplin.filter;

import com.caplin.filter.Filter;
import com.caplin.filter.FilterChain;
import com.caplin.filter.MatchList;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 18/05/13
 * Time: 21:15
 * To change this template use File | Settings | File Templates.
 */
public class FilterChainTest {

    @Test
    public void filterChain() {
        FilterChain chain = new FilterChain();
        Filter filter1 = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        chain.addFilter(filter1);
        chain.addFilter(filter2);

        String search = "search";

        ArrayList<String> strings = new ArrayList<String>();
        strings.add("list1");
        strings.add("list2");

        MatchList matchList = mock(MatchList.class);
        when(filter1.run(anyString(), any(MatchList.class))).thenReturn(matchList);
        when(filter2.run(anyString(), any(MatchList.class))).thenReturn(matchList);

        chain.run(search, strings);

        verify(filter1, times(1)).run(anyString(), any(MatchList.class));
        verify(filter2, times(1)).run(anyString(), any(MatchList.class));
        verify(matchList, times(1)).toArrayListOfStrings();
    }

}
