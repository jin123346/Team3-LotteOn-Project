// 기본 3개출력, 더보기 버튼을 눌렀을 때 최대 10개까지 출력되도록
window.onload = function () {
    function AllShowMore(showClass, liClass, TextClass) {
        const showMore = document.querySelector(showClass + ' .showMore');
        const liClasses = document.querySelectorAll(liClass);
        let showing = false;

        // index 3 이상은 모두 hidden 처리
        liClasses.forEach((liClass, index) => {
            if (index >= 3) {
                liClass.classList.add("hidden");
            }
        });

        // 3 이상 10 이하
        showMore.addEventListener('click', function (e) {
            e.preventDefault();
            showing = !showing; // showing이 true로 보여지고 있는 상태 (클릭이 되었다는 의미)

            liClasses.forEach((liClass, index) => {
                if (index >= 3 && index < 10) {
                    if (!showing) {
                        liClass.classList.add("hidden");
                    } else {
                        liClass.classList.remove("hidden");
                    }
                }
            });

            showMore.textContent = showing ? "간단히 보기" : TextClass;
        });
    }

    AllShowMore('.first_show', '.first_li', "더보기");
    AllShowMore('.second_show', '.second_li', "더보기");
    AllShowMore('.third_show', '.third_li', "더보기");

}
// // 체크박스 전체 선택 기능
// const selectAllCheckbox = document.getElementById('select-all');
// const checkboxes = document.querySelectorAll('.checkbox-item');
//
// selectAllCheckbox.addEventListener('change', function () {
//     checkboxes.forEach(checkbox => {
//         checkbox.checked = selectAllCheckbox.checked;
//     });
// });

// document.addEventListener('DOMContentLoaded', function () {
//     const links = document.querySelectorAll('#aside .aside_list ul li a');
//     const titleElement = document.querySelector('#service .title h2');
//     const descriptionElement = document.querySelector('#service .title p');
//     const thirdContainer = document.querySelector('.third.container'); // 세 번째 컨테이너
//
//     // 각 카테고리에 대한 하위 카테고리 정의
//     const categoryMap = {
//         '회원': ['가입', '탈퇴'],
//         '쿠폰/이벤트': ['쿠폰', '이벤트'],
//         '주문/결제': ['주문', '결제'],
//         '배송': ['배송지연', '배송누락'],
//         '취소/반품/교환': ['취소', '반품', '교환'],
//         '여행/숙박/항공': ['여행', '숙박', '항공'],
//         '안전거래': ['해외거래', '국내거래']
//     };
//
//     // 첫 번째 링크에 기본적으로 active 클래스 추가
//     if (links.length > 0) {
//         links[0].classList.add('active');
//         titleElement.textContent = links[0].textContent; // 초기 제목 설정
//         descriptionElement.textContent = links[0].textContent + ' 관련 자주 묻는 질문입니다'; // 초기 설명 설정
//
//         // 첫 번째 카테고리에 대한 하위 카테고리 업데이트
//         const subCategories = categoryMap[links[0].textContent]; // 첫 번째 카테고리의 하위 카테고리
//         if (subCategories.length > 0) {
//             document.querySelector('.first.container h2').textContent = subCategories[0]; // 첫 번째 하위 카테고리
//         }
//         if (subCategories.length > 1) {
//             document.querySelector('.second.container h2').textContent = subCategories[1]; // 두 번째 하위 카테고리
//         } else {
//             document.querySelector('.second.container h2').textContent = ''; // 두 번째 카테고리가 없을 경우 비워둠
//         }
//     }
//
//     // 링크 클릭 이벤트 처리
//     links.forEach(link => {
//         link.addEventListener('click', function (e) {
//             e.preventDefault(); // 기본 링크 클릭 동작 방지
//
//             // 모든 링크에서 active 클래스 제거
//             links.forEach(item => item.classList.remove('active'));
//             // 클릭한 링크에 active 클래스 추가
//             this.classList.add('active');
//
//             const selectedCategory = this.textContent; // 클릭한 링크의 텍스트
//             const subCategories = categoryMap[selectedCategory]; // 해당 카테고리의 하위 카테고리 가져오기
//
//             // 제목 및 설명 업데이트
//             titleElement.textContent = selectedCategory; // 상위 카테고리 제목
//             descriptionElement.textContent = selectedCategory + ' 관련 자주 묻는 질문입니다'; // 설명 업데이트
//
//             // 하위 카테고리 업데이트
//             const firstContainer = document.querySelector('.first.container h2');
//             const secondContainer = document.querySelector('.second.container h2');
//             const thirdContainer = document.querySelector('.third.container h2');
//
//             // 첫 번째 하위 카테고리 제목 업데이트
//             if (subCategories.length > 0) {
//                 firstContainer.textContent = subCategories[0]; // 첫 번째 하위 카테고리
//             }
//             // 두 번째 하위 카테고리 제목 업데이트
//             if (subCategories.length > 1) {
//                 secondContainer.textContent = subCategories[1]; // 두 번째 하위 카테고리
//             } else {
//                 secondContainer.textContent = ''; // 두 번째 카테고리가 없을 경우 비워둠
//             }
//
//             // 세 번째 카테고리 제목 업데이트 및 display 처리
//             if (selectedCategory === '취소/반품/교환') {
//                 thirdContainer.textContent = '교환'; // 제목 업데이트
//                 document.querySelector('.third.container').style.display = 'block'; // 표시
//             } else if (selectedCategory === '여행/숙박/항공') {
//                 thirdContainer.textContent = '항공'; // 제목 업데이트
//                 document.querySelector('.third.container').style.display = 'block'; // 표시
//             } else {
//                 thirdContainer.textContent = ''; // 제목 비우기
//                 document.querySelector('.third.container').style.display = 'none'; // 숨김
//             }
//         });
//     });
// });
