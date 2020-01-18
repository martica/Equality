package org.martica.equality;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;

public class ToggleAutoEqualizeAction extends ToggleAction {

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return Properties.getAutoEqualize(e.getProject());
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean autoEqualize) {
        Properties.setAutoEqualize(e.getProject(), autoEqualize);
        if (autoEqualize) {
           IdeSplitter.forProject(e.getProject()).ifPresent(IdeSplitter::equalize);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(e.getProject() != null);
    }
}
