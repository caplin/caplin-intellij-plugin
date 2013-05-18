package tests.util;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 18/05/13
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class FolderTreeUtility {

    public static HashMap<String, VirtualFile> getVirtualFilesFromTree(String paths) {
        HashMap<String, VirtualFile> files = new HashMap<String, VirtualFile>();
        HashMap<String, VirtualFile[]> fileToChildren = new HashMap<String, VirtualFile[]>();

        String[] pathsArray = paths.split(",");
        for (int pathsCount = 0, pathsLength = pathsArray.length; pathsCount < pathsLength; pathsCount++) {

            String[] pathArray = pathsArray[pathsCount].split("/");

            for (int pathCount = 0, pathLength = pathArray.length; pathCount < pathLength; pathCount++) {

                String file = pathArray[pathCount];

                if (!files.containsKey(file)) {

                    VirtualFile virtualFile = mock(VirtualFile.class);
                    files.put(file, virtualFile);

                    boolean bIsDirectory = !file.contains(".");
                    when(virtualFile.isDirectory()).thenReturn(bIsDirectory);

                    if (!bIsDirectory) {
                        when(virtualFile.getExtension()).thenReturn(file.split("\\.")[1]);
                        when(virtualFile.getNameWithoutExtension()).thenReturn(file.split("\\.")[0]);
                    }

                    if (pathCount != 0 && files.containsKey(pathArray[pathCount-1])) {
                        VirtualFile parent = files.get(pathArray[pathCount - 1]);
                        when(virtualFile.getParent()).thenReturn(parent);
                        when(parent.findChild(file)).thenReturn(virtualFile);
                        when(virtualFile.getName()).thenReturn(file);

                        VirtualFile[] children = fileToChildren.get(parent.getName());
                        if (children == null) {
                            children = new VirtualFile[1];
                            children[0] = virtualFile;
                            fileToChildren.put(parent.getName(), children);
                            when(parent.getChildren()).thenReturn(children);
                        } else {
                            VirtualFile[] newchildren = new VirtualFile[children.length + 1];

                            for (int i = 0, l = children.length; i < l; i++) {
                                newchildren[i] = children[i];
                            }
                            newchildren[children.length] = virtualFile;
                            fileToChildren.put(parent.getName(), newchildren);
                            when(parent.getChildren()).thenReturn(newchildren);
                        }

                    }

                }
            }

        }

        return files;
    }

}
