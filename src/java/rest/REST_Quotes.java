/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.QuoteNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

/**
 * REST Web Service
 *
 * @author simon
 */
@Path("quote")
public class REST_Quotes {

    private final static Random random = new Random();

    private static int nextId;

    private static Map<Integer, String> quotes = new HashMap() {
        {
            put(1, "Friends are kisses blown to us by angels");
            put(2, "Do not take life too seriously. You will never get out of it alive");
            put(3, "Behind every great man, is a woman rolling her eyes");
            nextId = size() + 1;
        }
    };

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Quotes
     */
    public REST_Quotes() {
    }

    /**
     * Retrieves representation of an instance of rest.REST_Quotes
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getQuoteById(@PathParam("id") String id)
            throws QuoteNotFoundException, Throwable {

        if (id.equals("")) {
            throw new QuoteNotFoundException("Quote with requested id not found");
        }
        
        int key = -1;
        try {
            key = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            // do something clever
        }

//        if (key == 5) 
//            throw new Exception("Internal server Error, we are very sorry for the inconvenience");
        
        if (quotes.containsKey(key)) {

            JsonObject quoteOutJSON = new JsonObject();

            quoteOutJSON.addProperty("quote", quotes.get(key));
            String jsonResponse = new Gson().toJson(quoteOutJSON);
            return jsonResponse;
        } else {
            throw new QuoteNotFoundException("Quote with requested id not found");
        }
    }

    @GET
    @Path("random")
    @Produces("application/json")
    public String getRandomQuote()
            throws QuoteNotFoundException {

        if (!quotes.isEmpty()) {

            JsonObject quoteOutJSON = new JsonObject();
            int key = random.nextInt(quotes.size()) + 1;
            quoteOutJSON.addProperty("quote", quotes.get(key));
            String jsonResponse = new Gson().toJson(quoteOutJSON);
            return jsonResponse;
        } else {
            throw new QuoteNotFoundException(("No Quotes Created yet"));
        }
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public String createQuoteJSON(String quoteText) {
        //Get the quote text from the provided Json
        JsonObject quoteInJSON = new JsonParser().parse(quoteText).getAsJsonObject();
        String quoteTextInsert = quoteInJSON.get("quote").getAsString();
        quotes.put(nextId, quoteTextInsert);

        JsonObject quoteOutJSON = new JsonObject();
        quoteOutJSON.addProperty("id", nextId);
        quoteOutJSON.addProperty("quote", quotes.get(nextId));
        String jsonResponse = new Gson().toJson(quoteOutJSON);

        nextId++;

        return jsonResponse;
    }

    /**
     * PUT method for updating or creating an instance of REST_Quotes
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public String changeQuoteByIdJSON(@PathParam("id") String id, String quoteText)
            throws QuoteNotFoundException {
        
        
        int key = -1;
        try {
            key = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            // do something clever
        }

        if (quotes.containsKey(key)) {

            //Get the quote text from the provided Json
            JsonObject quoteInJSON = new JsonParser().parse(quoteText).getAsJsonObject();
            String quoteTextInsert = quoteInJSON.get("quote").getAsString();

            quotes.put(key, quoteTextInsert);

            JsonObject quoteOutJSON = new JsonObject();
            quoteOutJSON.addProperty("id", key);
            quoteOutJSON.addProperty("quote", quotes.get(key));
            String jsonResponse = new Gson().toJson(quoteOutJSON);

            return jsonResponse;
        } else {
            throw new QuoteNotFoundException("Quote with requested id not found");
        }
    }

    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public String deleteQuoteById(@PathParam("id") String id)
            throws QuoteNotFoundException {

        int key = -1;
        try {
            key = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            // do something clever
        }

        if (quotes.containsKey(key)) {

            JsonObject quoteOutJSON = new JsonObject();
            quoteOutJSON.addProperty("quote", quotes.get(key));
            String jsonResponse = new Gson().toJson(quoteOutJSON);

            quotes.remove(key);

            return jsonResponse;
        } else {
            throw new QuoteNotFoundException("Quote with requested id not found");
        }
    }
}
