document.addEventListener('DOMContentLoaded', function () {
    // Script for toggling the category menu
    // const viewAllButton = document.getElementById('viewAllButton');
    // const categoryMenu = document.querySelector('.categoryMenu');
    // const header = document.querySelector('.category.on');

    // viewAllButton.addEventListener('click', function () {
    //     // Toggle the visibility of the menu and the button bar transformation
    //     header.classList.toggle('menuVisible');
    // });

    // 서버에서 설정된 activeSubChildId와 로컬 저장소의 값을 비교
    // const activeId = document.querySelector('.cate_aside').getAttribute('data-active-id');
    // const storedActiveId = localStorage.getItem('activeSubChildId');
    //
    // if (storedActiveId && storedActiveId !== activeId) {
    //     const activeLink = document.querySelector(`.lnb-sub-menu a[href='/market/list/${storedActiveId}']`);
    //     if (activeLink) {
    //         activeLink.classList.add('cateactive');
    //         let parentItem = activeLink.closest('.lnb-item');
    //         while (parentItem) {
    //             parentItem.classList.add('open');
    //             const subMenu = parentItem.querySelector('.lnb-sub-menu');
    //             if (subMenu) {
    //                 subMenu.classList.remove('hidden');
    //             }
    //             parentItem = parentItem.parentElement.closest('.lnb-item');
    //         }
    //     }
    // }


    document.querySelectorAll('.shipping-fee').forEach(function(feeElement) {

        // shippingFee 값을 가져와 숫자로 변환
        const shippingFee = parseFloat(feeElement.innerText);
        console.log("shippingFee");

        // shippingFee가 0이면 무료배송으로 텍스트 변경 및 클래스 추가
        if (shippingFee === 0) {
            feeElement.innerText = "무료배송";
            feeElement.classList.add("shipping-label");
        }
    });


    // 모든 seller-rating 요소를 가져옵니다.
    document.querySelectorAll('.seller-rating').forEach(function(ratingElement) {
        // data-rating 속성에서 rating 값을 가져옵니다.
        const rating = parseFloat(ratingElement.getAttribute('data-rating'));
        const fullStars = Math.floor(rating);   // 꽉 찬 별 수
        const halfStar = rating % 1 !== 0;     // 반쪽 별 여부 확인

        // 꽉 찬 별 추가
        for (let i = 0; i < fullStars; i++) {
            const star = document.createElement('span');
            star.classList.add('star', 'full-star');
            star.innerHTML = '★';
            ratingElement.appendChild(star);
        }

        // 반쪽 별 추가
        if (halfStar) {
            const halfStar = document.createElement('span');
            halfStar.classList.add('star', 'half-star');
            halfStar.innerHTML = '★'; // 반쪽 별을 나타낼 경우, CSS를 추가해 스타일링 가능
            ratingElement.appendChild(halfStar);
        }

        // 5개 미만인 경우 빈 별 추가
        const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);
        for (let i = 0; i < emptyStars; i++) {
            const star = document.createElement('span');
            star.classList.add('star', 'empty-star');
            star.innerHTML = '&#9733 '; // 빈 별도 CSS를 통해 스타일링 가능
            ratingElement.appendChild(star);
        }
    });


    const viewAllButton = document.getElementById('viewAllButton');
    const categoryMenu = document.querySelector('.categoryMenu');
    const header = document.querySelector('.category.on');

    viewAllButton.addEventListener('click', function () {
        // Toggle the visibility of the menu with slide down/up animation
        header.classList.toggle('menuVisible');

        // If the menu is visible, expand the max-height to show content
        if (header.classList.contains('menuVisible')) {
            categoryMenu.style.maxHeight = categoryMenu.scrollHeight + 'px';
        } else {
            categoryMenu.style.maxHeight = '0'; // Collapse the menu
        }
    });


    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            document.querySelectorAll('h3 span').forEach(el => el.classList.remove('active'));
            this.parentElement.classList.add('active');
        });
    });

    const reviews = document.querySelectorAll('.p-review');
    const qnas = document.querySelectorAll('.p-qna');
    const moreqnaBtn = document.getElementById('moreQnaBtn');
    const moreReviewsBtn = document.getElementById('moreReviewsBtn');
    const pagination = document.getElementById('reviews-pagination');
    const pagination2 = document.getElementById('qna-pagination');
    let isExpanded = false;  // 더보기 상태를 저장하는 변수

    // 리뷰 더보기 버튼 클릭 시
    if (moreReviewsBtn != null) {
        moreReviewsBtn.addEventListener('click', function (e) {
            e.preventDefault(); // 기본 동작 방지
            if (!isExpanded) {
                // 더보기를 눌렀을 때: 3개 이상의 리뷰 표시
                isExpanded = true;
                reviews.forEach((review, index) => {
                    if (index >= 3) {
                        review.classList.add('show'); // 추가 리뷰 표시
                    }
                });
                moreReviewsBtn.textContent = '더보기 닫기'; // 버튼 텍스트 변경
                pagination.classList.add('show'); // 페이지네이션 표시
            } else {
                // 더보기 닫기를 눌렀을 때: 첫 3개의 리뷰만 표시
                isExpanded = false;
                reviews.forEach((review, index) => {
                    review.classList.remove('show'); // 숨김
                });
                moreReviewsBtn.textContent = '더보기'; // 버튼 텍스트 복구
                pagination.classList.remove('show'); // 페이지네이션 숨김
            }
        });
    }


    if(moreqnaBtn !=null){
        // qna 더보기 버튼 클릭 시
        moreqnaBtn.addEventListener('click', function (e) {
            e.preventDefault();
            if (!isExpanded) {
                // 더보기를 눌렀을 때: 3개 이상의 qna 표시 + 페이지네이션 활성화
                isExpanded = true;
                qnas.forEach((qna, index) => {
                    qna.classList.add('show');
                    pagination2.classList.add('show');
                });
                moreqnaBtn.textContent = '더보기 닫기';  // 더보기 버튼 텍스트 변경
            } else {
                // 더보기 닫기를 눌렀을 때: qna 숨김 + 페이지네이션 숨김
                isExpanded = false;
                qnas.forEach((qna, index) => {
                    qna.classList.remove('show');
                });
                pagination2.classList.remove('show');
                moreqnaBtn.textContent = '더보기';  // 더보기 버튼 텍스트 복구
            }
        });
    }



    // Image carousel functionality
    const reviewImages = document.getElementById('reviewImages');
    const leftArrow = document.getElementById('leftArrow');
    const rightArrow = document.getElementById('rightArrow');


    if(reviewImages !=null){

        let currentPosition = 0;
        const imageWidth = 150; // Width of each image (including margins)
        const visibleImages = 3; // Number of images to show at once
        const maxPosition = reviewImages.children.length - visibleImages; // Maximum scroll position

        function updateImagePosition() {
            reviewImages.style.transform = `translateX(${-currentPosition * imageWidth}px)`;
        }

        // Move left
        leftArrow.addEventListener('click', function () {
            if (currentPosition > 0) {
                currentPosition--;
                updateImagePosition();
            }
        });

        // Move right
        rightArrow.addEventListener('click', function () {
            if (currentPosition < maxPosition) {
                currentPosition++;
                updateImagePosition();
            }
        });

    }


    const lnbItems = document.querySelectorAll('.lnb-item > a');
    const subCategoryLinks = document.querySelectorAll('.lnb-sub-menu a');

// 로컬 스토리지에서 activeSubChildId와 activeParentChildId 가져오기
    const activeSubChildId = localStorage.getItem('activeSubChildId');
    const activeParentChildId = localStorage.getItem('activeParentChildId');

// activeSubChildId가 있을 경우 해당 링크를 활성화
    if (activeSubChildId) {
        const activeLink = document.querySelector(`.lnb-sub-menu a[href='/market/list/${activeSubChildId}']`);
        if (activeLink) {
            activeLink.classList.add('cateactive');
            let parentItem = activeLink.closest('.lnb-item');
            while (parentItem) {
                parentItem.classList.add('open');
                const subMenu = parentItem.querySelector('.lnb-sub-menu');
                if (subMenu) {
                    subMenu.classList.remove('hidden');
                }
                parentItem = parentItem.parentElement.closest('.lnb-item');
            }
        }
    }

// activeParentChildId가 있을 경우 상위 메뉴를 활성화
    if (activeParentChildId) {
        const parentLink = document.querySelector(`.lnb1 > a[href='/market/main/${activeParentChildId}']`);
        if (parentLink) {
            parentLink.classList.add('cateactive');
            const parentItem = parentLink.closest('.lnb1');
            parentItem.classList.add('open');
            const subMenu = parentItem.querySelector('.lnb-sub-menu');
            if (subMenu) {
                subMenu.classList.remove('hidden');
            }
        }
    }

// 메뉴 항목 클릭 시 상태 저장 및 토글
    lnbItems.forEach(function (lnbItem) {
        lnbItem.addEventListener('click', function (e) {
            e.preventDefault();

            // 모든 메뉴 항목의 open, active 초기화
            lnbItems.forEach(item => {
                const parent = item.parentElement;
                const subMenu = parent.querySelector('.lnb-sub-menu');
                parent.classList.remove('open');
                if (subMenu) {
                    subMenu.classList.add('hidden');
                }
                item.classList.remove('cateactive');
            });

            // 현재 항목 열기 및 active로 설정
            const parentItem = this.parentElement;
            const subMenu = parentItem.querySelector('.lnb-sub-menu');
            parentItem.classList.toggle('open');
            if (subMenu) {
                subMenu.classList.toggle('hidden');
            }

            // 상위 및 최상위 카테고리 클릭 시 activeSubChildId와 activeParentChildId 삭제
            localStorage.removeItem('activeSubChildId');
            localStorage.removeItem('activeParentChildId');

            // 클릭한 항목이 상위 카테고리인 경우만 activeParentChildId 저장
            const parentChildId = parentItem.querySelector('a').getAttribute('href').split('/').pop();
            localStorage.setItem('activeParentChildId', parentChildId);

            // 클릭한 항목에 cateactive 클래스 추가
            this.classList.add('cateactive');
        });
    });

// 서브카테고리 클릭 시 cateactive 상태 업데이트
    subCategoryLinks.forEach(function (link) {
        link.addEventListener('click', function () {
            subCategoryLinks.forEach(el => el.classList.remove('cateactive'));
            this.classList.add('cateactive');
            localStorage.setItem('activeSubChildId', this.getAttribute('href').split('/').pop());
        });
    });


    // const lnbItems = document.querySelectorAll('.lnb-item > a');
    // const subCategoryLinks = document.querySelectorAll('.lnb-sub-menu a');
    //
    // // 로컬 스토리지에서 activeSubChildId와 activeParentChildId 가져오기
    // const activeSubChildId = localStorage.getItem('activeSubChildId');
    // const activeParentChildId = localStorage.getItem('activeParentChildId');
    //
    // // activeSubChildId가 있을 경우 해당 링크를 활성화
    // if (activeSubChildId) {
    //     const activeLink = document.querySelector(`.lnb-sub-menu a[href='/market/list/${activeSubChildId}']`);
    //     if (activeLink) {
    //         activeLink.classList.add('cateactive');
    //         let parentItem = activeLink.closest('.lnb-item');
    //         while (parentItem) {
    //             parentItem.classList.add('open');
    //             const subMenu = parentItem.querySelector('.lnb-sub-menu');
    //             if (subMenu) {
    //                 subMenu.classList.remove('hidden');
    //             }
    //             parentItem = parentItem.parentElement.closest('.lnb-item');
    //         }
    //     }
    // }
    //
    // // activeParentChildId가 있을 경우 상위 메뉴를 활성화
    // if (activeParentChildId) {
    //     const parentLink = document.querySelector(`.lnb1 > a[href='/market/main/${activeParentChildId}']`);
    //     if (parentLink) {
    //         parentLink.classList.add('cateactive');
    //         const parentItem = parentLink.closest('.lnb1');
    //         parentItem.classList.add('open');
    //         const subMenu = parentItem.querySelector('.lnb-sub-menu');
    //         if (subMenu) {
    //             subMenu.classList.remove('hidden');
    //         }
    //     }
    // }
    //
    // // 메뉴 항목 클릭 시 상태 저장 및 토글
    // lnbItems.forEach(function (lnbItem) {
    //     lnbItem.addEventListener('click', function (e) {
    //         e.preventDefault();
    //
    //         // 모든 메뉴 항목의 open, active 초기화
    //         lnbItems.forEach(item => {
    //             const parent = item.parentElement;
    //             const subMenu = parent.querySelector('.lnb-sub-menu');
    //             parent.classList.remove('open');
    //             if (subMenu) {
    //                 subMenu.classList.add('hidden');
    //             }
    //             item.classList.remove('cateactive');
    //         });
    //
    //         // 현재 항목 열기 및 active로 설정
    //         const parentItem = this.parentElement;
    //         const subMenu = parentItem.querySelector('.lnb-sub-menu');
    //         parentItem.classList.toggle('open');
    //         if (subMenu) {
    //             subMenu.classList.toggle('hidden');
    //         }
    //
    //         // 클릭된 서브카테고리 및 부모 카테고리의 ID를 로컬 스토리지에 저장
    //         const subChildId = this.getAttribute('href').split('/').pop();
    //         const parentChildId = parentItem.querySelector('a').getAttribute('href').split('/').pop(); // 부모 카테고리의 ID 가져오기
    //
    //         localStorage.setItem('activeSubChildId', subChildId);
    //         localStorage.setItem('activeParentChildId', parentChildId);
    //
    //         // 클릭한 항목에 cateactive 클래스 추가
    //         this.classList.add('cateactive');
    //     });
    // });
    //
    // // 서브카테고리 클릭 시 cateactive 상태 업데이트
    // subCategoryLinks.forEach(function (link) {
    //     link.addEventListener('click', function () {
    //         subCategoryLinks.forEach(el => el.classList.remove('cateactive'));
    //         this.classList.add('cateactive');
    //         localStorage.setItem('activeSubChildId', this.getAttribute('href').split('/').pop());
    //     });
    // });


    // const lnbItems = document.querySelectorAll('.lnb-item > a');
    // const subCategoryLinks = document.querySelectorAll('.lnb-sub-menu a');
    //
    // // 로컬 스토리지에서 activeSubChildId를 가져와서 해당 항목 열기
    // const activeSubChildId = localStorage.getItem('activeSubChildId');
    // if (activeSubChildId) {
    //     const activeLink = document.querySelector(`.lnb-sub-menu a[href='/market/list/${activeSubChildId}']`);
    //     if (activeLink) {
    //         activeLink.classList.add('cateactive');
    //         let parentItem = activeLink.closest('.lnb-item');
    //         while (parentItem) {
    //             parentItem.classList.add('open');
    //             const subMenu = parentItem.querySelector('.lnb-sub-menu');
    //             if (subMenu) {
    //                 subMenu.classList.remove('hidden');
    //             }
    //             parentItem = parentItem.parentElement.closest('.lnb-item');
    //         }
    //     }
    // }
    //
    // // 메뉴 항목 클릭 시 상태 저장 및 토글
    // lnbItems.forEach(function (lnbItem) {
    //     lnbItem.addEventListener('click', function (e) {
    //         e.preventDefault();
    //         const href = e.currentTarget.getAttribute('href');
    //         if (href) {
    //             const subChildId = href.split('/').pop();
    //             localStorage.setItem('activeSubChildId', subChildId);
    //             console.log("Saved activeSubChildId:", subChildId); // 디버깅용
    //         } else {
    //             console.warn("No href attribute found on this element:", e.currentTarget);
    //         }
    //
    //         // 모든 메뉴 항목의 open, active 초기화
    //         lnbItems.forEach(item => {
    //             const parent = item.parentElement;
    //             const subMenu = parent.querySelector('.lnb-sub-menu');
    //             parent.classList.remove('open');
    //             if (subMenu) {
    //                 subMenu.classList.add('hidden');
    //             }
    //             item.classList.remove('active');
    //         });
    //
    //         // 현재 항목 열기 및 active로 설정
    //         const parentItem = this.parentElement;
    //         const subMenu = parentItem.querySelector('.lnb-sub-menu');
    //         parentItem.classList.toggle('open');
    //         if (subMenu) {
    //             subMenu.classList.toggle('hidden');
    //         }
    //
    //         // 클릭된 서브카테고리의 ID를 로컬 스토리지에 저장
    //         const subChildId = this.getAttribute('href').split('/').pop();
    //         localStorage.setItem('activeSubChildId', subChildId);
    //
    //         // 클릭한 항목에 cateactive 클래스 추가
    //         this.classList.add('cateactive');
    //     });
    // });
    //
    // // 서브카테고리 클릭 시 cateactive 상태 업데이트
    // subCategoryLinks.forEach(function (link) {
    //     link.addEventListener('click', function () {
    //         subCategoryLinks.forEach(el => el.classList.remove('cateactive'));
    //         this.classList.add('cateactive');
    //         localStorage.setItem('activeSubChildId', this.getAttribute('href').split('/').pop());
    //     });
    // });
    //
    // // Submenu functionality for lnb-items
    // const lnbItems = document.querySelectorAll('.lnb-item > a');
    //
    // lnbItems.forEach(function (lnbItem) {
    //     lnbItem.addEventListener('click', function (e) {
    //         e.preventDefault(); // Prevent default anchor behavior
    //
    //         // Remove 'open' and 'active' from all lnb-items
    //         lnbItems.forEach(item => {
    //             const parent = item.parentElement;
    //             const subMenu = parent.querySelector('.lnb-sub-menu');
    //
    //             // Close the sub-menu of any other lnb-items
    //             if (parent !== this.parentElement) {
    //                 parent.classList.remove('open');
    //                 if (subMenu) {
    //                     subMenu.classList.add('hidden');
    //                 }
    //             }
    //
    //             // Remove active class from other links
    //             item.classList.remove('active');
    //
    //             // Remove active from all li inside sub-menus
    //             if (subMenu) {
    //                 subMenu.querySelectorAll('li').forEach(li => {
    //                     li.classList.remove('active');
    //                 });
    //             }
    //         });
    //
    //         // Toggle the current item
    //         const parentItem = this.parentElement;
    //         const subMenu = parentItem.querySelector('.lnb-sub-menu');
    //         parentItem.classList.toggle('open');
    //         if (subMenu) {
    //             subMenu.classList.toggle('hidden');
    //
    //             // Add 'active' class to the clicked sub-menu's li items (if desired)
    //             subMenu.querySelectorAll('li').forEach(li => {
    //                 li.addEventListener('click', function () {
    //                     // Remove 'active' from all other li in the sub-menu
    //                     subMenu.querySelectorAll('li').forEach(el => el.classList.remove('active'));
    //                     // Add 'active' to the clicked li
    //                     this.classList.add('active');
    //                 });
    //             });
    //         }
    //         // Add active class to the clicked link
    //         this.classList.add('active');
    //     });
    // });



   /* const modifyButtons = document.querySelectorAll('.countModifyBtn');
    const cancelButtons = document.querySelectorAll('.cancel-btn');
    const applyButtons = document.querySelectorAll('.apply-btn');
    const currentValue=null;
    if(modifyButtons != null){
        // 수정하기 버튼 클릭 시
        modifyButtons.forEach((button, index) => {
            button.addEventListener('click', function (e) {
                e.preventDefault();

                // 해당 행의 수정하기 관련 요소들 표시
                const row = this.closest('tr');
                const inputField = row.querySelector('input[name="quantity"]');
                const numberControls = row.querySelector('.number-controls');
                const editButtons = row.querySelector('.edit-buttons');
                console.log(button);

                // 수정 모드로 전환
                inputField.style.display = 'none';
                numberControls.style.display = 'flex';
                editButtons.style.display = 'flex';
                button.classList.add('hidden');

                // 기존 값 반영
                currentValue = inputField.value;
                row.querySelector('.quantity-input').value = currentValue;
            });
        });

        // 취소 버튼 클릭 시
        cancelButtons.forEach((button) => {
            button.addEventListener('click', function () {
                const row = this.closest('tr');
                const inputField = row.querySelector('input[name="quantity"]');
                const numberControls = row.querySelector('.number-controls');
                const editButtons = row.querySelector('.edit-buttons');

                // 수정 모드에서 빠져나오기
                inputField.style.display = 'block';
                numberControls.style.display = 'none';
                editButtons.style.display = 'none';
                 // hidden 클래스 제거
                const modifyButton = row.querySelector('.countModifyBtn');
                modifyButton.classList.remove('hidden');

            });
        });

        // 변경하기 버튼 클릭 시
        applyButtons.forEach((button) => {
            button.addEventListener('click', function () {
                const row = this.closest('tr');
                const inputField = row.querySelector('input[name="quantity"]');
                const numberControls = row.querySelector('.number-controls');
                const editButtons = row.querySelector('.edit-buttons');

                // 새로운 값 적용
                const newValue = row.querySelector('.quantity-input').value;
                inputField.value = newValue;


                 // Fetch를 이용해 서버로 POST 요청
                // fetch('/updateQuantity', {
                //     method: 'POST',
                //     headers: {
                //         'Content-Type': 'application/json'
                //     },
                //     body: JSON.stringify({
                //         productId: productId,
                //         quantity: updatedQuantity
                //     })
                // })
                // .then(response => response.json())
                // .then(data => {
                //     if (data.success) {
                //         // 성공 시 처리할 로직
                //         console.log('수량이 성공적으로 업데이트되었습니다.');
                //     } else {
                //         // 실패 시 처리할 로직
                //         console.error('수량 업데이트에 실패했습니다.');
                //     }
                // })
                // .catch(error => {
                //     console.error('에러 발생:', error);
                // });

                // 수정 모드에서 빠져나오기
                inputField.style.display = 'block';
                numberControls.style.display = 'none';
                editButtons.style.display = 'none';
                 // hidden 클래스 제거
                const modifyButton = row.querySelector('.countModifyBtn');
                modifyButton.classList.remove('hidden');

            });
        });

        // + - 버튼 클릭 시 숫자 조정
        document.querySelectorAll('.qnt-decrease').forEach((button) => {
            button.addEventListener('click', function () {
                const inputField = this.nextElementSibling;
                let currentValue = parseInt(inputField.value);
                if (currentValue > 1) {
                    inputField.value = currentValue - 1;
                }
            });
        });

        document.querySelectorAll('.qnt-increase').forEach((button) => {
            button.addEventListener('click', function () {
                const inputField = this.previousElementSibling;
                let currentValue = parseInt(inputField.value);
                inputField.value = currentValue + 1;
            });
        });
    }

*/




});
