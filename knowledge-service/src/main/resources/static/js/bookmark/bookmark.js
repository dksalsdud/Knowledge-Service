// Toast 메시지 자동 숨김
document.addEventListener('DOMContentLoaded', function() {
    const toast = document.getElementById('toast');
    if (toast) {
        setTimeout(() => {
            toast.style.animation = 'fadeOut 0.3s ease-out forwards';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
});

// 북마크 삭제 함수
function removeBookmark(button) {
    const bookmarkId = button.getAttribute('data-bookmark-id');
    const userId = button.getAttribute('data-user-id');
    const card = button.closest('.bookmark-card');
    
    if (!confirm('이 북마크를 삭제하시겠습니까?')) {
        return;
    }
    
    // 카드 페이드 아웃 애니메이션
    card.style.transition = 'all 0.3s ease-out';
    card.style.opacity = '0';
    card.style.transform = 'translateX(-20px)';
    
    // Form 생성 및 제출 (DELETE 요청 시뮬레이션)
    setTimeout(() => {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `/bookmarks/remove/${bookmarkId}?userId=${userId}`;
        
        // _method 파라미터 추가 (DELETE 메소드 시뮬레이션)
        const methodInput = document.createElement('input');
        methodInput.type = 'hidden';
        methodInput.name = '_method';
        methodInput.value = 'DELETE';
        form.appendChild(methodInput);
        
        // CSRF 토큰 추가 (Spring Security 사용 시)
        const csrfToken = document.querySelector('meta[name="_csrf"]');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
        if (csrfToken && csrfHeader) {
            const csrfInput = document.createElement('input');
            csrfInput.type = 'hidden';
            csrfInput.name = csrfHeader.getAttribute('content');
            csrfInput.value = csrfToken.getAttribute('content');
            form.appendChild(csrfInput);
        }
        
        document.body.appendChild(form);
        form.submit();
    }, 300);
}

// 북마크 토글 함수 (노트 상세 페이지용)
function toggleBookmark(userId, noteId) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/bookmarks/toggle?userId=${userId}&noteId=${noteId}`;
    
    // CSRF 토큰 추가
    const csrfToken = document.querySelector('meta[name="_csrf"]');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
    if (csrfToken && csrfHeader) {
        const csrfInput = document.createElement('input');
        csrfInput.type = 'hidden';
        csrfInput.name = csrfHeader.getAttribute('content');
        csrfInput.value = csrfToken.getAttribute('content');
        form.appendChild(csrfInput);
    }
    
    document.body.appendChild(form);
    form.submit();
}

// 검색 기능 (추후 확장 가능)
function searchBookmarks(query) {
    const cards = document.querySelectorAll('.bookmark-card');
    const lowerQuery = query.toLowerCase();
    
    cards.forEach(card => {
        const title = card.querySelector('.bookmark-title h3').textContent.toLowerCase();
        const preview = card.querySelector('.bookmark-preview').textContent.toLowerCase();
        
        if (title.includes(lowerQuery) || preview.includes(lowerQuery)) {
            card.style.display = 'flex';
        } else {
            card.style.display = 'none';
        }
    });
}

// fadeOut 애니메이션 정의
const style = document.createElement('style');
style.textContent = `
    @keyframes fadeOut {
        from {
            opacity: 1;
            transform: translateX(0);
        }
        to {
            opacity: 0;
            transform: translateX(400px);
        }
    }
`;
document.head.appendChild(style);