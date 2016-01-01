package com.smartdse.android;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;


/*
 * This class is used for grabbing the source code of a webpage
 */

public class SrcGrabber
{




    public SrcGrabber()
    {

    }

    public String grabSource(String url) throws IOException, URISyntaxException
    {


        Document doc = Jsoup.connect(url).get();
        return  doc.body().text();


    }







}