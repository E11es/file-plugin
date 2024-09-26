package custom.solutions.fileplugin;

import com.intellij.codeInsight.actions.LayoutCodeDialog;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.fileChooser.actions.FileChooserAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.messages.MessageDialog;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryImpl;
import com.intellij.ui.messages.MessagesServiceImpl;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

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

    @Override
    public void update(@NotNull AnActionEvent e) {
        ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
        Caret caret = e.getData(LangDataKeys.CARET);
        boolean isDataPresented = data != null && data.getContentSize() > 0;
        boolean isCaretSelectionPresent = caret != null && caret.getSelectedText() != null;
        e.getPresentation().setEnabled(isDataPresented || isCaretSelectionPresent);
    }

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

        //TODO:  implemented through Application.runWriteAction() API. Avoid nulls with generalize some methods maybe
        PsiFile fileFromText = PsiFileFactory.getInstance(e.getProject()).createFileFromText("console_output",
                FileTypes.PLAIN_TEXT, initialContent);

        FileSaverDescriptor descriptor = new FileSaverDescriptor("Save As", "Save console input");
        VirtualFile virtualFileDir = FileChooser.chooseFile(descriptor, e.getProject(), null);
        if (virtualFileDir.isDirectory()) {
            PsiDirectory directory = PsiManager.getInstance(e.getProject()).findDirectory(virtualFileDir);
            directory.add(fileFromText);
        }
    }

    //TODO: obtain alternatives. What is the point?
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
