package org.martica.equality;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class ListenerStartupActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        EditorFactory.getInstance().addEditorFactoryListener(new SplitListener(), project);
    }
}
