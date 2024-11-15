package com.lotteon.service;

import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.VersionDTO;
import com.lotteon.entity.Version;
import com.lotteon.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class VersionService {
    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;

    // Insert a new version and clear the cache to keep it updated
    @CacheEvict(value = "Version", allEntries = true)
    public void insertVersion(VersionDTO versionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Current logged-in username

        // Set the writer for the version
        versionDTO.setVer_writer(currentUsername);
        versionRepository.save(modelMapper.map(versionDTO, Version.class));
    }



    // Retrieve all versions
    public List<VersionDTO> getAllVersions() {
        return versionRepository.findAll().stream()
                .map(version -> {
                    VersionDTO dto = modelMapper.map(version, VersionDTO.class);
                    dto.setRdate(version.getRdate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Delete specified versions by ID
    public void deleteCheck(List<Integer> data) {
        for (Integer id : data) {
            versionRepository.deleteById(id);
        }
    }

    // Refresh the cache
    @CacheEvict(value = "Version", allEntries = true)
    public void refreshVersionCache() {
        log.info("Evicting and refreshing Version cache...");
        getLatestVersion();  // 캐시 강제 초기화
    }

    // Schedule the cache to update daily
    @Scheduled(cron = "0 39 2 * * *") // Run every day at 2:39 AM
    public void scheduledVersionCacheUpdate() {
        refreshVersionCache();
    }

    // Method to get the latest version from the database and cache it
    @Cacheable(value = "Version", key = "1")
    public VersionDTO getLatestVersion() {
        log.info("Attempting to fetch Version from cache or database.");
        Version version = versionRepository.findTopByOrderByVerIdDesc().orElse(null);
        if (version != null) {
            log.info("Fetched Version from database and caching it.");
            VersionDTO Version = modelMapper.map(version, VersionDTO.class);
            return Version;
        } else {
            log.info("No Version found in database.");
            return null;
        }
    }

    // Method to check cache first, and if empty, fetch from database
    public VersionDTO getVersionWithCacheCheck() {
        // Access the Version cache
        Cache cache = cacheManager.getCache("Version");

        // Safely retrieve the cached value (check for null and type)
        VersionDTO cachedVersion = null;
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(1); // Key "1" based on @Cacheable configuration
            if (wrapper != null) {
                Object cachedObject = wrapper.get();
                if (cachedObject instanceof VersionDTO) {
                    cachedVersion = (VersionDTO) cachedObject;
                }
            }
        }

        // If cache is empty, load the latest version from DB and cache it
        if (cachedVersion == null) {
            log.info("Cache miss - Fetching Version from the database");
            cachedVersion = getLatestVersion();
        } else {
            log.info("Cache hit - Returning cached VersionDTO");
        }

        return cachedVersion;
    }
}
