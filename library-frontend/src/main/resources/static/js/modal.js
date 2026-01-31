class MemberModal {
    constructor() {
        this.init();
    }

    init() {
        this.overlay = document.getElementById('memberPopupModal');
        this.content = document.getElementById('memberPopupContent');
    }

    async showModal(mode, data = null) {
        if (!this.overlay || !this.content) {
            console.error('모달 요소 없음');
            return;
        }

        this.overlay.style.display = 'flex';
        this.content.innerHTML = '로딩 중...';

        let url;
        if (mode === 'register') url = '/members/register-modal';
        else if (mode === 'login') url = '/members/login-modal';
        else if (mode === 'edit') url = `/members/edit-modal/${data}`;
        else if (mode === 'view') url = `/members/detail-modal/${data}`;
        else return;

        try {
            const response = await fetch(url, {
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            });
            this.content.innerHTML = await response.text();
        } catch (e) {
            this.content.innerHTML = '<div class="text-danger">로딩 실패</div>';
        }
    }

    close() {
        this.overlay.style.display = 'none';
    }
}

// 전역 객체로 사용
const modal = new MemberModal();
