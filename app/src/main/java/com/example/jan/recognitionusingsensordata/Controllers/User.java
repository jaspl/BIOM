package com.example.jan.recognitionusingsensordata.Controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * Class defines user
 */
public class User {
  public String username;
  public String[]sequence;

    public User(String username, String[] sequence) {
        this.username = username;
        this.sequence = sequence;
    }
}
