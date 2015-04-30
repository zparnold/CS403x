package edu.wpi.cs.cs403xproj4.wpifreebies.services;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.MultipartEntity;

/**
 * Created by Tyler on 4/29/2015.
 */
public class RequestParams {
    String url;
    UrlEncodedFormEntity formData;

    public RequestParams(String url) {
        this.url = url;
    }

    public RequestParams(String url, UrlEncodedFormEntity formData) {
        this.url = url;
        this.formData = formData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlEncodedFormEntity getFormData() {
        return formData;
    }

    public void setFormData(UrlEncodedFormEntity formData) {
        this.formData = formData;
    }
}
