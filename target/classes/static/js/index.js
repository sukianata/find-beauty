{
	var pageNum=1;
	//var pageSize=10;
	var maxPage =0;
}

$(document).ready(function(){
  	//load();
	$("#confirm").click(load);
	$("#change").click(change);
	$("#last").click(last);
	$("#next").click(next);
})
function load(){
	$("#pageNum").text(pageNum);
	var empname=$("#empname").val();
	var empno=$("#empno").val();
	var dat_bir_b=$("#dat_bir_b").val();
	var dat_bir_e=$("#dat_bir_e").val();
	var sex=$("#sex").val();
	var origin=$("#origin").val();
	var params={
			"emp_no":empno,
			"emp_name":empname,
			"dat_bir_b":dat_bir_b,
			"dat_bir_e":dat_bir_e,
			"sex":sex,
			"origin":origin,
			"pageNum":pageNum
		}
	$.ajax({
		url:ctx+'/load',
		type:"post",
		data:params,
		dataType:"json",
		async:false,
		cache:false,
		success:function(data){
//			alert(data);
			var total=data[0]['total'];
			$("#total").text(total);
			var data=data[0]['data'];
			if(data==null){
				alert("no datas found!")
				$("#flat_tb").html("");
				$("#carousel").html("");
				return;
			}
			var len=data.length;
			var html="";
			if(len==0){
				alert("no datas found!")
				$("#flat_tb").html("");
				$("#carousel").html("");
				return;
			}
			for(var i =0;i<len;i++){
				var cssContent="rotateY("+parseInt(60*i)+"deg) translateZ(288px)"
				html+="<figure style=\"transform:"+cssContent+"\">";
				html+=data[i]['html'];
				html+="</figure>";
			}
			var html2="";
			for(var i =0;i<len;i++){
				if(parseInt(i+1)%5==1){
					html2+="<tr>"
					html2+="<td>"+data[i]['html']+"</td>"
				}else if(parseInt(i+1)%5==0){
					html2+="<td>"+data[i]['html']+"</td>"
					html2+="</tr>"
				}else{
					html2+="<td>"+data[i]['html']+"</td>"
				}
			}
			$("#flat_tb").html(html2);
			$("#carousel").html(html);
		}
	})
}
function change(){
	if($(".Container").css("display")=="block"){
		$(".Container").hide();
		$("#flat").show();
	}else{
		$(".Container").show();
		$("#flat").hide();
	}
}
function next(){
	pageNum=pageNum+1;
	load();
}
function last(){
	pageNum=pageNum-1;
	load();
}