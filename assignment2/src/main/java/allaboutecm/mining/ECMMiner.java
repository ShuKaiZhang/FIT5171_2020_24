package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * TODO: implement and test the methods in this class.
 * Note that you can extend the Neo4jDAO class to make implementing this class easier.
 */
public class ECMMiner {
    private static Logger logger = LoggerFactory.getLogger(ECMMiner.class);

    private final DAO dao;

    public ECMMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * Returns the most prolific musician in terms of number of albums released.
     *
     * @Param k the number of musicians to be returned.
     * @Param startYear, endYear between the two years [startYear, endYear].
     * When startYear/endYear is negative, that means startYear/endYear is ignored.
     */
    public List<Musician> mostProlificMusicians(int k, int startYear, int endYear) {
        if(startYear < 1900 || endYear <1900 || startYear > Calendar.getInstance().get(Calendar.YEAR) || endYear > Calendar.getInstance().get(Calendar.YEAR)||k <= 0) {
            return Lists.newArrayList();
        }
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        Map<String, Musician> nameMap = Maps.newHashMap();
        for (Musician m : musicians) {
            nameMap.put(m.getName(), m);
        }

        ListMultimap<String, Album> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap = MultimapBuilder.treeKeys().arrayListValues().build();

        for (Musician musician : musicians) {
            Set<Album> albums = musician.getAlbums();
            for (Album album : albums) {
                boolean toInclude =
                        !((startYear > 0 && album.getReleaseYear() < startYear) ||
                                (endYear > 0 && album.getReleaseYear() > endYear));

                if (toInclude) {
                    multimap.put(musician.getName(), album);
                }
            }
        }

        Map<String, Collection<Album>> albumMultimap = multimap.asMap();
        for (String name : albumMultimap.keySet()) {
            Collection<Album> albums = albumMultimap.get(name);
            int size = albums.size();
            countMap.put(size, nameMap.get(name));
        }

        List<Musician> result = Lists.newArrayList();
        List<Integer> sortedKeys = Lists.newArrayList(countMap.keySet());
        sortedKeys.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys) {
            List<Musician> list = countMap.get(count);
//            if (list.size() >= k) {
//                break;
//            }
//            ??????????????
            if (result.size() + list.size() >= k) {
                int newAddition = k - result.size();
                for (int i = 0; i < newAddition; i++) {
                    result.add(list.get(i));
                }
            } else {
                result.addAll(list);
            }
        }

        return result;
    }

    /**
     * Most talented musicians by the number of different musical instruments they play
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostTalentedMusicians(int k) {
        if(k <= 0) {
            return Lists.newArrayList();
        }
        int count = k;
        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        List<MusicianInstrument> list = new ArrayList<>(musicianInstruments);
        Collections.sort(list, new Comparator<MusicianInstrument>() {
            @Override
            public int compare(MusicianInstrument o1, MusicianInstrument o2) {
                return o2.getMusicalInstruments().size() - o1.getMusicalInstruments().size();
            }
        });
        List<Musician> result = Lists.newArrayList();
        if (count>list.size()){
            count=list.size();
        }
        List<MusicianInstrument> selected = list.subList(0, count);

        for (MusicianInstrument m:selected){
            result.add(m.getMusician());
        }
        return result;
    }

    /**
     * Musicians that collaborate the most widely, by the number of other musicians they work with on albums.
     *
     * @Param k the number of musicians to be returned.
     */

    public List<Musician> mostSocialMusicians(int k) {
        if(k <= 0) {
            return Lists.newArrayList();
        }
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        ListMultimap<Integer, Musician> musicianList = MultimapBuilder.treeKeys().arrayListValues().build();
        //Collections.sort(list, (o1, o2) -> o2.getFeaturedMusicians().size() - o1.getFeaturedMusicians().size());

        for(Musician m : musicians) {
            Set<Album> albums = m.getAlbums();
            HashSet<Musician> musicianHashSet = new HashSet<>();
            for(Album a : albums) {
                List<Musician> AlbumOfMusician = a.getFeaturedMusicians();
                musicianHashSet.addAll(Sets.newHashSet(AlbumOfMusician));
            }
            musicianList.put(musicianHashSet.size() - 1, m);
        }
        List<Musician> result = Lists.newArrayList();
        List<Integer> sorting = Lists.newArrayList(musicianList.keySet());
        sorting.sort(Ordering.natural().reverse());
        for(Integer count : sorting) {
            List<Musician> list = musicianList.get(count);
            if(result.size() + list.size() >= k) {
                int newCount = k - result.size();
                for(int i = 0; i < newCount; i++) {
                    result.add(list.get(i));
                }
            } else {
                result.addAll(list);
            }
        }
        if (k>result.size()){
            k=result.size();
        }
        return result;
    }

    /**
     * Busiest year in terms of number of albums released.
     *
     * @Param k the number of years to be returned.
     */

    public List<Integer> busiestYears(int k) {
        if (k > 0){
            Collection<Album> albums = dao.loadAll(Album.class);
            ListMultimap<Integer, Album> albumMap = MultimapBuilder.treeKeys().arrayListValues().build();
            ListMultimap<Integer, Integer> countMap = MultimapBuilder.treeKeys().arrayListValues().build();
            for (Album a: albums){
                int year = a.getReleaseYear();
                albumMap.put(year,a);
            }

            for (Integer i : albumMap.keySet()) {
                int totalAlbum = albumMap.get(i).size();
                countMap.put(totalAlbum,i);
            }

            List<Integer> result = Lists.newArrayList();
            List<Integer> sortedKeys = Lists.newArrayList(countMap.keySet());
            sortedKeys.sort(Ordering.natural().reverse());
            for (Integer count: sortedKeys) {
                List<Integer> list = countMap.get(count);
                if (result.size() + list.size() >= k) {
                    int remain = k - result.size();
                    for (int i = 0; i < remain; i++) {
                        result.add(list.get(i));
                    }
                }else {
                    result.addAll(list);
                }
            }

            return result;

        } else {
            List<Integer> result = new ArrayList<>();
            return result;
        }
    }

    /**
     * Most similar albums to a give album. The similarity can be defined in a variety of ways.
     * For example, it can be defined over the musicians in albums, the similarity between names
     * of the albums & tracks, etc.
     *
     * @Param k the number of albums to be returned.
     * @Param album
     */

    public List<Album> mostSimilarAlbums(int k, Album album) {
        if(k <= 0) {
            return Lists.newArrayList();
        }
        int count = k;
        Collection<Album> albums = dao.loadAll(Album.class);
        List<Album> list = new ArrayList<>(albums);
        List<Musician> musician = album.getFeaturedMusicians();
        Map<Album, Double> map = Maps.newHashMap();
        for (Album a : list) {
            double same = 0;
            for (Musician m:musician){
                if (a.getFeaturedMusicians().contains(m)) {
                    same++;
                }
            }
            map.put(a,same/musician.size());
        }


        return Lists.newArrayList();
    }

}
