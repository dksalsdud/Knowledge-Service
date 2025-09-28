// 스크롤 시 애니메이션 트리거
document.addEventListener("scroll", function() {
    const elements = document.querySelectorAll(".slide-up");
    elements.forEach(el => {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight * 0.8) {
            el.classList.add("fade-in");
        }
    });
});

// 페이지 로드 시 초기 애니메이션
document.addEventListener("DOMContentLoaded", function() {
    // Hero 섹션 애니메이션
    const heroContent = document.querySelector('.hero-content');
    if (heroContent) {
        heroContent.classList.add('fade-in');
    }
    
    // Features 섹션 스크롤 체크
    const features = document.querySelector('.features');
    if (features) {
        const rect = features.getBoundingClientRect();
        if (rect.top < window.innerHeight * 0.8) {
            features.classList.add("fade-in");
        }
    }
});