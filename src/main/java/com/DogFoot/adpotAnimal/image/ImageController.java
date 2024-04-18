package com.DogFoot.adpotAnimal.image;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/image")
public class ImageController {

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> upload(@RequestPart("image") MultipartFile imageFile) {
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif");

        String originalFilename = imageFile.getOriginalFilename();  // 파일의 원본 이름을 가져옴
        if (originalFilename == null) {
            return new ResponseEntity<>("이미지 파일이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!allowedExtensions.contains(ext)) {
            return new ResponseEntity<>("이미지 파일만 업로드 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        String savePath = "./images/";
        String storeFileName = UUID.randomUUID() + "." + ext;
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            String key = savePath + now + "/" + storeFileName;
            File temp = new File(savePath + now + "/");

            if (!temp.exists()) {
                temp.mkdirs();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(key);
            fileOutputStream.write(imageFile.getBytes());
            fileOutputStream.close();

            String imageUrl = "/images/" + now + "/" + storeFileName;
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("이미지 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
