package com.cesarandres.ps2link.module.twitter;

/**
 * This class will hold the information of a tweet.
 */
public class PS2Tweet implements Comparable<PS2Tweet> {
    private String user;
    private String content;
    private String tag;
    private Integer date;
    private String imgUrl;
    private String id;

    /**
     * @param id      id of this tweet. This is unique for every tweet
     * @param user    user that originally created or retweeted this tweet
     * @param date    unix time of when this tweet was created
     * @param content text contained in this tweet
     * @param tag     tag or alias of the user
     * @param imgUrl  url to retrieve the image of the user
     */
    public PS2Tweet(String id, String user, int date, String content, String tag, String imgUrl) {
        this.user = user;
        this.date = date;
        this.content = content;
        this.tag = tag;
        this.id = id;
        this.imgUrl = imgUrl;
    }

    /**
     * Empty constructor. None of the fields will be initialized
     */
    public PS2Tweet() {
    }

    /**
     * @return the user that created this tweet
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user that created this tweet
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the content of this tweet
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content for this tweet
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the unix date when this tweet was created
     */
    public int getDate() {
        return date;
    }

    /**
     * @param date the unix date when this tweet was created
     */
    public void setDate(int date) {
        this.date = date;
    }

    /**
     * @return the unique id of this tweet
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the unique identifier for this tweeet
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the alias of the user
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the alias of this user
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(PS2Tweet other) {
        return this.date.compareTo(other.getDate()) * -1;
    }

    /**
     * @return the url of the thumbnail for this tweet
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl the url of the thumbnail for this tweet
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
