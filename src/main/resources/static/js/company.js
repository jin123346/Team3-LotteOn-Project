// // 모달 관련 변수
// var modal = document.getElementById("myModal");
// var modalImg = document.getElementById("img01");
// var close = document.getElementsByClassName("close")[0];
//
// // 모든 이미지 요소에 클릭 이벤트 추가
// document.querySelectorAll(".modal-trigger").forEach(function(img) {
//   img.onclick = function() {
//     modal.style.display = "block";
//     modalImg.src = this.src; // 클릭된 이미지의 경로를 모달 이미지에 설정
//   }
// });
//
// // 닫기 버튼 클릭 시 모달 닫기
// close.onclick = function() {
//   modal.style.display = "none";
// }

document.addEventListener("DOMContentLoaded", function () {
  const target = document.querySelector('.mainbackgoroud');

  console.log(target);
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        target.classList.add('show');
      }
    });
  });

  observer.observe(target);
});