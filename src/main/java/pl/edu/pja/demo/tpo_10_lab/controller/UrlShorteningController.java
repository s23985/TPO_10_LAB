package pl.edu.pja.demo.tpo_10_lab.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.pja.demo.tpo_10_lab.model.CreateShortenedUrlRequestDto;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrl;
import pl.edu.pja.demo.tpo_10_lab.services.ShortenedUrlMapper;
import pl.edu.pja.demo.tpo_10_lab.services.UrlShorteningService;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE})
public class UrlShorteningController {

    private final ShortenedUrlMapper shortenedUrlMapper;
    private final UrlShorteningService service;

    public UrlShorteningController(UrlShorteningService urlShorteningService, ShortenedUrlMapper shortenedUrlMapper) {
        this.service = urlShorteningService;
        this.shortenedUrlMapper = shortenedUrlMapper;
    }
    
    @PostMapping("/api/links")
    public ResponseEntity<?> createShortenedUrl(@RequestBody CreateShortenedUrlRequestDto requestDto) {
        ShortenedUrl shortenedUrl = service.createShortenedUrl(requestDto.getName(), requestDto.getTargetUrl(), requestDto.getPassword());

        URI shortenedUrlLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(shortenedUrl.getId())
                .toUri();

        return ResponseEntity.created(shortenedUrlLocation).build();
    }

    @GetMapping("/api/links/{shortUrl}")
    public ResponseEntity<?> getShortenedUrl(@PathVariable String shortUrl) {
        return service.getShortenedUrl(shortUrl)
                .map(shortenedUrl -> ResponseEntity.ok(shortenedUrlMapper.mapToGetShortenedUrlResponseDto(shortenedUrl)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/red/{shortUrl}")
    public ResponseEntity<?> redirect(@PathVariable String shortUrl) {
        Optional<ShortenedUrl> url = service.getShortenedUrl(shortUrl).map(shortenedUrlMapper::map);
        if (url.isPresent()) {
            service.incrementVisits(url.get());
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .header("Location", url.get().getTargetUrl())
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/api/links/{shortUrl}")
    public ResponseEntity<?> updateShortenedUrl(@PathVariable String shortUrl, @RequestBody JsonMergePatch patch) {
        try {
            service.updateShortenedUrl(shortUrl, patch);
            return ResponseEntity.noContent().build();
        }catch (NoSuchElementException ex){
            return ResponseEntity.notFound().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header("reason", e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/api/links/{shortUrl}")
    public ResponseEntity<?> deleteShortenedUrl(@PathVariable String shortUrl, @RequestHeader(required = false) String pass) {
        try {
            service.deleteShortenedUrl(shortUrl, pass);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header("reason", e.getMessage())
                    .build();
        }
    }
}
