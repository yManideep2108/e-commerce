package com.ecommerce.manideep.sb.ecom.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        //File name of current or original file
        String originalFileName = file.getOriginalFilename();
        //generate a unique file name
        //If file name is ball.jpg and randomuuid is 1234
        //Then file name will be 1234.jpg
        String uniqueId =  UUID.randomUUID().toString();
        String fileName =  uniqueId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        //check if path exists and create
        String filePath = path + File.separator + fileName ;
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        //returning the file name
        return fileName ;
    }
}
