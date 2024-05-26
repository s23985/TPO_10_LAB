package pl.edu.pja.demo.tpo_10_lab.services;

import org.springframework.stereotype.Service;
import pl.edu.pja.demo.tpo_10_lab.model.GetShortenedUrlResponseDto;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrl;
import pl.edu.pja.demo.tpo_10_lab.model.ShortenedUrlDto;

@Service
public class ShortenedUrlMapper {
    public GetShortenedUrlResponseDto mapToGetShortenedUrlResponseDto(ShortenedUrl shortenedUrl) {
        GetShortenedUrlResponseDto responseDto = new GetShortenedUrlResponseDto();
        
        responseDto.setId(shortenedUrl.getId());
        responseDto.setName(shortenedUrl.getName());
        responseDto.setTargetUrl(shortenedUrl.getTargetUrl());
        responseDto.setRedirectUrl(shortenedUrl.getRedirectUrl());
        responseDto.setVisits(shortenedUrl.getVisits());
        
        return responseDto;
    }

    public GetShortenedUrlResponseDto mapToGetShortenedUrlResponseDto(ShortenedUrlDto dto) {
        return mapToGetShortenedUrlResponseDto(map(dto));
    }
    
    public ShortenedUrl map(ShortenedUrlDto shortenedUrlDto){
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        
        shortenedUrl.setId(shortenedUrlDto.getId());
        shortenedUrl.setName(shortenedUrlDto.getName());
        shortenedUrl.setTargetUrl(shortenedUrlDto.getTargetUrl());
        shortenedUrl.setVisits(shortenedUrlDto.getVisits());
        shortenedUrl.setPassword(shortenedUrlDto.getPassword());
        
        return shortenedUrl;
    }

    public ShortenedUrlDto mapToDto(ShortenedUrl url) {
        ShortenedUrlDto dto = new ShortenedUrlDto();

        dto.setId(url.getId());
        dto.setName(url.getName());
        dto.setTargetUrl(url.getTargetUrl());
        dto.setVisits(url.getVisits());
        dto.setPassword(url.getPassword());

        return dto;
    }
}
