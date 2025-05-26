package org.project.main;

import org.project.server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter Port No: ");
        int port = scan.nextInt();
        scan.close();
        Server server = new Server(port);
        server.start();
    }
}
