<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	pageContext.setAttribute("ctx", request.getContextPath());
%>

<!DOCTYPE HTML>
<html>
<head>
<title>find picture</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script src="common/jquery-1.8.2.min.js"></script>
<script src="js/index.js"></script>
<script type="text/javascript">
	var ctx = "${ctx}";
</script>
</head>
<style>
* {
	padding: 0;
	margin: 0;
	outline: none;
	box-sizing: border-box;
}

*:before,*:after {
	box-sizing: border-box;
}

html,body {
	min-height: 100%;
}

body {
	background-image: radial-gradient(mintcream 0%, lightgray 100%);;
}

.Container {
	margin: 5% auto;
	width: 210px;
	height: 140px;
	position: relative;
	perspective: 1000px;
}

#carousel {
	width: 100%;
	height: 100%;
	position: absolute;
	transform-style: preserve-3d;
	animation: rotation 20s infinite linear;
}

#carousel:hover {
	animation-play-state: paused;
}

#carousel figure {
	display: block;
	position: absolute;
	width: 220px;
	height: 120px;
	left: 10px;
	top: 10px;
	background: black;
	overflow: hidden;
	border: solid 5px black;
}

img {
	filter: grayscale(0);
	cursor: pointer;
	transition: all 0.3s ease 0s;
	width: 100%;
	height: 100%;
}

img:hover {
	filter: grayscale(0);
	transform: scale(1.2, 1.2);
}


@keyframes rotation {

from { 
	transform: rotateY(0deg);
}

to {
	transform: rotateY(600deg);
  }
}
</style>
<body>
	<div>
		<span>工號：<input id="empno" type="text" style="width: 80px"/></span>
		<span>姓名：<input id="empname" type="text" style="width: 80px"/></span>
		<span>日期區間：<input id="dat_bir_b" type="text" style="width: 80px"/>-<input id="dat_bir_e" style="width: 80px"/></span>
		<span>性別：
			<select id="sex"style="width: 50px">
				<option value=""></option>
				<option value="1">男</option>
				<option value="0">女</option>
			</select>
		</span>
		<span>來源：<input id="origin" type="text" style="width: 80px"/></span>
		<span><input type="button" id="confirm" value="確認" style="width: 50px"/></span>
		<a href="javascript:void(0)" id="change">切換顯示方式</a>
		<a href="javascript:void(0)" id="last">上一頁</a>
		<a href="javascript:void(0)" id="next">下一頁</a>
		當前第<span id="pageNum">0</span>頁,共<span id="total">0</span>頁
	</div>
	<div id="flat" style="display:block;margin-top:50px;">
		<table id="flat_tb"></table>
	</div>
	<div class="Container" style="display:none">
		<div id="carousel">
			<!-- <figure>
				<img src="../myWeb/素材/5cms.jpg" alt="">
			</figure>
			<figure>
				<img src="../myWeb/素材/5cms2.jpg" alt="">
			</figure>
			<figure>
				<img src="../myWeb/素材/5cms3.jpg" alt="">
			</figure>
			<figure>
				<img src="../myWeb/素材/5cms4.jpg" alt="">
			</figure>
			<figure>
				<img src="../myWeb/素材/5cms5.jpg" alt="">
			</figure>
			<figure>
				<img src="../myWeb/素材/5cms6.jpg" alt="">
			</figure> -->
		</div>
	</div>
</body>
</html>
