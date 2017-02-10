//Maxim Torubarov, Mihir Patel

package sl.application;
import java.util.*;

public class SongNameComparator implements Comparator<Song> {

	@Override
	public int compare(Song s1, Song s2) {
		return s1.getName().compareToIgnoreCase(s2.getName()) ; 
	}
	
}
