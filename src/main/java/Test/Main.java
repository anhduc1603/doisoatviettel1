package Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Đức", "Anh", "Hà");
        StringBuilder  text = new StringBuilder();
        System.out.println(String.join(", ",list));
    }

}
