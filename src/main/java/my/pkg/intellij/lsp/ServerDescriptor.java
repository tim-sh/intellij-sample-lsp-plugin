package my.pkg.intellij.lsp;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor;
import com.intellij.platform.lsp.api.customization.LspFormattingSupport;
import my.pkg.intellij.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.intellij.util.PathUtil.getJarPathForClass;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ServerDescriptor extends ProjectWideLspServerDescriptor {

    private static final String RELATIVE_SERVER_PATH = "lsp-server/server.ts";

    public ServerDescriptor(@NotNull Project project, @NotNull String presentableName) {
        super(project, presentableName);
    }

    @NotNull
    @Override
    public GeneralCommandLine createCommandLine() throws ExecutionException {
        String serverPath = getServerPath();
        return new GeneralCommandLine("ts-node", serverPath, "--stdio").withCharset(UTF_8);
    }

    @NotNull
    private static String getServerPath() {
        Path thisPath = Paths.get(getJarPathForClass(ServerDescriptor.class));
        return thisPath
                .getParent()
                .resolve(RELATIVE_SERVER_PATH)
                .toString();
    }

    @Override
    public boolean isSupportedFile(@NotNull VirtualFile virtualFile) {
        return Language.FILE_EXTENSION.equals(virtualFile.getExtension());
    }

    @Nullable
    public LspFormattingSupport getLspFormattingSupport() {
        return new LspFormattingSupport() {
            @Override
            public boolean shouldFormatThisFileExclusivelyByServer(@NotNull VirtualFile file, boolean ideCanFormatThisFileItself, boolean serverExplicitlyWantsToFormatThisFile) {
                return true;
            }
        };
    }
}
