package sl.application;


public class Song{
	private String name ; //Name of the Song 
	private String artist ; //Name of the Artist who made the song
	private int year ; //year in which this song was released  
	private String album ; //name of the album which this song was in 
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public Song(String name, String artist, int year, String album) {
		this.name = name;
		this.artist = artist;
		this.year = year;
		this.album = album;
	}
	
	public Song (String name , String artist) {
		this.name = name ; 
		this.artist = artist ;
	}
	
	public Song (String name , String artist, int year) {
		this.name = name ; 
		this.artist = artist ;
		this.year = year ; 
	}
	
	public Song (String name , String artist, String album) {
		this.name = name ; 
		this.artist = artist ;
		this.album = album ; 
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
	
}
