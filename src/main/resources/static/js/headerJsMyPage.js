document.addEventListener('DOMContentLoaded', function() {
    const viewAllButton = document.getElementById('viewAllButton');
    const categoryMenu = document.querySelector('.categoryMenu');

    viewAllButton.addEventListener('click', function() {
        categoryMenu.classList.toggle('expanded');  // expanded 클래스 토글
    });

    // 카테고리 메뉴가 표시되면 display 속성을 'block'으로 설정
    const categoryMenuContainer = document.querySelector('.categoryMenu');
    if (categoryMenuContainer) {
        categoryMenuContainer.classList.toggle('show');
    }
});

// document.addEventListener('DOMContentLoaded', function () {
//     // Script for toggling the category menu
//     const viewAllButton = document.getElementById('viewAllButton');
//     const categoryMenu = document.querySelector('.categoryMenu');
//     // const header = document.querySelector('.category.on');
//
//
//
//     // 재귀적으로 카테고리 계층 구조를 DOM에 추가하는 함수
//     function buildCategoryTree(container, categories) {
//         categories.forEach(category => {
//             const li = document.createElement('li');
//             li.textContent = category.name;
//
//             // 하위 카테고리가 있을 경우, 하위 목록을 생성하고 추가
//             if (category.children && category.children.length > 0) {
//                 const subUl = document.createElement('ul');
//                 buildCategoryTree(subUl, category.children);
//                 li.appendChild(subUl);
//             }
//
//             container.appendChild(li);
//         });
//     }
//
//
//
//
//     viewAllButton.addEventListener('click', function (event) {
//         console.log("왜 안되노");
//         if (event.target.tagName === 'LI' && event.target.querySelector('ul')) {
//             event.target.classList.toggle('expanded');
//         }
//     });
//
// });
//
//
// // 하위 메뉴 보이기 함수
// function showSubmenu(element) {
//     const submenu = element.querySelector('ul');
//     console.log('submenu : '+submenu);
//     if (submenu) {
//         submenu.style.display = 'block';
//         adjustHeightAndPosition();
//     }
// }
//
// // 하위 메뉴 숨기기 함수
// function hideSubmenu(element) {
//     const submenu = element.querySelector('ul');
//     if (submenu) submenu.style.display = 'none';
// }
//
// // 1depth의 총 높이에 맞춰 2depth, 3depth 높이 및 위치 설정 함수
// function adjustHeightAndPosition() {
//     const menu1depth = document.getElementById('menu-1depth');
//     const menu2depths = document.querySelectorAll('.menu-2depth');
//     const menu3depths = document.querySelectorAll('.menu-3depth');
//
//     // 1depth의 전체 높이를 가져와서 하위 메뉴에 적용
//     const height = menu1depth.offsetHeight;
//
//     // 2depth와 3depth의 높이를 1depth의 높이에 맞춤
//     menu2depths.forEach(menu2 => {
//         menu2.style.height = `${height}px`;
//     });
//
//     menu3depths.forEach(menu3 => {
//         const parentMenu2 = menu3.closest('.menu-2depth');
//         const firstMenu2Item = parentMenu2.querySelector('li');
//
//         // 2depth의 첫 번째 항목과 동일한 위치에 3depth 배치
//         if (firstMenu2Item) {
//             const offsetTop = firstMenu2Item.offsetTop;
//             menu3.style.top = `${offsetTop}px`;
//         }
//         menu3.style.height = `${height}px`;
//     });
// }