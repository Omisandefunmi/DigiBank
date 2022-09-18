package com.semicolon.DigiBank.data.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private String id;
    private String bankName;
    private final Map<String, User> users = new HashMap<>();
}
