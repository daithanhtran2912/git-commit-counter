package model;

import java.util.Date;

public class CommitInfo extends Contributor {

    private String commitMessage;
    private Date when;

    public CommitInfo(String name, String emailAddress) {
        super(name, emailAddress);
    }

    public CommitInfo(String name, String emailAddress, String commitMessage, Date when) {
        super(name, emailAddress);
        this.commitMessage = commitMessage;
        this.when = when;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getEmailAddress() {
        return super.getEmailAddress();
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        super.setEmailAddress(emailAddress);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "CommitInfo: CommitMessage=" + commitMessage + ", When=" + when + ", Name=" + getName() +
                ", Email address=" + getEmailAddress();
    }
}
