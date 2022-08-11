package vttp2022.ssf.day16_giphy.services;

import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.day16_giphy.models.Gif;
import vttp2022.ssf.day16_giphy.repositories.GifRepository;

@Service
public class GifService {

    // *** DEFINE BASE URL FOR API ***
    private static final String URL = "https://api.giphy.com/v1/gifs/search";
    private static final String trendingURL = "https://api.giphy.com/v1/gifs/trending";

    // *** DECLARE API_KEY ***
    @Value("${API_KEY}")
    private String key;

    
    @Autowired
    private GifRepository gifRepo;

    public List<Gif> getGif(String q, String limit, String rating) {

        // *** REQUEST AND RESPONSE ***

        // ** UriComponentsBuilder
        // Create the url from query string
        String url = UriComponentsBuilder.fromUriString(URL)
                .queryParam("q", q)
                .queryParam("limit", limit)
                .queryParam("rating", rating)
                .queryParam("api_key", key)
                .toUriString();

        // ** RequestEntity
        // Create the GET request - GET <url>
        // Using RequestEntity.get(<url>).build();
        RequestEntity<Void> req = RequestEntity.get(url).build();

        // ** RestTemplate + ResponseEntity
        // Make the call to Giphy API - Receive the Response
        // init a RestTemplate to receive the data (like RedisTemplate)
        // Receive it as a ResponseEntity, using RestTemplate.exchange
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);

        // ** resp.getStatusCodeValue() + return Collections.emptyList();
        // Check status code
        // If unsuccessful,
        if (resp.getStatusCodeValue() != 200) {
            System.err.println("Error status code is not 200 ");
            return Collections.emptyList();
        }

        // ** Payload (resp.getBody();)
        // If successful,
        // Get the payload as String, by resp.getBody();
        String payload = resp.getBody();
        System.out.println("payload: " + payload);

        // *** EXTRACTING THE DATA AND RETURNING APPROP. TYPE FOR CONTROLLER/MODEL TO USE ***

        // ** Reader -> JsonReader -> readObject();
        // Convert payload to JsonObject
        // Unified language between apps is "stringified" json
        // new StringReader(<payload>)
        Reader strReader = new StringReader(payload);
        // JsonReader | Json.createReader(<Reader>)
        JsonReader jsonReader = Json.createReader(strReader);
        // fully converted into Json Object by readObject();
        JsonObject gifResult = jsonReader.readObject();

        // ** .getJsonArray / .getJsonObject
        // Getting the data
        // From after converting to Json Object
        // Pinpoint to Data array
        JsonArray gifData = gifResult.getJsonArray("data");

        // Pinpoint to each object in Data array,
        // For Each Object,
        // Pinpoint to its Images object -> Original object
        // create new Gif, which will extract url from original jObj, then add into
        // List<Gif>
        List<Gif> gifList = new LinkedList<>();
        for (int i = 0; i < gifData.size(); i++) {
            // data[i]
            JsonObject gifDataElements = gifData.getJsonObject(i);
            // data[i].images
            JsonObject gifImg = gifDataElements.getJsonObject("images");
            // data[i].images.original
            JsonObject gifImgOri = gifImg.getJsonObject("original");
            gifList.add(Gif.create(gifImgOri));
        }

        // List to be pumped into and used by Model
        return gifList;
    }

    public List<Gif> getTrending () {

        //Create the url with query string
        String url = UriComponentsBuilder.fromUriString(trendingURL)
        .queryParam("limit", 25)
        .queryParam("rating", "g")
        .queryParam("api_key", key)
        .toUriString();
        
        //Create the GET request - GET <url>
        RequestEntity<Void> req = RequestEntity.get(url).build();

        // Make the call to Giphy API
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp =  template.exchange(req, String.class);

        //Check status code
        if (resp.getStatusCodeValue() != 200) {
            System.err.println("Error status code is not 200 ");
            return Collections.emptyList();
        }

        // Get the payload and do something with it
        String payload = resp.getBody();
        System.out.println("payload: " + payload);

        //Convert payload to JsonObject
        // Convert the String to a Reader
        Reader strReader = new StringReader(payload); //unified language between apps is "stringified" json
        // Create a JsonReader from Reader
        JsonReader jsonReader = Json.createReader(strReader);
        // Read the payload as Json object
        JsonObject gifResult = jsonReader.readObject();
        JsonArray gifData = gifResult.getJsonArray("data");

        List<Gif> gifList = new LinkedList<>();
        for (int i = 0; i < gifData.size(); i++) {
            //data[i]
            JsonObject gifDataElements = gifData.getJsonObject(i);
            //data[i].images
            JsonObject gifImg = gifDataElements.getJsonObject("images");
            //data[i].images.original
            JsonObject gifImgOri = gifImg.getJsonObject("original");
            gifList.add(Gif.create(gifImgOri));
        }

        return gifList;
    }
    
}
