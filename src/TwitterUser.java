import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TwitterUser implements Comparable<TwitterUser>, Cloneable {

	private int usersID;

	private ArrayList<TwitterUser> following = new ArrayList<TwitterUser>();
	private ArrayList<TwitterUser> neighborHood = new ArrayList<TwitterUser>();
	// Empty constructor. Keeping it in just in case I do need to do something with that.
	// It's always a good idea to have constructors.
	public TwitterUser() 
	{

	}

	public TwitterUser(int userID) 
	{
		this.usersID = userID;
	}
	// Recursive method.
	public ArrayList<TwitterUser> getNeighborhood(TwitterUser users, int depth)
	{
		// if the depth I'm searching for is > 0
		if(depth > 0)
		{
			// for each twitter user getting the following array list
			for(TwitterUser user: users.getFollowing())
			{
				// if the neighbor hood contains these users
				if(neighborHood.contains(users))
				{
					// recursive call with a depth - 1
					getNeighborhood(user, depth -1);
				}
				else
				{
					// other wise I add that user to the neighbor hood
					// and have another recursive call.
					neighborHood.add(user);
					getNeighborhood(user, depth -1);
				}
			}
		}
		return neighborHood;
	}
	
	// This method adds to the following array list the user passed into this function as long
	// as the doesn't contain the user.
	public void followUser(TwitterUser user) 
	{
		if (!following.contains(user)) 
		{
			following.add(user);
		}
	}

	public void clearFollowing() 
	{
		following.clear();
	}

	public ArrayList<TwitterUser> getFollowing() 
	{
		// I'm making an array list copy of the current one I have.
		ArrayList<TwitterUser> copy = new ArrayList<TwitterUser>();
		
		// for each twitter user in the following array list I'm adding to this copy array list a twitter user object.
		for (TwitterUser t : following) 
		{
			copy.add(t);
		}
		return copy;
	}

	public int getID() 
	{
		return usersID;
	}

	public void setUserID(int newId) 
	{
		usersID = newId;
	}

	@Override
	public int compareTo(TwitterUser tUserObj) 
	{
		// > 1 means greater than.
		if (usersID > tUserObj.getID()) 
		{
			return 1;
			
		} 
		// -1 means less than.
		else if (usersID < tUserObj.getID()) 
		{
			return -1;
			
		} 
		// 0 means equivallent.
		else 
		{
			return 0;
		}
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + usersID;
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterUser other = (TwitterUser) obj;
		if (usersID != other.usersID)
			return false;
		return true;
	}
	// this method makes a cloned object of the twitter users.
	public TwitterUser clone() throws CloneNotSupportedException
	{
		TwitterUser clonedObj = new TwitterUser(0);
		
		ArrayList<TwitterUser> clonedFollowersOfUsers = new ArrayList<>();
		
		clonedObj.usersID = this.usersID;
		
		if(this.following != null)
		{
			clonedFollowersOfUsers = (ArrayList<TwitterUser>) this.following.clone();
			
			clonedObj.setFollowing(clonedFollowersOfUsers);
		}
		
		return clonedObj;
	}

	public String toString() 
	{
		return Integer.toString(usersID);
	}
	
	//COMMENT EXPLANATION!
	// I'm passing into this method a twitter user object being a user and I'm returning a collection of users.
	public Collection<TwitterUser> getFollowing(TwitterUser user) 
	{
		// I'm making a collection of users.
		Collection<TwitterUser> users = new ArrayList<TwitterUser>();
		
		// for every user getting the followers
		// I add to the users array list a new TwitterUser Integer object argument.
		for (Integer t : user.getFollowers()) 
		{
			users.add(new TwitterUser(t));
		}
		
		return users;
	}
	
	// followers list not following
	private List<Integer> followers = new ArrayList<>();

	public void setFollowers(List<Integer> followers) 
	{
		// for each Integer object in the followers list 
		for (Integer i : followers) 
		{
			// if the followers doesn't contain some said i object. Then I want to add that to the followers list.
			if (!followers.contains(i))
			{
				followers.add(i);
			}
		}
	}
	// getter
	public List<Integer> getFollowers() 
	{
		return followers;
	}
	// setter for following array list.
	public void setFollowing(ArrayList<TwitterUser> following) 
	{
		this.following = following;
	}

	// Followers end
}