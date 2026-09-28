package com.example.capston2.Controller;

import com.example.capston2.Model.Client;
import com.example.capston2.Model.Designer;
import com.example.capston2.Repository.ClientRepository;
import com.example.capston2.Repository.DesignerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final ClientRepository clientRepository;
    private final DesignerRepository designerRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Client client = clientRepository.findClientByEmail(email);
        if (client != null && password.equals(client.getPassword())) {
            session.setAttribute("role", "CLIENT");
            session.setAttribute("userId", client.getId());
            session.setAttribute("userName", client.getName());
            return "redirect:/dashboard";
        }

        Designer designer = designerRepository.findDesignerByEmail(email);
        if (designer != null && password.equals(designer.getPassword())) {
            session.setAttribute("role", "DESIGNER");
            session.setAttribute("userId", designer.getId());
            session.setAttribute("userName", designer.getName());
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Email or password is incorrect.");
        model.addAttribute("email", email);
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup/client")
    public String signupClient(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        if (clientRepository.findClientByEmail(email) != null || designerRepository.findDesignerByEmail(email) != null) {
            model.addAttribute("error", "That email is already registered.");
            return "signup";
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            model.addAttribute("error", "Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
            return "signup";
        }

        Client client = new Client(null, name, email, password);
        clientRepository.save(client);
        session.setAttribute("role", "CLIENT");
        session.setAttribute("userId", client.getId());
        session.setAttribute("userName", client.getName());
        return "redirect:/dashboard";
    }

    @PostMapping("/signup/designer")
    public String signupDesigner(@RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String visualField,
                                 @RequestParam(required = false, defaultValue = "") String bio,
                                 @RequestParam(required = false, defaultValue = "") String profileImageUrl,
                                 HttpSession session,
                                 Model model) {
        if (clientRepository.findClientByEmail(email) != null || designerRepository.findDesignerByEmail(email) != null) {
            model.addAttribute("error", "That email is already registered.");
            return "signup";
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            model.addAttribute("error", "Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
            return "signup";
        }

        Designer designer = new Designer();
        designer.setName(name);
        designer.setEmail(email);
        designer.setPassword(password);
        designer.setVisualField(visualField);
        designer.setBio(bio);
        designer.setProfileImageUrl(profileImageUrl);
        designer.setAverageRating(0.0);
        designer.setRatingCount(0);
        designerRepository.save(designer);

        session.setAttribute("role", "DESIGNER");
        session.setAttribute("userId", designer.getId());
        session.setAttribute("userName", designer.getName());
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
