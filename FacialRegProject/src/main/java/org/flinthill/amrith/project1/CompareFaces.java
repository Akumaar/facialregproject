package org.flinthill.amrith.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * This is Lambda Funciton class
 * @author akumaar
 *
 */
public class CompareFaces implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
    	
        context.getLogger().log("Input: " + input.getClass().getName());
        
        // Input values from mobile client
        ArrayList<String> values =(ArrayList<String>)input;        
        String bucketName =  values.get(0);
        String face1 = values.get(1);
        String face2 = values.get(2);
        String collectionId = values.get(3);
        String fileName = values.get(4);
        CompareFacesHelper helper = new CompareFacesHelper();     
        return helper.searchFaces(collectionId, bucketName, fileName);
        // Testing Git
        
    }
    
    

}
