package app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiController {

    @PostMapping("/api/add")
    public void add(@RequestParam("data") String data) {

    }

    @GetMapping
    public List<String> getAll() {
        return new ArrayList<>();
    }
}
