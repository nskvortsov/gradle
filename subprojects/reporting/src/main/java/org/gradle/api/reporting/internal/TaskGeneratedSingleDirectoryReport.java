/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.reporting.internal;

import org.gradle.api.Task;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.internal.IConventionAware;
import org.gradle.api.internal.provider.DefaultProvider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.reporting.DirectoryReport;

import javax.inject.Inject;
import java.io.File;

public class TaskGeneratedSingleDirectoryReport extends TaskGeneratedReport implements DirectoryReport {

    private final String relativeEntryPath;
    // TODO - make these managed properties, once instant execution can deserialize abstract beans
    private final DirectoryProperty outputLocation;
    private final Property<Boolean> activated;

    @Inject
    public TaskGeneratedSingleDirectoryReport(String name, Task task, String relativeEntryPath) {
        super(name, OutputType.DIRECTORY, task);
        this.relativeEntryPath = relativeEntryPath;
        this.outputLocation = getObjectFactory().directoryProperty().convention(getProjectLayout().dir(new DefaultProvider<>(() -> {
            return (File) ((IConventionAware) TaskGeneratedSingleDirectoryReport.this).getConventionMapping().getConventionValue(null, "destination", false);
        })));
        this.activated = getObjectFactory().property(Boolean.class).convention(false);
    }

    @Inject
    protected ProjectLayout getProjectLayout() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected ObjectFactory getObjectFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Property<Boolean> getActivated() {
        return activated;
    }

    @Override
    public DirectoryProperty getOutputLocation() {
        return outputLocation;
    }

    @Override
    public File getEntryPoint() {
        if (relativeEntryPath == null) {
            return getDestination();
        } else {
            return new File(getDestination(), relativeEntryPath);
        }
    }
}
