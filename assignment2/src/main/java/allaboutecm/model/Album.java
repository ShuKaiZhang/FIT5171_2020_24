package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.net.URL;
import java.util.*;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Represents an album released by ECM records.
 *
 * See {@https://www.ecmrecords.com/catalogue/143038750696/the-koln-concert-keith-jarrett}
 */
@NodeEntity
public class Album extends Entity {

    @Property(name="releaseYear")
    private int releaseYear;

    @Property(name="recordNumber")
    private String recordNumber;

    @Property(name="albumName")
    private String albumName;

    /**
     * CHANGE: instead of a set, now featuredMusicians is a list,
     * to better represent the order in which musicians are featured in an album.
     */
    @Relationship(type="featuredMusicians")
    private List<Musician> featuredMusicians;

    @Relationship(type="instruments")
    private Set<MusicianInstrument> instruments;

    @Convert(URLConverter.class)
    @Property(name="albumURL")
    private URL albumURL;

    @Property(name="tracks")
    private List<String> tracks;

    @Property(name="style")
    private String style;

    @Property(name="releaseFormat")
    private String releaseFormat;

    public Album() {
    }

    public Album(int releaseYear, String recordNumber, String albumName) {
        notNull(recordNumber);
        notBlank(recordNumber);

        if (recordNumber.substring(0,4).equals("ECM ")){
            this.recordNumber = recordNumber;

        }else {
            //System.out.println(recordNumber.substring(0,3));
            throw new IllegalArgumentException("The record number must start with 'ECM '!");
        }

        if (releaseYear> Calendar.getInstance().get(Calendar.YEAR) ||releaseYear<1900){
            throw new IllegalArgumentException("Release Year is not a valid year");
        }
        else {
            this.releaseYear = releaseYear;
        }
        //
        notNull(albumName);
        notBlank(albumName);
        if(albumName.length()>100) {
            throw new IllegalArgumentException("Album name is too long.");
        } else {
            this.albumName = albumName;
        }
        //

        this.releaseFormat = null;
        this.style = null;
        this.albumURL = null;

        featuredMusicians = Lists.newArrayList();
        instruments = Sets.newHashSet();
        tracks = new ArrayList<>();
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        notNull(recordNumber);
        notBlank(recordNumber);
        if (recordNumber.substring(0,4).equals("ECM ")){
            this.recordNumber = recordNumber;

        }else
            throw new IllegalArgumentException("The record number must start with 'ECM '!");
    }

    public List<Musician> getFeaturedMusicians() {
        return featuredMusicians;
    }

    public void setFeaturedMusicians(List<Musician> featuredMusicians) {
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

    public List<String> getTracks() {
        return tracks;
    }

    public void setTracks(List<String> tracks) {
        notNull(tracks);
        if (tracks.isEmpty()||tracks.contains("")){
            throw new IllegalArgumentException("The tracks cannot be blank!");
        }
        else{
            this.tracks = tracks;}
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        if (releaseYear> Calendar.getInstance().get(Calendar.YEAR) ||releaseYear<1900){
            throw new IllegalArgumentException("Release Year is not a valid year");
        }
        else {
            this.releaseYear = releaseYear;
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        notNull(albumName);
        notBlank(albumName);

        if(albumName.length()>100) {
            throw new IllegalArgumentException("Album name is too long.");
        } else {
            this.albumName = albumName;
        }
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
