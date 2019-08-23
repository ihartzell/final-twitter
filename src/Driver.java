import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Driver 
{
	// I'm making a static array list of twitter user objects so that all the static methods have access to this.
	static ArrayList<TwitterUser> allUsers = new ArrayList<TwitterUser>();
	static ArrayList<TwitterUser> sortedAllUsers = new ArrayList<TwitterUser>();
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) 
	{
		String fileName;
		String strLine;
		int userID, followerID; 
		TwitterUser userObj, followerObj;

		long startTime = System.currentTimeMillis();
		long endTime;
		
		System.out.print("I will display the file information \n");
		fileName = "small.txt";
		
		System.out.println("Running the file ...: \n");

		try 
		{
			// I'm creating a buffered reader object for extra speed and it's assigned a FileReader object being the filename.
			BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
			
			System.out.println("Reading data from the file...\n");
			
			// I'm making a followers stack of Integer array objects.
			Stack<Integer[]> followers = new Stack<Integer[]>();
			
			// I'm making a hash set of TwitterUsers up to 1 million.
			HashSet<TwitterUser> hashSet = new HashSet<TwitterUser>(1000000);
			
			// Looping through reading of the file.
			while ((strLine = fileReader.readLine()) != null && !strLine.isEmpty()) 
			{
				// I'm splitting the spaces in a String array.
				String[] idsSpaces = strLine.split(" ");
				userID = Integer.parseInt(idsSpaces[0]);
				followerID = Integer.parseInt(idsSpaces[1]);
				
				// Adding the userId and the follower Id to this hash set.
				hashSet.add(new TwitterUser(userID));
				hashSet.add(new TwitterUser(followerID));
				
				// I'm pushing the array of followers Integer objects hash set to the top of the stack.
				followers.push(new Integer[] { userID, followerID });
			}
			endTime = System.currentTimeMillis();
			System.out.println("\nFinished adding " + hashSet.size()+ " users in " + (int) ((endTime - startTime) / 1000)+ " seconds.");
			
			// close the file.
			fileReader.close();

			// I'm mapping all TwitterUsers to their Integer Id via hash map.
			HashMap<Integer, TwitterUser> hashMap = new HashMap<Integer, TwitterUser>();
			
			// for each twitter user in the hash set I want to put the twitter user object  in the hash map.
			for (TwitterUser t : hashSet) 
			{
				hashMap.put(t.getID(), t);
			}
			
			// I'm creating a BufferedReader object like earlier.
			fileReader = new BufferedReader(new FileReader(fileName));

			startTime = System.currentTimeMillis();
			// counter variable to keep track of all the users.
			int counter = 0;
			
			System.out.println("\nFriending...");
			
			// Looping through the reading of the file.
			while ((strLine = fileReader.readLine()) != null && !strLine.isEmpty())
			{
				String[] idsSpaces = strLine.split(" ");
				userID = Integer.parseInt(idsSpaces[0]);
				followerID = Integer.parseInt(idsSpaces[1]);
				
				// I'm assigning these user and follower objects to the hashMap which can contain multiple null values vs the hash set.
				userObj = hashMap.get(userID);
				followerObj = hashMap.get(followerID);

				userObj.followUser(followerObj);
				counter++;
			}
			// close the file.
			fileReader.close();
			endTime = System.currentTimeMillis();
			System.out.println("\nFinished all " + counter+ " friending operations in "+ (int) ((endTime - startTime) / 1000) + " seconds. \n");
			
			// for each twitteruser in the hash map I add this twitter user object argument to the users array list.
			for (TwitterUser t : hashMap.values()) 
			{
				allUsers.add(t);
			}
			// calling for the mapFollowers method where I map the followers stack.
			mapFollowers(followers);
			
			// I'm assigning my sortedAllUsers array list to a cloned version of the allUsers list and cast it as an ArrayList of twitter users.
			sortedAllUsers = (ArrayList<TwitterUser>) allUsers.clone();
			
			// I'm sorting the now parallel array list and I'm using my comparator for the three different cases.
			Collections.sort(sortedAllUsers, new GetByPopularityComparatorForUsers());
		} 
		catch (FileNotFoundException ex)
		{
			System.out.println("I couldn't find the file.");
		} 
		catch(IOException ex)
		{
			System.out.println("An Issue occured at runtime.");
		}
		

		// Followers test begin
		System.out.println("\nFollowers test!");
		System.out.println("----------------- ");
		System.out.print("\nTest for the followers function:"+ "\nEnter the user you would like see list of followers: ");
		userID = input.nextInt();

		test_getFollowing(getUser(userID));
		// Followers test end

		// Popularity test begin
		System.out.println("\nPopularity test!");
		System.out.println("-----------------");
		System.out.print("\nTest for the followers getByPopularity function:"+ "\nEnter a number to see who who the most popular user is based on the number you input.");
		
		int idx = input.nextInt();
		TwitterUser u = getByPopularity(idx);
		
		System.out.print("\nThe  " + idx + "th popular user is : " + u.getID()+ ", number of followers  " + u.getFollowers().size() + "\n");
		// Popularity test end

		System.out.println("\t\tExiting Program...");
	}
	// I'm getting whatever the userID is for the allUsers array list.
	public static TwitterUser getUser(int userID) 
	{
		TwitterUser tempTwitObj;
		tempTwitObj = new TwitterUser(userID);
		
		// while all of the users doesn't contain this temporary twitter user object.
		// print out error message.
		while (!allUsers.contains(tempTwitObj)) 
		{
			System.out.print("\tInvalid user ID.  Enter a valid user id: ");
			userID = input.nextInt();
			tempTwitObj.setUserID(userID);
		}
		return allUsers.get(allUsers.indexOf(tempTwitObj));
	}
	// I'm passing a stack of Integer array objects named followers.
	private static void mapFollowers(Stack<Integer[]> followers)
	{
		// as long as the stack has something.
		while (!followers.isEmpty()) 
		{
			// pop off the stack 
			Integer[] anEntry = followers.pop();
			
			// first element in the stack.
			Integer aUserId = anEntry[1];
			
			List<Integer> f = findFollowersForUser((Stack<Integer[]>) followers.clone(), aUserId);
			
			System.err.println("User " + aUserId + " has " + f.size()+ " followers.");
			
			TwitterUser aUser = getUser(aUserId);
			aUser.setFollowers(f);
		}
	}
	
	// Creates a list of Integer objects.
	private static List<Integer> findFollowersForUser(Stack<Integer[]> stackList, Integer id) {

		List<Integer> list = new ArrayList<Integer>();
		
		// as long as this list contains something.
		while (!stackList.isEmpty())
		{
			// I pop of the ids off the stack list.
			Integer[] ids = stackList.pop();
			
			// if the id - the ids is nothing
			// then I want to add to this list the first element of zero.
			if (id - ids[1] == 0) 
			{
				list.add(ids[0]);
				
			}
		}
		return list;
	}
	// Tests the getFollowing method.
	public static void test_getFollowing(TwitterUser user) 
	{
		// I'm creating a collections of twitter user objects which is the followerUsers
		Collection<TwitterUser> followerUsers = user.getFollowing(user);
		
		// I'm creating an Integer list of followers
		List<Integer> followers = new ArrayList<Integer>();
		
		// for each twitter user in the followerUsers collection I add some user to that.
		for (TwitterUser aUser : followerUsers) 
		{
			followers.add(aUser.getID());
		}
		System.out.println("User " + user.getID() + " has " + followers+ " followers. ");
	}
	//COMMENT EXPLANATION!
	//This sorts a collection of users through what ever integer is passed into this function
	//The method returns a parallel array list to allUsers called sortedAllUsers
	//Need this in O(1) time.
	private static TwitterUser getByPopularity(int x) 
	{
		return sortedAllUsers.get(x);
	}
}
