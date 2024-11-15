// document.addEventListener('DOMContentLoaded', function () {
//     const links = document.querySelectorAll('#aside .aside_list ul li a');
//     const titleElement = document.querySelector('#service .title h2');
//     const descriptionElement = document.querySelector('#service .title p');



//     document.querySelectorAll('.selectOption').forEach(link => {
//         link.addEventListener('click', function (e) {
//             e.preventDefault();
//             const parentId = this.getAttribute('data-id');
//             console.log("정마요귀여웡:" + parentId);
//
//             fetch(`/admin/qna/list/page?parentId=${parentId}`)
//                 .then(resp => resp.json())
//                 .then(qnaPageResponseDTO => {
//                     console.log("마요는 맨날 야옹야옹하고 울어:", qnaPageResponseDTO);
//
//                     const qnaList = document.getElementById('qnaList');
//                     qnaList.innerHTML = '';
//
//                     qnaPageResponseDTO.qnadtoList.forEach(qna => {
//                         const li = document.createElement('li');
//                         li.className = 'first_li'; // 클래스 추가
//                         li.innerHTML = `
//                            <a href="/cs/qna/detail/${qna.qnaNo}">
//                             <span>[${qna.category.parent.name}]</span>
//                             <span>${qna.qnatitle}</span>
//                             <span class="review-status">${qna.qna_status === 'review' ? '검토중' : '답변완료'}</span>
//                             <span class="author-id">${qna.qnawriter}</span>
//                             <span class="date">${qna.rdate.substring(0, 10)}</span>
//                         </a>`;
//
//                         qnaList.appendChild(li);
//
//
//                     })
//                 .catch(err => {
//                   console.log(err);
//                 });
//         })
//     })
// })


    // // 첫 번째 링크에 기본적으로 active 클래스 추가 및 초기 제목, 설명 설정
    // if (links.length > 0) {
    //     links[0].classList.add('active');
    //     titleElement.textContent = links[0].textContent;
    //     descriptionElement.textContent = `${links[0].textContent} 관련 문의내용 입니다.`;
    // }
    //
    // // 현재 URL에서 cate 파라미터를 추출
    // const urlParams = new URLSearchParams(window.location.search);
    // const currentCate = urlParams.get('cate');
    //
    // // qnaList에 들어왔을 때 자동으로 cate=1로 리다이렉트
    // if (window.location.pathname.includes('/cs/qna/list') && currentCate === null) {
    //     window.location.href = 'http://127.0.0.1:8085/cs/qna/list?cate=1';
    //     return; // 이후 코드 실행 방지
    // }

    // 링크 클릭 이벤트 처리
    // links.forEach(link => {
    //     link.addEventListener('click', function () {
    //         // 모든 링크에서 active 클래스 제거
    //         links.forEach(item => item.classList.remove('active'));
    //         // 클릭한 링크에 active 클래스 추가
    //         this.classList.add('active');
    //
    //         // 클릭한 카테고리명으로 제목 및 설명 업데이트
    //         const selectedCategory = this.textContent;
    //
    //         // '1:1' 카테고리의 경우 '>' 제외 및 설명 업데이트
    //         if (selectedCategory.startsWith('1:1')) {
    //             titleElement.textContent = selectedCategory.replace('>', '').trim();
    //             descriptionElement.textContent = `오후 4시 이후 접수 건은 다음날 (평일) 오전 9시 이후 답변드리겠습니다. (단, 롯데마트 고객센터는 주말/공휴일에도 운영)`;
    //         } else {
    //             titleElement.textContent = selectedCategory;
    //             descriptionElement.textContent = `${selectedCategory} 관련 문의내용 입니다.`;
    //         }
    //     });
    // });

    // // 추가된 부분: first_choice 및 second_choice 동적 변경
    // const firstChoice = document.querySelector('.first_choice');
    // const secondChoice = document.querySelector('.second_choice');
    //
    // const options = {
    //     1: ["가입", "탈퇴"],
    //     2: ["쿠폰", "이벤트"],
    //     3: ["주문", "결제"],
    //     4: ["배송지연", "배송누락"],
    //     5: ["취소", "반품", "교환"],
    //     6: ["여행", "숙박", "항공"],
    //     7: ["해외거래", "국내거래"]
    // };
    //
    // // 첫 번째 선택박스를 "회원"으로 자동 설정
    // firstChoice.value = 1; // "회원"에 해당하는 value는 1입니다.
    //
    // // 첫 번째 선택박스의 값 변경 시 두 번째 선택박스 업데이트
    // const updateSecondChoice = () => {
    //     const selectedValue = firstChoice.value;
    //
    //     // second_choice 옵션 초기화
    //     secondChoice.innerHTML = '<option value="">선택하세요</option>';
    //
    //     if (options[selectedValue]) {
    //         options[selectedValue].forEach(function (item) {
    //             const option = document.createElement('option');
    //             option.value = item;
    //             option.textContent = item;
    //             secondChoice.appendChild(option);
    //         });
    //     }
    //
    //     // second_choice의 첫 번째 값을 기본 선택으로 설정
    //     if (secondChoice.options.length > 1) {
    //         secondChoice.value = secondChoice.options[0].value; // 첫 번째 옵션을 선택합니다.
    //     }
    // };
    //
    // // 초기화 및 자동 설정
    // updateSecondChoice(); // 페이지 로드 시 호출하여 두 번째 선택박스 업데이트
    //
    // firstChoice.addEventListener('change', updateSecondChoice); // 선택박스 변경 시 업데이트
// });