package com.plawande.springsecurityjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunctionalResource {

    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    @GetMapping("/user/public")
    public String userPublic() {
        return ("<h1>Welcome User Public</h1>");
    }

    @GetMapping("/user/private")
    public String userPrivate() {
        return ("<h1>Welcome User Private</h1>");
    }

    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }
}
