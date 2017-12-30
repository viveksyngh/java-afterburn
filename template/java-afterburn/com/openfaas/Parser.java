package com.openfaas;

import java.io.*;

public class Parser {
    public void acceptIncoming(DataInputStream dataStream, BufferedWriter out, HeaderReader parser) throws IOException {

        StringBuffer rawHeader = parser.readHeader();
        System.err.println("rawHeader: \'"+ rawHeader + "\''");

        HttpHeader header = new HttpHeader(rawHeader.toString());

        if(header.getMethod() != null) {
            System.err.println("\'" + header.getMethod() + "\' method");
            System.err.println(header.getContentLength()  + " bytes");
            
            byte[] body = header.readBody(dataStream);

            function.Handler handler = new function.Handler();
            HttpResponse response = new HttpResponse(); 
            response.setContentType("text/plain");
	    String functionResponse = "";
	    try {
		  functionResponse = handler.function(new String(body), header.getMethod());
		  response.setStatus(200);
	     } 
	    catch (Exception ex) {
		functionResponse = ex.getMessage();
		response.setStatus(500);
	     }
            StringBuffer outBuffer = response.serialize(functionResponse);

            out.write(outBuffer.toString(), 0, outBuffer.length());
            out.flush();
        }
    }
}
