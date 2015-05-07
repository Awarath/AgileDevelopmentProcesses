package se.chalmers.group8.github.connector;


import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;

/**
 * Created by Tio on 5/4/15.
 */
public class Authenticating {



    //Authenticating
    public static void GetAuthentication() throws IOException {

        String token = "e59caac4be0c98332727683c0447fc178aab0c61";

        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(token);
        System.out.println("GetAuthentication---------------------------------------------------");
        //GetRepository();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    RepositoryService service = new RepositoryService();
                    System.out.println("GetRepository-------------------------------------------------------");
                    for (Repository repo : service.getRepositories("AgileDevelopmentProcesses"))
                        System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }



    //Get a user's repositories （unused）
    public static void GetRepository() throws IOException {

        RepositoryService service = new RepositoryService();
        System.out.println("GetRepository-------------------------------------------------------");
        for (Repository repo : service.getRepositories("AgileDevelopmentProcesses"))
            System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());

    }

    //Get the branch status of a repository
    public static void GetBranchStatus() {

    }

}
