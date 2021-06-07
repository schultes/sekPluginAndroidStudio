package drhd.sequalsk.transpiler.validation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;

import javax.swing.*;
import java.util.List;

/**
 * The {@link TranspilerResultAnalyzer} analyzes {@link TranspilerResult}s and generates warnings if something is wrong
 * with the result. This is the base class for all kinds of warnings.
 */
abstract public class TranspilerResultInspection {

    /**
     * The result that is beeing analyzed.
     */
    protected TranspilerResult transpilerResult;

    /**
     * Returns true if this inspection is not relevant for the analyzed result.
     */
    public boolean isValidResult(TranspilerResult result) {
        this.transpilerResult = result;
        return generateResultInspector().isValidResult(result);
    }

    /**
     * Returns the title of the warning - a very short description of the problem that is presented to the user.
     */
    abstract public String getTitle();

    /**
     * Returns a detailed description of the warning. Will be opened in a separate window.
     */
    abstract public String getDescription();

    /**
     * Creates a new instance of the necessary result inspector.
     */
    abstract protected TranspilerResultInspector generateResultInspector();

    /**
     * Returns the severity of the warning.
     */
    abstract protected Severity getSeverity();

    /**
     * Creates a list of custom actions that are used to further analyze the problem with the transpiler result.
     */
    abstract public List<AnAction> getActions();

    public Icon getIcon() {
        if(getSeverity() == Severity.INFO) return AllIcons.General.Information;
        if(getSeverity() == Severity.WARNING) return AllIcons.General.Warning;
        if(getSeverity() == Severity.ERROR) return AllIcons.General.Error;
        return null;
    }

    protected enum Severity {
        INFO, WARNING, ERROR
    }

    /**
     * The inspector analyzes and validates the result. It decides if the warning must be shown or not.
     *
     * Every {@link TranspilerResultInspection} has its own inspector that analyzes the {@link TranspilerResult}.
     */
    protected interface TranspilerResultInspector {

        /**
         * Performs the inspection of the result - returns
         */
        boolean isValidResult(TranspilerResult result);

    }

}
