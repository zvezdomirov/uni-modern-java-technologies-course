package bg.sofia.uni.fmi.mjt.jira.interfaces;

import bg.sofia.uni.fmi.mjt.jira.issues.Issue;

public interface Filter {
    Issue find(String issueId);
}
