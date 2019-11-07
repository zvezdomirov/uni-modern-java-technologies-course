package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;
import bg.sofia.uni.fmi.mjt.jira.exceptions.NullParameterException;

public class Task extends Issue {

    public Task(IssuePriority priority, Component component, String description) {
        super(priority, component, description);
    }

    @Override
    public void addAction(WorkAction action, String description) {
        if (action == WorkAction.FIX ||
                action == WorkAction.IMPLEMENTATION ||
                action == WorkAction.TESTS) {
            throw new IllegalArgumentException(
                    "This action is not suitable for issues of type \"Task\"");
        }
        super.addAction(action, description);
    }

    @Override
    public void resolve(IssueResolution resolution) {
        if (resolution == null) {
            throw new NullParameterException("IssueResolution");
        }
        this.setResolution(resolution);
        this.setStatus(IssueStatus.RESOLVED);
    }
}
