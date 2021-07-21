import model.Contributor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import utils.CommitCounter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] arg) {
        try {
            String filePath = "some file path";
            // yyyy/MM/dd HH:mm:ss
            String startDate = "date";
            String endDate = "date";

            Git git = Git.init().setDirectory(new File(filePath)).call();
            CommitCounter counter = new CommitCounter();
            List<Contributor> contributors = counter.getAllContributors(git);
            Map<String, Integer> totalCommits = counter.countCommit(git, startDate, endDate, contributors);
            for (Map.Entry<String, Integer> contributor : totalCommits.entrySet()) {
                System.out.println(contributor.toString());
            }
        } catch (IOException | GitAPIException | ParseException e) {
            e.printStackTrace();
        }
    }
}
