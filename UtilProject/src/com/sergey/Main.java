package com.sergey;

/**
 * Created by alex on 18.07.2014.
 */
public class Main {
    public static void main(String... args){
        new Thread(null, new Runnable() {
            public void run() {
                new MainForm();
            }
        }, "1", 1 << 30).start();

    }
}
