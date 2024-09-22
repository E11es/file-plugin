package custom.solutions.fileplugin;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
        FileSaverDescriptor descriptor = new FileSaverDescriptor("Save As", "Save console input");

//        ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
        Caret caret = e.getData(LangDataKeys.CARET);
//        No data can be provided through data-methods
//        boolean isDataPresented = data != null && data.getContentSize() > 0;
        boolean isCaretSelectionPresent = caret != null && caret.getSelectedText() != null;


        String initialContent = null;
        //TODO: how to get all data from console view?
//        if (isDataPresented) {
//            initialContent = data.toString();
//        } else

        if (isCaretSelectionPresent) {
            initialContent = caret.getSelectedText();
        }

        Project project = Objects.requireNonNull(e.getData(CommonDataKeys.PROJECT));
        PsiFile file = PsiFileFactory.getInstance(project)
                .createFileFromText(
                        "my_fancy_name.txt.ft",
                        FileTypes.PLAIN_TEXT,
                        initialContent,
                        0,
                        true);

        // FIXME: no save action performed. Maybe access? Or incorrect usage of saveFileDialog? Maybe there is another API?
        FileSaverDialog saveFileDialog = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, project);
        VirtualFileWrapper result = saveFileDialog.save(file.getVirtualFile(), file.getName());

/*        About creation of VFS: Usually, you don't (create VFS).
          As a general rule, files are created either through the PSI API or through the regular java.io.File API
 */

    }

    //TODO: obtain alternatives. What is the point?
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
