package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@SpringBootApplication
public class CodeSharingPlatform {

    public static void main(String[] args) {
        SpringApplication.run(CodeSharingPlatform.class, args);
    }

}

@RestController
class RestManager {

    Code codeExample;
    CodeService cs;

    @Autowired
    public RestManager(CodeService cs) {
        this.cs = cs;
        this.codeExample = new Code(cs);
    }

    @GetMapping(path = "/api/code/latest")
    public ResponseEntity<String> getJsonLatest() {
        try {
            String result = codeExample.getJsonLatest();
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Code Not Found", exc);
        }
    }

    @GetMapping(path = "/api/code/{uuid}")
    public ResponseEntity<String> getJsonById(@PathVariable("uuid") final String uuid) {
        try {
            String result = codeExample.getJsonById(uuid);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Code Not Found", exc);
        }
    }

    @GetMapping(path = "/code/latest", produces = "text/html")
    public ResponseEntity<String> getCodeWebLatest() {
        try {
            String result = codeExample.getWebLatest()  ;
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Code Not Found", exc);
        }
    }

    @GetMapping(path = "/code/{uuid}", produces = "text/html")
    public ResponseEntity<String> getCodeWebId(@PathVariable("uuid") final String uuid) {
        try {
            String result = codeExample.getWebById(uuid);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Code Not Found", exc);
        }
    }

    @GetMapping(path = "/code/new", produces = "text/html")
    public ResponseEntity<String> getForm() {
        try {
            String result = codeExample.getForm();
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Code Not Found", exc);
        }
    }

    @PostMapping(value = "/api/code/new", consumes = "application/json")
    public ResponseEntity<String> saveCode(@RequestBody String body) {
        String savedCode = codeExample.setCode(body);
        return new ResponseEntity<>(savedCode, HttpStatus.OK);
    }

}

