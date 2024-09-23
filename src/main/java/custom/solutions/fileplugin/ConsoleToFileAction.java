package custom.solutions.fileplugin;

import com.intellij.execution.impl.ConsoleViewImpl;
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
        ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
        Caret caret = e.getData(LangDataKeys.CARET);
        //TODO: differentiate caret data and all console input provided. Maybe two actions or smth like. Extract to
        // method and from above.
        boolean isDataPresented = data != null && data.getContentSize() > 0;
        boolean isCaretSelectionPresent = caret != null && caret.getSelectedText() != null;
        //TODO: can we avoid cast at data? Is it a good idea?
        String initialContent = isDataPresented ? ((ConsoleViewImpl) data).getText() : caret.getSelectedText();

        /*
        There are some problems: if we want create a file, but PSI-provides logic for version control. It's not a
        good idea for our solution. Create Document or VirtualFile not good idea too (according to official
         documentation). We can use standard java File-api (approved by documentation of plugin development).

         */
    }

    //TODO: obtain alternatives. What is the point?
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
