package custom.solutions.fileplugin;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Action.
 */
public class ConsoleToFileAction extends DumbAwareAction {
    //        TODO: write a readme.
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("file_plugin");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd_HH.mm");

    /**
     * Parent's constructor call.
     */
    public ConsoleToFileAction() {
        super(
                BUNDLE.getString("plugin.button.title"),
                BUNDLE.getString("plugin.button.description"),
                AllIcons.FileTypes.Text
        );
    }

    /**
     * Update representation of button.
     *
     * @param event {@link AnActionEvent}.
     */
    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getData(LangDataKeys.PROJECT);
        ConsoleViewImpl consoleView = getCurrentConsoleView(event);
        event.getPresentation().setEnabled(project != null && consoleView != null);
    }

    /**
     * File created based on console output.
     *
     * @param event {@link AnActionEvent}.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getData(LangDataKeys.PROJECT);
//      TODO: add caret model mode
        ConsoleViewImpl consoleView = getCurrentConsoleView(event);

        if (project == null | consoleView == null) {
            return;
        }

        FileSaverDescriptor descriptor = new FileSaverDescriptor(
                BUNDLE.getString("file.saver.descriptor.title"),
                BUNDLE.getString("file.saver.descriptor.description")
        );
        //TODO: think about vf name that we can get from saveFileDialog.
        VirtualFile virtualFileDir = FileChooser.chooseFile(descriptor, project, null);

        String fileName = createFileName();
        String initialContent = consoleView.getText();

        ApplicationManager.getApplication().runWriteAction(() -> {
            PsiFile fileFromText = PsiFileFactory
                    .getInstance(project)
                    .createFileFromText(fileName, PlainTextFileType.INSTANCE, initialContent);

//             TODO: Think about this
//            FileSaverDialog saveFileDialog = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, project);

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
        /* Out plugin logic may cause an expensive operation. We should not block user interface. */
        return ActionUpdateThread.BGT;
    }

    /**
     * Get current project console view.
     *
     * @param event {@link AnActionEvent};
     * @return {@link ConsoleViewImpl}.
     */
    private ConsoleViewImpl getCurrentConsoleView(AnActionEvent event) {
        Editor editor = event.getData(LangDataKeys.EDITOR);
        if (editor == null) {
            return null;
        }
        return editor.getUserData(ConsoleViewImpl.CONSOLE_VIEW_IN_EDITOR_VIEW);
    }

    /**
     * Unique name of file creation.
     *
     * @return file name from bundle with current date.
     */
    private String createFileName() {
        return BUNDLE.getString("default.file.name") + SIMPLE_DATE_FORMAT.format(new Date()) + ".txt";
    }

}
