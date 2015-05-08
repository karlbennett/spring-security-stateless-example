package scratch.cucumber.example.service;

import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;

@Component
public class Base64HashFactory implements HashFactory {

    @Override
    public String encode(String password) {
        return DatatypeConverter.printBase64Binary(password.getBytes());
    }
}
