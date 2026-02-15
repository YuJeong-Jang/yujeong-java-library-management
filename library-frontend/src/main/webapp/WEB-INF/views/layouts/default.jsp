<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${title != null ? title : '도서관리 시스템'}</title>
            <script src="https://cdn.tailwindcss.com"></script>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        </head>

        <body class="bg-gray-50 min-h-screen">
            <!-- Navigation -->
            <nav class="bg-white shadow-lg border-b border-gray-200">
                <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div class="flex justify-between h-16">
                        <div class="flex items-center">
                            <a href="/" class="flex items-center space-x-3">
                                <i class="fas fa-book-open text-2xl text-blue-600"></i>
                                <span class="text-xl font-bold text-gray-800">도서관리 시스템</span>
                            </a>
                        </div>

                        <div class="flex items-center space-x-4">
                            <c:if test="${sessionScope.loginMember != null}">
                                <div class="hidden md:flex items-center space-x-6">
                                    <a href="/books"
                                        class="text-gray-700 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                                        <i class="fas fa-book mr-2"></i>도서 관리
                                    </a>
                                    <a href="/members"
                                        class="text-gray-700 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                                        <i class="fas fa-users mr-2"></i>회원 관리
                                    </a>
                                    <a href="/rentals"
                                        class="text-gray-700 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                                        <i class="fas fa-exchange-alt mr-2"></i>대여 관리
                                    </a>
                                </div>

                                <div class="flex items-center space-x-3">
                                    <span class="text-sm text-gray-600">
                                        <i class="fas fa-user mr-1"></i>${sessionScope.loginMember.name}님
                                    </span>
                                    <a href="/logout"
                                        class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">
                                        <i class="fas fa-sign-out-alt mr-2"></i>로그아웃
                                    </a>
                                </div>
                            </c:if>

                            <c:if test="${sessionScope.loginMember == null}">
                                <div class="flex items-center space-x-3">
                                    <a href="/login"
                                        class="text-gray-700 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium transition-colors">
                                        <i class="fas fa-sign-in-alt mr-2"></i>로그인
                                    </a>
                                    <a href="/register"
                                        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">
                                        <i class="fas fa-user-plus mr-2"></i>회원가입
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
                <c:if test="${error != null}">
                    <div class="mb-6 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
                        <i class="fas fa-exclamation-triangle mr-2"></i>${error}
                    </div>
                </c:if>

                <c:if test="${success != null}">
                    <div class="mb-6 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-lg">
                        <i class="fas fa-check-circle mr-2"></i>${success}
                    </div>
                </c:if>

                <!-- Page Content -->
                <jsp:include page="../${contentPage}" />
            </main>

            <!-- Footer -->
            <footer class="bg-white border-t border-gray-200 mt-12">
                <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
                    <div class="text-center text-gray-600">
                        <p>&copy; 2024 도서관리 시스템. All rights reserved.</p>
                    </div>
                </div>
            </footer>
        </body>

        </html>