package pl.edu.pja.demo.tpo_10_lab.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ShortenedUrl {

    @Id
    private String id;
    private String name;
    private String targetUrl;
    private String redirectUrl;
    private String password;
    private int visits;

    public String getId() {
        return id;
    }

    public void setId(String shortUrl) {
        this.id = shortUrl;
        setRedirectUrl("http://localhost:8080/red/" + shortUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    private void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
