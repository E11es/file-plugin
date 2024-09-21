package custom.solutions.fileplugin;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

/**
 * Action.
 */
public class ConsoleToFileAction extends DumbAwareAction {

    /**
     * Parent's constructor call.
     */
    public ConsoleToFileAction() {
        super("Export to File", "Creates file based on UI data", AllIcons.FileTypes.Any_type);
    }

    /**
     * Action view update.
     *
     * @param e {@link AnActionEvent}.
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
        Caret caret = e.getData(LangDataKeys.CARET);
        boolean isDataPresented = data != null && data.getContentSize() > 0;
        boolean isCaretSelectionPresent = caret != null && caret.getSelectedText() != null;
        e.getPresentation().setEnabled(isDataPresented || isCaretSelectionPresent);
    }

    /**
     * An action logic itself.
     *
     * @param e {@link AnActionEvent}.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        /*
        Steps:
        1. Create JPanel
        2. Configure file creation (name, path, etc) in JPanel
        3. Create file with configuration provided.
         */

//        ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
//        Project project = e.getProject();
//        DataContext dataContext = e.getDataContext();

    }

    //TODO: obtain alternatives. What is the point?
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
