package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.support.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TODO: add test cases to adequately test the Neo4jDAO class.
 */
class Neo4jDAOUnitTest {
    private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void setUp() {
        // See @https://neo4j.com/docs/ogm-manual/current/reference/ for more information.

        // To use an impermanent embedded data store which will be deleted on shutdown of the JVM,
        // you just omit the URI attribute.

        // Impermanent embedded store
        Configuration configuration = new Configuration.Builder().build();

        // Disk-based embedded store
        // Configuration configuration = new Configuration.Builder().uri(new File(TEST_DB).toURI().toString()).build();

        // HTTP data store, need to install the Neo4j desktop app and create & run a database first.
//        Configuration configuration = new Configuration.Builder().uri("http://neo4j:password@localhost:7474").build();

        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();

        dao = new Neo4jDAO(session);
    }

    @AfterEach
    public void tearDownEach() {
        session.purgeDatabase();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        session.purgeDatabase();
        session.clear();
        sessionFactory.close();
        File testDir = new File(TEST_DB);
        if (testDir.exists()) {
//            FileUtils.deleteDirectory(testDir.toPath());
        }
    }

    @Test
    public void daoIsNotEmpty() {
        assertNotNull(dao);
    }

    @Test
    public void successfulCreationAndLoadingOfMusician() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());

        assertEquals(1, dao.loadAll(Musician.class).size());

//        dao.delete(musician);
//        assertEquals(0, dao.loadAll(Musician.class).size());
    }

    @Test
    public void successfulCreationOfMusicianAndAlbum() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setAlbums(Sets.newHashSet(album));

        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, musicians.size());
        Musician loadedMusician = musicians.iterator().next();
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());
        assertEquals(musician.getAlbums(), loadedMusician.getAlbums());
    }

    @Test
    public void cannotSaveSameMusicianTwice() throws MalformedURLException {
        Musician musician1 = new Musician("Keith Jarrett");
        musician1.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        dao.createOrUpdate(musician1);

        Musician musician2 = new Musician("Keith Jarrett");
        musician2.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        dao.createOrUpdate(musician2);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, musicians.size());
        assertEquals(musician1.getName(), musicians.iterator().next().getName());
    }

    @Test
    public void saveMultiMusicians() {
        Set<Musician> musicianSet = Sets.newHashSet(
                new Musician("Shukai Zhang"),
                new Musician("Qiuli Wang"),
                new Musician("Yike Xu")
        );

        for(Musician musician: musicianSet) {
            dao.createOrUpdate(musician);
        }

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(musicianSet.size(),musicians.size(), "loaded number is not right");
        for (Musician musician: musicianSet) {
            assertTrue(musicians.contains(musician), "Not contains :"+ musician.getName());
        }
        for (Musician musician: musicians) {
            assertTrue(musicianSet.contains(musician), "Not contains :"+ musician.getName());
        }
    }

    @Test
    public void musicianInfoCanBeUpdated() {
        Musician musician = new Musician("Keith Jarrett");
        musician.setBiography("He is a singer!");
        dao.createOrUpdate(musician);

        musician.setBiography("He is an actor!");
        dao.createOrUpdate(musician);

        Musician mBiography = dao.load(Musician.class, musician.getId());
        assertNotEquals("He is a singer!", mBiography.getBiography());
    }

    @Test
    public void deleteMusician() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);
        dao.delete(musician);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(0, musicians.size(),"Delete failed");
    }

    @Test
    public void deleteNotExistMusician() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        assertThrows(IllegalArgumentException.class, () -> dao.delete(musician));
    }

    @Test
    public void findMusicianByName() {
        Musician musician = new Musician("Keith Jarrett");

        dao.createOrUpdate(musician);

        Musician foundMusician = dao.findMusicianByName("Keith Jarrett");

        assertEquals(musician, foundMusician, "Nothing found");
    }

    @Test
    public void findAlbumByName()throws MalformedURLException {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);

        Album returnedAlbum = dao.findAlbumByName("The Köln Concert");

        assertEquals(album, returnedAlbum, "Nothing found");
    }

    //other teams code.
    @Test
    public void successfuldeleteMusician() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());

        assertEquals(1, dao.loadAll(Musician.class).size());

        dao.delete(musician);
        assertEquals(0,dao.loadAll(Musician.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfAlbum() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Album.class).size());

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        Album loadedAlbum = dao.load(Album.class, album.getId());
        assertNotNull(loadedAlbum.getId());
        assertEquals(album, loadedAlbum);
        assertEquals(1, dao.loadAll(Album.class).size());

//        dao.delete(album);
//        assertEquals(0, dao.loadAll(Album.class).size());
    }

    @Test
    public void successfulUpdateOfAlbum() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Album.class).size());

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);

        album.setReleaseYear(1974);

        dao.createOrUpdate(album);
        Album loadedAlbum = dao.load(Album.class, album.getId());
        assertNotNull(loadedAlbum.getId());
        assertEquals(loadedAlbum.getReleaseYear(), 1974);

        assertEquals(1, dao.loadAll(Album.class).size());

        dao.delete(album);
        assertEquals(0, dao.loadAll(Album.class).size());
    }

    @Test
    public void successfulDeleteAlbum() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Album.class).size());

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        Album loadedAlbum = dao.load(Album.class, album.getId());
        assertNotNull(loadedAlbum.getId());
        assertEquals(album, loadedAlbum);
        assertEquals(1, dao.loadAll(Album.class).size());

//        dao.delete(album);
//        assertEquals(0, dao.loadAll(Album.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfMusicalInstrument() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());

        MusicalInstrument musicalInstrument = new MusicalInstrument("piano");
        dao.createOrUpdate(musicalInstrument);
        MusicalInstrument loadedMusIn = dao.load(MusicalInstrument.class, musicalInstrument.getId());
        assertNotNull(loadedMusIn.getId());
        assertEquals(musicalInstrument, loadedMusIn);
        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());

//        dao.delete(musicalInstrument);
//        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfMusicianInstrument() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());

        MusicianInstrument musician = new MusicianInstrument();
        dao.createOrUpdate(musician);
        MusicianInstrument loadedMuscanIn = dao.load(MusicianInstrument.class, musician.getId());
        assertNotNull(loadedMuscanIn.getId());
        assertEquals(musician, loadedMuscanIn);
        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());

//        dao.delete(musician);
//        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());


    }

    @Test
    public void successfulCreationOfMusicianInstrumentAndMuscialInstrument() throws MalformedURLException {
        MusicalInstrument musicalInstrument = new MusicalInstrument("violin");


        MusicianInstrument musicianInstrument = new MusicianInstrument();
        musicianInstrument.setMusicalInstruments(Sets.newHashSet(musicalInstrument));

        dao.createOrUpdate(musicalInstrument);
        dao.createOrUpdate(musicianInstrument);

        Collection<MusicianInstrument> musicians = dao.loadAll(MusicianInstrument.class);
        assertEquals(1, musicians.size());
        MusicianInstrument loadedMusicianIn = musicians.iterator().next();
        assertEquals(musicianInstrument, loadedMusicianIn);

        assertEquals(musicianInstrument.getMusicalInstruments(), loadedMusicianIn.getMusicalInstruments());

//        dao.delete(musicalInstrument);
//        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
//        dao.delete(musicianInstrument);
//        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
    }
    @Test
    public void successfulCreationOfMusicianInstrumentAndMusician() throws MalformedURLException {


        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        MusicianInstrument musicianInstrument = new MusicianInstrument();
        musicianInstrument.setMusician(musician);
        dao.createOrUpdate(musician);
        dao.createOrUpdate(musicianInstrument);

        Collection<MusicianInstrument> musicians = dao.loadAll(MusicianInstrument.class);
        assertEquals(1, musicians.size());
        MusicianInstrument loadedMusicianIn = musicians.iterator().next();
        assertEquals(musicianInstrument, loadedMusicianIn);
        assertEquals(musicianInstrument.getMusician(), loadedMusicianIn.getMusician());

//        dao.delete(musician);
//        assertEquals(0, dao.loadAll(Musician.class).size());
//        dao.delete(musicianInstrument);
//        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
    }
    @Test
    public void successfulCreationOfMusicianInstrumentAndAlbum() throws MalformedURLException {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        MusicianInstrument musicianInstrument = new MusicianInstrument();
        album.setInstruments(Sets.newHashSet(musicianInstrument));
        dao.createOrUpdate(album);
        dao.createOrUpdate(musicianInstrument);
        Collection<Album> albums = dao.loadAll(Album.class);
        assertEquals(1, albums.size());
        Album loadedAlbum = albums.iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getInstruments(), loadedAlbum.getInstruments());

//        dao.delete(musicianInstrument);
//        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
//        dao.delete(album);
//        assertEquals(0, dao.loadAll(Album.class).size());
    }
    @Test
    public void successfulCreationOfAll() throws MalformedURLException
    {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        MusicianInstrument musicianInstrument = new MusicianInstrument();
        Musician musician = new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("violin");

        album.setInstruments(Sets.newHashSet(musicianInstrument));
        musicianInstrument.setMusician(musician);
        musician.setAlbums(Sets.newHashSet(album));
        musicianInstrument.setMusicalInstruments(Sets.newHashSet(musicalInstrument));
        dao.createOrUpdate(album);
        dao.createOrUpdate(musicalInstrument);
        dao.createOrUpdate(musician);
        dao.createOrUpdate(musicianInstrument);
        Collection<Album> albums = dao.loadAll(Album.class);
        Collection<MusicianInstrument> musicianIn= dao.loadAll(MusicianInstrument.class);
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, albums.size());
        assertEquals(1, musicianIn.size());
        assertEquals(1, musicians.size());
        MusicianInstrument loadedMusicanIn = musicianIn.iterator().next();
        Album loadedAlbum = albums.iterator().next();
        Musician loadedMusican = musicians.iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getInstruments(), loadedAlbum.getInstruments());
        assertEquals(musician,loadedMusican);
        assertEquals(musician.getAlbums(), loadedMusican.getAlbums());
        assertEquals(musicianInstrument,loadedMusicanIn);
        assertEquals(musicianInstrument.getMusician(),loadedMusicanIn.getMusician());

//        dao.delete(album);
//        assertEquals(0, dao.loadAll(Album.class).size());
//        dao.delete(musicalInstrument);
//        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
//        dao.delete(musician);
//        assertEquals(0, dao.loadAll(Musician.class).size());
//        dao.delete(musicianInstrument);
//        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());

    }
}