package com.lotteon.service.product;


import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.request.OptionCombinationRequestDTO;
import com.lotteon.entity.product.Option;
import com.lotteon.repository.product.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;


    public OptionDTO updateStock(OptionDTO optionDTO){
        Optional<Option> option = optionRepository.findById(optionDTO.getId());
        if(option.isPresent()){
            Option optionEntity = option.get();
            optionEntity.setStock(optionDTO.getOptionStock());
            optionRepository.save(optionEntity);
        }


        return optionDTO;
    }


    public List<String> generateCombinations(List<OptionCombinationRequestDTO.OptionGroupDTO> optionGroups) {
        List<String> combinations = new ArrayList<>();
        generateCombinationsRecursive(optionGroups, 0, "", combinations);
        return combinations;
    }

    private void generateCombinationsRecursive(
            List<OptionCombinationRequestDTO.OptionGroupDTO> optionGroups,
            int index,
            String currentCombination,
            List<String> combinations) {

        // 종료 조건: 모든 그룹을 순회한 경우 조합 추가
        if (index == optionGroups.size()) {
            combinations.add(currentCombination);
            return;
        }

        // 현재 그룹 가져오기
        OptionCombinationRequestDTO.OptionGroupDTO currentGroup = optionGroups.get(index);
        String groupName = currentGroup.getGroupName();

        // 현재 그룹의 모든 옵션 항목에 대해 재귀 호출
        for (OptionCombinationRequestDTO.OptionItemDTO item : currentGroup.getOptionItems()) {
            String optionName = item.getOptionName();
            String newCombination = currentCombination.isEmpty()
                    ? groupName + ": " + optionName
                    : currentCombination + " / " + groupName + ": " + optionName;

            generateCombinationsRecursive(optionGroups, index + 1, newCombination, combinations);
        }
    }

}
