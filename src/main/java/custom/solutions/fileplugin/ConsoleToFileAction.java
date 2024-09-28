package custom.solutions.fileplugin;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
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
     * Update representation of button.
     *
     * @param e {@link AnActionEvent}.
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
        boolean isDataPresented = data != null && data.getContentSize() > 0;
        e.getPresentation().setEnabled(isDataPresented);
    }

    /**
     * File created based on console output.
     *
     * @param e {@link AnActionEvent}.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
        Project project = e.getProject();

        FileSaverDescriptor descriptor = new FileSaverDescriptor("Save As", "Save console input");
        VirtualFile virtualFileDir = FileChooser.chooseFile(descriptor, e.getProject(), null);

        ApplicationManager.getApplication().runWriteAction(() -> {
            if (project == null || data == null) {
                return;
            }
            String initialContent = ((ConsoleViewImpl) data).getText();

            PsiFile fileFromText = PsiFileFactory.getInstance(project)
                    .createFileFromText("console_output.txt", FileTypes.PLAIN_TEXT, initialContent);

            if (virtualFileDir == null || !virtualFileDir.isDirectory()) {
                throw new IllegalStateException("Virtual file must be presented as directory.");
            }
            PsiDirectory directory = PsiManager.getInstance(project).findDirectory(virtualFileDir);
            if (directory == null) {
                throw new IllegalStateException("Directory not found.");
            }
            directory.add(fileFromText);
        });
    }

    /**
     * Policy of action update exec.
     *
     * @return {@link ActionUpdateThread}.
     */
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
