package mk.finki.ukim.wp.lab.service.impl;

import mk.finki.ukim.wp.lab.model.Artist;
import mk.finki.ukim.wp.lab.model.Song;
import mk.finki.ukim.wp.lab.repository.jpa.AlbumRepository;
import mk.finki.ukim.wp.lab.repository.jpa.SongRepository;
import mk.finki.ukim.wp.lab.service.SongService;
import mk.finki.ukim.wp.lab.service.specifications.FieldFilterSpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static mk.finki.ukim.wp.lab.service.specifications.FieldFilterSpecification.filterEquals;

@Service
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> listSongs() {
        return songRepository.findAll();
    }

    @Override
    public Song findById(Long id){
        return songRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Song song) {
        songRepository.save(song);
    }

    @Override
    public void delete(Long id) {
        songRepository.deleteById(id);
    }

    @Override
    public List<Song> findAllByAlbumAndYear(Long albumId, int releaseYear) {
        Specification<Song> specification = Specification
                .where(filterEquals(Song.class, "album.id",albumId))
                .and(FieldFilterSpecification.greaterThan(Song.class,"releaseYear", (long) releaseYear));
        return this.songRepository.findAll(specification);
    }

    @Transactional
    @Override
    public Artist addArtistToSong(Artist artist, Song song) {
        song.getPerformers().add(artist);
        songRepository.save(song);
        return artist;
    }


}
