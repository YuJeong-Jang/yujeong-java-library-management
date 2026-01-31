// 회원 관리 JavaScript 함수들

// 회원 모달 열기
window.openMemberModal = function () {
    const modal = document.getElementById( 'memberModal' );
    if ( modal ) {
        modal.classList.remove( 'hidden' );
        modal.classList.add( 'flex' );
    }
};

// 회원 모달 닫기
window.closeMemberModal = function () {
    const modal = document.getElementById( 'memberModal' );
    const form = document.getElementById( 'memberForm' );
    if ( modal ) {
        modal.classList.add( 'hidden' );
        modal.classList.remove( 'flex' );
    }
    if ( form ) {
        form.reset();
        // 아이디 필드 읽기 전용 해제
        document.getElementById( 'loginId' ).readOnly = false;
        document.getElementById( 'password' ).placeholder = '';
    }

    // 모달 제목과 버튼 초기화
    document.querySelector( '#memberModal h2' ).innerHTML = '<i class="fas fa-user-plus mr-2 text-green-600"></i>회원 등록';
    const submitBtn = document.querySelector( '#memberModal button[onclick*="Form"]' );
    if ( submitBtn ) {
        submitBtn.innerHTML = '<i class="fas fa-check mr-1"></i>등록';
        submitBtn.setAttribute( 'onclick', 'submitMemberForm()' );
    }
};

// 회원 등록
window.submitMemberForm = function () {
    const form = document.getElementById( 'memberForm' );
    if ( form ) {
        const formData = new FormData( form );
        const data = Object.fromEntries( formData );

        // API 호출
        fetch( '/members', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify( data )
        } )
            .then( response => response.json() )
            .then( result => {
                if ( result.success ) {
                    alert( '회원이 성공적으로 등록되었습니다.' );
                    closeMemberModal();
                    location.reload();
                } else {
                    alert( '회원 등록에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                }
            } )
            .catch( error => {
                console.error( 'Error:', error );
                alert( '회원 등록 중 오류가 발생했습니다.' );
            } );
    }
};

// 회원 수정
window.editMember = function ( memberId ) {
    // 회원 정보 조회 후 모달에 채우기
    fetch( '/members/api/' + memberId )
        .then( response => response.json() )
        .then( result => {
            if ( result.success ) {
                const member = result.data;

                // 폼 필드에 기존 데이터 채우기
                document.getElementById( 'loginId' ).value = member.loginId || '';
                document.getElementById( 'loginId' ).readOnly = true; // 아이디는 수정 불가
                document.getElementById( 'password' ).value = ''; // 비밀번호는 비워둠
                document.getElementById( 'password' ).placeholder = '변경하지 않으려면 비워두세요';
                document.getElementById( 'name' ).value = member.name || '';
                document.getElementById( 'email' ).value = member.email || '';
                document.getElementById( 'phone' ).value = member.phone || '';

                // 모달 제목 변경
                document.querySelector( '#memberModal h2' ).innerHTML = '<i class="fas fa-user-edit mr-2 text-green-600"></i>회원 수정';

                // 버튼 텍스트 변경
                const submitBtn = document.querySelector( '#memberModal button[onclick="submitMemberForm()"]' );
                if ( submitBtn ) {
                    submitBtn.innerHTML = '<i class="fas fa-save mr-1"></i>수정';
                    submitBtn.setAttribute( 'onclick', 'updateMemberForm(' + memberId + ')' );
                }

                // 모달 열기
                openMemberModal();
            } else {
                alert( '회원 정보를 불러오는데 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
            }
        } )
        .catch( error => {
            console.error( 'Error:', error );
            alert( '회원 정보를 불러오는 중 오류가 발생했습니다.' );
        } );
};

// 회원 수정 저장
window.updateMemberForm = function ( memberId ) {
    const form = document.getElementById( 'memberForm' );
    if ( form ) {
        const formData = new FormData( form );
        const memberData = Object.fromEntries( formData );

        // 비밀번호가 비어있으면 제거
        if ( !memberData.password || memberData.password.trim() === '' ) {
            delete memberData.password;
        }

        fetch( '/members/' + memberId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify( memberData )
        } )
            .then( response => response.json() )
            .then( result => {
                if ( result.success ) {
                    alert( '회원 정보가 성공적으로 수정되었습니다.' );
                    closeMemberModal();
                    location.reload();
                } else {
                    alert( '회원 수정에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                }
            } )
            .catch( error => {
                console.error( 'Error:', error );
                alert( '회원 수정 중 오류가 발생했습니다.' );
            } );
    }
};

// 회원 비활성화
window.deactivateMember = function ( memberId ) {
    if ( confirm( '이 회원을 비활성화하시겠습니까?' ) ) {
        fetch( '/members/' + memberId + '/deactivate', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            }
        } )
            .then( response => response.json() )
            .then( result => {
                if ( result.success ) {
                    alert( '회원이 성공적으로 비활성화되었습니다.' );
                    location.reload();
                } else {
                    alert( '회원 비활성화에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                }
            } )
            .catch( error => {
                console.error( 'Error:', error );
                alert( '회원 비활성화 중 오류가 발생했습니다.' );
            } );
    }
};

// 회원 활성화
window.activateMember = function ( memberId ) {
    if ( confirm( '이 회원을 활성화하시겠습니까?' ) ) {
        fetch( '/members/' + memberId + '/activate', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            }
        } )
            .then( response => response.json() )
            .then( result => {
                if ( result.success ) {
                    alert( '회원이 성공적으로 활성화되었습니다.' );
                    location.reload();
                } else {
                    alert( '회원 활성화에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                }
            } )
            .catch( error => {
                console.error( 'Error:', error );
                alert( '회원 활성화 중 오류가 발생했습니다.' );
            } );
    }
};

// 관리자 권한 토글
window.toggleAdminRole = function ( memberId, currentRole ) {
    const isAdmin = currentRole === 'ADMIN' || currentRole === 1;
    const action = isAdmin ? '회수' : '부여';
    const newRole = isAdmin ? 0 : 1; // 0: USER, 1: ADMIN

    if ( confirm( `이 회원의 관리자 권한을 ${ action }하시겠습니까?` ) ) {
        fetch( '/members/' + memberId + '/role', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify( { role: newRole } )
        } )
            .then( response => response.json() )
            .then( result => {
                if ( result.success ) {
                    alert( `관리자 권한이 성공적으로 ${ action }되었습니다.` );
                    location.reload();
                } else {
                    alert( `관리자 권한 ${ action }에 실패했습니다: ` + ( result.message || '알 수 없는 오류' ) );
                }
            } )
            .catch( error => {
                console.error( 'Error:', error );
                alert( `관리자 권한 ${ action } 중 오류가 발생했습니다.` );
            } );
    }
};

// 기존 makeAdmin 함수는 호환성을 위해 유지
window.makeAdmin = function ( memberId ) {
    toggleAdminRole( memberId, 'USER' );
};

// 회원 삭제
window.deleteMember = function ( memberId ) {
    if ( confirm( '정말로 이 회원을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.' ) ) {
        fetch( '/members/' + memberId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        } )
            .then( response => response.json() )
            .then( result => {
                if ( result.success ) {
                    alert( '회원이 성공적으로 삭제되었습니다.' );
                    location.reload();
                } else {
                    alert( '회원 삭제에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                }
            } )
            .catch( error => {
                console.error( 'Error:', error );
                alert( '회원 삭제 중 오류가 발생했습니다.' );
            } );
    }
};

// 모달 외부 클릭 시 닫기
document.addEventListener( 'DOMContentLoaded', function () {
    const memberModal = document.getElementById( 'memberModal' );
    if ( memberModal ) {
        memberModal.addEventListener( 'click', function ( e ) {
            if ( e.target === this ) {
                closeMemberModal();
            }
        } );
    }
} );