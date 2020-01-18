package org.martica.equality;

import com.google.auto.value.AutoValue;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@AutoValue
abstract class IdeSplitter {
    enum SplitterOrientation {
        HORIZONTAL,
        VERTICAL
    }

    private static IdeSplitter create(Splitter splitter, Optional<IdeSplitter> first,
                                      Optional<IdeSplitter> second) {
        return new AutoValue_IdeSplitter(splitter, first, second);
    }

    private static Optional<IdeSplitter> buildTree(FileEditorManagerEx fileEditorManagerEx) {
        return getSplitters(fileEditorManagerEx);
    }

    static Optional<IdeSplitter> forProject(@Nullable Project project) {
        if (project == null) {
            return Optional.empty();
        }
        return buildTree(FileEditorManagerEx.getInstanceEx(project));
    }

    abstract Splitter getSplitter();

    abstract Optional<IdeSplitter> getFirst();

    abstract Optional<IdeSplitter> getSecond();

    private SplitterOrientation getSplitterOrientation() {
        return getSplitter().getOrientation() ? SplitterOrientation.HORIZONTAL : SplitterOrientation.VERTICAL;
    }

    private static Optional<IdeSplitter> getSplitters(FileEditorManagerEx editorManagerEx) {
        JPanel rootPanel = (JPanel) editorManagerEx.getSplitters().getComponent(0);
        return getSplitters(rootPanel);
    }

    private static Optional<IdeSplitter> getSplitters(JPanel panel) {

        Component component = panel.getComponent(0);

        if (!(component instanceof Splitter)) {
            return Optional.empty();
        }

        Splitter splitter = (Splitter) component;

        return Optional.of(IdeSplitter.create(
                splitter,
                getSplitters((JPanel) splitter.getFirstComponent()),
                getSplitters((JPanel) splitter.getSecondComponent())
        ));
    }

    static void equalize(IdeSplitter splitter) {
        int before = countSiblings(splitter.getFirst(), splitter.getSplitterOrientation());
        int after = countSiblings(splitter.getSecond(), splitter.getSplitterOrientation());
        int total = before + after;

        splitter.getSplitter().setProportion(1.0f * before / total);
        splitter.getFirst().ifPresent(IdeSplitter::equalize);
        splitter.getSecond().ifPresent(IdeSplitter::equalize);

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
