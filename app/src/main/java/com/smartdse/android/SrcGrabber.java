package com.smartdse.android;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/*
 * This class is used for grabbing the source code of a webpage
 */

public class SrcGrabber
{
    private HttpGet mRequest;
    private HttpClient mClient;
    private BufferedReader mReader;

    private StringBuffer mBuffer;
    private String mNewLine;



    public SrcGrabber()
    {
        mRequest = new HttpGet();
        mClient = new DefaultHttpClient();
        mReader = null;

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        mBuffer = new StringBuffer(2000);
        mNewLine = System.getProperty("line.separator");
    }

    public String grabSource(String url) throws ClientProtocolException, IOException, URISyntaxException
    {
        mBuffer.setLength(0);

        try
        {
            mRequest.setURI(new URI(url));
            HttpResponse response = mClient.execute(mRequest);

            mReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = mReader.readLine()) != null)
            {
                mBuffer.append(line);
                mBuffer.append(mNewLine);
            }
        }
        finally
        {
            closeReader();
        }

        return mBuffer.toString();
    }





    private void closeReader()
    {
        if (mReader == null)
            return;

        try
        {
            mReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}