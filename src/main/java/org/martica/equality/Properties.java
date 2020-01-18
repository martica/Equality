package org.martica.equality;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

import javax.annotation.Nullable;

class Properties {

    private static final String AUTO_EQUALIZE = "org.martica.equality.AutoEqualize";

    static boolean getAutoEqualize(@Nullable Project project) {
        if (project == null) {
            return false;
        }
        return PropertiesComponent.getInstance(project).getBoolean(AUTO_EQUALIZE);
    }

    static void setAutoEqualize(@Nullable Project project, boolean autoEqualize) {
        if (project != null) {
            PropertiesComponent.getInstance(project).setValue(AUTO_EQUALIZE, autoEqualize);
        }
    }

}
