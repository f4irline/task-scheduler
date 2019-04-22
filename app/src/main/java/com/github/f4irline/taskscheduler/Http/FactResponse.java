package com.github.f4irline.taskscheduler.Http;

/**
 * The model of the fact which is expected from the http request.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0406
 */
public class FactResponse {
    String text;
    String permalink;
    String source_url;
    String language;
    String source;

    /**
     * Returns the text in the response of the http request.
     *
     * @return the text in the response of the http request.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text variable.
     *
     * @param text the new text variable.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the permanent link in the response of the http request.
     *
     * @return the permanent link in the response of the http request.
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * Sets the permanent link variable.
     *
     * @param permalink the new permanent link variable.
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * Returns the source_url in the response of the http request.
     *
     * @return the source_url in the response of the http request.
     */
    public String getSource_url() {
        return source_url;
    }

    /**
     * Sets the source_url variable.
     *
     * @param source_url the new source_url variable.
     */
    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    /**
     * Returns the language in the response of the http request.
     *
     * @return the language in the response of the http request.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language variable.
     *
     * @param language the new language variable.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Returns the source in the response of the http request.
     *
     * @return the source in the response of the http request.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source variable.
     *
     * @param source the new source variable.
     */
    public void setSource(String source) {
        this.source = source;
    }
}
