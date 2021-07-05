package com.example.webscraper.scraper;

import com.example.webscraper.scraper.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperMain {

    @PostConstruct
    @Scheduled(cron = "1 * * * * *")
    public static void main() {

        List<String> finalList=new ArrayList<>();
        String url="https://www.imdb.com/movies-coming-soon/";

                //connections
        try {
            Document doc = Jsoup.connect(url).get();

            Element movies= doc.getElementsByClass("list detail").first();
            //Element tbody= doc.getElementsByTag("tbody").first();



            List<Element> mElements=  movies.getElementsByClass("overview-top");
            List<Movie> mymovies=new ArrayList<>();

            for(Element el: mElements){
                if(!el.text().equals("Watch Trailer") ) {

                    String title=el.getElementsByTag("h4").text();;
                    String type=el.getElementsByClass("cert-runtime-genre").text();
                    String rating=el.getElementsByTag("img").attr("title");
                    String cast=el.getElementsByClass("txt-block").text();
                    String outline= el.getElementsByClass("outline").text();

                        Movie item = new Movie(title, type, rating, cast, outline);
                        mymovies.add(item);
                }
            }

            for(Movie m: mymovies){
               // System.out.println("Title: " + m.getTitle()+ " "+ "Rating: "+ m.getRating());
                 String json = convertToJSON(m);
                sendData(json);
                 finalList.add(json);
            }

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static String convertToJSON(Movie mv) {
        ObjectMapper mapper=new ObjectMapper();
        String json="";

        try{
            json=mapper.writeValueAsString(mv);
            System.out.println(json);
        }catch(Exception e){ System.out.println(e);}
        return json;
    }

    public static void sendData(String json){

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/add");

            StringEntity entity=new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json" );

            CloseableHttpResponse response = client.execute(httpPost);
            client.close();

        }catch(Exception e){

            System.out.println(e);
        }
    }
}
