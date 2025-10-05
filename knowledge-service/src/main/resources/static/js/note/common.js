// 스크롤 시 애니메이션 트리거
document.addEventListener("scroll", function() {
    const elements = document.querySelectorAll(".slide-up");
    elements.forEach(el => {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight * 0.85) {
            el.classList.add("fade-in");
        }
    });
});

// 페이지 로드 시 초기 애니메이션
document.addEventListener("DOMContentLoaded", function() {
    // Fade-in 요소들
    const fadeElements = document.querySelectorAll('.fade-in');
    fadeElements.forEach((el, index) => {
        setTimeout(() => {
            el.style.opacity = '1';
            el.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Slide-up 요소들 스크롤 체크
    const slideUpElements = document.querySelectorAll('.slide-up');
    slideUpElements.forEach(el => {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight * 0.85) {
            el.classList.add("fade-in");
        }
    });
});

// 부드러운 호버 효과를 위한 추가 함수
function addHoverEffect(selector) {
    const elements = document.querySelectorAll(selector);
    elements.forEach(el => {
        el.addEventListener('mouseenter', function() {
            this.style.transition = 'all 0.4s cubic-bezier(0.4, 0, 0.2, 1)';
        });
    });
}