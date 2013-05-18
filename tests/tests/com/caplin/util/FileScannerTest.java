package tests.com.caplin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.caplin.util.FileScanner;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.mockito.Mockito.*;
import tests.util.FolderTreeUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RunWith(JUnit4.class)
public class FileScannerTest {

    @Test
    public void testCollectInterfaces() throws IOException {
        VirtualFile currentFile = mock(VirtualFile.class);
        AnActionEvent event = mock(AnActionEvent.class);
        when(event.getData(PlatformDataKeys.VIRTUAL_FILE)).thenReturn(currentFile);

        /*
        Check no interfaces
         */
        ArrayList<String> nointerfaces = FileScanner.getInterfaces(event);
        assertEquals("If there is no application root there should be no interfaces", 0, nointerfaces.size());

        /*
        Check multiple interfaces
         */
        HashMap<String,VirtualFile> virtualFilesFromTree =
                  FolderTreeUtility.getVirtualFilesFromTree(
                          "caplin/apps/bladeset/blade/src/bank/utility/Interface1.js," +
                          "caplin/apps/bladeset/blade/src/Class.js," +
                          "caplin/sdk/javascript/src/library/folder/Interface2.js");

        // The file the user is currently editing
        when(event.getData(PlatformDataKeys.VIRTUAL_FILE)).thenReturn(virtualFilesFromTree.get("Interface1.js"));

        /*
        Setup mock file contents
         */
        VirtualFile interface1 = virtualFilesFromTree.get("Interface1.js");
        when(interface1.contentsToByteArray()).thenReturn("/* slkfjd @interface*/\nfunction(){}".getBytes());

        VirtualFile interface2 = virtualFilesFromTree.get("Interface2.js");
        when(interface2.contentsToByteArray()).thenReturn("caplin.thirdparty('jquery');\n/* @interface */\nfunction bob(){};".getBytes());

        VirtualFile classFile = virtualFilesFromTree.get("Class.js");
        when(classFile.contentsToByteArray()).thenReturn("/* @constructor */\ncaplin.class = function(){\n}".getBytes());

        /*
        Verify the interfaces returned
         */
        ArrayList<String> interfaces = FileScanner.getInterfaces(event);
        assertEquals(2, interfaces.size());
        assertEquals("bank.utility.Interface1", interfaces.get(0));
        assertEquals("library.folder.Interface2", interfaces.get(1));
    }


}