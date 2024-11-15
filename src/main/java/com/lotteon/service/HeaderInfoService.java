package com.lotteon.service;

import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.entity.HeaderInfo;
import com.lotteon.repository.HeaderInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class HeaderInfoService {

    private final HeaderInfoRepository headerInfoRepository;
    private final ModelMapper modelMapper;

    public boolean existsById(Long id){
        return headerInfoRepository.existsById(id);
    }

    @CacheEvict(value = "headerInfo", key = "1")
    public void saveHeaderInfo(HeaderInfoDTO headerInfoDTO){
        HeaderInfo headerInfo = HeaderInfo.builder()
                .hd_title(headerInfoDTO.getHd_title())
                .hd_subtitle(headerInfoDTO.getHd_subtitle())
                .build();


        headerInfoRepository.save(headerInfo);
    }


    @CacheEvict(value = "headerInfo", key = "1")
    public void updateHeaderInfo(HeaderInfoDTO headerInfoDTO){
        HeaderInfo entity = headerInfoRepository.findById(headerInfoDTO.getHd_id()).orElseThrow(() -> new RuntimeException("FooterInfo not found"));

        entity.setHd_title(headerInfoDTO.getHd_title());
        entity.setHd_subtitle(headerInfoDTO.getHd_subtitle());

        headerInfoRepository.save(entity);
        log.info("Updated HeaderInfo and evicted cache.");

    }

    public void saveHeaderInfo2(HeaderInfoDTO headerInfoDTO){
        HeaderInfo headerInfo = HeaderInfo.builder()
                .hd_sName1(headerInfoDTO.getHd_sName1())
                .hd_sName2(headerInfoDTO.getHd_sName2())
                .hd_sName3(headerInfoDTO.getHd_sName3())
                .hd_oName1(headerInfoDTO.getHd_oName1())
                .hd_oName2(headerInfoDTO.getHd_oName2())
                .hd_oName3(headerInfoDTO.getHd_oName3())
                .build();


        headerInfoRepository.save(headerInfo);
    }

    @CacheEvict(value = "headerInfo", key = "1")
    public void updateHeaderInfo2(HeaderInfoDTO headerInfoDTO){
        HeaderInfo entity = headerInfoRepository.findById(headerInfoDTO.getHd_id()).orElseThrow(() -> new RuntimeException("FooterInfo not found"));

        entity.setHd_sName1(headerInfoDTO.getHd_sName1());
        entity.setHd_sName2(headerInfoDTO.getHd_sName2());
        entity.setHd_sName3(headerInfoDTO.getHd_sName3());
        entity.setHd_oName1(headerInfoDTO.getHd_oName1());
        entity.setHd_oName2(headerInfoDTO.getHd_oName2());
        entity.setHd_oName3(headerInfoDTO.getHd_oName3());

        headerInfoRepository.save(entity);
    }

    @CacheEvict(value = "headerInfo", key = "1")
    public void refreshHeaderInfoCache() {
        HeaderInfoDTO headerInfoDTO = getHeaderInfo();
        if (headerInfoDTO != null) {
            // 캐시가 비워지면, 즉시 새로운 데이터를 로드하여 캐시에 저장
            saveHeaderInfo(headerInfoDTO);
        }
    }
    @Scheduled(cron = "0 40 2 * * *") // 매일 10:10 AM에 실행
    public void scheduledHeaderInfoCacheUpdate() {
        refreshHeaderInfoCache();
    }



    @Cacheable(value = "headerInfo", key = "1")  // Cache with key "1" assuming a single HeaderInfo entry
    public HeaderInfoDTO  getHeaderInfo() {
        HeaderInfo headerInfo = headerInfoRepository.findById(1L).orElse(null);
        if (headerInfo != null) {
            return modelMapper.map(headerInfo, HeaderInfoDTO.class);
        } else {
            // 기본 HeaderInfoDTO 객체 생성하여 반환
            HeaderInfoDTO defaultHeaderInfo = new HeaderInfoDTO();
            defaultHeaderInfo.setHd_title("Default Title");
            defaultHeaderInfo.setHd_subtitle("Default Subtitle");
            return defaultHeaderInfo;
        }
    }

    // If headerInfo cache is empty, trigger data reload
    public HeaderInfoDTO fetchIfCacheEmpty() {
        HeaderInfoDTO headerInfoDTO = getHeaderInfo();
        if (headerInfoDTO == null) {
            refreshHeaderInfoCache(); // 캐시가 비어 있을 때 데이터 불러오기
            headerInfoDTO = getHeaderInfo();
        }
        return headerInfoDTO;
    }


}
