package drhd.sequalsk.platform.services;

import com.intellij.openapi.project.Project;
import drhd.sequalsk.utils.debugging.DebugLogger;

/**
 * Base class for every project service of this plugin.
 */
abstract public class PluginBaseService {

    /**
     * The project that the service is part of.
     */
    protected Project project;

    /**
     * Method to initialize the project service with the project that the service is part of. Must be called
     * before using this service.
     */
    public void initializeService(Project project) {
        this.project = project;
        initializeService();
        DebugLogger.info(this, "Initialized " + this.getClass().getSimpleName());
    }

    /**
     * Method to further initialize the service, e.g. subscribing to topics of the message bus.
     */
    protected void initializeService() {}

}
