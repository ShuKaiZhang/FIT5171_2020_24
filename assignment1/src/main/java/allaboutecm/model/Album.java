package allaboutecm.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.net.URL;
import java.util.*;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Represents an album released by ECM records.
 *
 * See {@https://www.ecmrecords.com/catalogue/143038750696/the-koln-concert-keith-jarrett}
 */
public class Album extends Entity {

    private int releaseYear;

    private String recordNumber;

    private String albumName;

    private Set<Musician> featuredMusicians;

    private Set<MusicianInstrument> instruments;

    private URL albumURL;

    private HashMap<String, Double> tracks;

    private String style;

    private String releaseFormat;

    public Album(int releaseYear, String recordNumber, String albumName) {
        notNull(recordNumber);
        notNull(albumName);

        notBlank(recordNumber);
        notBlank(albumName);

        this.releaseYear = releaseYear;
        this.recordNumber = recordNumber;
        this.albumName = albumName;

        this.releaseFormat = null;
        this.style = null;
        this.albumURL = null;

        featuredMusicians = Sets.newHashSet();
        instruments = Sets.newHashSet();
        tracks = new HashMap<>();
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        notNull(recordNumber);
        notBlank(recordNumber);
        if (recordNumber.substring(3).equals("ECM ")){
            this.recordNumber = recordNumber;

        }else
            throw new IllegalArgumentException("The record number must start with 'ECM '!");
    }

    public Set<Musician> getFeaturedMusicians() {
        return featuredMusicians;
    }


    public void setFeaturedMusicians(Set<Musician> featuredMusicians) {
        if (featuredMusicians.isEmpty()){
            throw new IllegalArgumentException("The featured musicians cannot be blank!");
        }else
            this.featuredMusicians = featuredMusicians;
    }

    public Set<MusicianInstrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<MusicianInstrument> instruments) {
        notNull(instruments);
        if (instruments.isEmpty()){
            throw new IllegalArgumentException("The instruments cannot be blank!");
        }else
        this.instruments = instruments;
    }

    public URL getAlbumURL() {
        return albumURL;
    }

    public void setAlbumURL(URL albumURL) {
        notNull(albumURL);
        this.albumURL = albumURL;
    }

    public HashMap<String, Double> getTracks() {
        return tracks;
    }

    public void setTracks(HashMap<String, Double> tracks) {
        notNull(tracks);
        if (tracks.isEmpty()||tracks.containsKey("")){
            throw new IllegalArgumentException("The tracks cannot be blank!");
        }
        else{
        this.tracks = tracks;}
        tracks.forEach((key, value) -> {
            if (value <= 0.0) {
                throw new IllegalArgumentException("The tracks length cannot less than 0!");
            }
        });
    }

        public int getReleaseYear() {
        return releaseYear;
    }
    //done
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
        if (releaseYear> Calendar.getInstance().get(Calendar.YEAR) ||releaseYear<1900){
            throw new IllegalArgumentException("Release Year is not a valid year");
        }
        else{
            this.releaseYear = releaseYear;
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        notNull(albumName);
        notBlank(albumName);

        this.albumName = albumName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return releaseYear == album.releaseYear &&
                recordNumber.equals(album.recordNumber) &&
                albumName.equals(album.albumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(releaseYear, recordNumber, albumName);
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getReleaseFormat() {
        return releaseFormat;
    }

    public void setReleaseFormat(String releaseFormat) {
        this.releaseFormat = releaseFormat;
    }
}
