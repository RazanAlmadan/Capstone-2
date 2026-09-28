package com.example.capston2.Controller;

import com.example.capston2.Repository.CatalogRepository;
import com.example.capston2.Repository.DesignerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final CatalogRepository catalogRepository;
    private final DesignerRepository designerRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/catalogs")
    public String catalogs(Model model) {
        model.addAttribute("catalogs", catalogRepository.findAll());
        model.addAttribute("designers", designerRepository.findAll());
        return "catalogs";
    }

    @GetMapping("/ai")
    public String aiPage() {
        return "ai";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
