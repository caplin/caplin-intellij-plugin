import com.caplin.util.Runner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;

/**
 * Created with IntelliJ IDEA.
 * User: stephens
 * Date: 11/05/13
 * Time: 08:18
 * To change this template use File | Settings | File Templates.
 */
public class Settings extends AnAction {
    public void actionPerformed(final AnActionEvent e) {
        Runner.runWriteCommand(e.getProject(), new Runnable() {
            public void run() {
                generateClass(e);
            }
        });
    }

    private void generateClass(AnActionEvent e) {
        StringBuilder builder = new StringBuilder("com.bladeset.blade.ClassName = function() {\n}");
        builder.append("\n\ncom.bladeset.blade.ClassName.prototype.method = new function(){}");
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        int offset = editor.getCaretModel().getOffset();
        editor.getDocument().insertString(offset, builder.toString());
    }
}
