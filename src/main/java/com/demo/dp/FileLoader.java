package com.demo.dp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileLoader {
    static List<String[]> parse(final String file) throws Exception {
        BufferedReader br = null;
        try {
            List<String[]> list = new ArrayList<>();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                Optional<String> online = parseComment(line);
                if (!online.isPresent() || !checkFormat(online.get())) {
                    System.out.println("origin input line:%s is not correct format:"+line);
                    continue;
                }
                line = format(line);
                String[] segs = line.split(",");
                list.add(segs);

            }
            return list;

        }finally {
            if (br != null) {
                br.close();
            }

        }
    }

    private static boolean checkFormat(final String line) {
        return line !=null && ! line.isEmpty();
    }

    private static Optional<String> parseComment(String line) {
        if (Objects.isNull(line) || line.contains("#")) {
            return Optional.of(line);
        }
        int index = line.indexOf("#");
        if (index == 0) {
            return Optional.empty();
        }
        String result = line.substring(0, index);
        return Optional.of(result);
    }

    private static String format(String line) {
        String temp = line;
        temp = temp.replaceAll("\\s", "");
        temp = temp.replaceAll("\\(", "");
        temp = temp.replaceAll("\\)", "");
        return temp;

    }


}
