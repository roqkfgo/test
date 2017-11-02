<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> --%>
<%-- <fmt:formatDate value="${lec.editDate}" pattern="yyyy-MM-dd" /> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>index</title>
		<link href="notice.css" type="text/css" rel="stylesheet" />
	</head>
	<body>
		<div id="main">
			<div class="top-wrapper clear">
				<div id="content">
					<h2>공지사항</h2>
					<h3 class="hidden">방문페이지 로그</h3>
					<ul id="breadscrumb" class="block_hlist clear">
						<li>HOME</li>
						<li>
							고객센터
						</li>
						<li>
							공지사항목록
						</li>
					</ul>
					<h3 class="hidden">공지사항 목록</h3>
					<form id="content-searchform" class="article-search-form" action="notice.do" method="get">
						<fieldset>
							<legend class="hidden">
								목록 검색 폼
							</legend>
							<input type="hidden" name="pg" value="" />
							<label for="f"
							class="hidden">검색필드</label>
							<select name="f">
								<c:if test="${field=='TITLE'}">
									<option value="TITLE" selected="selected">제목</option>
									<option value="CONTENT">내용</option>
								</c:if>
								<c:if test="${field=='CONTENT'}">
									<option value="TITLE" >제목</option>
									<option value="CONTENT" selected="selected">내용</option>
								</c:if>
							</select>
							<label class="hidden" for="q">검색어</label>
							<input type="text" name="q" value="${query}" />
							<input type="submit" value="검색" />
						</fieldset>
					</form>
					<table class="article-list margin-small">
						<caption class="hidden">
							공지사항
						</caption>
						<thead>
							<tr>
								<th class="seq">번호</th>
								<th class="title">제목</th>
								<th class="writer">작성자</th>
								<th class="regdate">작성일</th>
								<th class="hit">조회수</th>
							</tr>
						</thead>
						<tbody>
						<%-- <% for(int i=0; i<list.size(); i++) {%> --%>
						<c:forEach var="n" items="${list}">
							<tr>
								<td class="seq">${n.seq}</td>
								<td class="title"><a href="noticeDetail.do?seq=${n.seq}&pg=${pg}&f=${field}&q=${urlquery}&hitUp=on">${n.title}</a></td>
								<td class="writer">${n.writer}</td>
								<td class="regdate">${n.regdate}</td>
								<td class="hit">${n.hit}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					<p class="article-comment margin-small">
						<a class="btn-write button" href="noticeReg.do?pg=${pg}&f=${field}&q=${urlquery}"></a>   <!-- 글쓰기 -->
					</p>
					<p id="cur-page" class="margin-small">
						<span class="strong">${pg}</span> /	${finalPage}  page    <!-- (a):현재페이지표시하기 -->
					</p>
					<div id="pager-wrapper" class="margin-small">
						<div class="pager clear">
							<p id="btnPrev">
							<c:if test="${sPage != 1}">      <!-- (d) : 이전페이지묶음으로 이동 -->
								<a class="button btn-prev" href="notice.do?pg=${sPage-1}&f=${field}&q=${urlquery}">이전</a>
							</c:if>
							</p>
							<ul>
							<c:forEach var="i" begin="0" end="4">    <!-- (b):페이지번호표시 startPN이 중요 -->
								<li>
								<c:if test="${sPage+i <= finalPage}">    <!-- (f)마지막페이지 이후는 표시하지 않기   : 마지막으로 메뉴 이동할떄 페이지번호 가져가기 -->
									<c:if test="${sPage+i == pg}">     <!-- (c) 페이지 링크 걸기 -->
										<a class="strong" >${sPage+i}</a> <!-- (c) 현재페이지는 빨간색이고 링크제거 -->
									</c:if>	
									<c:if test="${sPage+i != pg}">
										<a href="notice.do?pg=${sPage+i}&f=${field}&q=${urlquery}">${sPage+i}</a>   <!-- (b):페이지번호표시 sPage이 중요 -->
									</c:if>	
								</c:if>		
								</li>
							</c:forEach>	
							</ul>
							<p id="btnNext">
							<c:if test="${sPage+4 < finalPage}">     <!-- (e): 다음 페이지묶음으로 이동 -->
								<a class="button btn-next" href="notice.do?pg=${sPage+5}&f=${field}&q=${urlquery}" >다음</a>
							</c:if>
							</p>
						</div>
					</div>
				</div>
				
			</div>
		</div>
		
	</body>
</html>
