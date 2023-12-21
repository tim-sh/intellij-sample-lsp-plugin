package my.pkg.intellij.lsp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import my.pkg.intellij.Language;
import org.jetbrains.annotations.NotNull;

public class ServerSupportProvider implements com.intellij.platform.lsp.api.LspServerSupportProvider {

    @Override
    public void fileOpened(@NotNull Project project, @NotNull VirtualFile virtualFile, @NotNull LspServerStarter lspServerStarter) {
        if (!Language.FILE_EXTENSION.equals(virtualFile.getExtension())) {
            return;
        }
        lspServerStarter.ensureServerStarted(new ServerDescriptor(project, Language.LABEL));
    }
}
