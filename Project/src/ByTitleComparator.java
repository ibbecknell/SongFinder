import java.util.Comparator;

/**
 * Comparator to sort a data structure by song title, if titles are the same,
 * sorts by artist title. If title and artist are the same, sorts by track id
 * 
 * implements Comparator Interface
 * 
 * @author missionbit
 *
 */
public class ByTitleComparator implements Comparator<Song> {

	@Override
	public int compare(Song o1, Song o2) {
		if (o1.getTitle().equals(o2.getTitle())) {
			if (o1.getArtist().equals(o2.getArtist())) {
				return o1.getTrackId().compareTo(o2.getTrackId());
			}
			return o1.getArtist().compareTo(o2.getArtist());
		}
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
