document.querySelectorAll('.admin_menu > li').forEach(menu => {
    const submenu = menu.querySelector('.admin_sub_menu');

    menu.addEventListener('click', function(e) {
        e.stopPropagation();


        if(submenu) {
            submenu.classList.toggle('open');

        }
    });

    // 서브 메뉴 항목 클릭 이벤트
    if(submenu){
        submenu.querySelectorAll('li').forEach(subLi => {
            subLi.addEventListener('click', function(e){
                e.stopPropagation();
            })
        })
    }


});
document.getElementById('toggle-aside').addEventListener('click', function(e) {
    e.preventDefault(); // 기본 링크 동작 방지

    const aside = document.getElementById('admin_aside');
    const main = document.getElementById('admin_service');
    const gate = document.getElementById('admin_gate'); // admin_gate 추가

    // aside가 숨겨져 있는지 여부에 따라 클래스 조정
    if (aside.classList.contains('hidden')) {
        aside.classList.remove('hidden'); // aside 보이기
        main.classList.remove('expanded'); // main 원래 너비로
        gate.classList.remove('expanded'); // gate도 원래 너비로
    } else {
        aside.classList.add('hidden'); // aside 숨기기
        main.classList.add('expanded'); // main 너비 늘리기
        gate.classList.add('expanded'); // gate도 너비 늘리기
    }
});