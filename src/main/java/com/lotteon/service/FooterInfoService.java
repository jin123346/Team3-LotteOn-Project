package com.lotteon.service;

import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.entity.FooterInfo;
import com.lotteon.repository.FooterInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class FooterInfoService {
    private final FooterInfoRepository footerInfoRepository;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;

    public boolean existsById(Long id) {
        return footerInfoRepository.existsById(id);
    }

    public void saveFooterInfo(FooterInfoDTO footerInfoDTO) {
        FooterInfo footerInfo = FooterInfo.builder()
                .ft_company(footerInfoDTO.getFt_company())
                .ft_ceo(footerInfoDTO.getFt_ceo())
                .ft_bo(footerInfoDTO.getFt_bo())
                .ft_mo(footerInfoDTO.getFt_mo())
                .ft_addr1(footerInfoDTO.getFt_addr1())
                .ft_addr2(footerInfoDTO.getFt_addr2())
                .ft_hp(footerInfoDTO.getFt_hp())
                .ft_time(footerInfoDTO.getFt_time())
                .ft_email(footerInfoDTO.getFt_email())
                .ft_troublehp(footerInfoDTO.getFt_troublehp())
                .ft_copyright(footerInfoDTO.getFt_copyright())
                .build();

        footerInfoRepository.save(footerInfo);
    }

    @CacheEvict(value = "footerInfo", allEntries = true)
    public void refreshFooterInfoCache() {
        getFooterInfo(); // 캐시를 초기화하기 위해 호출
    }
    @Scheduled(cron = "0 40 2 * * *") // 매일 10:10 AM에 실행
    public void scheduledFooterInfoCacheUpdate() {
        refreshFooterInfoCache();
    }
    @CacheEvict(value = "footerInfo", allEntries = true)
    public void updateFooterInfo(FooterInfoDTO footerInfo) {
        FooterInfo entity = footerInfoRepository.findById(footerInfo.getFt_id()).orElseThrow(() -> new RuntimeException("FooterInfo not found"));
        // 기존 엔티티의 데이터를 업데이트
        entity.setFt_company(footerInfo.getFt_company());
        entity.setFt_ceo(footerInfo.getFt_ceo());
        entity.setFt_bo(footerInfo.getFt_bo());
        entity.setFt_mo(footerInfo.getFt_mo());
        entity.setFt_addr1(footerInfo.getFt_addr1());
        entity.setFt_addr2(footerInfo.getFt_addr2());
        entity.setFt_hp(footerInfo.getFt_hp());
        entity.setFt_time(footerInfo.getFt_time());
        entity.setFt_email(footerInfo.getFt_email());
        entity.setFt_troublehp(footerInfo.getFt_troublehp());
        entity.setFt_copyright(footerInfo.getFt_copyright());

        footerInfoRepository.save(entity);
    }


    public FooterInfoDTO getFooterInfoWithCacheCheck() {
        Cache cache = cacheManager.getCache("footerInfo");
        FooterInfoDTO cachedFooterInfo = cache != null ? cache.get(1, FooterInfoDTO.class) : null;

        if (cachedFooterInfo == null) {
            log.info("Cache miss - Fetching FooterInfo from the database");
            return getFooterInfo(); // @Cacheable 메서드 호출로 캐시 갱신
        } else {
            log.info("Cache hit - Returning cached FooterInfo");
            return cachedFooterInfo;
        }
    }

    @Cacheable(value = "footerInfo")
    public FooterInfoDTO  getFooterInfo() {
        FooterInfo footerInfo = footerInfoRepository.findById(1L)
                .orElse(null);
        log.info("Fetched FooterInfo with ID 1 from database");
        return footerInfo != null ? modelMapper.map(footerInfo, FooterInfoDTO.class) : null;
    }






}
