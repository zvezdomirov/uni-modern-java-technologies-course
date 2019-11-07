package bg.sofia.uni.fmi.mjt.jira;

import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;
import bg.sofia.uni.fmi.mjt.jira.exceptions.LimitReachedException;
import bg.sofia.uni.fmi.mjt.jira.exceptions.NullParameterException;
import bg.sofia.uni.fmi.mjt.jira.interfaces.Filter;
import bg.sofia.uni.fmi.mjt.jira.interfaces.Repository;
import bg.sofia.uni.fmi.mjt.jira.issues.Issue;

public class Jira implements Filter, Repository {
    public static final int MAX_ISSUES = 100;
    private int issueCount;
    private Issue[] issues;

    public Jira() {
        this.issues = new Issue[MAX_ISSUES];
        this.issueCount = 0;
    }

    public void addActionToIssue(Issue issue, WorkAction action, String actionDescription) {
        if (issue == null ||
                action == null ||
                actionDescription == null) {
            throw new NullParameterException();
        }
        issue.addAction(action, actionDescription);
    }

    public void resolveIssue(Issue issue, IssueResolution resolution) {
        if (issue == null || resolution == null) {
            throw new NullParameterException();
        }
        for (Issue i : this.issues) {
            if (i.getIssueID().equals(issue.getIssueID())) {
                i.resolve(resolution);
            }
        }
    }

    @Override
    public Issue find(String issueId) {
        if (this.issues.length == 0) {
            throw new RuntimeException("No issues added.");
        }
        for (Issue issue : this.issues) {
            if (issue == null) {
                continue;
            }
            if (issueId.equals(issue.getIssueID())) {
                return issue;
            }
        }
        return null;
    }

    @Override
    public void addIssue(Issue issue) {
        if (issue == null) {
            throw new NullParameterException("Issue");
        }
        if (this.issueCount >= MAX_ISSUES) {
            throw new LimitReachedException(
                    "Issues limit reached: " + MAX_ISSUES);
        }
        for (Issue i : issues) {
            if (i == null) {
                continue;
            }
            if (issue.getIssueID().equals(i.getIssueID())) {
                throw new IllegalArgumentException("Issue already present.");
            }
        }
        issues[issueCount++] = issue;
    }
}
