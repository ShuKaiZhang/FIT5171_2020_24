package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * An artist that has been featured in (at least) one ECM record.
 *
 * See {@https://www.ecmrecords.com/artists/1435045745}
 */
@NodeEntity
public class Musician extends Entity {
    @Property(name="name")
    private String name;

    @Convert(URLConverter.class)
    @Property(name="musicianURL")
    private URL musicianUrl;

    @Relationship(type="albums")
    private Set<Album> albums;

    @Convert(URLConverter.class)
    @Property(name="musicianWiki")
    private URL musicianWiki;

    @Property(name="biography")
    private  String biography;

    public Musician() {
    }

    public Musician(String name) {
        notNull(name);
        notBlank(name);


        if (!name.trim().contains(" ")){
            throw new IllegalArgumentException("The albums cannot be blank!");
        }else {
            this.name = name;
            this.musicianUrl = null;
            //albums = Sets.newLinkedHashSet();
            this.musicianWiki = null;
            this.biography = null;
            albums = Sets.newHashSet();
        }
    }

    public String getName() {
        return name;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        notNull(albums);
        if (albums.isEmpty()||albums.contains(null)){
            throw new IllegalArgumentException("The albums cannot be blank!");
        }else
            this.albums = albums;
    }


    public URL getMusicianWiki() {
        return musicianWiki;
    }

    public void setMusicianWiki(URL musicianWiki) {
        this.musicianWiki = musicianWiki;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician that = (Musician) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public URL getMusicianUrl() {
        return musicianUrl;
    }

    public void setMusicianUrl(URL musicianUrl) {
        this.musicianUrl = musicianUrl;
    }

    @Override
    public void setsetMusicianUrl(String s) {

    }
}
