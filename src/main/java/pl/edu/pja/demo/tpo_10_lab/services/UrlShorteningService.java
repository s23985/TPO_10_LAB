package pl.edu.pja.demo.tpo_10_lab.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrl;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrlDto;
import pl.edu.pja.demo.tpo_10_lab.repositories.ShortenedUrlRepository;

import java.util.Optional;

@Service
public class UrlShorteningService {

    private final ShortenedUrlRepository repository;
    private final ShortenedUrlMapper shortenedUrlMapper;
    private final ObjectMapper objectMapper;

    public UrlShorteningService(ShortenedUrlRepository shortenedUrlRepository, ShortenedUrlMapper shortenedUrlMapper, ObjectMapper objectMapper) {
        this.repository = shortenedUrlRepository;
        this.shortenedUrlMapper = shortenedUrlMapper;
        this.objectMapper = objectMapper;
    }

    public ShortenedUrl createShortenedUrl(String name, String targetUrl, String password) {
        String shortUrl = RandomStringUtils.randomAlphanumeric(10);
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setId(shortUrl);
        shortenedUrl.setName(name);
        shortenedUrl.setTargetUrl(targetUrl);
        shortenedUrl.setPassword(password);
        shortenedUrl.setVisits(0);
        return repository.save(shortenedUrl);
    }

    public Optional<ShortenedUrlDto> getShortenedUrl(String shortUrl) {
        return repository.findById(shortUrl).map(shortenedUrlMapper::mapToDto);
    }

    public void incrementVisits(ShortenedUrl shortenedUrl) {
        shortenedUrl.setVisits(shortenedUrl.getVisits() + 1);
        repository.save(shortenedUrl);
    }

    public Optional<ShortenedUrl> updateShortenedUrl(String id, JsonMergePatch patch) throws Exception {
        ShortenedUrlDto shortenedUrlDto = repository
                .findById(id)
                .map(shortenedUrlMapper::mapToDto)
                .orElseThrow();
        ShortenedUrlDto patchedShortenedUrlDto = applyPatch(shortenedUrlDto, patch);
        
        ShortenedUrlDto changesInPatch = applyPatch(new ShortenedUrlDto(), patch);
        
        if (shortenedUrlDto.getPassword() == null 
                || shortenedUrlDto.getPassword().isEmpty() 
                || !shortenedUrlDto.getPassword().equals(changesInPatch.getPassword())) {
            throw new Exception("Wrong password");
        }

        if (!patchedShortenedUrlDto.getTargetUrl().equals(shortenedUrlDto.getTargetUrl())) {
            patchedShortenedUrlDto.setVisits(0);
        }
        
        return Optional.of(repository.save(shortenedUrlMapper.map(patchedShortenedUrlDto)));
    }

    public void deleteShortenedUrl(String shortUrl, String password) throws Exception {
        ShortenedUrl shortenedUrl = repository.findById(shortUrl).orElseThrow();

        if (shortenedUrl.getPassword() == null 
                || shortenedUrl.getPassword().isEmpty() 
                || !shortenedUrl.getPassword().equals(password)) {
            throw new Exception("Wrong password");
        }
        
        repository.delete(shortenedUrl);
    }

    private ShortenedUrlDto applyPatch(ShortenedUrlDto dto, JsonMergePatch patch) throws JsonProcessingException, JsonPatchException {
        JsonNode shortenedUrlNode = objectMapper.valueToTree(dto);
        JsonNode patchNode = patch.apply(shortenedUrlNode);
        return objectMapper.treeToValue(patchNode, ShortenedUrlDto.class);
    }
}
