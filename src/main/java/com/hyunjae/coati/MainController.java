package com.hyunjae.coati;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private EpisodeRepository episodeRepository;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("seriesList", seriesRepository.findAll());
        return "index";
    }

    @RequestMapping("/episode/{seriesId}")
    public String episode(Model model, @PathVariable Integer seriesId) {
        model.addAttribute("episodeList", episodeRepository.findBySeriesId(seriesId));
        return "episode";
    }
}
