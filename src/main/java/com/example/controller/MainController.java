package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.util.ConfigUtil;
import com.example.util.JDBCUtil;
import com.example.util.StringUtil;

@Controller
public class MainController {
	
	private static Logger logger= Logger.getLogger(MainController.class.getName());
	
	private static final int pageSize=10;
	
	@RequestMapping(value="/index",method = RequestMethod.GET)  
	public String home(){
		return "index";
	}
	@RequestMapping(value="load",method = RequestMethod.POST)
	public void load(HttpServletRequest request,HttpServletResponse response){
		
		//處理參數
		String empno=request.getParameter("emp_no");
		
		String empname=request.getParameter("emp_name");
		
		String dat_bir_b=request.getParameter("dat_bir_b");
		
		String dat_bir_e=request.getParameter("dat_bir_e");
		
		String sex=request.getParameter("sex");
		
		String origin=request.getParameter("origin");
		
		String pageString=request.getParameter("pageNum");
		
		int pageNum=1;
		
		if(!StringUtil.isEmpty(pageString)) {
			pageNum=Integer.valueOf(pageString);
		}
		
		int begin=(pageNum-1)*pageSize+1;
		int end =pageNum*pageSize;
		
		
		List<Map<String,Object>> list=new ArrayList<>();
		
		Connection connection=JDBCUtil.getConnection();
		PreparedStatement preparedStatement=null;
		ResultSet rs=null;
		ResultSetMetaData rsmd=null;
//		String sql="select * from t_src_employee_vmhs t where T .EMP_NO=?";
		StringBuffer sb=new StringBuffer();
		sb.append(" select * from (select t.emp_no ,t.emp_nam,rownum rn from t_src_employee_vmhs t where 1=1 ");
		int paramNum=0;
		Map<Integer,String> paramMap=new HashMap<>();
		if(!StringUtil.isEmpty(empno)) {
			sb.append(" and t.EMP_NO=?");
			paramNum++;
			paramMap.put(paramNum,empno);
		}
		if(!StringUtil.isEmpty(empname)){
			sb.append("and t.emp_nam like '%'||?||'%'");
			paramNum++;
			paramMap.put(paramNum,empname);
		}
		if(!StringUtil.isEmpty(dat_bir_b)) {
			sb.append(" and t.DAT_BIR>to_date(?,'yyyy/MM/dd')");
			paramNum++;
			paramMap.put(paramNum,dat_bir_b);
		}else{
			sb.append(" and t.DAT_BIR>to_date('1985/1/1','yyyy/MM/dd')");
		}
		if(!StringUtil.isEmpty(dat_bir_e)) {
			sb.append(" and t.DAT_BIR<to_date(?,'yyyy/MM/dd')");
			paramNum++;
			paramMap.put(paramNum,dat_bir_e);
		}
		if(!StringUtil.isEmpty(sex)) {
			sb.append(" and t.emp_sex=?");
			paramNum++;
			paramMap.put(paramNum,sex);
		}
		if(!StringUtil.isEmpty(origin)) {
			sb.append(" and t.origin like '%'||?||'%'");
			paramNum++;
			paramMap.put(paramNum,origin);
		}
		sb.append(" and length(T.EMP_NO)>=8");
		String sqlCount=(sb.toString()+")").replace("t.emp_no ,t.emp_nam", "count(*)").replace(",rownum rn","");
//		sb.append(" and rownum>="+begin);
		sb.append(" and rownum<="+end);
		sb.append(") where rn >="+begin);
		try {
			 if(sb.toString().contains("delete")||sb.toString().contains("drop")||sb.toString().contains("update")) return;
			 preparedStatement=connection.prepareStatement(sb.toString());
			 for(int i=1;i<paramNum+1;i++){
				 preparedStatement.setString(i,paramMap.get(i).toUpperCase().trim());
			 }
			 rs=preparedStatement.executeQuery();
			 rsmd=rs.getMetaData();
			 int count=rsmd.getColumnCount();
			 String[] colNames=new String[count];
			 for (int i = 0; i < count; i++) {
				 colNames[i]=rsmd.getColumnName(i+1);
			 }
			 while (rs.next()) {
				 Map<String, Object> map = new HashMap<>();
				 for (int i = 0; i < count; i++) {
					map.put(colNames[i], rs.getObject(colNames[i]));
				 }
				 list.add(map);
			 }
			 List<Map<String,String>> result=new LinkedList<>();
			 String htmlTemplate="<img id=\"$empNo$\" alt=\"$empname$\" src=\""+ConfigUtil.getProperty("img_url")+"\"></img>";
			 for (int i = 0; i < list.size(); i++) {
				 if("H2605718".equals(String.valueOf(list.get(i).get("EMP_NO")))) continue;
				 Map<String, String> map=new LinkedHashMap<>();
				 map.put("empno", list.get(i).get("EMP_NO").toString());
				 map.put("html", new String(htmlTemplate.replace("$empNo$",  String.valueOf(list.get(i).get("EMP_NO")))
						 .replace("$empname$", String.valueOf(list.get(i).get("EMP_NAM")))));
				 result.add(map);
			}
			 if(result.size()==0) result=null;
			 PreparedStatement preparedStatement2=connection.prepareStatement(sqlCount);
			 for(int i=1;i<paramNum+1;i++){
				 preparedStatement2.setString(i,paramMap.get(i).toUpperCase().trim());
			 }
			 ResultSet rs2=preparedStatement.executeQuery();
			 long total=1L;
			 while (rs.next()) {
				 //total=Integer.valueOf(rs.getString(1))/10;
				 total=(long) Math.ceil((double) Integer.valueOf(rs.getString(1)) / (double) pageSize);
				 total=total>0?total:1L;
			 }
			 rs2.close();
			 preparedStatement2.close();
			 List<Map<String,Object>> ll=new ArrayList<>();
			 Map<String,Object> m=new HashMap<>();
			 m.put("total", total);
			 m.put("data", result);
			 ll.add(m);
			 outJson(response, ll);
		} catch (SQLException e) {
			logger.error(e);
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(preparedStatement!=null){
					preparedStatement.close();
				}
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}
	
	private void outJson(HttpServletResponse response,Object object){
		String jsonString="";
		if(object==null){
		    jsonString="no datas found!";
		}else{
			jsonString=JSONArray.fromObject(object).toString();
		}/*else{
			jsonString=JSONObject.fromObject(object).toString();
		}*/
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out =null;
		try {
			out = response.getWriter();
			out.write(jsonString);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
