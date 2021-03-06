package com.bookxchange.service;


import com.bookxchange.model.AuthorEntity;
import com.bookxchange.model.BookEntity;
import com.bookxchange.pojo.Isbn;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IsbnService {
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Value("${isbn.url}")
    private String targetURL;

    private String getInfoFromApi(String isbn) {

        String result = "";
        HttpGet request = new HttpGet(targetURL + isbn);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            } else {
            result = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public BookEntity hitIsbnBookRequest(String isbn) {
        String infoFromApi = getInfoFromApi(isbn);
        Gson gson = new Gson();
        Isbn isbnDTO = gson.fromJson(infoFromApi, Isbn.class);
        BookEntity bookToReturn = new BookEntity();
        if (isbnDTO.getTotalItems() > 0) {
            bookToReturn.setIsbn(isbn);
            bookToReturn.setTitle(isbnDTO.getItems().get(0).getVolumeInfo().getTitle());

            List<String> author = isbnDTO.getItems().get(0).getVolumeInfo().getAuthors();
            ArrayList<AuthorEntity> authorsToAdd = new ArrayList<>();

            for (int i = 0; i < author.size(); i++) {
                Pattern patternAuthorName = Pattern.compile("^(.*)\\s(.{1,})$");
                Matcher matcher = patternAuthorName.matcher(author.get(i));

                if (matcher.find()) {
                    AuthorEntity selectedAuthor=new AuthorEntity(matcher.group(2), matcher.group(1));
                    System.out.println(selectedAuthor);
                    authorsToAdd.add(selectedAuthor);
                }
            }

            bookToReturn.setAuthors(authorsToAdd);
            if(isbnDTO.getItems().get(0).getVolumeInfo().getDescription() != null) {
                bookToReturn.setDescription(isbnDTO.getItems().get(0).getVolumeInfo().getDescription());
            }
            bookToReturn.setQuantity(0);

        } else {
            bookToReturn = null;
        }
        return bookToReturn;
    }

}
