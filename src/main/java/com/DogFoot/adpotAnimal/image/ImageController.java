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
    public ResponseEntity<String> upload(@RequestPart("Dto") ImageDto imageDto, @RequestPart("image") MultipartFile image) {
        String originalFilename = image.getOriginalFilename();  // 파일의 원본 이름을 가져옴
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index+1).toLowerCase(); // 파일의 확장자를 추출합니다.
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif");

        if(!allowedExtensions.contains(ext)){
            return new ResponseEntity<>("이미지 파일만 업로드 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        String savePath = "./images/";
        String storeFileName = UUID.randomUUID() + "." + ext;
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            String key = savePath + now + "/" + storeFileName;
            File temp = new File(savePath + now + "/");

            if(!temp.exists()){
                temp.mkdirs();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(key);
            fileOutputStream.write(image.getBytes());
            fileOutputStream.close();

            imageDto.setImageUrl("/images/" + now + "/" + storeFileName);
            return new ResponseEntity<>("이미지가 성공적으로 저장되었습니다.", HttpStatus.OK);
        } catch (IOException e){
            return new ResponseEntity("이미지 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
