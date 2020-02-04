package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;
import bg.sofia.uni.fmi.mjt.jira.exceptions.NullParameterException;
import bg.sofia.uni.fmi.mjt.jira.exceptions.UnresolvableIssueException;

public class Bug extends Issue {

    public Bug(IssuePriority priority, Component component, String description) {
        super(priority, component, description);
    }

    @Override
    public void resolve(IssueResolution resolution) {
        if (resolution == null) {
            throw new NullParameterException("IssueResolution");
        }
        if (!this.containsAction(WorkAction.FIX) ||
                !this.containsAction(WorkAction.TESTS)) {
            throw new UnresolvableIssueException(
                    "Issue doesn't contain needed actions to be resolved.");
        }
        this.setResolution(resolution);
        this.setStatus(IssueStatus.RESOLVED);
    }
}
