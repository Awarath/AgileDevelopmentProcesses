package se.chalmers.group8.github.connector;


import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;

/**
 * Created by Tio on 5/4/15.
 */
public class Authenticating {

    private String token = "e59caac4be0c98332727683c0447fc178aab0c61";

    //Authenticating
    public static void GetAuthentication() throws IOException {

        GitHubClient client = new GitHubClient();
        client.setOAuth2Token("token");

    }

    //Get a user's repositories
    public static void GetRepository () throws IOException {

        RepositoryService service = new RepositoryService();
        for (Repository repo : service.getRepositories("defunkt"))
            System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());

    }

    //Get the branch status of a repository
    public static void GetBranchStatus() {

    }

}
