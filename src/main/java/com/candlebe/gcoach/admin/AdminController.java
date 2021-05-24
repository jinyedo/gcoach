package com.candlebe.gcoach.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/admin")
    public String getAdmin() {
        log.info("관리자 페이지 접속..........");
        return "admin_upload";
    }
}
