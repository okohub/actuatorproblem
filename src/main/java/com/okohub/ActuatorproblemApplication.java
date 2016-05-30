package com.okohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ActuatorproblemApplication {

   public static void main(String[] args) {
      SpringApplication.run(ActuatorproblemApplication.class, args);
   }

   @RestController
   public static class HelloApi {

      @GetMapping("api/hello")
      public String hello() {
         return "hello";
      }

      @Secured(value = "ROLE_ADMIN")
      @GetMapping("api/secure")
      public String secure() {
         return "secure";
      }
   }

}
