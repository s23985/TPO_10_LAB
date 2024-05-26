package pl.edu.pja.demo.tpo_10_lab.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrl;

public interface ShortenedUrlRepository extends CrudRepository<ShortenedUrl, String> {
}
