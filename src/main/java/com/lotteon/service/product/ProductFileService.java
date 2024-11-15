package com.lotteon.service.product;

import com.lotteon.dto.product.ProductFileDTO;
import com.lotteon.entity.product.ProductFile;
import com.lotteon.repository.product.ProductFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductFileService {

    private final ProductFileRepository productFileRepository;

    private final ModelMapper modelMapper;

    public ProductFile insertFile(ProductFileDTO file){
        log.info("Insert file : "+file);
        ProductFile savedFile = modelMapper.map(file, ProductFile.class);
        log.info("Saved file : "+savedFile);
        ProductFile FileEntity = productFileRepository.save(savedFile);
        return FileEntity;
    }



}
