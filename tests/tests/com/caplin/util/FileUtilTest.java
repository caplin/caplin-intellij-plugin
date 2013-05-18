package tests.com.caplin.util;

import com.caplin.util.FileUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import tests.util.FolderTreeUtility;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnit4.class)
public class FileUtilTest {

    @Test
    public void isCaplinApp() {
        /*
        Setup mock application folders.
         */
        HashMap<String,VirtualFile> virtualFilesFromTree =
                FolderTreeUtility.getVirtualFilesFromTree("my documents/apps/bladeset/src/file.js");

        VirtualFile currentFile = virtualFilesFromTree.get("file.js");
        VirtualFile nonCaplinParent = virtualFilesFromTree.get("my documents");
        VirtualFile srcFile = virtualFilesFromTree.get("src");

        AnActionEvent event = mock(AnActionEvent.class);
        when(event.getData(PlatformDataKeys.VIRTUAL_FILE)).thenReturn(currentFile);

        assertTrue("The file is in a caplin app if there are both src and apps folders in the tree", FileUtil.isCaplinApp(currentFile));
        assertTrue("Virtual file not retrieved from the AnActionEvent correctly", FileUtil.isCaplinApp(event));

        /*
        Modify the mock tree so that there is no apps folder
        my documents/src/file.js
         */
        when(currentFile.getParent()).thenReturn(srcFile);
        when(srcFile.getParent()).thenReturn(nonCaplinParent);
        assertFalse("Both src and apps folders should be present if this is a caplin app", FileUtil.isCaplinApp(currentFile));

        /*
        Modify current tree so that there are no apps or src folders
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
    public void getNamespaceAndFullClass() {
        /*
        Setup mock application folders.
         */
        VirtualFile currentFile = mock(VirtualFile.class);
        when(currentFile.getName()).thenReturn("File.js");
        when(currentFile.getNameWithoutExtension()).thenReturn("File");

        VirtualFile srcFolder = mock(VirtualFile.class);
        when(srcFolder.getName()).thenReturn("src");

        VirtualFile utilsFolder = mock(VirtualFile.class);
        when(utilsFolder.getName()).thenReturn("utils");

        VirtualFile componentFolder = mock(VirtualFile.class);
        when(componentFolder.getName()).thenReturn("component");

        VirtualFile bladesetFolder = mock(VirtualFile.class);
        when(bladesetFolder.getName()).thenReturn("bladeset");

        VirtualFile namespaceFolder = mock(VirtualFile.class);
        when(namespaceFolder.getName()).thenReturn("caplin");

        VirtualFile mydocumentsFolder = mock(VirtualFile.class);
        when(mydocumentsFolder.getName()).thenReturn("mydocuments");


        /*
        Setup mock source tree and test.
        mydocuments/src/caplin/bladeset/component/utils/file.js
         */
        when(currentFile.getParent()).thenReturn(utilsFolder);
        when(utilsFolder.getParent()).thenReturn(componentFolder);
        when(componentFolder.getParent()).thenReturn(bladesetFolder);
        when(bladesetFolder.getParent()).thenReturn(namespaceFolder);
        when(namespaceFolder.getParent()).thenReturn(srcFolder);
        when(srcFolder.getParent()).thenReturn(mydocumentsFolder);
        assertEquals("caplin.bladeset.component.utils.", FileUtil.getNameSpace(currentFile));
        assertEquals("caplin.bladeset.component.utils.File", FileUtil.getFullClass(currentFile));

        AnActionEvent event = mock(AnActionEvent.class);
        when(event.getData(PlatformDataKeys.VIRTUAL_FILE)).thenReturn(currentFile);
        assertEquals("caplin.bladeset.component.utils.File", FileUtil.getFullClass(event));

        /*
        Return empty string when no src folder is found
         */
        when(namespaceFolder.getParent()).thenReturn(null);
        assertEquals("", FileUtil.getNameSpace(currentFile));
        assertEquals("File", FileUtil.getFullClass(currentFile));
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

    @Test
    public void getFileFromEvent() {
        VirtualFile currentFile = mock(VirtualFile.class);
        AnActionEvent event = mock(AnActionEvent.class);
        when(event.getData(PlatformDataKeys.VIRTUAL_FILE)).thenReturn(currentFile);
        assertEquals(currentFile, FileUtil.getVirtualFile(event));
    };

    @Test
    public void getConstructorFromPsiFile() {
        PsiElement PsiFile = mock(PsiElement.class);
        PsiElement[] children = new PsiElement[4];

        PsiElement comment = mock(PsiElement.class);
        when(comment.getText()).thenReturn("/* A comment */\n");

        PsiElement thirdpartyInclude = mock(PsiElement.class);
        when(thirdpartyInclude.getText()).thenReturn("caplin.thirdparty('JQuery');\n");

        PsiElement constructor = mock(PsiElement.class);
        when(constructor.getText()).thenReturn("mock.constructor = function(){};\n");

        children[0] = comment;
        children[1] = thirdpartyInclude;
        children[2] = comment;
        children[3] = constructor;

        when(PsiFile.getChildren()).thenReturn(children);

        assertEquals(constructor, FileUtil.getConstructorFromPsiFile(PsiFile));

        /*
        Check null is returned if no constructor exists.
         */
        children[0] = thirdpartyInclude;
        children[1] = comment;
        children[2] = comment;
        children[3] = comment;
        assertEquals("The first element should be returned if no constructor is found", thirdpartyInclude, FileUtil.getConstructorFromPsiFile(PsiFile));


        PsiElement[] emptyFile = new PsiElement[0];
        when(PsiFile.getChildren()).thenReturn(emptyFile);
        assertNull("If the file is empty then return null", FileUtil.getConstructorFromPsiFile(PsiFile));
    }

    @Test
    public void getApplicationRoot() {

        VirtualFile currentFile = mock(VirtualFile.class);

        VirtualFile srcFolder = mock(VirtualFile.class);
        when(srcFolder.getName()).thenReturn("src");

        VirtualFile utilsFolder = mock(VirtualFile.class);
        when(utilsFolder.getName()).thenReturn("utils");

        VirtualFile appsFolder = mock(VirtualFile.class);
        when(appsFolder.getName()).thenReturn("apps");

        VirtualFile sdkFolder = mock(VirtualFile.class);
        when(sdkFolder.getName()).thenReturn("sdk");

        VirtualFile caplinFolder = mock(VirtualFile.class);
        when(caplinFolder.getName()).thenReturn("caplin");


        /*
        Setup mock source tree and test.
        CaplinTrader/utils/file.js
        CaplinTrader/apps
        CaplinTrader/sdk
         */
        when(currentFile.getParent()).thenReturn(utilsFolder);
        when(utilsFolder.getParent()).thenReturn(caplinFolder);

        when(caplinFolder.findChild("apps")).thenReturn(appsFolder);
        when(caplinFolder.findChild("sdk")).thenReturn(sdkFolder);

        assertEquals(caplinFolder, FileUtil.getApplicationRoot(currentFile));

        /*
        Test no applicaton root found
         */
        when(caplinFolder.findChild("apps")).thenReturn(null);
        when(caplinFolder.findChild("sdk")).thenReturn(null);

        assertEquals(null, FileUtil.getApplicationRoot(currentFile));
    }

    @Test
    public void isInterface() throws IOException {
        VirtualFile file = mock(VirtualFile.class);
        when(file.contentsToByteArray()).thenReturn("/* \n @interface \n */ \n bob = function(){};".getBytes());
        assertTrue(FileUtil.isInterface(file));

        when(file.contentsToByteArray()).thenReturn("bob = function(){};".getBytes());
        assertFalse(FileUtil.isInterface(file));

        when(file.contentsToByteArray()).thenReturn("".getBytes());
        assertFalse(FileUtil.isInterface(file));
    };

    @Test
    public void isJSFile() throws IOException {
        VirtualFile file = mock(VirtualFile.class);
        when(file.getExtension()).thenReturn("js");
        assertTrue(FileUtil.isJSFile(file));

        when(file.getExtension()).thenReturn("txt");
        assertFalse(FileUtil.isJSFile(file));

        /*
        Test null
         */
        assertFalse(FileUtil.isJSFile(null));
    };












}