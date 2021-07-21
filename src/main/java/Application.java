import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Application {

    public static void main(String[] arg) {
        try {
            getCommitsLogFromExistingRepo();
        } catch (IOException | GitAPIException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void getCommitsLogFromExistingRepo() throws GitAPIException, IOException, ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
        final Date start = formatter.parse("2020/04/25 20:12:00");
        final Date end = formatter.parse("2021/07/21 21:53:00");
        Git git = Git.init().setDirectory(new File("some file path")).call();
        for (RevCommit commit : git.log().all().call()) {
            PersonIdent committerInfo = commit.getAuthorIdent();
            if (isCommitBetween(committerInfo.getWhen(), start, end)) {
                System.out.printf("|%s| %s - %s-%s\n", formatter.format(committerInfo.getWhen()),
                        commit.getFullMessage(), committerInfo.getName(), committerInfo.getEmailAddress());
            }
        }
    }

    private static boolean isCommitBetween(Date commitDate, Date start, Date end) {
        return commitDate.getTime() >= start.getTime() && commitDate.getTime() <= end.getTime();
    }
}
