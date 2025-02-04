<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="teamproject.vo.*" %>
<%@ page import="java.util.*" %>
<%

List<ApplicantVO> alist = (List<ApplicantVO>)request.getAttribute("alist");
Map<String, Integer> statusCounts = (Map<String, Integer>) request.getAttribute("statusCounts");
if (statusCounts == null) {
    statusCounts = new HashMap<>(); // null 방지
}

%>

<%@ include file="../include/header.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/k_styles.css" />
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/mypage_styles.css" />

        <!-- 메인 컨텐츠 -->
        <main>
            <div class="main-container">
                <!-- 지원 현황 목록 -->
                <section class="apply-count">
				    <div class="apply-count-div">
				        <div>미열람</div>
				        <span class="a-count"><%= statusCounts.getOrDefault("W", 0) %>건</span>
				    </div>
				    <div class="apply-count-div">
				        <div>합격</div>
				        <span class="a-count"><%= statusCounts.getOrDefault("E", 0) %>건</span>
				    </div>
				    <div class="apply-count-div">
				        <div>불합격</div>
				        <span class="a-count"><%= statusCounts.getOrDefault("D", 0) %>건</span>
				    </div>
				    <div class="apply-count-div" id="showAll">
				        <div>전체 보기</div>
				        <span class="a-count">
				            <%= statusCounts.getOrDefault("W", 0) + statusCounts.getOrDefault("E", 0) + statusCounts.getOrDefault("D", 0) %>건
				        </span>
				    </div>
				</section>

                <br><br><br>
                <section class="apply-list">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>회사명</th>
                                <th>제목</th>
                                <th>신청일</th>
                                <th>심사 상태</th>
                            </tr>
                        </thead>
                        <tbody>
	                        <%
	                        for(ApplicantVO avo : alist){
	                        %>
	                        <tr data-status="<%= avo.getApplicant_state().equals("E") ? "합격" : avo.getApplicant_state().equals("D") ? "불합격" : "미열람" %>">
	                                <td>
	                                	<!-- <img src="https://img.icons8.com/?size=100&id=99634&format=png&color=ff5252"> -->
	                                	<%= avo.getCompany_name() %>
	                                </td>
	                                <td><%= avo. getResume_title() %></td>
	                                <td><%= avo.getApplicant_registration_date() %></td>
	                                <td>
		                                <%
		                                if(avo.getApplicant_state().equals("E")){
			                                %>
			                                <span>합격</span>
			                                <%	
		                                }else if(avo.getApplicant_state().equals("D")){
			                                %>
			                                <span>불합격</span>
			                                <%	
		                                }else{
		                                	 %>
		 	                                <span>미열람</span>
		 	                                <%	
		                                }
		                                %>
	                                </td>
	                            </tr>
	                        <%	
	                        }
	                        %>
                        </tbody>
                    </table>
                </section>
            </div>
        </main>

		<script>
		document.addEventListener("DOMContentLoaded", () => {
		    const applyListRows = document.querySelectorAll(".apply-list tbody tr"); // 모든 지원 내역 행 가져오기

		    document.querySelectorAll(".apply-count-div").forEach(div => {
		        div.addEventListener("click", () => {
		            const selectedStatus = div.querySelector("div").textContent.trim(); // 선택한 필터 텍스트

		            applyListRows.forEach(row => {
		                const rowStatus = row.dataset.status; // 각 행의 심사 상태
		                
		                if (selectedStatus === "전체 보기" || rowStatus === selectedStatus) {
		                    row.style.display = ""; // 보이기
		                } else {
		                    row.style.display = "none"; // 숨기기
		                }
		            });
		        });
		    });
		});
		</script>

<%@ include file="../include/footer.jsp" %>
