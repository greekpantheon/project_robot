package com.athena.robot.subprocess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PresetInitializer {
    private static final Logger log=LogManager.getLogger(PresetInitializer.class);
    public void initialize() {
        Scanner scanner=new Scanner(System.in);
        log.trace("Starting the Initialize Process with the OS detection");
        String os=checkSourceSystem();
        log.debug("This is the source System "+os);
        log.trace("Asking for folder path");
        String folderPath=checkPathForFolder(scanner);
        log.trace("Starting the folder creation at the path with today date");
        String wallpaperFolderPath=createFolderPath(scanner, folderPath);
        log.debug("New Folder has been Created at the location"+wallpaperFolderPath);
        System.out.println("Enter the number of minutes we want to change wallpaper ");
        int timeToChange=getIntegerValCheck(scanner);
        System.out.println("Getting the value for Time as "+timeToChange);
        System.out.println("How many Wallpapers do you want to download ??");
        int wallpapersPerSub=getIntegerValCheck(scanner);
        String duration=getProperDuration(scanner);
        List<String> subRedditName=gatherSubReddit(scanner);
        DownloadWallPapers dw=new DownloadWallPapers();
        dw.downloadWallPapers(wallpapersPerSub,subRedditName,duration,wallpaperFolderPath);
        //!TODO Add a new File Somewhere the Settings or log file is created.
        scanner.close();
        //!TODO Add code for the setting the code for the folders.
    }

    private List<String> gatherSubReddit(Scanner scanner) {
        List<String> subredditList=new ArrayList<>();
        subredditList.add("EarthPorn");
        subredditList.add("r/RoomPorn");
        //FIXME fix the following code to add continous input from the users for array values.
//        //Adding default subreddits
//        System.out.println("Enter the subreddit you want to gather pics with either with r/ or just name, without / at end ");
//        String valueToAdd=scanner.nextLine();
//        while (scanner.hasNextLine()){
//            subredditList.add(valueToAdd);
//        }
        return subredditList;
    }

    private String getProperDuration(Scanner scanner) {
        System.out.println("How long back do you want to go\n" +
                "\t\t\t1. all -- Get ALL Top WallPapers (default Value)\n" +
                "\t\t\t2. month --- go back month\n" +
                "\t\t\t3. year     -- go back year\n" +
                "\t\t\t4. day --- go back day." +
                "Enter the Number only:::");
        int value=getIntegerValCheck(scanner);
        String properDuration="all";
        switch (value){
            case 1:
                properDuration="all";
                break;
            case 2:
                properDuration="month";
                break;
            case 3:
                properDuration="year";
                break;
            case 4:
                properDuration="day";
                break;
            default:
                System.out.println("Please enter propervalue");
                getProperDuration(scanner);
                break;
        }


        return properDuration;
    }

    private int getIntegerValCheck(Scanner scanner) {
        while(!scanner.hasNextInt()){
            System.out.println("Please enter a Integer Value");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private String createFolderPath(Scanner scanner, String folderPath) {
        log.trace("Starting the folder creation");
        String wallPaperCreatePath=null;
        try{
            log.trace("Starting the try block");
            if(null==folderPath||folderPath.isEmpty()) {
                log.trace("Folder path is empty so calling the checkPathFolder for the method");
                wallPaperCreatePath = checkPathForFolder(scanner);
            }
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyy");
            log.trace("Adding the wallpaper at the  end to generate new folder");
            wallPaperCreatePath=folderPath+"\\Wallpapers_"+simpleDateFormat.format(date);
            Path newPath=Paths.get(wallPaperCreatePath);
            if(newPath.toFile().exists()){
                log.debug("Folder is already Created");
            }else{
                log.trace("Creating the new folder for wallpapers");
                Files.createDirectory(newPath);
            }
            log.trace("New Path"+wallPaperCreatePath);
        }catch (Exception e){
            log.error("error occurred in the wallpaper Folder creation"+e);
        }
        return wallPaperCreatePath;
    }

    private String checkPathForFolder(Scanner scanner) {

        System.out.println("Enter the Location for the Folder :");
        String path;
        path = scanner.nextLine();
        log.debug("path before"+path);
        Path filePath= Paths.get(path);
        if(!filePath.toFile().isDirectory()){
            log.debug("Please enter a path for directory");
            checkPathForFolder(scanner);
        }else {
            log.debug("Entered path is a directory" + filePath.toFile().isDirectory());
        }
        return path;
    }

    private String checkSourceSystem(){
       log.trace("Starting the source System check");
        String source=System.getProperty("os.name").toLowerCase();
        log.trace("Source system is out "+source);
        if(source.contains("win")){
            source="Windows";
        }else if(source.contains("nix")||source.contains("nux")){
            source="Unix";
        }else  if(source.contains("mac")){
            source="Mac OS X";
        }
        return source;
    }
}
