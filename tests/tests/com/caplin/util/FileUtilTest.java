package tests.com.caplin.util;

import com.caplin.util.FileUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(JUnit4.class)
public class FileUtilTest {

    @Test
    public void isCaplinApp() {
        /*
        Setup mock application folders.
         */
        VirtualFile currentFile = mock(VirtualFile.class);
        when(currentFile.getName()).thenReturn("file.js");

        VirtualFile srcParent = mock(VirtualFile.class);
        when(srcParent.getName()).thenReturn("src");

        VirtualFile bladesetParent = mock(VirtualFile.class);
        when(bladesetParent.getName()).thenReturn("bladeset");

        VirtualFile appsParent = mock(VirtualFile.class);
        when(appsParent.getName()).thenReturn("apps");

        VirtualFile nonCaplinParent = mock(VirtualFile.class);
        when(nonCaplinParent.getName()).thenReturn("my documents");

        AnActionEvent event = mock(AnActionEvent.class);
        when(event.getData(PlatformDataKeys.VIRTUAL_FILE)).thenReturn(currentFile);

        /*
        Setup mock source tree and test.
        my documents/apps/bladeset/src/file.js
         */
        when(currentFile.getParent()).thenReturn(srcParent);
        when(srcParent.getParent()).thenReturn(bladesetParent);
        when(bladesetParent.getParent()).thenReturn(appsParent);
        when(appsParent.getParent()).thenReturn(nonCaplinParent);
        assertTrue("The file is in a caplin app if there are both src and apps folders in the tree", FileUtil.isCaplinApp(currentFile));
        assertTrue("Virtual file not retrieved from the AnActionEvent correctly", FileUtil.isCaplinApp(event));

        /*
        Setup mock source tree and test.
        my documents/src/file.js
         */
        when(currentFile.getParent()).thenReturn(srcParent);
        when(srcParent.getParent()).thenReturn(nonCaplinParent);
        assertFalse("Both src and apps folders should be present if this is a caplin app", FileUtil.isCaplinApp(currentFile));

        /*
        Setup mock source tree and test.
        my documents/file.js
         */
        when(currentFile.getParent()).thenReturn(nonCaplinParent);
        assertFalse("Both src and apps folders should be present if this is a caplin app", FileUtil.isCaplinApp(currentFile));

        /*
        Setup mock source tree and test.
        file.js
         */
        when(currentFile.getParent()).thenReturn(null);
        assertFalse("The file is not in a caplin app if there is no parent folder", FileUtil.isCaplinApp(currentFile));

    }

    @Test
    public void getConstructorEndOffsetFromTextReturnsZeroIfNoConstructorPresent() {
        String javascript = "";
        assertEquals(0, FileUtil.getConstructorEndOffsetFromText(javascript));

        javascript = "            \n          \n          ";
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

        javascript = "caplin.thirdparty('bob');\n/*a comment here*/\nvar bob = function(){ some more code \n in here \n};";
        assertEquals(95, FileUtil.getConstructorEndOffsetFromText(javascript));
    }

}