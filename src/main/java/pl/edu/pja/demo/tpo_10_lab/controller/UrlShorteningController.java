package pl.edu.pja.demo.tpo_10_lab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.pja.demo.tpo_10_lab.model.CreateShortenedUrlRequestDto;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrl;
import pl.edu.pja.demo.tpo_10_lab.model.UpdateShortenedUrlRequestDto;
import pl.edu.pja.demo.tpo_10_lab.services.UrlShorteningService;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE})
public class UrlShorteningController {

    private UrlShorteningService service;

    public UrlShorteningController(UrlShorteningService urlShorteningService) {
        this.service = urlShorteningService;
    }

    @PostMapping("/api/links")
    public ResponseEntity<?> createShortenedUrl(@RequestBody CreateShortenedUrlRequestDto requestDto) {
        ShortenedUrl shortenedUrl = service.createShortenedUrl(requestDto.getName(), requestDto.getTargetUrl(), requestDto.getPassword());

        URI shortenedUrlLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(shortenedUrl.getShortUrl())
                .toUri();

        return ResponseEntity.created(shortenedUrlLocation).build();
    }

    @GetMapping("/api/links/{shortUrl}")
    public ResponseEntity<?> getShortenedUrl(@PathVariable String shortUrl) {
        return service.getShortenedUrl(shortUrl)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/red/{shortUrl}")
    public ResponseEntity<?> redirect(@PathVariable String shortUrl) {
        Optional<ShortenedUrl> url = service.getShortenedUrl(shortUrl);
        if (url.isPresent()) {
            service.incrementVisits(url.get());
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", url.get().getTargetUrl()).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/api/links/{shortUrl}")
    public ResponseEntity<?> updateShortenedUrl(@PathVariable String shortUrl, @RequestBody UpdateShortenedUrlRequestDto request) {
        try {
            service.updateShortenedUrl(shortUrl, request.getName(), request.getTargetUrl(), request.getPassword());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/api/links/{shortUrl}")
    public ResponseEntity<?> deleteShortenedUrl(@PathVariable String shortUrl, @RequestParam String password) {
        try {
            service.deleteShortenedUrl(shortUrl, password);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
