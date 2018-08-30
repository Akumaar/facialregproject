package org.flinthill.amrith.project1;
import java.awt.Image;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.S3Object;



public class SearchFaces {
	
	public static final String COLLECTION_ID = "FHSStudents";
	public static final String S3_BUCKET = "amrithtest";
	private AmazonRekognition amazonRekognition;
	
	
	 
	
	public AmazonRekognition getAmazonRekognition() {
		return amazonRekognition;
	}
	
	
	public void setAmazonRekognition() {
		
		ProfilesConfigFile configFile = new ProfilesConfigFile("credential"); 
		//configFile.

		AWSCredentials credentials = new ProfileCredentialsProvider(configFile,"default").getCredentials();
	     
		 amazonRekognition = AmazonRekognitionClientBuilder
	         .standard()
	         .withRegion(Regions.US_EAST_1 )
	         .withCredentials(new AWSStaticCredentialsProvider(credentials))
	         .build();
		//this.amazonRekognition = amazonRekognition;
	}
	

	public void createCollections() {

		
		 CreateCollectionRequest request = new CreateCollectionRequest()
		         .withCollectionId(COLLECTION_ID);
		 CreateCollectionResult result = amazonRekognition.createCollection(request);
		 
		      System.out.println("CollectionArn : " +
		    		  result.getCollectionArn());
		      System.out.println("Status code : " +
		    		  result.getStatusCode().toString());

		
	}
	
	public static void main (String[] args) {
		


				 SearchFaces searchfaces = new SearchFaces(); 
				 searchfaces.setAmazonRekognition();
				 //searchfaces.createCollections();
	    	     callIndexFaces(COLLECTION_ID, searchfaces.getAmazonRekognition(), "IDConstants.jpg ","IDReference");
	    	     //callIndexFaces(COLLECTION_ID, searchfaces.getAmazonRekognition(), "NickHappy.jpg","Nick");
	    	     //callIndexFaces(COLLECTION_ID, searchfaces.getAmazonRekognition(), "Dylan1.jpg","Dylan");

	    	      Float threshold = 92F;
	    	      int maxFaces = 2;
	
	    	         
	    	      
//	    	      String fileName = "AmrithSad.jpg";
//	    	      Image image = getImageUtil(S3_BUCKET, fileName);
//
//	    	      //5. Search collection for faces similar to the largest face in the image.
//	    	      SearchFacesByImageResult searchFacesByImageResult =
//	    	    		  searchfaces.callSearchFacesByImage(COLLECTION_ID, image, threshold, maxFaces,
//	    	        		 searchfaces.getAmazonRekognition());
//
//	    	      //System.out.println("Faces matching largest face in image  " + fileName);
//	    	      List < FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();
//	    	      if (faceImageMatches.size() == 0){
//	    	    	  System.out.println("You are not a FlintHill student");
//	    	      }else{
//	    	    	  System.out.println(" You are a FlintHill Student, Access Granted ");
//	    	      }
//	    	      for (FaceMatch face: faceImageMatches) {
//	    	         System.out.println(face.getFace().toString());
//	    	         System.out.println();
//	    	      }

	}
	
		// Indexes the faces into a collection
		private static IndexFacesResult callIndexFaces(
			    String collectionId, AmazonRekognition amazonRekognition,String fileName, String name) {
				IndexFacesRequest req = new IndexFacesRequest()
				         .withImage(
				        		 getImageUtil(S3_BUCKET, fileName))
				         .withCollectionId(collectionId)
				         .withExternalImageId(name);
		    
			      return amazonRekognition.indexFaces(req);		      
		}

	   // Calling search faces of Rekgonition API
	   public  SearchFacesByImageResult callSearchFacesByImage(String collectionId,
	      Image image, Float threshold, int maxFaces, AmazonRekognition amazonRekognition
	   ) {
	      SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
	         .withCollectionId(collectionId)
	         .withImage(image)
	         .withFaceMatchThreshold(threshold)
	         .withMaxFaces(maxFaces);
	      return amazonRekognition.searchFacesByImage(searchFacesByImageRequest);
	   }
		   
	   // Calling search faces of Rekgonition API   
	   private static Image getImageUtil(String bucket, String key) {
		      return new Image()
		         .withS3Object(new S3Object()
		            .withBucket(bucket)
		            .withName(key));
		   }
	   
	
}