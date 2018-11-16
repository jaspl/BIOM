package com.example.jan.recognitionusingsensordata.Controllers;

import java.util.ArrayList;
import java.util.List;

public class User {
  String username;
  ArrayList<String>sequence;

    public User(String username, ArrayList<String> sequence) {
        this.username = username;
        this.sequence = sequence;
    }
}
