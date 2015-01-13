package jp.leopanda.articleSpreader.modifiedjumblr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ResponseWrapper {

    private JsonElement response;
    private JumblrClient client;

    public void setClient(JumblrClient client) {
        this.client = client;
    }
    public User getUser() {
        return get("user", User.class);
    }
    
    public Blog getBlog() {
        return get("blog", Blog.class);
    }

    public JumblrClient getClient(){
    	return this.client;
    }
 
    public Long getId() {
    	Long ret = 0L;
        JsonObject object = (JsonObject) response;
        if(object.get("id") != null){
        	ret = object.get("id").getAsLong();
        }
        return ret;
    }

    private <T extends Resource> T get(String field, Class<T> k) {
        Gson gson = gsonParser();
        JsonObject object = (JsonObject) response;
        T e = gson.fromJson(object.get(field).toString(), k);
        e.setClient(client);
        return e;
    }


    private Gson gsonParser() {
        return new GsonBuilder().
            registerTypeAdapter(Post.class, new PostDeserializer()).
            create();
    }

}
