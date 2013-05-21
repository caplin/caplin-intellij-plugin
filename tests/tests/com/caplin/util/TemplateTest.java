package tests.com.caplin.util;

import com.caplin.util.FileUtil;
import com.caplin.util.Template;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 21/05/13
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class TemplateTest {
    @Test
    public void processTemplate() {
        HashMap data = new HashMap();
        data.put("fullClass", "caplin.class.Class");
        data.put("interface", "caplin.interface.Interface");

        String expected = "caplin.implement(caplin.class.Class, caplin.interface.Interface);";
        String actual = Template.INSTANCE.process("interface.ftl", data);

        assertEquals(expected, actual);
    }

}
