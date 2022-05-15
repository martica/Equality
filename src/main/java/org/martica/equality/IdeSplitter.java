package org.martica.equality;

import com.intellij.openapi.fileEditor.impl.EditorsSplitters;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Optional;

class IdeSplitter {
    enum SplitterOrientation {
        HORIZONTAL,
        VERTICAL
    }

    final Splitter splitter;
    final Optional<IdeSplitter> first;
    final Optional<IdeSplitter> second;

    IdeSplitter(Splitter splitter) {
        this.splitter = splitter;
        this.first = getSplitterForPanel(splitter.getFirstComponent());
        this.second = getSplitterForPanel(splitter.getSecondComponent());
    }

    static Optional<IdeSplitter> forProject(@Nullable Project project) {
        if (project == null) {
            return Optional.empty();
        }

        EditorsSplitters splitters = FileEditorManagerEx.getInstanceEx(project).getSplitters();
        if (splitters.getComponentCount() == 0) {
            return Optional.empty();
        }

        Component component = splitters.getComponent(0);
        if (!(component instanceof JComponent)) {
            return Optional.empty();
        }

        return getSplitterForPanel((JComponent) component);
    }

    private SplitterOrientation getSplitterOrientation() {
        return splitter.getOrientation() ? SplitterOrientation.HORIZONTAL : SplitterOrientation.VERTICAL;
    }

    private static Optional<IdeSplitter> getSplitterForPanel(JComponent panel) {
        if (!(panel instanceof  JPanel) || panel.getComponentCount() == 0) {
            return Optional.empty();
        }

        Component component = panel.getComponent(0);

        if (!(component instanceof Splitter)) {
            return Optional.empty();
        }

        return Optional.of(new IdeSplitter((Splitter) component));
    }

    void equalize() {
        int before = first.map(split -> split.countSiblings(getSplitterOrientation())).orElse(1);
        int total = before + second.map(split -> split.countSiblings(getSplitterOrientation())).orElse(1);

        splitter.setProportion(1.0f * before / total);

        first.ifPresent(IdeSplitter::equalize);
        second.ifPresent(IdeSplitter::equalize);
    }

    private int countSiblings(SplitterOrientation orientation) {
        Integer before = first.map(split -> split.countSiblings(orientation)).orElse(1);
        Integer after = second.map(split -> split.countSiblings(orientation)).orElse(1);

        if (getSplitterOrientation() == orientation) {
            // On-axis splits count the sum of nested splits.
            return before + after;
        } else {
            // Off-axis splits count the max of nested splits.
            return Math.max(before, after);
        }
    }

}
