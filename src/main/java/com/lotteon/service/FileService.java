package com.lotteon.service;



import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.dto.product.ProductFileDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.dto.product.ReviewRequestDTO;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.entity.product.ReviewFile;
import com.lotteon.repository.BannerRepository;
import com.lotteon.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;
    private final BannerRepository bannerRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    //banner UploadFile
    public BannerDTO uploadFile(BannerDTO bannerDTO) {

        // 파일 업로드 경로 파일 객체 생성
        File fileUploadPath = new File(uploadPath);

        // 파일 업로드 디렉터리가 존재하지 않으면 디렉터리 생성
        if (!fileUploadPath.exists()) {
            fileUploadPath.mkdirs();
        }

        // 파일 업로드 시스템 경로 구하기
        String path = fileUploadPath.getAbsolutePath();

        // 파일 정보 객체 가져오기
        MultipartFile file = bannerDTO.getFile(); // 배너 DTO에서 파일 정보 가져오기

        BannerDTO newBannerDTO = new BannerDTO();

        if (!file.isEmpty()) {
            String oName = file.getOriginalFilename();
            String ext = oName.substring(oName.lastIndexOf("."));
            String sName = UUID.randomUUID().toString() + ext;


            // 허용된 확장자 목록
            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");

            // 확장자가 허용된 목록에 있는지 확인
            if (!allowedExtensions.contains(ext)) {
                throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG, JPEG, PNG만 업로드할 수 있습니다.");
            }

            // 파일 저장
            try {
                file.transferTo(new File(path, sName));
            } catch (IOException e) {
                log.error(e);
            }
            newBannerDTO.setBan_oname(oName);
            newBannerDTO.setBan_image(sName);


        }
        return newBannerDTO;
    }

    public List<String> getFilesByCategory(ProductCategory category) {
        String categoryPath = uploadPath + category.getFullPath();
        File directory = new File(categoryPath);

        // 디렉터리가 없으면 생성
        if (!directory.exists()) {
            directory.mkdirs();
        }

        List<String> files = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    files.add(file.getName());
                }
            }
        }
        return files;
    }

    //productFile upload;
    public List<ProductFileDTO> uploadFile(MultiValueMap<String, MultipartFile> images,ProductCategory category) {
        //파일 시스템 경로 구하기
        String categoryPath = uploadPath + "productImg/"+ category.getFullPath();
        String savedPth= "productImg/"+ category.getFullPath();
        File fileuploadpath = new File(categoryPath);


        if (!fileuploadpath.exists()) {
            fileuploadpath.mkdirs();
        }
        String path = fileuploadpath.getAbsolutePath();
        List<ProductFileDTO> fileDTOs = new ArrayList<>();


        for (String key : images.keySet()) {
            List<MultipartFile> files = images.get(key);
            if (files != null) {
                for (MultipartFile file : files) {
                    String originalName = file.getOriginalFilename();
                    if (originalName != null && !originalName.isEmpty()) {
                        // 확장자 추출
                        String ext = originalName.substring(originalName.lastIndexOf("."));
                        // 저장될 파일 이름 생성 (UUID + 확장자)
                        String savedName = UUID.randomUUID().toString() + ext;

                        // 파일 저장
                        try {
                            file.transferTo(new File(path, savedName));

                            log.info("savedPath : "+savedPth);
                            // 업로드된 파일 정보를 DTO로 변환하여 저장
                            ProductFileDTO productFileDTO = ProductFileDTO.builder()
                                    .oName(originalName)
                                    .sName(savedName)
                                    .path(savedPth)
                                    .type(key) // 파일의 MIME 타입 저장
                                    .build();

                            fileDTOs.add(productFileDTO); // 리스트에 추가
                        } catch (IOException e) {
                            log.error(e);
                            // 파일 저장 중 오류 발생 시 처리
                        }
                    }
                }
            }
        }
        log.info(fileDTOs);
        return fileDTOs;

    }

    public HeaderInfoDTO uploadFiles(HeaderInfoDTO headerInfoDTO) {
        File fileuploadpath = new File(uploadPath + "ConfigImg/");

        if (!fileuploadpath.exists()) {
            fileuploadpath.mkdirs();
        }

        String path = fileuploadpath.getAbsolutePath();

        MultipartFile file1 = headerInfoDTO.getFile1();
        MultipartFile file2 = headerInfoDTO.getFile2();
        MultipartFile file3 = headerInfoDTO.getFile3();

        HeaderInfoDTO newHeaderInfoDTO = new HeaderInfoDTO();

        // 파일1 업로드 처리
        if (!file1.isEmpty()) {
            String oName = file1.getOriginalFilename();
            String ext = oName.substring(oName.lastIndexOf("."));
            String sName = "headerLogo.jpg";  // 'favicon'이라는 고정 이름 사용

            // 허용된 확장자 목록
            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".ico");

            // 확장자가 허용된 목록에 있는지 확인
            if (!allowedExtensions.contains(ext)) {
                throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG, JPEG, PNG, ICO만 업로드할 수 있습니다.");
            }

            // 파일 저장
            try {
                file1.transferTo(new File(path, sName));
                log.info("file1 저장 완료: {}", sName); // 로그 추가
            } catch (IOException e) {
                log.error(e);
            }

            newHeaderInfoDTO.setHd_oName1(oName);
            newHeaderInfoDTO.setHd_sName1(sName); // 파일 이름 저장
            log.info("file1 DTO에 저장된 파일 이름: {}", newHeaderInfoDTO.getHd_sName1()); // 로그 추가
        }

        // 파일2 업로드 처리
        if (!file2.isEmpty()) {
            String oName = file2.getOriginalFilename();
            String ext = oName.substring(oName.lastIndexOf("."));
            String sName = "footerLogo.jpg";  // 'favicon'이라는 고정 이름 사용

            // 허용된 확장자 목록
            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".ico");

            // 확장자가 허용된 목록에 있는지 확인
            if (!allowedExtensions.contains(ext)) {
                throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG, JPEG, PNG, ICO만 업로드할 수 있습니다.");
            }

            // 파일 저장
            try {
                file2.transferTo(new File(path, sName));
                log.info("file2 저장 완료: {}", sName); // 로그 추가
            } catch (IOException e) {
                log.error(e);
            }

            newHeaderInfoDTO.setHd_oName2(oName);
            newHeaderInfoDTO.setHd_sName2(sName); // 파일 이름 저장
            log.info("file2 DTO에 저장된 파일 이름: {}", newHeaderInfoDTO.getHd_sName2()); // 로그 추가
        }

        // 파일3 업로드 처리
        if (!file3.isEmpty()) {
            String oName = file3.getOriginalFilename();
            String ext = oName.substring(oName.lastIndexOf("."));
            String sName = "favicon" + ext;  // 'favicon'이라는 고정 이름 사용

            // 허용된 확장자 목록
            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".ico");

            // 확장자 확인
            if (!allowedExtensions.contains(ext)) {
                throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG, JPEG, PNG, ICO만 업로드할 수 있습니다.");
            }

            // 파일 저장
            try {
                file3.transferTo(new File(path, sName));
                log.info("file3(파비콘) 저장 완료: {}", sName);
            } catch (IOException e) {
                log.error(e);
            }


            newHeaderInfoDTO.setHd_oName3(oName);
            newHeaderInfoDTO.setHd_sName3(sName); // 파일 이름 저장
            log.info("file3 DTO에 저장된 파일 이름: {}", newHeaderInfoDTO.getHd_sName3()); // 로그 추가
        }

        // 최종적으로 저장된 파일 이름 확인
        log.info("최종 DTO에 저장된 파일 이름들: {}, {}, {}",
                newHeaderInfoDTO.getHd_sName1(),
                newHeaderInfoDTO.getHd_sName2(),
                newHeaderInfoDTO.getHd_sName3(),
                newHeaderInfoDTO.getHd_oName1(),
                newHeaderInfoDTO.getHd_oName2(),
                newHeaderInfoDTO.getHd_oName3()
        );

        return newHeaderInfoDTO;
    }

    public ReviewRequestDTO uploadReviewFiles(ReviewRequestDTO reviewDTO) {
        File fileUploadPath = new File(uploadPath + "ReviewImg/");

        if (!fileUploadPath.exists()) {
            fileUploadPath.mkdirs();
        }

        List<MultipartFile> files = reviewDTO.getPReviewFiles();
        List<ReviewFile> reviewFiles = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                log.debug("Processing file: " + file.getOriginalFilename());
                if (!file.isEmpty()) {
                    String originalName = file.getOriginalFilename();
                    String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
                    String savedName = UUID.randomUUID().toString() + ext;
                    String filePath = fileUploadPath.getAbsolutePath() + File.separator + savedName;

                    List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");
                    if (!allowedExtensions.contains(ext)) {
                        throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG, JPEG, PNG만 업로드할 수 있습니다.");
                    }

                    try {
                        file.transferTo(new File(filePath));
                        ReviewFile reviewFile = new ReviewFile();
                        reviewFile.setSname(savedName);
                        reviewFile.setPath(filePath);
                        log.debug("File saved: " + savedName + " at " + filePath);
                        reviewFiles.add(reviewFile);
                    } catch (IOException e) {
                        log.error("파일 업로드 오류: ", e);
                    }
                } else {
                    log.warn("Empty file detected: " + file.getOriginalFilename());
                }
            }
        }

        log.debug("Total saved review files: " + reviewFiles.size());
        reviewDTO.setSavedReviewFiles(reviewFiles); // DTO에 파일 리스트 설정

        return reviewDTO;
    }


    public void deleteFile(int fileNo,String filePath){



        File file = new File(filePath);
        if(file.exists()){
            if (file.delete()) {
                System.out.println("File deleted successfully: " + filePath);
            } else {
                System.out.println("Failed to delete the file: " + filePath);
            }
        } else {
            String productPath = uploadPath+"productImg/";
            File file1 = new File(productPath);
            if (file.delete()) {
                System.out.println("File deleted successfully: " + filePath);
            } else {
                System.out.println("File not found: " + filePath);
            }
        }


    }
}
