package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: perform unit testing on the ECMMiner class, by making use of mocking.
 */
class ECMMinerUnitTest {
    private DAO dao;
    private ECMMiner ecmMiner;

    @BeforeEach
    public void setUp() {

        dao = mock(Neo4jDAO.class);
        ecmMiner = new ECMMiner(dao);
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, -1, 0})
    public void mostProlificMusiciansReturnEmptyWhenKInvalid(int k) {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

        List<Musician> musicians = ecmMiner.mostProlificMusicians(k, 1970,2000);
        assertEquals(0, musicians.size());
    }

    @Test
    public void mostProlificMusiciansReturnEmptyWhenInvalidYear() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

        List<Musician> musicians = ecmMiner.mostProlificMusicians(1, 1980,1950);
        assertEquals(0, musicians.size());
        musicians = ecmMiner.mostProlificMusicians(1, 1899,1980);
        assertEquals(0, musicians.size());
        musicians = ecmMiner.mostProlificMusicians(1, 1980,1899);
        assertEquals(0, musicians.size());
    }

    @Test
    public void mostProlificMusiciansReturnOnlyOneMusician() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln Conce123t");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album1,album2));
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));
        List<Musician> musicians = ecmMiner.mostProlificMusicians(3, 1950,1980);
        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }


//    @Test
//    public void shouldReturnTheMusicianWhenThereIsOnlyOne() {
//        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
//        Musician musician = new Musician("Keith Jarrett");
//        musician.setAlbums(Sets.newHashSet(album));
//        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));
//
//        List<Musician> musicians = ecmMiner.mostProlificMusicians(5, -1, -1);
//
//        assertEquals(1, musicians.size());
//        assertTrue(musicians.contains(musician));
//    }

    @Test
    public void shouldReturnOneMusicianWhenMusiciansHaveSameNumberOfAlbums() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln Conce123t");
        Musician musician1 = new Musician("Keith Jarrett");
        musician1.setAlbums(Sets.newHashSet(album1,album2));

        Album album3 = new Album(1972, "ECM 1061/65", "The Concert");
        Album album4 = new Album(1973, "ECM 1062/62", "The Köln");
        Musician musician2 = new Musician("Keith Jt");
        musician2.setAlbums(Sets.newHashSet(album3,album4));

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician1,musician2));
        List<Musician> musicians = ecmMiner.mostProlificMusicians(1, 1950,1980);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician1)||musicians.contains(musician2));
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

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician1,musician2,musician3));
        List<Musician> musicians = ecmMiner.mostProlificMusicians(2, 1950,1980);

        assertEquals(2, musicians.size());
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


        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1,musicianInstrument2));
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician1));
    }

    @Test
    public void mostTalentedMusiciansSameValue() {

        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Guitar")));



        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));


        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1,musicianInstrument2));
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician1)||musicians.contains(musician2));
    }

    @Test
    public void mostTalentedMusiciansLessThanK() {

        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Guitar")));



        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));


        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1,musicianInstrument2));
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(5);

        assertEquals(2, musicians.size());
        assertTrue(musicians.contains(musician1)||musicians.contains(musician2));
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, -1, 0})
    public void mostTalentedMusiciansInvalidK(int arg) {

        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));



        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));


        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1,musicianInstrument2));
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(arg);

        assertEquals(0, musicians.size());
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

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician1, musician2, musician3));
        List<Musician> musicians = ecmMiner.mostSocialMusicians(1);
        assertEquals(1, musicians.size());
        assertEquals(musicians.get(0), musician1);
    }

    @Test
    public void mostSocialMusiciansKMoreThanList() {
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

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician1, musician2, musician3));
        List<Musician> musicians = ecmMiner.mostSocialMusicians(10);
        assertEquals(3, musicians.size());
        assertEquals(musicians.get(0), musician1);
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, -1, 0})
    public void mostSocialMusiciansReturnEmptyWhenKInvalid(int k) {
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

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician1, musician2, musician3));

        List<Musician> musicians = ecmMiner.mostSocialMusicians(k);
        assertEquals(0, musicians.size());
    }

    @Test
    public void mostSocialMusiciansOnlyReturnOneWhenHaveTheSameNumber() {
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("KK Slider");
        //Musician musician3 = new Musician("Alan Walker");

        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1064/62", "The Köln");

        List<Musician> musicianList1 = new ArrayList<>();
        List<Musician> musicianList2 = new ArrayList<>();

        musicianList1.add(musician1);
        musicianList1.add(musician2);
        musicianList2.add(musician1);
        musicianList2.add(musician2);

        album1.setFeaturedMusicians(musicianList1);
        album2.setFeaturedMusicians(musicianList2);

        musician1.setAlbums(Sets.newHashSet(album1, album2));
        musician2.setAlbums(Sets.newHashSet(album1, album2));

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician1, musician2));
        List<Musician> musicians = ecmMiner.mostSocialMusicians(1);
        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician1)||musicians.contains(musician2));
    }




//=======
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("K in busiest year is <= 0")
    public void KInBusinessIsNegtive(int arg){
        Album album = new Album(1975,"ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album,album1,album2,album3));
        List<Integer> busiestYear = ecmMiner.busiestYears(arg);
        assertEquals(0,busiestYear.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("K in busiest year is normal")
    public void KInBusinessIsNormal(int arg){
        Album album = new Album(1975,"ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album,album1,album2,album3));
        List<Integer> busiestYear = ecmMiner.busiestYears(arg);
        assertEquals(arg,busiestYear.size());
    }

    @Test
    @DisplayName("K in busiest year is exceed the released years")
    public void KInBusinessIsExceed(){
        Album album = new Album(1975,"ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album,album1,album2,album3));
        List<Integer> busiestYear = ecmMiner.busiestYears(5);
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

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album2,album3));
        List<Album> busiestYear = ecmMiner.mostSimilarAlbums(1,album1);
        assertEquals(1,busiestYear.size());
        assertTrue(busiestYear.contains(album2));
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, -1, 0})
    public void mostSimilarAlbumsInvalidK(int arg){
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("Keith k");
        Musician musician3 = new Musician("Keith J");

        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        album1.setFeaturedMusicians(Lists.newArrayList(musician1,musician2));

        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        album2.setFeaturedMusicians(Lists.newArrayList(musician1,musician2,musician3));

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album2));
        List<Album> busiestYear = ecmMiner.mostSimilarAlbums(arg,album1);
        assertEquals(0,busiestYear.size());
    }

    @Test
    public void mostSimilarAlbumsSameValue(){
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("Keith k");
        Musician musician3 = new Musician("Keith J");

        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        album1.setFeaturedMusicians(Lists.newArrayList(musician1,musician2));

        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        album2.setFeaturedMusicians(Lists.newArrayList(musician1,musician3));

        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        album3.setFeaturedMusicians(Lists.newArrayList(musician3,musician2));

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album2,album3));
        List<Album> busiestYear = ecmMiner.mostSimilarAlbums(1,album1);
        assertEquals(1,busiestYear.size());
        assertTrue(busiestYear.contains(album2)||busiestYear.contains(album3));
    }

    @Test
    public void mostSimilarAlbumsLessThanK(){
        Musician musician1 = new Musician("Keith Jarrett");
        Musician musician2 = new Musician("Keith k");
        Musician musician3 = new Musician("Keith J");

        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        album1.setFeaturedMusicians(Lists.newArrayList(musician1,musician2));

        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        album2.setFeaturedMusicians(Lists.newArrayList(musician1,musician3));

        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");
        album3.setFeaturedMusicians(Lists.newArrayList(musician3,musician2));

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album2,album3));
        List<Album> busiestYear = ecmMiner.mostSimilarAlbums(5,album1);
        assertEquals(2,busiestYear.size());
        assertTrue(busiestYear.contains(album2)&&busiestYear.contains(album3));
    }

    @Test
    @DisplayName("the multiple busiest year test")
    public void multipleBusiestYearTest(){
        Album album = new Album(1980,"ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1980,"ECM 1029/66", "Jay Zhou");
        Album album2 = new Album(1985,"ECM 1033/67", "May Day");
        Album album3 = new Album(1990,"ECM 1068/68", "I love JJ");

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album,album1,album2,album3));
        List<Integer> albums = ecmMiner.busiestYears(4);
        assertEquals(3, albums.size());
    }


    @Test
    public void mostPopularInstrumentNormalTest() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1, musicianInstrument2));
        List<MusicalInstrument> mostPopularInstrument = ecmMiner.mostPopularInstrument(1);
        assertEquals(1, mostPopularInstrument.size());
        assertEquals(mostPopularInstrument.get(0), new MusicalInstrument("Piano"));
    }

    @Test
    public void mostPopularInstrumentKMoreThanList() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1, musicianInstrument2));
        List<MusicalInstrument> mostPopularInstrument = ecmMiner.mostPopularInstrument(5);
        assertEquals(2, mostPopularInstrument.size());
    }

    @Test
    public void mostPopularInstrumentSameValue() {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1, musicianInstrument2));
        List<MusicalInstrument> mostPopularInstrument = ecmMiner.mostPopularInstrument(1);
        assertEquals(1, mostPopularInstrument.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, -1, 0})
    public void mostPopularInstrumentReturnEmptyWhenKInvalid(int k) {
        Musician musician1 = new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,Sets.newHashSet(
                new MusicalInstrument("Piano"),new MusicalInstrument("Guitar")));

        Musician musician2 = new Musician("Keith Jt");
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,Sets.newHashSet(
                new MusicalInstrument("Piano")));

        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument1, musicianInstrument2));
        List<MusicalInstrument> mostPopularInstrument = ecmMiner.mostPopularInstrument(k);
        assertEquals(0, mostPopularInstrument.size());
    }
}