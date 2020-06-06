package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: perform integration testing of both ECMMiner and the DAO classes together.
 */
class ECMMinerIntegrationTest {
    private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;
    private static ECMMiner ecmMiner;


    @BeforeAll
    public static void setUp() {
        Configuration configuration = new Configuration.Builder().build();
        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();

        dao = new Neo4jDAO(session);
        ecmMiner = new ECMMiner(dao);
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

        }
    }

    @Test
    public void mostProlificMusiciansNormalTest() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln Conce123t");
        Musician musician1 = new Musician("Keith Jarrett");
        musician1.setAlbums(Sets.newHashSet(album1,album2));

        Album album3 = new Album(1972, "ECM 1061/65", "The Concert");
        Album album4 = new Album(1973, "ECM 1062/62", "The Köln");
        Album album5 = new Album(1973, "ECM 1062/61", "The Kölnk");
        Musician musician2 = new Musician("Keith Jt");
        musician2.setAlbums(Sets.newHashSet(album3,album4,album5));


        Album album6 = new Album(1972, "ECM 1161/61", "The Ctoncert");
        Musician musician3 = new Musician("Keith Jts");
        musician3.setAlbums(Sets.newHashSet(album6));

        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician3);
        List<Musician> musicians = ecmMiner.mostProlificMusicians(2, 1950,1980);

        assertEquals(2, musicians.size());
        assertTrue(musicians.contains(musician1)&&musicians.contains(musician2));
    }

    @Test
    public void mostProlificMusiciansInvalidInput() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln Conce123t");
        Musician musician1 = new Musician("Keith Jarrett");
        musician1.setAlbums(Sets.newHashSet(album1,album2));

        Album album3 = new Album(1972, "ECM 1061/65", "The Concert");
        Album album4 = new Album(1973, "ECM 1062/62", "The Köln");
        Album album5 = new Album(1973, "ECM 1062/61", "The Kölnk");
        Musician musician2 = new Musician("Keith Jt");
        musician2.setAlbums(Sets.newHashSet(album3,album4,album5));


        Album album6 = new Album(1972, "ECM 1161/61", "The Ctoncert");
        Musician musician3 = new Musician("Keith Jts");
        musician3.setAlbums(Sets.newHashSet(album6));

        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician3);
        List<Musician> musicians = ecmMiner.mostProlificMusicians(-2, 1950,1980);

        assertEquals(0, musicians.size());
    }

    @Test
    public void mostProlificMusiciansLargeK() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln Conce123t");
        Musician musician1 = new Musician("Keith Jarrett");
        musician1.setAlbums(Sets.newHashSet(album1,album2));

        Album album3 = new Album(1972, "ECM 1061/65", "The Concert");
        Album album4 = new Album(1973, "ECM 1062/62", "The Köln");
        Album album5 = new Album(1973, "ECM 1062/61", "The Kölnk");
        Musician musician2 = new Musician("Keith Jt");
        musician2.setAlbums(Sets.newHashSet(album3,album4,album5));


        Album album6 = new Album(1972, "ECM 1161/61", "The Ctoncert");
        Musician musician3 = new Musician("Keith Jts");
        musician3.setAlbums(Sets.newHashSet(album6));

        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician3);
        List<Musician> musicians = ecmMiner.mostProlificMusicians(10, 1950,1980);

        assertEquals(3, musicians.size());
        assertTrue(musicians.contains(musician1)&&musicians.contains(musician2));
    }


    @Test
    public void mostTalentedMusiciansNormalTest() {

        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));



        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));


        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician1));
    }

    @Test
    public void mostTalentedMusiciansInvalidK() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(-10);

        assertEquals(0, musicians.size());
    }

    @Test
    public void mostTalentedMusiciansLargeK() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(10);

        assertEquals(2, musicians.size());
    }


    @Test
    public void mostSocialMusiciansNormalTest() {
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("KK Slider");
        Musician musician3 = new Musician("Alan Walker");

        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln");

        List<Musician> musicianList1 = new ArrayList<>();
        List<Musician> musicianList2 = new ArrayList<>();

        musicianList1.add(musician1);
        musicianList1.add(musician2);
        musicianList2.add(musician1);
        musicianList2.add(musician3);

        album1.setFeaturedMusicians(musicianList1);
        album2.setFeaturedMusicians(musicianList2);

        musician1.setAlbums(Sets.newHashSet(album1, album2));
        musician2.setAlbums(Sets.newHashSet(album1));
        musician3.setAlbums(Sets.newHashSet(album2));

        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician3);
        List<Musician> musicians = ecmMiner.mostSocialMusicians(1);
        assertEquals(1, musicians.size());
        assertEquals(musicians.get(0), musician1);
    }


    @Test
    public void mostSocialMusiciansInvalidK() {
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("KK Slider");
        Musician musician3 = new Musician("Alan Walker");

        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln");

        List<Musician> musicianList1 = new ArrayList<>();
        List<Musician> musicianList2 = new ArrayList<>();

        musicianList1.add(musician1);
        musicianList1.add(musician2);
        musicianList2.add(musician1);
        musicianList2.add(musician3);

        album1.setFeaturedMusicians(musicianList1);
        album2.setFeaturedMusicians(musicianList2);

        musician1.setAlbums(Sets.newHashSet(album1, album2));
        musician2.setAlbums(Sets.newHashSet(album1));
        musician3.setAlbums(Sets.newHashSet(album2));

        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician3);
        List<Musician> musicians = ecmMiner.mostSocialMusicians(-1);
        assertEquals(0, musicians.size());
    }

    @Test
    public void mostSocialMusiciansLargeK() {
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("KK Slider");
        Musician musician3 = new Musician("Alan Walker");

        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln");

        List<Musician> musicianList1 = new ArrayList<>();
        List<Musician> musicianList2 = new ArrayList<>();

        musicianList1.add(musician1);
        musicianList1.add(musician2);
        musicianList2.add(musician1);
        musicianList2.add(musician3);

        album1.setFeaturedMusicians(musicianList1);
        album2.setFeaturedMusicians(musicianList2);

        musician1.setAlbums(Sets.newHashSet(album1, album2));
        musician2.setAlbums(Sets.newHashSet(album1));
        musician3.setAlbums(Sets.newHashSet(album2));

        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician2);
        dao.createOrUpdate(musician3);
        List<Musician> musicians = ecmMiner.mostSocialMusicians(10);
        assertEquals(3, musicians.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("K in busiest year is normal")
    public void KInBusiestIsNormal(int arg){
        Album album = new Album(1975,"ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        List<Integer> busiestYear = ecmMiner.busiestYears(arg);
        assertEquals(arg,busiestYear.size());
    }

    @Test
    public void BusiestInvalidK(){
        Album album = new Album(1975,"ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        List<Integer> busiestYear = ecmMiner.busiestYears(-10);
        assertEquals(0,busiestYear.size());
    }

    @Test
    public void BusiestLargeK(){
        Album album = new Album(1975,"ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        List<Integer> busiestYear = ecmMiner.busiestYears(10);
        assertEquals(4,busiestYear.size());
    }



    @Test
    public void mostSimilarAlbumsNormalTest(){
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("Keith k");
        Musician musician3 = new Musician("Keith J");

        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        album1.setFeaturedMusicians(Lists.newArrayList(musician1,musician2));

        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        album2.setFeaturedMusicians(Lists.newArrayList(musician1,musician2,musician3));

        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        album3.setFeaturedMusicians(Lists.newArrayList(musician3,musician2));

        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);

        List<Album> busiestYear = ecmMiner.mostSimilarAlbums(1,album1);
        assertEquals(1,busiestYear.size());
        assertTrue(busiestYear.contains(album2));
    }

    @Test
    public void mostSimilarAlbumsLargeK(){
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("Keith k");
        Musician musician3 = new Musician("Keith J");

        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        album1.setFeaturedMusicians(Lists.newArrayList(musician1,musician2));

        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        album2.setFeaturedMusicians(Lists.newArrayList(musician1,musician2,musician3));

        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        album3.setFeaturedMusicians(Lists.newArrayList(musician3,musician2));

        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);

        List<Album> busiestYear = ecmMiner.mostSimilarAlbums(10,album1);
        assertEquals(2,busiestYear.size());
    }

    @Test
    public void mostSimilarAlbumsInvalidK(){
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("Keith k");
        Musician musician3 = new Musician("Keith J");

        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        album1.setFeaturedMusicians(Lists.newArrayList(musician1,musician2));

        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        album2.setFeaturedMusicians(Lists.newArrayList(musician1,musician2,musician3));

        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        album3.setFeaturedMusicians(Lists.newArrayList(musician3,musician2));

        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);

        List<Album> busiestYear = ecmMiner.mostSimilarAlbums(-10,album1);
        assertEquals(0,busiestYear.size());
    }

    @Test
    public void mostPopularInstrumentNormalTest() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);

        List<MusicalInstrument> mostPopularInstrument = ecmMiner.mostPopularInstrument(1);
        assertEquals(1, mostPopularInstrument.size());
        assertEquals(mostPopularInstrument.get(0), new MusicalInstrument("Piano"));
    }

    @Test
    public void mostPopularInstrumentLargeK() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);

        List<MusicalInstrument> mostPopularInstrument = ecmMiner.mostPopularInstrument(10);
        assertEquals(2, mostPopularInstrument.size());
    }

    @Test
    public void mostPopularInstrumentInvalidK() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);

        List<MusicalInstrument> mostPopularInstrument = ecmMiner.mostPopularInstrument(-1);
        assertEquals(0, mostPopularInstrument.size());
    }
}