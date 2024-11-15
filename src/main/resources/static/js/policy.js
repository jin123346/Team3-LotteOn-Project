// 현재 URL 경로를 가져옴
const currentPage = window.location.pathname;

// 페이지에 따라 body 클래스 추가
if (currentPage.includes('buyer')) {
    document.body.classList.add('buyer-page');
} else if (currentPage.includes('seller')) {
    document.body.classList.add('seller-page');
} else if (currentPage.includes('finance')) {
    document.body.classList.add('finance-page');
} else if (currentPage.includes('location')) {
    document.body.classList.add('location-page');
} else if (currentPage.includes('privacy')) {
    document.body.classList.add('privacy-page');
}