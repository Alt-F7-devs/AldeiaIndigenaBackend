package com.altf7.sei;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class GerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("J(@VS$e3Sf%<KwbxAE]t"));
    }
}
