// 슬라이드 관련 부분
const slides = document.querySelectorAll('.slide');
const prevBtn = document.querySelector('.prev');
const nextBtn = document.querySelector('.next');
const dotsContainer = document.querySelector('.slider-pagination');


let currentSlide = 0;
const slidesPerPage = 1; // 한 페이지에 보여줄 슬라이드 개수
const totalPages = Math.ceil(slides.length / slidesPerPage); // 페이지 수 계산

// 슬라이드 수에 맞게 dot 동적으로 생성
for (let i = 0; i < totalPages; i++) {
    const dot = document.createElement('span');
    dot.classList.add('dot');

    if (i === 0) {
        dot.classList.add('active'); // 첫 번째 dot에 active 추가
    }

    dot.addEventListener('click', () => showSlide(i)); // 클릭 시 해당 페이지로 이동
    dotsContainer.appendChild(dot); // dot을 페이지네이션에 추가
}

const dots = document.querySelectorAll('.dot'); // 동적으로 생성된 dot들을 다시 가져옴

function showSlide(pageIndex) {
    if (pageIndex >= totalPages) {
        currentSlide = 0;  // 마지막 페이지 이후에는 첫 번째 페이지로 돌아옴
    } else if (pageIndex < 0) {
        currentSlide = totalPages - 1;  // 첫 페이지 이전에는 마지막 페이지로 돌아옴
    } else {
        currentSlide = pageIndex;
    }

    document.querySelector('.slider-wrapper').style.transform = `translateX(-${currentSlide * 100}%)`;

    // 활성화된 dot 갱신
    dots.forEach(dot => dot.classList.remove('active'));
    dots[currentSlide].classList.add('active');
}


// Event listeners for buttons
prevBtn.addEventListener('click', () => showSlide(currentSlide - 1));
nextBtn.addEventListener('click', () => showSlide(currentSlide + 1));

const autoSlideInterval = setInterval(() => {
    showSlide(currentSlide + 1);
}, 3000); // 3000ms = 3초

// Event listeners for pagination dots
dots.forEach((dot, index) => {
    dot.addEventListener('click', () => showSlide(index));
});

// Initialize first slide
showSlide(currentSlide);


const hitProducts = document.querySelectorAll('.hit-product-item');
const prevHitBtn = document.querySelector('.prev-hit');
const nextHitBtn = document.querySelector('.next-hit');
const hitWrapper = document.querySelector('.hit-products-wrapper');

let currentHitIndex = 0;
const maxHitsVisible = 4; // 한번에 보이는 상품 수
const maxHits = hitProducts.length - maxHitsVisible; // 전체 상품 수에서 보이는 상품 수를 뺀 값

function showHitSlide(index) {
    if (index > maxHits) {
        currentHitIndex = 0;
    } else if (index < 0) {
        currentHitIndex = maxHits;
    } else {
        currentHitIndex = index;
    }

    hitWrapper.style.transform = `translateX(-${currentHitIndex * (100 / maxHitsVisible)}%)`;
}

// Event listeners for buttons
prevHitBtn.addEventListener('click', () => showHitSlide(currentHitIndex - 1));
nextHitBtn.addEventListener('click', () => showHitSlide(currentHitIndex + 1));

