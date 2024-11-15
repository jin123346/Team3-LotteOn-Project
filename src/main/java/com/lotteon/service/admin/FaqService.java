package com.lotteon.service.admin;

import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.FaqDTO;
import com.lotteon.dto.page.FaqPageResponseDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.entity.BoardCate;
import com.lotteon.entity.Faq;
import com.lotteon.repository.BoardRepository;
import com.lotteon.repository.admin.FaqRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class FaqService {
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    public Faq insertfaq(FaqDTO faqDTO) {
        BoardCate selectedCate = boardRepository.findByBoardCateId(faqDTO.getCategoryid());
        BoardCateDTO selectedCated =  modelMapper.map(selectedCate, BoardCateDTO.class);

        faqDTO.setCategory(selectedCated);
        Faq faq = new Faq();
        faq.setCate(selectedCate);
        faq.setFaqtitle(faqDTO.getFaqtitle());
        faq.setFaqcontent(faqDTO.getFaqcontent());

        faqRepository.save(faq);

        return faq;
    }


    public Faq updatefaq(FaqDTO faqDTO) {
        Optional<Faq> faq = faqRepository.findById(faqDTO.getFaqNo());
        BoardCate selectedCate = boardRepository.findByBoardCateId(faqDTO.getCategoryid());
        BoardCateDTO selectedCated =  modelMapper.map(selectedCate, BoardCateDTO.class);

        faqDTO.setCategory(selectedCated);

        if (faq.isPresent()) {
            Faq faq1 = faq.get();
            faq1.setCate(selectedCate);
            faq1.setFaqtitle(faqDTO.getFaqtitle());
            faq1.setFaqcontent(faqDTO.getFaqcontent());
            return faqRepository.save(faq1);
        }
        return null;
    }


    public FaqDTO selectfaq(int no) {
        Optional<Faq> optfaq = faqRepository.findById(no); // int 타입 그대로 사용

        if (optfaq.isPresent()) {
            Faq faq = optfaq.get();
            BoardCate cate = faq.getCate();
            FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);

            if (cate != null) {
                faqDTO.setCategory(modelMapper.map(cate, BoardCateDTO.class));
            }

            return faqDTO;
        }

        return null; // 여기서 null 반환 시 호출하는 쪽에서 처리 필요
    }

    public void deleteCheck(List<Integer> data){
        for (Integer id : data) {
            faqRepository.deleteById(id);
        }
    }

    public void deletefaq(int no){
        Optional<Faq> optfaq = faqRepository.findById(no);
        if(optfaq.isPresent()){
            Faq faq = optfaq.get();
            faqRepository.delete(faq);
        }
    }

    public List<FaqDTO> selectAllfaq(){
        List<Faq> faqs = faqRepository.findAll();
        List<FaqDTO> faqDTOs = new ArrayList<>();
        for (Faq faq : faqs) {
            FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
            faqDTOs.add(faqDTO);
        }
        return faqDTOs;
    }


    public FaqPageResponseDTO selectfaqListAll(PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pagefaq = null;
        if(pageRequestDTO.getChildId() != null){
            pagefaq = faqRepository.selectFaqForOption2(pageRequestDTO, pageable);
        }else if(pageRequestDTO.getParentId() != null){
            pagefaq = faqRepository.selectFaqForOption1(pageRequestDTO, pageable);
        }else {
            pagefaq = faqRepository.selectFaqAllForList(pageRequestDTO, pageable);
        }
        List<FaqDTO> faqList = pagefaq.getContent().stream().map(tuple -> {
            Integer id = tuple.get(0, Integer.class); // Get the ID
            Faq faq = faqRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Faq not found with ID: " + id)); // Handle not found

            BoardCate cate = faq.getCate();
            if (cate == null) {
                throw new RuntimeException("Category not found for Faq with ID: " + id); // Handle null category
            }

            FaqDTO faqdto = modelMapper.map(faq, FaqDTO.class);
            // Ensure the mapping for the category is safe
            faqdto.setCategory(modelMapper.map(cate, BoardCateDTO.class));
            return faqdto;
        }).toList();
        log.info("여기확인해!!!!!!!!! : " + faqList);
        int total = (int) pagefaq.getTotalElements();

        return FaqPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .faqdtoList(faqList)
                .total(total)
                .build();

    }


    }



