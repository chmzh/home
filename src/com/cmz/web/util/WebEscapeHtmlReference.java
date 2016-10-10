package com.cndw.web.util;

import org.apache.velocity.app.event.implement.EscapeHtmlReference;

public class WebEscapeHtmlReference extends EscapeHtmlReference {  
	  
    @Override  
    protected String escape(Object text) {  
        return escapeHtml(text);  
    }  
  
    private static String escapeHtml(Object value) {  
        if (value == null)  
            return null;  
  
        if (value instanceof String) {  
            String result = value.toString();
            result = result.replaceAll("&", "&amp;").replaceAll(">", "&gt;")  
                    .replaceAll("<", "&lt;").replaceAll("\"", "&quot;");  
            return result;  
        } else {  
            return value.toString();  
        }  
    }  
}  
