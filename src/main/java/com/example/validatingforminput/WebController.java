package com.example.validatingforminput;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionAttributes({"people", "cpfExists", "positions", "positionAlreadyFilledUp", "saveButtonClicked"})
@Controller
public class WebController implements WebMvcConfigurer {

    private List<PersonForm> people;
    private List<Integer> positions;
    private Map<Integer, List<PersonForm>> peopleByPosition;
    private boolean cpfExists;
    private boolean positionAlreadyFilledUp;
    private boolean saveButtonClicked;
    private List<PersonForm> peopleRegistered = new ArrayList<>();
    private static final int CANDIDATE_LIMIT = 15; //TODO
    private static final int POSITION_LIMIT = 3; //TODO

    public WebController() {
        people = new ArrayList<>();
        peopleByPosition = new HashMap<>();
        positions = List.of(1, 2, 3, 4, 5);
        for (Integer position : positions) {
            peopleByPosition.put(position, new ArrayList<>());
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/styles/**")) {
            registry.addResourceHandler("/styles/**").addResourceLocations("classpath:/styles/");
        }
    }

    @GetMapping("/results")
    public String results() {
        if (people.isEmpty() && !saveButtonClicked) {
            saveButtonClicked = true;
            return "redirect:/";
        }

        return "results";
    }

    @GetMapping("/")
    public String showForm(PersonForm personForm, Model model) {
        model.addAttribute("people", people);
        model.addAttribute("cpfExists", cpfExists);
        model.addAttribute("positions", positions);
        model.addAttribute("positionAlreadyFilledUp", positionAlreadyFilledUp);
        model.addAttribute("saveButtonClicked", saveButtonClicked);

        return "form";
    }


    @PostMapping("/")
    public String postPerson(@Valid PersonForm personForm, BindingResult bindingResult) {
        System.out.println("personForm = " + personForm);

        if (bindingResult.hasErrors()) {
            return "form";
        }

        cpfExists = false;
        positionAlreadyFilledUp = false;

        if (people.size() < CANDIDATE_LIMIT) {
            for (PersonForm person : people) {
                if (person.getCpf().equals(personForm.getCpf())) {
                    cpfExists = true;
                    return "redirect:/";
                }
            }
            List<PersonForm> peopleForThePosition = peopleByPosition.get(personForm.getPosition());
            if (peopleForThePosition.size() < POSITION_LIMIT) {
                peopleForThePosition.add(personForm);
                people.add(personForm);
            } else {
                positionAlreadyFilledUp = true;
                return "redirect:/";
            }
        }

        return "redirect:/";
    }

    public boolean isMaxCandidatesNumberReached(){
        return people.size() == CANDIDATE_LIMIT;
    }
}
