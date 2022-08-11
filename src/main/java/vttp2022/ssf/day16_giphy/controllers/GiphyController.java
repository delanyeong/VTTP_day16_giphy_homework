package vttp2022.ssf.day16_giphy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssf.day16_giphy.models.Gif;
import vttp2022.ssf.day16_giphy.services.GifService;

@Controller
@RequestMapping (path={"/","","/index.html"})
public class GiphyController {

    @Autowired
    private GifService gifSvc;
    
    @GetMapping (path={"/search"})
    public String getGif 
    (@RequestParam String q,
    @RequestParam String limit,
    @RequestParam String rating, 
    Model model) {
        
        List<Gif> g = gifSvc.getGif(q, limit, rating);

        model.addAttribute("g",g);
        model.addAttribute("title", "Results Page");
        return "gifresult";
    }

    @GetMapping (path={"/trending"})
    public String getTrending 
    (Model model) {
        List<Gif> trending = gifSvc.getTrending();
        model.addAttribute("g",trending);
        model.addAttribute("title", "Trending Page");
        return "gifresult";
    }

}
