package utils;

import model.CommitInfo;
import model.Contributor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class CommitCounter {

    private static final String DATE_TIME_PATTERN = "yyyy/MM/dd HH:mm:ss";
    private static final String DEFAULT_TIME_ZONE = "GMT+7:00";
    private DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
    private TimeZone timeZone;

    public CommitCounter() {
        dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
    }

    public List<Contributor> getAllContributors(Git git) throws IOException, GitAPIException {
        List<Contributor> contributors = new ArrayList<>();
        List<RevCommit> revCommits = new ArrayList<>();
        git.log().all().call().forEach(revCommits::add);
        revCommits.forEach(commit -> {
            PersonIdent authorIdent = commit.getAuthorIdent();
            Contributor author = new Contributor(authorIdent.getName(), authorIdent.getEmailAddress());
            if (!contributors.contains(author)) {
                contributors.add(author);
            }
        });
        return contributors;
    }

    public List<CommitInfo> getCommitsLogFromExistingRepo(Git git, String fromDate, String toDate)
            throws GitAPIException, IOException, ParseException {
        final Date start;
        final Date end;
        Iterable<RevCommit> revCommits = git.log().all().call();
        List<CommitInfo> commitInfos = new ArrayList<>();

        if ((fromDate == null || fromDate.isEmpty()) && (toDate == null || toDate.isEmpty())) {
            // get all commits
            for (RevCommit commit : revCommits) {
                PersonIdent authorIndent = commit.getAuthorIdent();
                CommitInfo commitInfo = new CommitInfo(authorIndent.getName(), authorIndent.getEmailAddress(),
                        commit.getFullMessage(), authorIndent.getWhen());
                commitInfos.add(commitInfo);
            }
            return commitInfos;
        }

        if (fromDate != null && !fromDate.isEmpty() && toDate != null && !toDate.isEmpty()) {
            // get commits from start to end date
            start = dateFormat.parse(fromDate);
            end = dateFormat.parse(toDate);
            for (RevCommit commit : revCommits) {
                PersonIdent authorIndent = commit.getAuthorIdent();
                if (isCommitTimeBetween(authorIndent.getWhen(), start, end)) {
                    CommitInfo commitInfo = new CommitInfo(authorIndent.getName(), authorIndent.getEmailAddress(),
                            commit.getFullMessage(), authorIndent.getWhen());
                    commitInfos.add(commitInfo);
                }
            }
            return commitInfos;
        }

        if (fromDate == null || fromDate.isEmpty()) {
            // get all commits from the beginning
            end = dateFormat.parse(toDate);
            for (RevCommit commit : revCommits) {
                PersonIdent authorIndent = commit.getAuthorIdent();
                if (isCommitTimeBefore(authorIndent.getWhen(), end)) {
                    CommitInfo commitInfo = new CommitInfo(authorIndent.getName(), authorIndent.getEmailAddress(),
                            commit.getFullMessage(), authorIndent.getWhen());
                    commitInfos.add(commitInfo);
                }
            }
            return commitInfos;
        }

        // get all commits from input date till current
        start = dateFormat.parse(fromDate);
        for (RevCommit commit : revCommits) {
            PersonIdent authorIndent = commit.getAuthorIdent();
            if (isCommitTimeAfter(authorIndent.getWhen(), start)) {
                CommitInfo commitInfo = new CommitInfo(authorIndent.getName(), authorIndent.getEmailAddress(),
                        commit.getFullMessage(), authorIndent.getWhen());
                commitInfos.add(commitInfo);
            }
        }
        return commitInfos;
    }

    public Map<String, Integer> countCommit(Git git, String fromDate, String toDate, List<Contributor> contributors)
            throws ParseException, IOException, GitAPIException {
        Map<String, Integer> result = new HashMap<>();
        List<CommitInfo> commits = this.getCommitsLogFromExistingRepo(git, fromDate, toDate);
        contributors.forEach(contributor -> {
            int totalCommit = (int) commits.stream()
                    .map(commitInfo -> new Contributor(commitInfo.getName(), commitInfo.getEmailAddress()))
                    .filter(contributor::equals)
                    .count();
            result.put(contributor.getName(), totalCommit);
        });
        return result;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        dateFormat.setTimeZone(this.timeZone);
    }

    private boolean isCommitTimeBetween(Date commitDate, Date start, Date end) {
        return isCommitTimeAfter(commitDate, start) && isCommitTimeBefore(commitDate, end);
    }

    private boolean isCommitTimeBefore(Date commitDate, Date end) {
        return commitDate.getTime() <= end.getTime();
    }

    private boolean isCommitTimeAfter(Date commitDate, Date start) {
        return commitDate.getTime() >= start.getTime();
    }
}
