package com.lotteon.service.admin;

import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.QnaDTO;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.dto.page.PageRequestDTO;
import com.lotteon.dto.page.QnaPageResponseDTO;
import com.lotteon.entity.BoardCate;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Adminqna;
import com.lotteon.entity.admin.QAdminqna;
import com.lotteon.entity.product.Option;
import com.lotteon.repository.BoardRepository;
import com.lotteon.repository.QnaRepository;
import com.lotteon.repository.admin.AdminQnaRepository;
import com.lotteon.repository.user.SellerRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class QnaService {

    private final AdminQnaRepository adminQnaRepository;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final QnaRepository qnaRepository;
    private final SellerRepository sellerRepository;


    public List<adminQnaDTO> selectQnaListBySeller(PageRequestDTO pageRequestDTO,String uid){
        Pageable pageable = PageRequest.of(0,5,Sort.by("date").descending());

        Optional<Seller> seller = sellerRepository.findByUserUid(uid);
        int sellerNo = 0;
        if(seller.isPresent()){
            sellerNo = Math.toIntExact(seller.get().getId());
        }
        Page<Tuple> page = adminQnaRepository.selectAdminqnBySellerForList(pageRequestDTO, pageable, sellerNo);

        List<adminQnaDTO>  adminQnaDTOS = page.stream().map(a -> modelMapper.map(a, adminQnaDTO.class)).collect(Collectors.toList());
        return adminQnaDTOS;

    }

    public QnaPageResponseDTO selectQnaListAll(PageRequestDTO pageRequestDTO){

    Pageable pageable = pageRequestDTO.getPageable("no");

    Page<Tuple> pageqna = null;
    if(pageRequestDTO.getChildId() != null){
        pageqna = adminQnaRepository.selectAdminqnaForOption2(pageRequestDTO, pageable);
    }else if(pageRequestDTO.getParentId() != null){
        pageqna = adminQnaRepository.selectAdminqnaForOption1(pageRequestDTO, pageable);
    }else if(pageRequestDTO.getQnawriter() != null){
        pageqna = adminQnaRepository.selectAdminqnaForQnaWriter(pageRequestDTO, pageable);
    } else{
        pageqna = adminQnaRepository.selectAdminqnaAllForList(pageRequestDTO, pageable);
    }

    List<adminQnaDTO> qnaList = pageqna.getContent().stream().map(tuple -> {
        Integer id = tuple.get(0, Integer.class); // Get the ID
        Adminqna qna = adminQnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faq not found with ID: " + id)); // Handle not found

        BoardCate cate = qna.getCate();
        if (cate == null) {
            throw new RuntimeException("Category not found for Faq with ID: " + id); // Handle null category
        }

        adminQnaDTO qnaDTO = modelMapper.map(qna, adminQnaDTO.class);
        // Ensure the mapping for the category is safe
        qnaDTO.setCategory(modelMapper.map(cate, BoardCateDTO.class));
        return qnaDTO;
    }).toList();
    log.info("여기확인해!!!!!!!!! : " + qnaList);
    int total = (int) pageqna.getTotalElements();

    return QnaPageResponseDTO.builder()
            .pageRequestDTO(pageRequestDTO)
            .qnadtoList(qnaList)
            .total(total)
            .build();

    }
    public void deleteCheck(List<Integer> data){
        for(Integer id : data){
            adminQnaRepository.deleteById(id);
        }
    }

    public adminQnaDTO selectQna(int no){
        Optional<Adminqna> optqna = adminQnaRepository.findById(no);

        if(optqna.isPresent()){
            Adminqna qna = optqna.get();
            BoardCate cate = qna.getCate();
            adminQnaDTO adminqnaDTO = modelMapper.map(qna, adminQnaDTO.class);
            if(cate != null){
                adminqnaDTO.setCategory(modelMapper.map(cate, BoardCateDTO.class));
            }
            return adminqnaDTO;

        }
        return null;
    }
    public Adminqna replyQna(int no, adminQnaDTO qnadto){
        Optional<Adminqna> optqna = adminQnaRepository.findById(no);
        if(optqna.isPresent()){
            Adminqna qna = optqna.get();
            qna.setQnareply(qnadto.getQnareply());
            qna.setQna_status(Adminqna.Status.completed);
            return adminQnaRepository.save(qna);
        }
        return null;
    }

    // productId에 따른 Q&A 목록 조회
    public List<adminQnaDTO> getQnaByProductId(long productId) {

        Pageable pageable = PageRequest.of(0,10,Sort.by("date").descending());
        Page<Adminqna> qnaList = adminQnaRepository.findByProductId(productId,pageable);

        // QnA 엔티티를 QnaDTO로 변환하여 반환
        return qnaList.stream()
                .map(adminQna -> modelMapper.map(adminQna, adminQnaDTO.class))
                .collect(Collectors.toList());
    }

    public List<adminQnaDTO> getQnaByWriterAndProductId(long productId) {
        Pageable pageable = PageRequest.of(0,10,Sort.by("date").descending());

        Page<Adminqna> adminqnaList = adminQnaRepository.findByProductId(productId,pageable);

            List<adminQnaDTO> adminQnaDTOS = adminqnaList.stream().map(adminqna -> modelMapper.map(adminqna, adminQnaDTO.class)).collect(Collectors.toList());
            return adminQnaDTOS;

    }

    public void deleteqna (int no){
        Optional<Adminqna> optqna = adminQnaRepository.findById(no);
        if(optqna.isPresent()){
            Adminqna qna = optqna.get();
            adminQnaRepository.delete(qna);
        }
    }

}
