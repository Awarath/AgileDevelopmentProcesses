package se.chalmers.group8.github.connector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tio on 5/14/15.
 */

public class DataProcessor {

    public static String createdURL;
    public static String createdSha;

    public static List nameList = new ArrayList();
    public static List shaList = new ArrayList();
    public static List tempBranchList = new ArrayList();
    public static List tempNanmeList = new ArrayList();
    public static List tempTimeList = new ArrayList();
    public static List tempCommentList = new ArrayList();

    public static int branchIndex;


    public static void setBranchURL (String URL) {

        createdURL = URL;

    }

    public static void setSha(String sha) {

        createdSha = "https://api.github.com/repos/Awarath/AgileDevelopmentProcesses/commits/" + sha;

    }


    public static void setBranchName (String repo) {

        nameList.add(repo);

    }

    public static void setShaList (String sha) {

        shaList.add(sha);

    }

    public static void setTempBranch (String name) {

        tempBranchList.add(name);

    }

    public static void setTempName (String name) {

        tempNanmeList.add(name);

    }

    public static void setTempTime (String name) {

        tempTimeList.add(name);

    }

    public static void setTempComment (String name) {

        tempCommentList.add(name);

    }

    public static void setBranchIndex () {

        branchIndex += 1;

    }

    public static void compareList(String sha) {
        if(shaList.contains(sha)) {
            //System.out.println("The branch" + nameList.get(branchIndex) +  " is not changed!!!");
        } else {
            System.out.println("The branch" + nameList.get(branchIndex) + " is changed!!!");
            shaList.set(branchIndex, sha);
        }

    }

}
