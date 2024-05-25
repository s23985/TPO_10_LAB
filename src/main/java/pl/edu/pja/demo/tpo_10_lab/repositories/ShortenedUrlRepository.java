package pl.edu.pja.demo.tpo_10_lab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrl;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Integer> {

    ShortenedUrl findByShortUrl(String shortUrl);
}
