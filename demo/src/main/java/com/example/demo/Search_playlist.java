package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

/**
 * Prints a list of videos based on a search term.
 *
 * @author Jeremy Walker
 * 
 * youtube api 소스 : https://developers.google.com/youtube/v3/docs/search/list?hl=ko#--
 * 키 생성 참고 : http://19park.tistory.com/104
 *           http://guitarhero03.tistory.com/entry/Youtube-v3-%EC%97%B0%EB%8F%99%ED%95%B4%EC%84%9C-%EC%9E%90%EA%B8%B0-%EB%8F%99%EC%98%81%EC%83%81-%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EB%B6%88%EB%9F%AC%EC%98%A4%EA%B8%B0-001
 * 
 * youtube API 관리 : https://console.developers.google.com/apis/
 * 
 */
public class Search_playlist {

  /** Global instance properties filename. */
  private static String PROPERTIES_FILENAME = "youtube.properties";

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
  private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

  /** Global instance of Youtube object to make all API requests. */
  private static YouTube youtube;


  /**
   * Initializes YouTube object to search for videos on YouTube (Youtube.Search.List). The program
   * then prints the names and thumbnails of each of the videos (only first 50 videos).
   *
   * @param args command line args.
   */
  public static void main(String[] args) {
    // Read the developer key from youtube.properties
    Properties properties = new Properties();
    try {
      InputStream in = Search_playlist.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
      properties.load(in);

    } catch (IOException e) {
      System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
          + " : " + e.getMessage());
      System.exit(1);
    }

    try {
      /*
       * The YouTube object is used to make all API requests. The last argument is required, but
       * because we don't need anything initialized when the HttpRequest is initialized, we override
       * the interface and provide a no-op function.
       */
      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
        public void initialize(HttpRequest request) throws IOException {}
      }).setApplicationName("youtube-cmdline-search-sample").build();

      // Get query term from user.
      String queryTerm = getInputQuery();

 //     var sTargetUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=UCLjGAm9RZZF1UDXl-ZIf9ww&maxResults=10&key=AIzaSyCO0VV5DJmN1-X35VRL4Gz1ehkUxb4cyhY";
      
//      YouTube.Search.List search = youtube.search().list("id,snippet");
      com.google.api.services.youtube.YouTube.Playlists.List search = youtube.playlists().list("snippet");
      
      /*
       * It is important to set your developer key from the Google Developer Console for
       * non-authenticated requests (found under the API Access tab at this link:
       * code.google.com/apis/). This is good practice and increased your quota.
       */
      String apiKey = properties.getProperty("youtube.apikey");
      search.setKey(apiKey);
//      search.setQ(queryTerm);
      /*
       * We are only searching for videos (not playlists or channels). If we were searching for
       * more, we would add them as a string like this: "video,playlist,channel".
       */
//      search.setType("video");
      
      search.setChannelId( "UCLjGAm9RZZF1UDXl-ZIf9ww" );
      
      /*
       * This method reduces the info returned to only the fields we need and makes calls more
       * efficient.
       */
//      search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
      search.setFields("items(kind,id,snippet/title,snippet/thumbnails/default/url)");
      search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
//      SearchListResponse searchResponse = search.execute();
      PlaylistListResponse searchResponse = search.execute();

//      List<SearchResult> searchResultList = searchResponse.getItems();
      List<Playlist> searchResultList = searchResponse.getItems();

      if (searchResultList != null) {
        prettyPrint(searchResultList.iterator(), queryTerm);
      }
    } catch (GoogleJsonResponseException e) {
      System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
          + e.getDetails().getMessage());
    } catch (IOException e) {
      System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /*
   * Returns a query term (String) from user via the terminal.
   */
  private static String getInputQuery() throws IOException {

    String inputQuery = "";

    System.out.print("Please enter a search term: ");
    BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
    inputQuery = bReader.readLine();

    if (inputQuery.length() < 1) {
      // If nothing is entered, defaults to "YouTube Developers Live."
      inputQuery = "YouTube Developers Live";
    }
    return inputQuery;
  }

  /*
   * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
   * thumbnail.
   *
   * @param iteratorSearchResults Iterator of SearchResults to print
   *
   * @param query Search query (String)
   */
  private static void prettyPrint(Iterator<Playlist> iterator, String query) {

    System.out.println("\n=============================================================");
    System.out.println(
        "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
    System.out.println("=============================================================\n");

    if (!iterator.hasNext()) {
      System.out.println(" There aren't any results for your query.");
    }

    while (iterator.hasNext()) {

      Playlist singleVideo = iterator.next();
      String rId = singleVideo.getEtag();

      // Double checks the kind is video.
//      if (singleVideo.getKind().equals("youtube#video")) {
        Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

        System.out.println(" Etag : " + singleVideo.getEtag());
        System.out.println(" Kind : " + singleVideo.getKind());
        System.out.println(" Video Id : " + singleVideo.getId());
        System.out.println(" Video Url : https://youtu.be/" + singleVideo.getId());
        System.out.println(" Title : " + singleVideo.getSnippet().getTitle());
        System.out.println(" Thumbnail : " + thumbnail.getUrl());
        System.out.println("\n-------------------------------------------------------------\n");
//      }
    }
  }
}