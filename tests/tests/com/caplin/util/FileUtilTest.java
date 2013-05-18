package tests.com.caplin.util;

import com.caplin.util.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.mockito.Mockito.*;


@RunWith(JUnit4.class)
public class FileUtilTest {

    @Test
    public void getConstructorEndOffsetFromTextReturnsZeroIfNoConstructorPresent() {
        String javascript = "";
        assertEquals(0, FileUtil.getConstructorEndOffsetFromText(javascript));

        javascript = "a";
        assertEquals(0, FileUtil.getConstructorEndOffsetFromText(javascript));

        javascript = "a b c \n d e f \n h i j";
        assertEquals(0, FileUtil.getConstructorEndOffsetFromText(javascript));

        javascript = "var bob = function(){";
        assertEquals(0, FileUtil.getConstructorEndOffsetFromText(javascript));
    }

    @Test
    public void getConstructorEndOffsetFromText() {
        String javascript = "var bob = function(){}";
        assertEquals(22, FileUtil.getConstructorEndOffsetFromText(javascript));

        javascript = "var bob = function(){};";
        assertEquals(23, FileUtil.getConstructorEndOffsetFromText(javascript));

        javascript = "/*a comment here*/\nvar bob = function(){ some more code \n in here \n}";
        assertEquals(68, FileUtil.getConstructorEndOffsetFromText(javascript));

        javascript = "/*a comment here*/\nvar bob = function(){ some more code \n in here \n};";
        assertEquals(69, FileUtil.getConstructorEndOffsetFromText(javascript));
    }

}