package comparators;

import java.util.Comparator;

import project1_librarybuilding.Song;

/**
 * Comparator to sort a data structure by artist name, if artist names are the
 * same, sorts by title. If artist and title are the same, sorts by track id
 * 
 * Implements Comparator Interface
 * 
 * @author missionbit
 *
 */
public class ByArtistComparator implements Comparator<Song> {

	@Override
	public int compare(Song o1, Song o2) {
		if (o1.getArtist().equals(o2.getArtist())) {
			if (o1.getTitle().equals(o2.getTitle())) {
				return o1.getTrackId().compareTo(o2.getTrackId());
			}
			return o1.getTitle().compareTo(o2.getTitle());
		}
		return o1.getArtist().compareTo(o2.getArtist());
	}

}
