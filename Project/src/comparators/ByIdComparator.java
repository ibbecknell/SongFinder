package comparators;

import java.util.Comparator;

import project1_librarybuilding.Song;

public class ByIdComparator implements Comparator<Song> {
	@Override
	public int compare(Song o1, Song o2) {
		return o1.getTrackId().compareTo(o2.getTrackId());
	}
}
