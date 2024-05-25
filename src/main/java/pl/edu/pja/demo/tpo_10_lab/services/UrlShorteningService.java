package pl.edu.pja.demo.tpo_10_lab.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrl;
import pl.edu.pja.demo.tpo_10_lab.repositories.ShortenedUrlRepository;

import java.util.Optional;

@Service
public class UrlShorteningService {

    private ShortenedUrlRepository repository;

    public UrlShorteningService(ShortenedUrlRepository shortenedUrlRepository) {
        this.repository = shortenedUrlRepository;
    }

    public ShortenedUrl createShortenedUrl(String name, String targetUrl, String password) {
        String shortUrl = RandomStringUtils.randomAlphanumeric(10);
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setShortUrl(shortUrl);
        shortenedUrl.setName(name);
        shortenedUrl.setTargetUrl(targetUrl);
        shortenedUrl.setPassword(password);
        shortenedUrl.setVisits(0);
        return repository.save(shortenedUrl);
    }

    public Optional<ShortenedUrl> getShortenedUrl(String shortUrl) {
        return Optional.ofNullable(repository.findByShortUrl(shortUrl));
    }

    public void incrementVisits(ShortenedUrl shortenedUrl) {
        shortenedUrl.setVisits(shortenedUrl.getVisits() + 1);
        repository.save(shortenedUrl);
    }

    public void updateShortenedUrl(String shortUrl, String name, String targetUrl, String password) throws Exception {
        ShortenedUrl shortenedUrl = repository.findByShortUrl(shortUrl);
        if (shortenedUrl == null) {
            throw new Exception("Shortened URL not found");
        }
        if (!shortenedUrl.getPassword().equals(password)) {
            throw new Exception("Wrong password");
        }
        shortenedUrl.setName(name);
        shortenedUrl.setTargetUrl(targetUrl);
        repository.save(shortenedUrl);
    }

    public void deleteShortenedUrl(String shortUrl, String password) throws Exception {
        ShortenedUrl shortenedUrl = repository.findByShortUrl(shortUrl);
        if (shortenedUrl == null) {
            throw new Exception("Shortened URL not found");
        }
        if (!shortenedUrl.getPassword().equals(password)) {
            throw new Exception("Wrong password");
        }
        repository.delete(shortenedUrl);
    }
}
