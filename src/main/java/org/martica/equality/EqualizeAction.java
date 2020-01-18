package org.martica.equality;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class EqualizeAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        IdeSplitter.forProject(event.getProject()).ifPresent(IdeSplitter::equalize);
    }
}
