package news.demosisto.demosistonewspro.Objects;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Created by len on 9/12/16.
 */
public class PostItem {
    private int postId;
    String date,URL,short_URL,content,title;
    public int getPostId(){return postId;};
    public String getDate(){return StringEscapeUtils.unescapeJava(date);};
    public String getURL(){return StringEscapeUtils.unescapeJava(URL);};
    public String getShort_URL(){return StringEscapeUtils.unescapeJava(short_URL);};
    public String getContent(){return StringEscapeUtils.unescapeJava(content);};
    public String getTitle(){return StringEscapeUtils.unescapeJava(title);};


    public PostItem(String inPostId, String inDate, String inUrl,String inShort_Url,String inContent,String inTitle){
        postId = Integer.parseInt(inPostId);
        date = inDate;
        URL = inUrl;
        short_URL = inShort_Url;
        content = inContent;
        title = inTitle;

    }
    public String toString(){
        return
                "Post Id: " + getPostId()
                + " Title: " + getTitle()
                + " Posted Date: " + getDate()
                + " URL: " + getURL()
                + " Short URL: " + getShort_URL()
                + " Content: " + getContent();

    }
}
