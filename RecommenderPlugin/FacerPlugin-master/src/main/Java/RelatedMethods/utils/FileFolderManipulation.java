package RelatedMethods.utils;

import java.io.*;


public class FileFolderManipulation {

	public static void main(String[] args) throws IOException {
		/*Path sourceDirectory = Paths.get("F:/PhD/4thYear/FOCUS-ICSE19-artifact-evaluation/crossminer-FOCUS-4c02746/dataset/SH_S/evaluation");
        Path targetDirectory = Paths.get("F:\\LOOCV\\C2.2");


        CustomFileVisitor fileVisitor = new CustomFileVisitor(sourceDirectory, targetDirectory);
        //You can specify your own FileVisitOption
        Files.walkFileTree(sourceDirectory, fileVisitor);*/
		
		File srcFolder = new File("F:/PhD/4thYear/FOCUS-ICSE19-artifact-evaluation/crossminer-FOCUS-4c02746/dataset/SH_S/evaluation");
    	File destFolder = new File("F:\\LOOCV\\C2.2");
    	//copyFolder(srcFolder,destFolder);
    	//make sure source exists
    	if(!srcFolder.exists()){

           //System.out.println("Directory does not exist.");
           //just exit
           System.exit(0);

        }else{

           try{
        	copyFolder(srcFolder,destFolder);
           }catch(IOException e){
        	e.printStackTrace();
        	//error, just exit
                System.exit(0);
           }
        }
    	
    	//System.out.println("Done");


	}
	
	  public static void copyFolder(File src, File dest)
		    	throws IOException{
		    	
		    	if(src.isDirectory()){
		    		
		    		//if directory not exists, create it
		    		if(!dest.exists()){
		    		   boolean wasCopied = dest.mkdirs();
		    		   //System.out.println("Directory copied from " 
		                //              + src + "  to " + dest);
		    		}
		    		
		    		//list all the directory contents
		    		String files[] = src.list();
		    		
		    		for (String file : files) {
		    		   //construct the src and dest file structure
		    		   File srcFile = new File(src, file);
		    		   File destFile = new File(dest, file);
		    		   //recursive copy
		    		   copyFolder(srcFile,destFile);
		    		}
		    	   
		    	}else{
		    		//if file, then copy it
		    		//Use bytes stream to support all file types
		    		if(!src.getName().contentEquals(".gitkeep")){
		    		InputStream in = new FileInputStream(src);
		    	        OutputStream out = new FileOutputStream(dest); 
		    	                     
		    	        byte[] buffer = new byte[1024];
		    	    
		    	        int length;
		    	        //copy the file content in bytes 
		    	        while ((length = in.read(buffer)) > 0){
		    	    	   out.write(buffer, 0, length);
		    	        }
		 
		    	        in.close();
		    	        out.close();
		    	        //System.out.println("File copied from " + src + " to " + dest);
		    		}
		    	}
		    }

	
}

