import java.util.Comparator;

public class GetByPopularityComparatorForUsers implements Comparator<TwitterUser> 
{
	@Override
	public int compare(TwitterUser obj1, TwitterUser obj2)
	{
		int startingCriteria = 0;

		// 1. Number of followers (largest to smallest)
		if (obj1.getFollowers().size() > obj2.getFollowers().size())
		{
			startingCriteria = 1;
		}
		// 2. If two users have the same number of followers, sort by the number
		// of people that user is following (largest to smallest)
		if (obj1.getFollowers().size() == obj2.getFollowers().size())
		{
			startingCriteria = obj1.getFollowing().size() > obj2.getFollowing().size() ? 1 : 0;
		}
		// 3. If two users have the same number of followers and are following
		// the same number of people, sort by user id (smallest to largest)
		if ((obj1.getFollowers().size() == obj2.getFollowers().size()) && (obj1.getFollowing().size() == obj2.getFollowing().size()))
		{
			startingCriteria = (obj1.getID() < obj2.getID() ? 1 : 0);
		}
		return startingCriteria;
	}
}
