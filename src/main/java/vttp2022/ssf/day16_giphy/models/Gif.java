package vttp2022.ssf.day16_giphy.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Gif {
    
    private String gifOriImg;


    public String getGifOriImg() {
        return this.gifOriImg;
    }

    public void setGifOriImg(String gifOriImg) {
        this.gifOriImg = gifOriImg;
    }

    public static Gif create(JsonObject gifImgOri) {
        Gif g = new Gif();
        g.setGifOriImg(gifImgOri.getString("url"));
        return g;
    }

    
}
