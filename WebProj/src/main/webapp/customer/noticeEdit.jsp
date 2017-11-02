<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	/* String pg = (String)request.getAttribute("pg");  
	String field = (String)request.getAttribute("field");
	String query = (String)request.getAttribute("query");
	String urlquery = (String)request.getAttribute("urlquery");
	Notice n = (Notice)request.getAttribute("n");
	System.out.println("Detail_query="+query); */

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>index</title>
		<link href="../css/customer.css" type="text/css" rel="stylesheet" />
	</head>
	<body>
		
		<div id="main">
			<div class="top-wrapper clear">
				<div id="content">
					<h2>공지사항</h2>
					<h3 class="hidden">방문페이지위치</h3>
					<ul id="breadscrumb" class="block_hlist">
						<li>HOME</li>
						<li>
							고객센터
						</li>
						<li>
							공지사항수정
						</li>
					</ul>
					<form action="noticeEdit.do" method="post">
					<div id="notice-article-detail" class="article-detail margin-large" >						
						<dl class="article-detail-row">
							<dt class="article-detail-title">
								제목
							</dt>
							<dd class="article-detail-data">&nbsp;
							<input name="title" value="${n.title}" />
							</dd>
						</dl>	
						<dl class="article-detail-row half-row">
							<dt class="article-detail-title">
								작성자
							</dt>
							<dd class="article-detail-data half-data" >
								${n.writer}
							</dd>
						</dl>
						<dl class="article-detail-row half-row">
							<dt class="article-detail-title">
								조회수
							</dt>
							<dd class="article-detail-data half-data">
								${n.hit}
							</dd>
						</dl>
						<div class="article-content" >
							<textarea id="txtContent" class="txtContent" name="content">${n.content}</textarea>
						</div>						
					</div>
					<p class="article-comment margin-small">
						<input type="hidden" name="seq" value="${n.seq}"/>
						<input type="hidden" name="pg" value="${pg}"/>
						<input type="hidden" name="f" value="${field}"/>
						<input type="hidden" name="q" value="${query}"/>
						<input class="btn-save button" type="submit" value="수정" />
						<a class="btn-cancel button" href="noticeDetail.do?seq=${n.seq}&pg=${pg}&f=${field}&q=${urlquery}"></a>						
					</p>		
					</form>					
				</div>				
							
			</div>
		</div>
		
	</body>
</html>
