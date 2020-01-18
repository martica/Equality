package org.martica.equality;

import com.google.auto.value.AutoValue;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@AutoValue
abstract class IdeSplitter {
    enum SplitterOrientation {
        HORIZONTAL,
        VERTICAL;

        public SplitterOrientation opposite() {
            switch (this) {
                case VERTICAL:
                    return HORIZONTAL;
                case HORIZONTAL:
                    return VERTICAL;
            }
            throw new AssertionError("Exhaustive switch.");
        }

        public static SplitterOrientation fromBool(boolean isHorizontal) {
            return isHorizontal ? HORIZONTAL : VERTICAL;
        }
    }

    static IdeSplitter create(Splitter splitter, Optional<IdeSplitter> first,
                              Optional<IdeSplitter> second) {
        return new AutoValue_IdeSplitter(splitter, first, second);
    }

    static Optional<IdeSplitter> buildTree(FileEditorManagerEx fileEditorManagerEx) {
        return getSplitters(fileEditorManagerEx);
    }

    abstract Splitter getSplitter();

    abstract Optional<IdeSplitter> getFirst();

    abstract Optional<IdeSplitter> getSecond();

    SplitterOrientation getSplitterOrientation() {
        return SplitterOrientation.fromBool(getSplitter().getOrientation());
    }

    void setProportion(float proportion) {
        getSplitter().setProportion(proportion);
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

}
