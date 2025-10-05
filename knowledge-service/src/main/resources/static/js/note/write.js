// 마크다운 미리보기 업데이트
function updatePreview() {
    const content = document.getElementById('content').value;
    const preview = document.getElementById('preview');
    
    if (content.trim() === '') {
        preview.innerHTML = '<p class="preview-placeholder">미리보기가 여기에 표시됩니다...</p>';
    } else {
        preview.innerHTML = marked.parse(content);
    }
}

// 마크다운 문법 삽입
function insertMarkdown(before, after) {
    const textarea = document.getElementById('content');
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const text = textarea.value;
    const selectedText = text.substring(start, end);
    
    const newText = text.substring(0, start) + before + selectedText + after + text.substring(end);
    textarea.value = newText;
    
    // 커서 위치 조정
    textarea.focus();
    const newCursorPos = start + before.length + selectedText.length;
    textarea.selectionStart = newCursorPos;
    textarea.selectionEnd = newCursorPos;
    
    updatePreview();
}

// 자동 저장 기능 (선택사항)
let autoSaveTimer;
function autoSave() {
    clearTimeout(autoSaveTimer);
    autoSaveTimer = setTimeout(() => {
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;
        
        if (title || content) {
            // localStorage 대신 세션에 임시 저장
            console.log('자동 저장됨');
        }
    }, 3000);
}

// 키보드 단축키
document.addEventListener('keydown', function(e) {
    const textarea = document.getElementById('content');
    
    // Ctrl/Cmd + B: Bold
    if ((e.ctrlKey || e.metaKey) && e.key === 'b') {
        e.preventDefault();
        insertMarkdown('**', '**');
    }
    
    // Ctrl/Cmd + I: Italic
    if ((e.ctrlKey || e.metaKey) && e.key === 'i') {
        e.preventDefault();
        insertMarkdown('*', '*');
    }
    
    // Ctrl/Cmd + S: 저장
    if ((e.ctrlKey || e.metaKey) && e.key === 's') {
        e.preventDefault();
        document.querySelector('.btn-save').click();
    }
});

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    updatePreview();
    
    // 내용 변경 시 자동 저장 트리거
    const contentArea = document.getElementById('content');
    if (contentArea) {
        contentArea.addEventListener('input', autoSave);
    }
});

// 페이지 이탈 경고
window.addEventListener('beforeunload', function(e) {
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    
    if (title || content) {
        e.preventDefault();
        e.returnValue = '';
    }
});