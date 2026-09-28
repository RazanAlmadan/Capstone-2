package com.example.capston2.Controller;

import com.example.capston2.Model.Catalog;
import com.example.capston2.Model.Designer;
import com.example.capston2.Repository.CatalogRepository;
import com.example.capston2.Repository.DesignerRepository;
import com.example.capston2.Service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final DesignerRepository designerRepository;
    private final CatalogRepository catalogRepository;
    private final ClientService clientService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("designers", designerRepository.findAll());
        return "home";
    }

    @GetMapping("/designer/{id}")
    public String designerProfile(@PathVariable Integer id, Model model) {
        Designer designer = designerRepository.findById(id).orElse(null);
        if (designer == null) {
            return "redirect:/designers";
        }
        model.addAttribute("designer", designer);
        List<Catalog> catalogs = catalogRepository.findByDesignerId(id);
        model.addAttribute("catalogs", catalogs);
        return "designer-profile";
    }

    @GetMapping("/designers")
    public String designers(Model model) {
        model.addAttribute("designers", designerRepository.findAll());
        return "designers";
    }

    @GetMapping("/designers/search")
    public String searchDesigners(@RequestParam String keyword, Model model) {
        List<Designer> results = designerRepository.searchDesigners(keyword);
        model.addAttribute("designers", results);
        model.addAttribute("keyword", keyword);
        return "designers";
    }

    @GetMapping("/designers/category")
    public String searchByCategory(@RequestParam String category, Model model) {
        model.addAttribute("designers", clientService.searchByCategory(category));
        model.addAttribute("selectedCategory", category);
        return "designers";
    }

    @GetMapping("/designers/rating")
    public String getDesignersByRating(Model model) {
        model.addAttribute("designers", clientService.getDesignersOrderByRating());
        model.addAttribute("selectedCategory", null);
        return "designers";
    }

    @GetMapping("/designers/rating/category")
    public String getDesignersByRatingAndCategory(@RequestParam String category, Model model) {
        model.addAttribute("designers", clientService.getDesignersOrderByRatingAndCategory(category));
        model.addAttribute("selectedCategory", category);
        return "designers";
    }
}
