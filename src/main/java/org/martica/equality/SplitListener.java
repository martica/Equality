package org.martica.equality;

import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.concurrency.AppExecutorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class SplitListener implements EditorFactoryListener {

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        runEqualizer(event.getEditor().getProject()).run();
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        AppExecutorUtil.getAppScheduledExecutorService()
                .schedule(runEqualizer(event.getEditor().getProject()), 125, TimeUnit.MILLISECONDS);

    }

    @NotNull
    private Runnable runEqualizer(@Nullable Project project) {
        return () -> {
            if (Properties.getAutoEqualize(project)) {
                IdeSplitter.forProject(project).ifPresent(IdeSplitter::equalize);
            }
        };
    }
}
