package com.demo.dp;

import com.demo.dp.FileLoader;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("Start...");
        if (args.length != 4) {
            return;
        }
        String delayFile = args[0];
        String nodeFile = args[1];
        String packetFile = args[2];
        String answerFile = args[3];
        List<String[]> packages = FileLoader.parse(packetFile);
        List<String[]> nodes = FileLoader.parse(nodeFile);
        List<String[]> delays = FileLoader.parse(delayFile);
        List<String[]> results = calculate(packages, nodes, delays);
        if (results != null) {
            outputResult(results, answerFile);
        }
        System.out.println("End...");
        System.exit(0);
    }

    private static void outputResult(List<String[]> results, String answerFile) throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(answerFile),UTF_8))){
            bufferedWriter.write("#(package,starttime,node,...)");
            for (String[] segs : results) {
                String id = segs[0];
                String start = segs[1];
                String end = segs[2];
                String time = segs[3];
                String line = String.format(Locale.ROOT, "(%s,%s,%s,%s)", id, time, start, end);
                line = "\n"+line;
                bufferedWriter.write(line);
            }
        }

    }

    private static List<String[]> calculate(List<String[]> packages, List<String[]> nodes, List<String[]> delays) {
        RouteServer routeServer = new RouteServer();
        packages =  routeServer.calculate(packages, nodes, delays);
        return  packages;
    }

}
