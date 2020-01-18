package org.martica.equality;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.martica.equality.IdeSplitter.SplitterOrientation;

import java.util.Optional;

public class EqualizeAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();

        Optional<IdeSplitter> splitters = IdeSplitter.buildTree(FileEditorManagerEx.getInstanceEx(project));
        splitters.ifPresent(EqualizeAction::equalize);
    }

    private static void equalize(IdeSplitter splitter) {
        int left = countSiblings(splitter.getFirst(), splitter.getSplitterOrientation());
        int right = countSiblings(splitter.getSecond(), splitter.getSplitterOrientation());
        int total = left + right;

        splitter.setProportion(1.0f * left / total);
        splitter.getFirst().ifPresent(EqualizeAction::equalize);
        splitter.getSecond().ifPresent(EqualizeAction::equalize);

    }

    private static int countSiblings(Optional<IdeSplitter> ideSplitter, SplitterOrientation orientation) {
        if (!ideSplitter.isPresent()) {
            return 1;
        }

        IdeSplitter splitter = ideSplitter.get();
        if (splitter.getSplitterOrientation() != orientation) {
            return 1;
        }

        return countSiblings(splitter.getFirst(), orientation) + countSiblings(splitter.getSecond(), orientation);
    }

}
