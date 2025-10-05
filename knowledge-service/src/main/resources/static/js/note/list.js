// 뷰 전환 함수
function toggleView(view) {
    const listView = document.getElementById('listView');
    const gridView = document.getElementById('gridView');
    const buttons = document.querySelectorAll('.view-btn');
    
    if (view === 'list') {
        listView.classList.remove('hidden');
        gridView.classList.add('hidden');
        buttons[0].classList.add('active');
        buttons[1].classList.remove('active');
        
        // 애니메이션 효과
        listView.style.opacity = '0';
        setTimeout(() => {
            listView.style.transition = 'opacity 0.3s ease';
            listView.style.opacity = '1';
        }, 50);
    } else {
        listView.classList.add('hidden');
        gridView.classList.remove('hidden');
        buttons[0].classList.remove('active');
        buttons[1].classList.add('active');
        
        // 애니메이션 효과
        gridView.style.opacity = '0';
        setTimeout(() => {
            gridView.style.transition = 'opacity 0.3s ease';
            gridView.style.opacity = '1';
        }, 50);
    }
}

// 필터 탭 기능
document.addEventListener('DOMContentLoaded', function() {
    const filterTabs = document.querySelectorAll('.filter-tab');
    
    filterTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            filterTabs.forEach(t => t.classList.remove('active'));
            this.classList.add('active');
            
            // 여기에 필터링 로직 추가 가능
        });
    });
});