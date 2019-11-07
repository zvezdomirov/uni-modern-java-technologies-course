package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;
import bg.sofia.uni.fmi.mjt.jira.exceptions.LimitReachedException;
import bg.sofia.uni.fmi.mjt.jira.exceptions.NullParameterException;

import java.time.LocalDateTime;

public abstract class Issue {
    public static final int MAX_ACTIONS = 20;
    private static int issueCount = 0;
    private int actionCount;
    private String issueId;
    private String description;
    private IssuePriority priority;
    private IssueResolution resolution;
    private IssueStatus status;
    private Component component;
    private LocalDateTime createdOn;
    private LocalDateTime lastModifiedOn;
    private String[] actionLog;

    public Issue(IssuePriority priority, Component component, String description) {
        this.issueId = String.format(
                "%s-%d",
                component.getShortName(),
                issueCount++);
        this.description = description;
        this.priority = priority;
        this.resolution = IssueResolution.UNRESOLVED;
        this.status = IssueStatus.OPEN;
        this.component = component;
        this.createdOn = LocalDateTime.now();
        this.lastModifiedOn = LocalDateTime.now();
        this.actionLog = new String[MAX_ACTIONS];
        this.actionCount = 0;
    }

    public String getIssueID() {
        return issueId;
    }

    public String getDescription() {
        return description;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public IssueResolution getResolution() {
        return resolution;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public Component getComponent() {
        return component;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getLastModifiedOn() {
        return lastModifiedOn;
    }

    public String[] getActionLog() {
        return actionLog;
    }

    public void setResolution(IssueResolution resolution) {
        this.resolution = resolution;
    }

    public void setStatus(IssueStatus status) {
        if (status == null) {
            throw new NullParameterException("IssueStatus");
        }
        this.status = status;
        this.lastModifiedOn = LocalDateTime.now();
    }

    public void addAction(WorkAction action, String description) {
        if (action == null) {
            throw new NullParameterException("WorkAction");
        }
        if (this.actionCount >= MAX_ACTIONS) {
            throw new LimitReachedException(
                    "Actions limit reached: " + MAX_ACTIONS);
        }
        this.actionLog[actionCount++] = String.format(
                "%s: %s",
                action.getName(),
                description);
        this.lastModifiedOn = LocalDateTime.now();
    }

    public boolean containsAction(WorkAction action) {
        for (String a : this.actionLog) {
            if (a == null) {
                continue;
            }
            if (a.contains(action.getName())) {
                return true;
            }
        }
        return false;
    }

    public abstract void resolve(IssueResolution resolution);
}
